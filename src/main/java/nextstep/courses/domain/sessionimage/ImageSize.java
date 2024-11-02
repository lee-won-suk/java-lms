package nextstep.courses.domain.sessionimage;

import nextstep.courses.exception.CustomException;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class ImageSize {
    public static final int MINIMUM_WIDTH = 300;
    public static final int MINIMUM_HEIGHT = 200;
    public static final double EXPECTED_SIZE_PERCENT = 1.5;
    public static final int FIRST_DECIMAL_PLACE = 2;
    private final int width;
    private final int height;

    public ImageSize(int width, int height) {
        validateSize(width, height);
        this.width = width;
        this.height = height;
    }

    private void validateSize(int width, int height) {
        if (width < MINIMUM_WIDTH || height < MINIMUM_HEIGHT) {
            throw CustomException.IMAGE_SIZE_ERROR;
        }

        if (new BigDecimal((double) width / height)
                .setScale(FIRST_DECIMAL_PLACE, RoundingMode.FLOOR).doubleValue() != EXPECTED_SIZE_PERCENT) {
            throw CustomException.IMAGE_PERCENT_ERROR;
        }
    }
}
