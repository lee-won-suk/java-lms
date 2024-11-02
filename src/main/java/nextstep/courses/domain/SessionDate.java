package nextstep.courses.domain;

import nextstep.courses.exception.CustomException;

import java.time.LocalDateTime;

public class SessionDate {
    private LocalDateTime startDate;
    private LocalDateTime endDate;

    public SessionDate(LocalDateTime startDate, LocalDateTime endDate) {
        if (startDate.isAfter(endDate)) {
            throw CustomException.NOT_ALLOWED_DATE;
        }
        this.startDate = startDate;
        this.endDate = endDate;
    }
}
