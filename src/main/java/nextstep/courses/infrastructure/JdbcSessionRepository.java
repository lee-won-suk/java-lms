package nextstep.courses.infrastructure;

import nextstep.courses.domain.*;
import nextstep.courses.domain.image.ImageCapacity;
import nextstep.courses.domain.image.ImageSize;
import nextstep.courses.domain.image.ImageType;
import nextstep.courses.domain.image.SessionImage;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Repository
public class JdbcSessionRepository implements SessionRepository {
    private final JdbcOperations jdbcTemplate;

    public JdbcSessionRepository(JdbcOperations jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public long save(Session session) {
        String sql = "insert into session (session_amount," +
                " course_type," +
                " session_state," +
                " image_capacity,image_type, image_width, image_height, max_student_count," +
                "start_date, end_date, course_id" +
                ") values(?,?,?,?,?,?,?,?,?,?,?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, new String[]{"id"});
            ps.setInt(1, session.getPricingType().getSessionAmount());
            ps.setString(2, session.getPricingType().getCourseType().toString());
            ps.setString(3, session.getState().toString());
            ps.setInt(4, session.getImage().getCapacity().getImageSize());
            ps.setString(5, session.getImage().getType().toString());
            ps.setInt(6, session.getImage().getSize().getWidth());
            ps.setInt(7, session.getImage().getSize().getHeight());
            ps.setInt(8, session.getMaxStudentCount());
            ps.setTimestamp(9, Timestamp.valueOf(session.getDate().getStartDate()));
            ps.setTimestamp(10, Timestamp.valueOf(session.getDate().getEndDate()));
            ps.setLong(11, session.getCourseId());
            return ps;
        }, keyHolder);

        long pk = keyHolder.getKey().longValue();

        for (Long studentId : session.getStudents()) {
            saveSessionStudent(pk, studentId, session.getCourseId());
        }
        return pk;
    }

    private long saveSessionStudent(long pk, Long studentId, Long courseId) {

        String sql = "insert into session_students (id, session_id, course_id"
                + ") values(?,?,?);";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, new String[]{"id"});
            ps.setLong(1, studentId);
            ps.setLong(2, pk);
            ps.setLong(3, courseId);
            return ps;
        }, keyHolder);

        return keyHolder.getKey().longValue();
    }

    @Override
    public Session findById(Long id) {
        String sql = "select id," +
                " session_amount," +
                " course_type," +
                " session_state," +
                " image_capacity," +
                " image_type," +
                " image_width, image_height, max_student_count, start_date, end_date, course_id from session where id = ?";
        RowMapper<Session> rowMapper = (rs, rowNum) -> {
            long pk = rs.getLong(1);
            List<Long> students = findStudents(pk);

            return new Session(pk,
                    students,
                    new PricingType(CourseType.getCourseType(rs.getString("course_type")), rs.getInt("session_amount")),
                    SessionState.valueOf(rs.getString("session_state")),
                    new SessionImage(new ImageCapacity(rs.getInt("image_capacity")),
                            ImageType.valueOf(rs.getString("image_type")), new ImageSize(rs.getInt("image_width"),
                            rs.getInt("image_height"))),
                    rs.getInt("max_student_count"),
                    new SessionDate(toLocalDateTime(rs.getTimestamp("start_date")), toLocalDateTime(rs.getTimestamp("end_date")))
                    , rs.getLong("course_id"));
        };

        return jdbcTemplate.queryForObject(sql, rowMapper, id);
    }

    private List<Long> findStudents(long pk) {
        String sql = "select id from session_students where session_id = ?";
        return jdbcTemplate.query(sql, rs -> {
            List<Long> studentIdList = new ArrayList<>();
            while (rs.next()) {
                studentIdList.add(rs.getLong(1));
            }
            return studentIdList;
        }, pk);
    }

    private LocalDateTime toLocalDateTime(Timestamp timestamp) {
        if (timestamp == null) {
            return null;
        }
        return timestamp.toLocalDateTime();
    }
}
