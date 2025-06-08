package skolard.persistence.stub;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import skolard.objects.Tutor;
import skolard.persistence.TutorPersistence;

/**
 * In-memory stub implementation of TutorPersistence.
 * This simulates storing tutor data without needing a real database.
 */
public class TutorStub implements TutorPersistence {

    private Map<String, Tutor> tutors; // Map to hold tutors keyed by email

    /**
     * Constructor initializes the in-memory structure and populates it with sample data.
     */
    public TutorStub() {
        tutors = new HashMap<>();
        addSampleTutors();
    }

    /**
     * Basic stub hash method for passwords (development use only).
     */
    private String hash(String plain) {
        return Integer.toHexString(plain.hashCode());
    }

    /**
     * Adds a sample tutor for demonstration and testing purposes.
     */
    private void addSampleTutors() {
        String course1 = "COMP 1010";
        String course2 = "COMP 3350";

        Map<String, Double> courses = new HashMap<>();
        courses.put(course1, 4.0);
        courses.put(course2, 3.5);

        Tutor sampleTutor = new Tutor(
                "Yab Matt",
                "mattyab@myumanitoba.ca",
                hash("pass123"),
                "Experienced in Java and C++ tutoring.",
                courses
        );

        addTutor(sampleTutor);
    }

    /**
     * Adds a tutor if their email is not already registered.
     *
     * @param tutor The tutor to add
     * @return The added tutor, or null if one with same email exists
     */
    @Override
    public Tutor addTutor(Tutor tutor) {
        if (!tutors.containsKey(tutor.getEmail())) {
            tutors.put(tutor.getEmail(), tutor);
            return tutor;
        }

        return null;
    }

    /**
     * Retrieves a tutor by their email address.
     *
     * @param email The tutor's email
     * @return The Tutor object if found, else null
     */
    @Override
    public Tutor getTutorByEmail(String email) {
        return tutors.get(email);
    }

    /**
     * Deletes a tutor from the map using their email.
     *
     * @param email Email of the tutor to be removed
     */
    @Override
    public void deleteTutorByEmail(String email) {
        tutors.remove(email);
    }

    /**
     * Updates a tutor's record if they already exist.
     *
     * @param updatedTutor The new version of the tutor object
     */
    @Override
    public void updateTutor(Tutor updatedTutor) {
        if (tutors.containsKey(updatedTutor.getEmail())) {
            tutors.replace(updatedTutor.getEmail(), updatedTutor);
        }
    }

    /**
     * Returns a list of all tutors currently stored in memory.
     *
     * @return List of Tutor objects
     */
    @Override
    public List<Tutor> getAllTutors() {
        return new ArrayList<>(tutors.values());
    }

    @Override
    public void addCourseToTutor(Tutor tutor, String course, Double grade) {
        if(course != null && grade != null) {
            tutor.addCourse(course, grade);
            updateTutor(tutor);
        }
    }

    @Override
    public void removeCourseFromTutor(Tutor tutor, String course) {
        Map<String, Double> courses = new HashMap<>();
        if(course != null) {
            courses = tutor.getCoursesWithGrades();
            courses.remove(course);
            tutor = new Tutor(tutor.getName(), tutor.getEmail(), 
                tutor.getHashedPassword(), tutor.getBio(), courses);
            updateTutor(tutor);
        }
    }

    /**
     * Authenticates a tutor by comparing stored and provided hashed passwords.
     *
     * @param email          Tutor's email
     * @param hashedPassword Hashed password to check
     * @return Tutor if credentials match, otherwise null
     */
    @Override
    public Tutor authenticate(String email, String hashedPassword) {
        Tutor tutor = tutors.get(email);
        if (tutor != null && tutor.getHashedPassword().equals(hashedPassword)) {
            return tutor;
        }
        return null;
    }
}
