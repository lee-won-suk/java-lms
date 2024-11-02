package nextstep.courses.domain;

import nextstep.courses.exception.CustomException;
import nextstep.courses.domain.sessionimage.ImageCapacity;
import nextstep.courses.domain.sessionimage.ImageSize;
import nextstep.courses.domain.sessionimage.ImageType;
import nextstep.courses.domain.sessionimage.SessionImage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

public class ImageTypeTest {


    public static final int IMAGE_CAPACITY = 1;
    public static final int WIDTH = 300;
    public static final int HEIGHT = 200;
    public static final String JPG = "jpg";
    ImageCapacity imageCapacity;
    ImageSize imageSize;
    ImageType imageType;
    SessionImage sessionImage;
    @BeforeEach
    public void setUp() {

        imageCapacity = new ImageCapacity(IMAGE_CAPACITY);
        imageSize = new ImageSize(WIDTH, HEIGHT);
        imageType = ImageType.jpg;

    }

    @Test
    public void 이미지타입_성공_테스트() {

        assertDoesNotThrow(() -> {
            SessionImage sessionImage = new SessionImage(imageCapacity, imageType,imageSize);
        });
    }

    @Test
    public void 이미지타입_오입력_실패_테스트() {
        String IMAGE_TYPE = "git";
        assertThatThrownBy(() -> {
            imageType = ImageType.validateType(IMAGE_TYPE);
        }).isInstanceOf(CustomException.class);
    }
}
