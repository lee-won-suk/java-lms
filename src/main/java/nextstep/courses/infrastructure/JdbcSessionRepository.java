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

@Repository()
public class JdbcSessionRepository implements SessionRepository {
    private final JdbcOperations jdbcTemplate;

    public JdbcSessionRepository(JdbcOperations jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public long save(Session session) {
        String sql = "insert into session (session_amount," +
                " is_premium," +
                " session_state," +
                " image_capacity,image_type, image_width, image_height, max_student_count," +
                "start_date, end_date" +
                ") values(?,?,?,?,?,?,?,?,?,?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, new String[]{"id"});
            ps.setInt(1, session.getPricingType().getSessionAmount());
            ps.setBoolean(2, session.getPricingType().getIsPremium());
            ps.setString(3, session.getState().getState());
            ps.setInt(4, session.getImage().getCapacity().getImageSize());
            ps.setString(5, session.getImage().getType().toString());
            ps.setInt(6, session.getImage().getSize().getWidth());
            ps.setInt(7, session.getImage().getSize().getHeight());
            ps.setInt(8, session.getMaxStudentCount());
            ps.setTimestamp(9, Timestamp.valueOf(session.getDate().getStartDate()));
            ps.setTimestamp(10, Timestamp.valueOf(session.getDate().getEndDate()));
            return ps;
        }, keyHolder);

        long pk = keyHolder.getKey().longValue();

        for (Long studentId : session.getStudents()) {
            saveSessionStudent(pk, studentId);
        }
        return pk;
    }

    private long saveSessionStudent(long pk, Long studentId) {

        String sql = "insert into session_students (id, session_id"
                + ") values(?,?);";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, new String[]{"id"});
            ps.setLong(1, studentId);
            ps.setLong(2, pk);
            return ps;
        }, keyHolder);

        return keyHolder.getKey().longValue();
    }

    @Override
    public Session findById(Long id) {
        String sql = "select id," +
                " session_amount," +
                " is_premium," +
                " session_state," +
                " image_capacity," +
                " image_type," +
                " image_width, image_height, max_student_count, start_date, end_date, course_id from session where id = ?";
        RowMapper<Session> rowMapper = (rs, rowNum) -> {
            long pk = rs.getLong(1);
            List<Long> students = findStudents(pk);

            return new Session(pk,
                    students,
                    new PricingType( rs.getBoolean(3), rs.getInt(2)),
                    SessionState.valueOf(rs.getString(4)),
                    new SessionImage(new ImageCapacity(rs.getInt(5)), ImageType.valueOf(rs.getString(6)), new ImageSize(rs.getInt(7),rs.getInt(8))),
                    rs.getInt(9),
                    new SessionDate(toLocalDateTime(rs.getTimestamp(10)), toLocalDateTime(rs.getTimestamp(11)))
                    ,rs.getLong(12) );
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
