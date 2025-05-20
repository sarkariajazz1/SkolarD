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

    //Return sessions that are within the student's time range
    public List<Session> filterByStudentTimeRange(LocalDateTime studentStart, LocalDateTime studentEnd, String courseName){
        if (items.isEmpty()){
            return Collections.emptyList();
        } else if (studentStart == null || studentEnd == null || courseName.equals(null) || courseName.equals("")){
            return Collections.emptyList();
        }

        return items.stream()
                .filter(session -> session.getCourseName().equalsIgnoreCase(courseName))
                .filter(session ->
                        !session.getStartDateTime().isBefore(studentStart) &&
                        !session.getEndDateTime().isAfter(studentEnd))
                .collect(Collectors.toList());
    }

}

