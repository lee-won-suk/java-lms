package nextstep.courses.domain.image;

import nextstep.courses.exception.CustomException;

import java.util.Arrays;

public enum ImageType {

    gif,
    jpg,
    jpeg,
    png,
    svg;

    public static ImageType validateType(String imageType) {
        return Arrays.stream(values()).filter(type -> type.name().equals(imageType))
                .findFirst()
                .orElseThrow(() -> CustomException.INVALID_IMAGE_TYPE );
    }
}
