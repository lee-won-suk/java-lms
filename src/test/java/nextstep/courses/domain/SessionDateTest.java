package nextstep.courses.domain;

import nextstep.courses.exception.CustomException;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class SessionDateTest {

    @Test
    public void 시작일이_종료일보다늦는_실패테스트() {
        assertThatThrownBy(() -> {
            final int TEST_MINUS_DAYS = 2;
            new SessionDate(LocalDateTime.now(), LocalDateTime.now().minusDays(TEST_MINUS_DAYS));
        }).isInstanceOf(CustomException.class);
    }


}
