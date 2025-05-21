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
        
        // Step 1: Initialize DB connection, uses stub for iteration 1
        PersistenceFactory.initialize(PersistenceType.STUB, false);

        matchingHandler matcher = new matchingHandler(new TutorList());
        //matcher.addSession(session1);
        //To be matched with a student

        // To print results of matching

    }
}
