package nextstep.courses.domain;

import java.util.List;

public interface CourseSessionRepository {
    int save(Long courseId, List<Long> sessionIds);
    List<Long> findByCourseId(Long id);
}
