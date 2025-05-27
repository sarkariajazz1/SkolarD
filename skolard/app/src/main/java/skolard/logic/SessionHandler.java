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
            //Discuss how session IDs should be made and how to access SessionStub generateID method
            sessionPersistence.addSession(new Session(-1, tutor, null, start, end, courseName));
        }
    }

    public void bookASession(User user, int sessionID){
        if(user instanceof Student){
            Student student = (Student) user;
            Session session = sessionPersistence.getSessionById(sessionID);
            if(!session.isBooked()){
                session.bookSession(student);
            } else{
                if(session.getStudent().equals(user)){
                    throw new IllegalArgumentException("Session is already booked");
                } else{
                    throw new IllegalArgumentException("Session is already booked by someone else");
                }
            }
            
        }
    }
}
