package nextstep.courses.exception;

public class CustomException extends RuntimeException {

    public static final CustomException NOT_ALLOWED_DATE = new CustomException("허용되지 않은 시작날짜입니다.");
    public static final CustomException OVER_MAX_IMAGE_CAPACITY = new CustomException("이미지 사이즈는 1MB 이하여야 합니다.");
    public static final CustomException IMAGE_SIZE_ERROR = new CustomException("이미지 사이즈 오류입니다.");
    public static final CustomException IMAGE_PERCENT_ERROR = new CustomException("이미지 비율 오류");
    public static final CustomException NOT_ALLOWED_PREMIUM_AMOUNT = new CustomException("유료강의 금액은 0원이 될 수 없습니다.");
    public static final CustomException NOT_ALLOWED_FREE_AMOUNT = new CustomException("무료 강의 금액이 0원이 아닙니다.");
    public static final CustomException NOT_MATCHING_SESSION_AMOUNT = new CustomException("강의 금액과 맞지 않습니다.");
    public static final CustomException MAX_STUDENTS_OVER = new CustomException("수강인원 초과");
    public static final CustomException INVALID_SESSION_STATE = new CustomException("강의신청 기간이 아닙니다.");
    public static final CustomException INVALID_IMAGE_TYPE = new CustomException("이미지 타입이 올바르지 않습니다.");

    public CustomException(String message) {
        super(message);
    }

    @Override
    public synchronized Throwable fillInStackTrace() {
        return this;
    }

}
