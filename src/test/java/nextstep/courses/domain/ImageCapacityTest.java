package nextstep.courses.domain;

import nextstep.courses.exception.CustomException;
import nextstep.courses.domain.sessionimage.ImageCapacity;
import nextstep.courses.domain.sessionimage.ImageSize;
import nextstep.courses.domain.sessionimage.ImageType;
import nextstep.courses.domain.sessionimage.SessionImage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

public class ImageCapacityTest {

    public static final int IMAGE_CAPACITY = 1024 * 1024;
    public static final int WIDTH = 300;
    public static final int HEIGHT = 200;

    ImageCapacity imageCapacity;
    ImageSize imageSize;
    ImageType imageType;

    @Test
    public void 이미지크기는_1MB이하_테스트() {
        imageCapacity = new ImageCapacity(IMAGE_CAPACITY);
        imageSize = new ImageSize(WIDTH, HEIGHT);
        imageType = ImageType.jpg;

        assertDoesNotThrow(() -> {
            new SessionImage(imageCapacity,imageType,imageSize);
        });
    }

    @ParameterizedTest
    @ValueSource(ints = {0,1024 * 1025})
    public void 이미지크기는_1MB제외실패_테스트(int imageCapacity) {
        assertThatThrownBy(() -> {
            new ImageCapacity(imageCapacity);
              }).isInstanceOf(CustomException.class);
    }

}
