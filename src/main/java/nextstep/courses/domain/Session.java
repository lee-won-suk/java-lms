package nextstep.courses.domain;

import nextstep.courses.exception.CustomException;
import nextstep.courses.domain.image.SessionImage;
import nextstep.payments.domain.Payment;

import java.util.ArrayList;
import java.util.List;

public class Session {
    private Long id;
    private List<Long> students;
    private PricingType pricingType;
    private SessionState state;
    private SessionImage image;
    private int maxStudentCount;
    private SessionDate date;


    public Session(PricingType pricingType, int maxStudentCount, SessionState state, SessionDate date
            , SessionImage image) {
        this(0L, new ArrayList<Long>(),pricingType,state,image,maxStudentCount,date);
    }

    public Session(List<Long> students, PricingType pricingType, SessionState state, SessionImage image, int maxStudentCount, SessionDate date) {
        this.id = 0L;
        this.students = students;
        this.pricingType = pricingType;
        this.state = state;
        this.image = image;
        this.maxStudentCount = maxStudentCount;
        this.date = date;
    }

    public Session(Long id, List<Long> students, PricingType pricingType, SessionState state, SessionImage image, int maxStudentCount, SessionDate date) {
        this.id = id;
        this.students = students;
        this.pricingType = pricingType;
        this.state = state;
        this.image = image;
        this.maxStudentCount = maxStudentCount;
        this.date = date;
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

    public List<Long> getStudents() {
        return students;
    }

    public PricingType getPricingType() {
        return pricingType;
    }

    public SessionState getState() {
        return state;
    }

    public SessionImage getImage() {
        return image;
    }

    public int getMaxStudentCount() {
        return maxStudentCount;
    }

    public SessionDate getDate() {
        return date;
    }

}
