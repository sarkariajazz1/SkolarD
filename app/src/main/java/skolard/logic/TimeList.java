package skolard.logic;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import skolard.objects.Session;

public class TimeList extends PriorityList<Session> {

    //Default constructor
    public TimeList(){
        super();
    }

    //Return sessions that are within the student's time range
    public List<Session> filterSessionsByStudentTimeRange(LocalDateTime studentStart, LocalDateTime studentEnd) {
        return items.stream()
                .filter(session ->
                        !session.getStartDateTime().isBefore(studentStart) &&
                        !session.getEndDateTime().isAfter(studentEnd))
                .collect(Collectors.toList());
    }

}

