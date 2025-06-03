package skolard.logic.profile;

import java.util.ArrayList;
import java.util.HashMap;

import skolard.objects.Student;
import skolard.objects.Tutor;
import skolard.objects.User;
import skolard.persistence.PersistenceRegistry;
import skolard.persistence.StudentPersistence;
import skolard.persistence.TutorPersistence;
import skolard.utils.EmailUtil;

interface ProfileFormatter {
    String basic(User u);
    String full(User u);
}

class DefaultProfileFormatter implements ProfileFormatter {
    @Override
    public String basic(User u) {
        return "Name: " + u.getName() + "\n"
             + "Email: " + u.getEmail() + "\n";
    }

    @Override
    public String full(User u) {
        StringBuilder sb = new StringBuilder(basic(u));
        if (u instanceof Tutor) {
            Tutor t = (Tutor) u;
            sb.append("Bio: ").append(t.getBio()).append("\n");
            sb.append("Courses: ")
              .append(String.join(", ", t.getCourses()))
              .append("\n");
            t.getCourseGrades().forEach((c, g) ->
                sb.append(" - ").append(c).append(": ").append(g).append("\n")
            );
            sb.append("Avg Rating: ").append(t.getAverageRating()).append("\n");
        } else if (u instanceof Student) {
            Student s = (Student) u;
            sb.append("Upcoming: ")
              .append(s.getUpcomingSessions().size())
              .append("\n");
            sb.append("Past: ")
              .append(s.getPastSessions().size())
              .append("\n");
        }
        return sb.toString();
    }
}
/**
 * ProfileHandler manages viewing and modifying profile data
 * for users in the SkolarD platform, specifically Students and Tutors.
 */
public class ProfileHandler {
    private final StudentPersistence studentPersistence;
    private final TutorPersistence tutorPersistence;
    private ProfileFormatter profileFormatter;

    /**
     * Constructor using default persistence implementations.
     */
    public ProfileHandler() {
        // TO DO
        this.studentPersistence = PersistenceRegistry.getStudentPersistence();
        this.tutorPersistence = PersistenceRegistry.getTutorPersistence();
        this.profileFormatter = new DefaultProfileFormatter();
    }

    /**
     * Constructor to inject dependencies for persistence.
     * @param studentPersistence interface to manage student data.
     * @param tutorPersistence interface to manage tutor data.
     */
    public ProfileHandler(StudentPersistence studentPersistence, TutorPersistence tutorPersistence) {
        this.studentPersistence = studentPersistence;
        this.tutorPersistence = tutorPersistence; 
        this.profileFormatter = new DefaultProfileFormatter();
    }

        public ProfileHandler(StudentPersistence sp,
                          TutorPersistence tp,
                          ProfileFormatter fmt) {
        this.studentPersistence = sp;
        this.tutorPersistence   = tp;
        this.profileFormatter   = fmt;
    }

    public Tutor getTutor(String email) {
        if (!EmailUtil.isValid(email)) return null;
        return tutorPersistence.getTutorByEmail(email.trim().toLowerCase());
    }

    public Student getStudent(String email) {
        if (!EmailUtil.isValid(email)) return null;
        return studentPersistence.getStudentByEmail(email.trim().toLowerCase());
    }

    /**
     * Adds a new student to the persistence layer.
     * @param name student's name
     * @param email student's email
     */
    public void addStudent(String name, String email, String hashedPassword) {
        if (name == null || name.trim().length() < 2) {
            throw new IllegalArgumentException("Name must be at least 2 characters");
        }
        if (!EmailUtil.isValid(email)) {
            throw new IllegalArgumentException("Invalid email address");
        }
        String key = email.trim().toLowerCase();
        // if (studentPersistence.emailExists(key)) {
        //     throw new IllegalArgumentException("A student with this email already exists");
        // }
        Student newStudent = new Student(name.trim(), key,hashedPassword);
        studentPersistence.addStudent(newStudent);
    }

    /**
     * Adds a new tutor to the persistence layer.
     * @param name tutor's name
     * @param email tutor's email
     */
    public void addTutor(String name, String email, String hashedPassword) {
        if (name == null || name.trim().length() < 2) {
            throw new IllegalArgumentException("Name must be at least 2 characters");
        }
        if (!EmailUtil.isValid(email)) {
            throw new IllegalArgumentException("Invalid email address");
        }
        String key = email.trim().toLowerCase();
        // if (studentPersistence.emailExists(key)) {
        //     throw new IllegalArgumentException("A student with this email already exists");
        // }
        Tutor newTutor = new Tutor(name.trim(), key,hashedPassword, "Edit your bio...", new ArrayList<>(), new HashMap<>());            
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

    public String viewBasicProfile(User u) {
        return (u == null)
            ? ""
            : profileFormatter.basic(u);
    }

    public String viewFullProfile(User u) {
        return (u == null)
            ? ""
            : profileFormatter.full(u);
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
    public void addTutoringCourse(Tutor tutor, String course, Double grade){
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
