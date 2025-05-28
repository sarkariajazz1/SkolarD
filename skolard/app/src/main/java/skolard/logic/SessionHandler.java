package skolard.logic;

import java.time.LocalDateTime;
import java.util.List;

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
            List<Session> tutorSessions = sessionPersistence.getSessionsByTutorEmail(tutor.getEmail());

            for(int i = 0; i < tutorSessions.size(); i++){
                LocalDateTime tutorStartTime = tutorSessions.get(i).getStartDateTime();
                LocalDateTime tutorEndTime = tutorSessions.get(i).getEndDateTime();
                if(start.isAfter(tutorStartTime) && start.isBefore(tutorEndTime) 
                        || end.isAfter(tutorStartTime) && end.isBefore(tutorEndTime)){
                    throw new IllegalArgumentException("Session conflicts within existing sessions");
                } else if(tutorStartTime.isAfter(start) && tutorStartTime.isBefore(end) 
                        || tutorEndTime.isAfter(start) && tutorEndTime.isBefore(end)){
                        throw new IllegalArgumentException("Existing sessions conflicts within session");
                }
            }

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
