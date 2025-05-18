package skolard.logic;

import skolard.objects.Session;
import skolard.objects.Student;

public class matchingHandler {
    // main decision making class
    // this class will be used to match the tutor and student   
    private PriorityList<Session> availableSessions;

    public matchingHandler(){
        //Can be instance of any other class that extends PriorityList
        this.availableSessions = new TutorList();
    }

    public matchingHandler(PriorityList<Session> sessionList){
        this.availableSessions = sessionList;
    }

    public void addSession(Session session) {
        if (session == null) {
            throw new IllegalArgumentException("Session cannot be null.");
        }
        availableSessions.addItem(session);
    }
    //TODO: implement the logic to match the student to the tutor
    public void matchStudentToTutor(Student student, String courseName){}

    
}
