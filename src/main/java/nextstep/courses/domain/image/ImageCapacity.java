package nextstep.courses.domain.image;

import nextstep.courses.exception.CustomException;

public class ImageCapacity {

    public static final int MINIMUM_SIZE = 0;
    public static final int MAXIMUM_SIZE = 1024 * 1024;

    private final int imageSize;


    public ImageCapacity(int imageSize) {
        validateSize(imageSize);
        this.imageSize = imageSize;
    }

    private void validateSize(int imageSize) {
        if (imageSize <= MINIMUM_SIZE || imageSize > MAXIMUM_SIZE) {
            throw CustomException.OVER_MAX_IMAGE_CAPACITY;
        }
    }

    public int getImageSize() {
        return imageSize;
    }

}
