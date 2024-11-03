package nextstep.courses.domain;

import nextstep.courses.exception.CustomException;
import nextstep.courses.domain.image.ImageSize;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class ImageSizeTest {

    @ParameterizedTest
    @CsvSource(value = {"200,200", "300,199", "200,199", "400,300"})
    public void 이미지타입_사이즈및비율실패_테스트(int width, int height) {
        assertThatThrownBy(() -> {
            new ImageSize(width, height);
        }).isInstanceOf(CustomException.class);
    }
}
