package skolard.logic.profile;

import skolard.objects.Student;
import skolard.objects.Tutor;
import skolard.objects.User;

/**
 * Default implementation of ProfileFormatter.
 * This class provides methods to convert Student and Tutor objects
 * into readable string representations for UI or display purposes.
 */
public class DefaultProfileFormatter implements ProfileFormatter {

    /**
     * Formats the basic profile information common to all users (name and email).
     *
     * @param user the user whose basic info is to be formatted
     * @return a formatted string containing name and email
     */
    @Override
    public String basic(User user) {
        return "Name: " + user.getName() + "\n" +
               "Email: " + user.getEmail() + "\n";
    }

    /**
     * Formats full profile details based on the type of user (Tutor or Student).
     * Adds extra fields such as bio, courses, ratings, and session counts.
     *
     * @param user the user object (can be either Tutor or Student)
     * @return a detailed formatted profile string
     */
    @Override
    public String full(User user) {
        // Start with the common basic info
        StringBuilder sb = new StringBuilder(basic(user));

        // Add tutor-specific details
        if (user instanceof Tutor tutor) {
            sb.append("Bio: ").append(tutor.getBio()).append("\n");

            // Include list of courses, if any
            if (!tutor.getCourses().isEmpty()) {
                sb.append("Courses: ").append(String.join(", ", tutor.getCourses())).append("\n");
            }

            // Include grades for each course, if available
            if (!tutor.getCoursesWithGrades().isEmpty()) {
                sb.append("Grades:\n");
                tutor.getCoursesWithGrades().forEach((course, grade) ->
                    sb.append(" - ").append(course).append(": ").append(grade).append("\n")
                );
            }

            // Append the average rating for the tutor
            sb.append("Average Rating: ").append(tutor.getAverageRating()).append("\n");

        // Add student-specific session counts
        } else if (user instanceof Student student) {
            sb.append("Upcoming Sessions: ").append(student.getUpcomingSessions().size()).append("\n");
            sb.append("Past Sessions: ").append(student.getPastSessions().size()).append("\n");
        }

        return sb.toString();
    }
}
