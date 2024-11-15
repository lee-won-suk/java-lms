package nextstep.courses.infrastructure;

import nextstep.courses.domain.Course;
import nextstep.courses.domain.CourseRepository;
import nextstep.courses.domain.CourseSessionRepository;
import nextstep.courses.domain.Session;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Repository("courseRepository")
public class JdbcCourseRepository implements CourseRepository {
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    private JdbcSessionRepository jdbcSessionRepository;
    private CourseSessionRepository courseSessionRepository;

    public JdbcCourseRepository(NamedParameterJdbcTemplate namedParameterJdbcTemplate, JdbcSessionRepository sessionRepository, CourseSessionRepository courseSessionRepository) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
        this.jdbcSessionRepository = sessionRepository;
        this.courseSessionRepository = courseSessionRepository;
    }

    @Override
    public int save(Course course) {
        String sql = "insert into course (title, creator_id, created_at, class_number) " +
                "values(:title, :creatorId, :createdAt, :classNumber)";
        SqlParameterSource parameterSource = new MapSqlParameterSource()
                .addValue("title", course.getTitle())
                .addValue("creatorId", course.getCreatorId())
                .addValue("createdAt", course.getCreatedAt())
                .addValue("classNumber", course.getClassNumber());

        return namedParameterJdbcTemplate.update(sql, parameterSource);
    }

    @Override
    public Course findById(Long id) {
        String sql = "select id, title, creator_id, created_at, updated_at, class_number from course where id = :id";
        List<Session> sessions = getSessions(id);/*getSessions(id);*/
        RowMapper<Course> ROW_MAPPER = (rs, rowNum) -> new Course(
                rs.getLong("id"),
                rs.getString("title"),
                rs.getLong("creator_id"),
                toLocalDateTime(rs.getTimestamp("created_at")),
                toLocalDateTime(rs.getTimestamp("updated_at")),
                rs.getInt("class_number"),
                sessions
        );
        MapSqlParameterSource parameterSource = new MapSqlParameterSource().addValue("id",id);
        return namedParameterJdbcTemplate.queryForObject(sql, parameterSource, ROW_MAPPER);
    }

    private List<Session> getSessions(Long id) {
        List<Long> sessionIds = courseSessionRepository.findByCourseId(id);
        return sessionIds.stream().map(jdbcSessionRepository::findById).collect(Collectors.toList());
    }


    private static LocalDateTime toLocalDateTime(Timestamp timestamp) {
        if (timestamp == null) {
            return null;
        }
        return timestamp.toLocalDateTime();
    }
}
