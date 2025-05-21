package skolard.logic;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import skolard.objects.Session;

public class TimeList extends PriorityList<Session> {

    //Default constructor
    public TimeList(){
        super();
    }

public List<Session> filterByStudentTimeRange(LocalDateTime studentStart, LocalDateTime studentEnd, String courseName) {
    if (items.isEmpty()) {
        return Collections.emptyList();
    } else if (studentStart == null || studentEnd == null || courseName == null || courseName.isEmpty()) {
        return Collections.emptyList();
    }

    return items.stream()
            .filter(session -> session.getCourseName().equalsIgnoreCase(courseName))
            .filter(session ->
                    !session.getEndDateTime().isBefore(studentStart) &&
                    !session.getStartDateTime().isAfter(studentEnd))
            .collect(Collectors.toList());
}


}



