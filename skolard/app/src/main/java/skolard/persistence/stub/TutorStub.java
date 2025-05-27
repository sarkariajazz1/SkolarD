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

    private Map<String, Tutor> tutors;           // Map to hold tutors keyed by email

    /**
     * Constructor initializes the in-memory structure and populates it with sample data.
     */
    public TutorStub() {
        confirmCreation();
        addSampleTutor();
    }

    /**
     * Ensures the tutors map is initialized before use.
     */
    private void confirmCreation() {
        if(tutors == null) {
            tutors = new HashMap<>();
        }
    }

    /**
     * Adds a sample tutor with two courses and example grades.
     * This is for demonstration and testing purposes only.
     */
    private void addSampleTutor() {
        String course1 = "COMP 1010";
        String course2 = "COMP 3350";
        ArrayList<String> courses = new ArrayList<String>();
        Map<String, String> courseGrades = new HashMap<>();

        courses.add(course1);
        courses.add(course2);

        courseGrades.put(course1, "A");
        courseGrades.put(course2, "B+");

        addTutor(new Tutor("Yab Matt", "mattyab@myumanitoba.ca",
                "", courses, courseGrades));
    }

    /**
     * Adds a tutor if a tutor with the same email already exists.
     * (This logic may need revision — typically you'd only add if email is NOT present.)
     *
     * @param tutor The tutor to add
     * @return The newly created tutor object, or null if not added
     */
    @Override
    public Tutor addTutor(Tutor tutor) {
        confirmCreation();

        // Only add the tutor if they don't already exist
        if (!tutors.containsKey(tutor.getEmail())) {
            Tutor newTutor = new Tutor(
                    tutor.getName(),
                    tutor.getEmail(),
                    tutor.getBio(),
                    tutor.getCourses(),
                    tutor.getCourseGrades()
            );
            tutors.put(newTutor.getEmail(), newTutor);
            return newTutor;
        }

        return null; // Return null if already exists (or you can choose to return existing)
    }


    /**
     * Retrieves a tutor by their email address.
     *
     * @param email The tutor's email
     * @return The Tutor object if found, else null
     */
    @Override
    public Tutor getTutorByEmail(String email) {
        confirmCreation();
        return tutors.get(email);
    }

    /**
     * Deletes a tutor from the map using their email.
     *
     * @param email Email of the tutor to be removed
     */
    @Override
    public void deleteTutorByEmail(String email) {
        confirmCreation();
        if(tutors.containsKey(email)) {
            tutors.remove(email);
        }
    }

    /**
     * Updates a tutor's record if they already exist.
     *
     * @param updatedTutor The new version of the tutor object
     */
    @Override
    public void updateTutor(Tutor updatedTutor) {
        confirmCreation();
        if(tutors.containsKey(updatedTutor.getEmail())) {
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
        confirmCreation();
        List<Tutor> tutorList = new ArrayList<>();
        for (Tutor tutor : tutors.values()) {
            tutorList.add(tutor);
        }
        return tutorList;
    }

    /**
     * Clears all tutor records from memory (optional helper).
     */
    public void close() {
        this.tutors = null;
    }
}


