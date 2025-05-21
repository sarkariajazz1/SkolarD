package skolard.logic;

import java.util.ArrayList;
import java.util.Map;
import skolard.objects.Student;
import skolard.objects.Tutor;
import skolard.objects.User;
import skolard.persistence.StudentPersistence;
import skolard.persistence.TutorPersistence;
import skolard.persistence.stub.StudentStub;

/**
 * ProfileHandler manages viewing and modifying profile data
 * for users in the SkolarD platform, specifically Students and Tutors.
 */
public class ProfileHandler {
    private StudentPersistence studentPersistence;
    private TutorPersistence tutorPersistence;

    public ProfileHandler(StudentPersistence studentPersistence, TutorPersistence tutorPersistence) {
        this.studentPersistence = studentPersistence;
        this.tutorPersistence = tutorPersistence; 
    }

    public User getUser(String email) {
        User user = null;

        user = studentPersistence.getStudentByEmail(email);

        if(user == null) {
            user = tutorPersistence.getTutorByEmail(email);
        }
         
        return user;
    }

    public Student addStudent(String name, String email) {
        user.getId(),
                user.getName(),
                user.getEmail(),
                "Edit your bio..."
    }

    /**
     * Returns the basic profile information (name and email) of any user.
     * This works for both students and tutors.
     */
    public String viewBasicProfile(User user) {

        if(user != null) { 
        return "Name: " + user.getName() + "\n"
                + "Email: " + user.getEmail() + "\n";
        }

        return "";
    }

    /**
     * Displays the full profile details based on the user's type.
     * Includes:
     * - For Tutors: bio, courses taken (with grades), and average rating.
     * - For Students: number of upcoming and past sessions.
     */
    public String viewFullProfile(User user) {

        if(user != null) {
            StringBuilder sb = new StringBuilder();
            sb.append(viewBasicProfile(user)); // Start with name and email

            // If user is a tutor, display tutor-specific details
            if (user instanceof Tutor) {
                Tutor tutor = (Tutor) user;
                sb.append("Bio: ").append(tutor.getBio()).append("\n");

                // Courses represent ones the tutor has previously taken
                sb.append("Courses Taken: ").append(String.join(", ", tutor.getCourses())).append("\n");

                // Show grades for each course
                Map<String, String> courseGrades = tutor.getCourseGrades();
                if (!courseGrades.isEmpty()) {
                    sb.append("Grades: \n");
                    for (String course : courseGrades.keySet()) {
                        sb.append(" - ").append(course).append(": ").append(courseGrades.get(course)).append("\n");
                    }
                }

                // Display tutor's average rating (based on numeric grades only)
                sb.append("Average Rating: ").append(tutor.getAverageRating()).append("\n");
            }

            // If user is a student, display session-related info
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
     * Allows a tutor to update their personal bio.
     */
    public void updateBio(User user, String newBio) {

        if (user instanceof Tutor) {
            Tutor tutor = (Tutor) user;
            tutor.setBio(newBio);
            tutorPersistence.updateTutor(tutor);
        }
    }

    /**
     * Adds a new course (that the tutor has taken) along with the grade.
     * If the course already exists, the grade will be updated.
     */
    public void addTutoringCourse(User user, String course, String grade) {
        if (user instanceof Tutor) {
            Tutor tutor= (Tutor) user;
            // Add course if it's not already listed
            if (!tutor.getCourses().contains(course)) {
                tutor.getCourses().add(course);
            }
            // Add or update grade for the course
            tutor.addCourseGrade(course, grade);
        }
    }

    /**
     * Removes a course from the tutor's list of completed courses and deletes its grade.
     */
    public void removeTutoringCourse(User user, String course) {
        if (user instanceof Tutor) {
            Tutor tutor= (Tutor) user;
            tutor.getCourses().remove(course);                 // Remove course name
            tutor.getCourseGrades().remove(course);            // Remove associated grade
        }
    }

    /**
     * Promotes a basic User to a Tutor by creating a Tutor object.
     * Initializes with a blank bio, empty course list, and no grades.
     */
    public Tutor promoteToTutor(User user) {
        return new Tutor(
                user.getId(),
                user.getName(),
                user.getEmail(),
                "Edit your bio...",                  // Placeholder bio
                new ArrayList<>(),                   // Empty course list
                new java.util.HashMap<>()            // Empty course-grade mapping
        );
    }

    /**
     * Promotes a basic User to a Student by creating a Student object.
     * Initializes empty lists for past sessions (upcoming sessions are added via Session class).
     */
    public Student promoteToStudent(User user) {
        Student s = new Student(user.getId(), user.getName(), user.getEmail());
        s.setPastSessions(new ArrayList<>());    // Initialize empty past session list
        return s;
    }
}
