package skolard;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Map;
import skolard.logic.TutorList;
import skolard.logic.matchingHandler;
import skolard.objects.Student;
import skolard.objects.Tutor;


public class App {
    public static void main(String[] args) {
        LocalDateTime dateTime = LocalDateTime.now();
        Tutor tutor1 = new Tutor("t1", "Alice", "alice@example.com", "Experienced math tutor",
                new ArrayList<>(), Map.of("MATH101", "85", "CS102", "90"));

        Student student1 = new Student("s1", "David", "david@example.com");
        //Session session1 = new Session("s1", tutor1, null, dateTime, 60, "MATH101");

        matchingHandler matcher = new matchingHandler(new TutorList());
        //matcher.addSession(session1);
        //To be matched with a student

        // To print results of matching

    }
}
