package nextstep.courses.infrastructure;

import nextstep.courses.domain.*;
import nextstep.courses.domain.image.ImageCapacity;
import nextstep.courses.domain.image.ImageSize;
import nextstep.courses.domain.image.ImageType;
import nextstep.courses.domain.image.SessionImage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@JdbcTest
public class CourseRepositoryTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(CourseRepositoryTest.class);

    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    private CourseRepository courseRepository;
    private JdbcSessionRepository jdbcSessionRepository;
    private JdbcCourseSesisonRepository jdbcCourseSesisonRepository;

    @BeforeEach
    void setUp() {
        jdbcSessionRepository = new JdbcSessionRepository(namedParameterJdbcTemplate.getJdbcTemplate());
        jdbcCourseSesisonRepository = new JdbcCourseSesisonRepository(namedParameterJdbcTemplate.getJdbcTemplate());
        courseRepository = new JdbcCourseRepository(namedParameterJdbcTemplate, jdbcSessionRepository, jdbcCourseSesisonRepository);
    }

    @Test
    void crud() {
        List<Long> testStudents = List.of(1L, 2L, 3L);
        Session testSession = new Session(1L,
                testStudents,
                new PricingType(CourseType.BASIC, 0),
                SessionState.valueOf("START"),
                new SessionImage(new ImageCapacity(1024), ImageType.valueOf("gif"),
                        new ImageSize(300, 200)),
                10,
                new SessionDate(LocalDateTime.now(), LocalDateTime.now().plusDays(2)),1L );

        Long sessionPk = jdbcSessionRepository.save(testSession);

        jdbcCourseSesisonRepository.save(1L, List.of(sessionPk));

        Course course = new Course(1L,"TDD, 클린 코드 with Java", sessionPk);
        int count = courseRepository.save(course);
        assertThat(count).isEqualTo(1);
        Course savedCourse = courseRepository.findById(1L);
        assertThat(course.getTitle()).isEqualTo(savedCourse.getTitle());
        LOGGER.debug("Course: {}", savedCourse);
    }
}
