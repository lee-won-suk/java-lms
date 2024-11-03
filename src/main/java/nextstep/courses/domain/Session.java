package nextstep.courses.domain;

import nextstep.courses.exception.CustomException;
import nextstep.courses.domain.image.SessionImage;
import nextstep.payments.domain.Payment;

import java.util.ArrayList;
import java.util.List;

public class Session {
    private List<Long> students;
    private PricingType pricingType;
    private SessionState state;
    private SessionImage image;
    private int maxStudentCount;
    private SessionDate date;

    public Session(PricingType pricingType, int maxStudentCount, SessionState state, SessionDate date
            , SessionImage image) {
        this.pricingType = pricingType;
        this.students = new ArrayList<Long>();
        this.maxStudentCount = maxStudentCount;
        this.state = state;
        this.date = date;
        this.image = image;

    }

    public void requestSession(Payment payment) {
        validate();
        validateSessionState();
        pricingType.validateAmount(payment);
        students.add(payment.payingUser());
    }

    public void validate() {
        if (pricingType.isPremium() && students.size() >= maxStudentCount) {
            throw CustomException.MAX_STUDENTS_OVER;
        }
    }

    private void validateSessionState() {
        if (!state.isRequestSession()) {
            throw  CustomException.INVALID_SESSION_STATE;
        }

    }

}
