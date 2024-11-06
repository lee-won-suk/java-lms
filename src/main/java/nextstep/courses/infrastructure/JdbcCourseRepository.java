package nextstep.courses.infrastructure;

import nextstep.courses.domain.Course;
import nextstep.courses.domain.CourseRepository;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@Repository("courseRepository")
public class JdbcCourseRepository implements CourseRepository {
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public JdbcCourseRepository(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
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

        MapSqlParameterSource parameterSource = new MapSqlParameterSource()
                .addValue("id",id);
        RowMapper<Course> rowMapper = (rs, rowNum) -> new Course(
                rs.getLong("id"),
                rs.getString("title"),
                rs.getLong("creator_id"),
                toLocalDateTime(rs.getTimestamp("created_at")),
                toLocalDateTime(rs.getTimestamp("updated_at")),
                rs.getInt("class_number")
        );
        return namedParameterJdbcTemplate.queryForObject(sql, parameterSource, rowMapper);
    }

    private LocalDateTime toLocalDateTime(Timestamp timestamp) {
        if (timestamp == null) {
            return null;
        }
        return timestamp.toLocalDateTime();
    }
}
