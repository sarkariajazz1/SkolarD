package skolard.logic.profile;

import org.junit.jupiter.api.Test;
import skolard.objects.Student;
import skolard.objects.Tutor;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class DefaultProfileFormatterTest {

    private final DefaultProfileFormatter formatter = new DefaultProfileFormatter();

    @Test
    void testBasicFormat() {
        Student s = new Student("Sam", "sam@skolard.ca", "pass");
        String result = formatter.basic(s);
        assertTrue(result.contains("Name: Sam"));
        assertTrue(result.contains("Email: sam@skolard.ca"));
    }

    @Test
    void testFullFormatForTutor() {
        Tutor t = new Tutor("Amrit", "amrit@skolard.ca", "hash", "Physics tutor", Map.of("PHYS1050", 92.0));
        String result = formatter.full(t);
        assertTrue(result.contains("Bio: Physics tutor"));
        assertTrue(result.contains("Courses: PHYS1050"));
        assertTrue(result.contains("Average Rating: 92.0"));
    }

    @Test
    void testFullFormatForStudent() {
        Student s = new Student("Simran", "simran@skolard.ca", "hash");
        String result = formatter.full(s);
        assertTrue(result.contains("Upcoming Sessions: 0"));
        assertTrue(result.contains("Past Sessions: 0"));
    }
}
