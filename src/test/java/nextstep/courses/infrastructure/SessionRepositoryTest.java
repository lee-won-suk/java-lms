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
import org.springframework.jdbc.core.JdbcTemplate;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@JdbcTest
public class SessionRepositoryTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(SessionRepositoryTest.class);

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private SessionRepository sessionRepository;

    @BeforeEach
    void setUp() {
        sessionRepository = new JdbcSessionRepository(jdbcTemplate);
    }

    @Test
    void crud() {

        List<Long> testStudents = List.of(1L, 2L, 3L);
        Session testSession = new Session(1L,
                testStudents,
                new PricingType(false, 0),
                SessionState.valueOf("START"),
                new SessionImage(new ImageCapacity(1024), ImageType.valueOf("gif"),
                        new ImageSize(300, 200)),
                10,
                new SessionDate(LocalDateTime.now(), LocalDateTime.now().plusDays(2)));

        Long sessionPk = sessionRepository.save(testSession);
        Session savedSession = sessionRepository.findById(sessionPk);
        assertThat(savedSession.getStudents()).hasSameElementsAs(testStudents);
        LOGGER.debug("Session: {}", savedSession);
    }
}
