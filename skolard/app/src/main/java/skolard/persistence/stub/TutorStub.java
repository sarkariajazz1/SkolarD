package skolard.persistence.stub;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import skolard.objects.Tutor;
import skolard.persistence.TutorPersistence;

public class TutorStub implements TutorPersistence {
    private Map<String, Tutor> tutors;
    private static int uniqueID = 0;

    public TutorStub() {
        confirmCreation();

        addSampleTutor();
    }

    private void confirmCreation() {
        if(tutors == null) {
            tutors = new HashMap<>();
        }
    }

    private void addSampleTutor() {
        String course1 = "COMP 1010";
        String course2 = "COMP 3350";
        ArrayList<String> courses = new ArrayList<String>();
        Map<String, String> courseGrades = new HashMap<>();

        courses.add(course1);
        courses.add(course2);

        courseGrades.put(course1, "A");
        courseGrades.put(course2, "B+");

        addTutor(new Tutor("" + uniqueID++, "Yab Matt", "mattyab@myumanitoba.ca",
                "", courses, courseGrades));
    }

    public Tutor addTutor(Tutor tutor) {
        confirmCreation();

        Tutor newTutor = null;

        if(tutors.containsKey(tutor.getEmail())) {
            newTutor = new Tutor("" + uniqueID++, tutor.getName(), tutor.getEmail(),
                    tutor.getBio(), tutor.getCourses(), tutor.getCourseGrades());

            tutors.put(newTutor.getEmail(), newTutor);
        }

        return newTutor;
    }

    @Override
    public Tutor getTutorByEmail(String email) {
        confirmCreation();
        return tutors.get(email);
    }

    @Override
    public void deleteTutorByEmail(String email) {
        confirmCreation();

        if(tutors.containsKey(email)) {
            tutors.remove(email);
        }
    }


    @Override
    public void updateTutor(Tutor updatedTutor) {
        confirmCreation();

        if(tutors.containsKey(updatedTutor.getEmail())) {
            tutors.replace(updatedTutor.getEmail(), updatedTutor);
        }
    }

    @Override
    public List<Tutor> getAllTutors() {
        confirmCreation();

        List<Tutor> tutorList = new ArrayList<Tutor>();
        for (Tutor tutor : tutors.values()) {
            tutorList.add(tutor);
        }
        return tutorList;
    }

    public void close() {
        this.tutors = null;
    }
}