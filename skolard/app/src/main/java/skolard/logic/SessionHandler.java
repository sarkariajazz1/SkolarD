package skolard.logic;

import java.time.LocalDateTime;

import skolard.objects.Session;
import skolard.objects.Student;
import skolard.objects.Tutor;
import skolard.objects.User;
import skolard.persistence.SessionPersistence;

public class SessionHandler {
    private SessionPersistence sessionPersistence;

    public SessionHandler(SessionPersistence sessionPersistence){
        this.sessionPersistence = sessionPersistence;
    }

    public void createSession(User user, LocalDateTime start, LocalDateTime end, String courseName){
        if(user instanceof Tutor){
            Tutor tutor = (Tutor) user;
            sessionPersistence.addSession(new Session(-1, tutor, null, start, end, courseName));
        }
    }

    public void bookASession(User user, int sessionID){
        if(user instanceof Student){
            Student student = (Student) user;
            Session session = sessionPersistence.getSessionById(sessionID);
            session.bookSession(student);
        }
    }
}
