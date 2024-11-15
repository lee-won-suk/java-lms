package nextstep.courses.infrastructure;

import nextstep.courses.domain.CourseSessionRepository;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public class JdbcCourseSesisonRepository implements CourseSessionRepository {

    private final JdbcOperations jdbcTemplate;

    public JdbcCourseSesisonRepository(JdbcOperations jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }


    @Override
    public int save(Long courseId, List<Long> sessionIds) {
        String sql = "insert into course_session (course_id, session_id) values(?,?)";
        int totalInsert = 0;
        for (Long sessionId : sessionIds) {
            int count = jdbcTemplate.update(sql, courseId, sessionId);
            totalInsert += count;
        }
        return totalInsert;
    }

    @Override
    public List<Long> findByCourseId(Long courseId) {
        String sql = "select session_id from course_session where course_id = ?";
        RowMapper<Long> rowMapper = (rs, rowNum) -> rs.getLong("session_id");
        return jdbcTemplate.query(sql,rowMapper, courseId);
    }

}
