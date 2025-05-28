package skolard.logic;

import java.util.Map;
import skolard.objects.Student;
import skolard.objects.Tutor;
import skolard.objects.User;
import skolard.persistence.PersistenceFactory;
import skolard.persistence.StudentPersistence;
import skolard.persistence.TutorPersistence;

/**
 * ProfileHandler manages viewing and modifying profile data
 * for users in the SkolarD platform, specifically Students and Tutors.
 */
public class ProfileHandler {
    private StudentPersistence studentPersistence;
    private TutorPersistence tutorPersistence;

    /**
     * Constructor using default persistence implementations.
     */
    public ProfileHandler() {
        // TO DO
        this.studentPersistence = PersistenceFactory.getStudentPersistence();
        this.tutorPersistence = PersistenceFactory.getTutorPersistence();
    }

    /**
     * Constructor to inject dependencies for persistence.
     * @param studentPersistence interface to manage student data.
     * @param tutorPersistence interface to manage tutor data.
     */
    public ProfileHandler(StudentPersistence studentPersistence, TutorPersistence tutorPersistence) {
        this.studentPersistence = studentPersistence;
        this.tutorPersistence = tutorPersistence; 
    }



    /**
     * Attempts to fetch a tutor by email.
     * @param email email of the user.
     * @return Tutor object if found, null otherwise.
     */
    public Tutor getTutor(String email) {
        Tutor tutor = null;
        if (!email.equals(null) || !email.isEmpty()) {
            tutor = tutorPersistence.getTutorByEmail(email.toLowerCase());
        }
        return tutor;
    }

    /**
     * Attempts to fetch a student by email.
     * @param email email of the user.
     * @return Student object if found, null otherwise.
     */
    public Student getStudent(String email){
        Student student = null;
        if(!email.equals(null) || !email.isEmpty()){
            student = studentPersistence.getStudentByEmail(email.toLowerCase());
        }

        return student;
    }

    /**
     * Adds a new student to the persistence layer.
     * @param name student's name
     * @param email student's email
     */
    public void addStudent(String name, String email) {
        Student newStudent = new Student(name, email);
        studentPersistence.addStudent(newStudent);
    }

    /**
     * Adds a new tutor to the persistence layer.
     * @param name tutor's name
     * @param email tutor's email
     */
    public void addTutor(String name, String email) {
        Tutor newTutor = new Tutor(name, email, "Edit your bio...");
        tutorPersistence.addTutor(newTutor);
    }

    /**
     * Updates a tutor's profile in the persistence layer.
     */
    public void updateTutor(Tutor updatedTutor) {
        tutorPersistence.updateTutor(updatedTutor);
    }

    /**
     * Updates a student's profile in the persistence layer.
     */
    public void updateStudent(Student updatedStudent) {
        studentPersistence.updateStudent(updatedStudent);
    }

    /**
     * Returns a user's basic profile: name and email.
     * @param user User instance (either student or tutor)
     * @return formatted string or empty string if null
     */
    public String viewBasicProfile(User user) {
        if (user != null) {
            return "Name: " + user.getName() + "\n"
                 + "Email: " + user.getEmail() + "\n";
        }
        return "";
    }

    /**
     * Returns a user's full profile depending on type (Tutor or Student).
     * @param user User object
     * @return a detailed string with all relevant profile data
     */
    public String viewFullProfile(User user) {
        if (user != null) {
            StringBuilder sb = new StringBuilder();
            sb.append(viewBasicProfile(user)); // common info

            // Tutor-specific fields
            if (user instanceof Tutor) {
                Tutor tutor = (Tutor) user;
                sb.append("Bio: ").append(tutor.getBio()).append("\n");
                sb.append("Courses Taken: ").append(String.join(", ", tutor.getCourses())).append("\n");

                Map<String, String> courseGrades = tutor.getCourseGrades();
                if (!courseGrades.isEmpty()) {
                    sb.append("Grades: \n");
                    for (String course : courseGrades.keySet()) {
                        sb.append(" - ").append(course)
                          .append(": ").append(courseGrades.get(course)).append("\n");
                    }
                }

                sb.append("Average Rating: ").append(tutor.getAverageRating()).append("\n");
            }

            // Student-specific fields
            if (user instanceof Student) {
                Student s = (Student) user;
                int upcoming = s.getUpcomingSessions() != null ? s.getUpcomingSessions().size() : 0;
                int past = s.getPastSessions() != null ? s.getPastSessions().size() : 0;
                sb.append("Upcoming Sessions: ").append(upcoming).append("\n");
                sb.append("Past Sessions: ").append(past).append("\n");
            }

            return sb.toString();
        }
        return "";
    }

    /**
     * Updates a tutor's bio if the user is an instance of Tutor.
     * @param user User instance
     * @param newBio updated biography string
     */
    public void updateBio(User user, String newBio) {
        if (user instanceof Tutor) {
            Tutor tutor = (Tutor) user;
            tutor.setBio(newBio);
            tutorPersistence.updateTutor(tutor); // persist change
        }
    }

    /**
     * Adds or updates a course and grade for a tutor.
     * @param user User instance
     * @param course course name
     * @param grade grade received
     */
    public void addTutoringCourse(Tutor tutor, String course, String grade){
        String courseAllLower = course.toLowerCase();
        
        if (!tutor.getCourses().contains(courseAllLower)) {
            tutor.getCourses().add(courseAllLower);
        }
        tutor.addCourseGrade(courseAllLower, grade);
    }

    /**
     * Removes a course and its grade from a tutor’s profile.
     * @param user User instance
     * @param course course name to be removed
     */
    public void removeTutoringCourse(Tutor tutor, String course) {
        tutor.getCourses().remove(course.toLowerCase());
        tutor.getCourseGrades().remove(course.toLowerCase());
    }
}
