package nextstep.courses.domain;

import nextstep.courses.exception.CustomException;
import nextstep.payments.domain.Payment;

public class PricingType {

    private final int sessionAmount;
    private final boolean isPremium;



    public PricingType(boolean isPremium, int sessionAmount) {
        validate(isPremium,sessionAmount);
        this.isPremium = isPremium;
        this.sessionAmount = sessionAmount;
    }

    private void validate(boolean isPremium, int sessionAmount) {
        if (isPremium && sessionAmount <= 0) {
            throw CustomException.NOT_ALLOWED_PREMIUM_AMOUNT;
        }
        if (!isPremium && sessionAmount > 0) {
            throw CustomException.NOT_ALLOWED_FREE_AMOUNT;
        }
    }

    public boolean isPremium() {
        return isPremium;
    }

    public void validateAmount(Payment payment) {
        if (isPremium && !payment.matchingAmount(sessionAmount)) {
            throw CustomException.NOT_MATCHING_SESSION_AMOUNT;
        }
    }

    public int getSessionAmount() {
        return sessionAmount;
    }

    public boolean getIsPremium() {
        return isPremium;
    }
}
