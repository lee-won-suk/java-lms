package nextstep.courses.domain;

import nextstep.courses.Exception.CustomException;
import nextstep.courses.domain.sessionimage.ImageCapacity;
import nextstep.courses.domain.sessionimage.ImageSize;
import nextstep.courses.domain.sessionimage.ImageType;
import nextstep.courses.domain.sessionimage.SessionImage;
import nextstep.payments.domain.Payment;
import nextstep.users.domain.NsUser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class SessionTest {
    public static final int IMAGE_CAPACITY = 1;
    public static final int WIDTH = 300;
    public static final int HEIGHT = 200;
    public static final String JPG = "jpg";
    SessionDate sessionDate;
    Payment matchPayment;
    Payment unMatchPayment;
    ImageCapacity imageCapacity;
    ImageSize imageSize;
    ImageType imageType;
    SessionImage sessionImage;

    @BeforeEach
    public void setUp() {

        sessionDate = new SessionDate(LocalDateTime.now(), LocalDateTime.now().plusDays(2));
        NsUser nsUser = new NsUser(1L, "test", "pwd", "wonsuk", "email");
        matchPayment = new Payment("test", 1L, nsUser, 10000L);
        unMatchPayment = new Payment("test", 1L, nsUser, 9000L);

        imageCapacity = new ImageCapacity(IMAGE_CAPACITY);
        imageSize = new ImageSize(WIDTH, HEIGHT);
        imageType = ImageType.jpg;
        sessionImage = new SessionImage(imageCapacity,imageType,imageSize);

    }

    @Test
    public void 무료강의_최대_수강인원제한없음_테스트() {
        PricingType pricingType = new PricingType(false, 0);
        Session session = new Session(pricingType, 0, SessionState.START, sessionDate,sessionImage);
        assertDoesNotThrow(() -> {
            session.requestSession(matchPayment);
        });
    }

    @Test
    public void 유료강의_최대_수강인원제한_테스트() {
        PricingType pricingType = new PricingType(true, 300);
        Session session = new Session(pricingType, 0, SessionState.START, sessionDate, sessionImage);
        assertThatThrownBy(() -> {
            session.requestSession(matchPayment);
        }).isInstanceOf(CustomException.class);
    }

    @Test
    public void 무료강의_강의금액0원_실패테스트() {
        assertThatThrownBy(() -> {
            new PricingType(false, 1);
        }).isInstanceOf(CustomException.class);
    }

    @Test
    public void 유료강의_강의금액불일치_테스트() {
        PricingType pricingType = new PricingType(true, 10000);
        Session session = new Session(pricingType, 1, SessionState.START, sessionDate, sessionImage);
        assertThatThrownBy(() -> {
            session.requestSession(unMatchPayment);
        }).isInstanceOf(CustomException.class);
    }

    @Test
    public void 유료강의_강의금액일치_테스트() {
        PricingType pricingType = new PricingType(true, 10000);
        Session session = new Session(pricingType, 1, SessionState.START, sessionDate, sessionImage);
        assertDoesNotThrow(() -> {
            session.requestSession(matchPayment);
        });
    }

    @Test
    public void 강의신청은_강의상태아닐때_실패테스트() {
        PricingType pricingType = new PricingType(true, 10000);
        Session session = new Session(pricingType, 1, SessionState.READY, sessionDate, sessionImage);
        assertThatThrownBy(() -> {
            session.requestSession(matchPayment);
        }).isInstanceOf(CustomException.class);
    }

    @Test
    public void 강의신청_모집상태_성공테스트() {
        PricingType pricingType = new PricingType(true, 10000);
        Session session = new Session(pricingType, 1, SessionState.START, sessionDate, sessionImage);
        assertDoesNotThrow(() -> {
            session.requestSession(matchPayment);
        });
    }

}
