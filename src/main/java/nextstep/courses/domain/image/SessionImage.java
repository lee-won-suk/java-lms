package nextstep.courses.domain.image;

public class SessionImage {

    private final ImageCapacity capacity;
    private final ImageType type;
    private final ImageSize size;

    public SessionImage(ImageCapacity capacity, ImageType type, ImageSize size) {
        ImageType.validateType(type.name());
        this.capacity = capacity;
        this.type = type;
        this.size = size;
    }

    public ImageCapacity getCapacity() {
        return capacity;
    }

    public ImageType getType() {
        return type;
    }

    public ImageSize getSize() {
        return size;
    }

}
