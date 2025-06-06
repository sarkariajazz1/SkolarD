package skolard.logic.profile;

import skolard.objects.Student;
import skolard.objects.Tutor;
import skolard.objects.User;

/**
 * Default implementation of ProfileFormatter.
 * Formats Tutor and Student profiles as readable text.
 */
public class DefaultProfileFormatter implements ProfileFormatter {

    @Override
    public String basic(User user) {
        return "Name: " + user.getName() + "\n" +
               "Email: " + user.getEmail() + "\n";
    }

    @Override
    public String full(User user) {
        StringBuilder sb = new StringBuilder(basic(user));

        if (user instanceof Tutor tutor) {
            sb.append("Bio: ").append(tutor.getBio()).append("\n");

            if (!tutor.getCourses().isEmpty()) {
                sb.append("Courses: ").append(String.join(", ", tutor.getCourses())).append("\n");
            }

            if (!tutor.getCoursesWithGrades().isEmpty()) {
                sb.append("Grades:\n");
                tutor.getCoursesWithGrades().forEach((course, grade) ->
                    sb.append(" - ").append(course).append(": ").append(grade).append("\n")
                );
            }

            sb.append("Average Rating: ").append(tutor.getAverageRating()).append("\n");

        } else if (user instanceof Student student) {
            sb.append("Upcoming Sessions: ").append(student.getUpcomingSessions().size()).append("\n");
            sb.append("Past Sessions: ").append(student.getPastSessions().size()).append("\n");
        }

        return sb.toString();
    }
}
