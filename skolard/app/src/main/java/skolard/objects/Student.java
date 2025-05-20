package skolard.objects;

import java.util.List;

public class Student extends User {
    private List<Session> pastSessions;
    private List<Session> upcomingSessions;

    public Student(String id, String name, String email) {
        super(id, name, email);
    }

    public List<Session> getPastSessions(){
        return pastSessions;
    }

    public List<Session> getUpcomingSessions(){
        return upcomingSessions;
    }

    public void setPastSessions(List<Session> pastSessions){
        this.pastSessions = pastSessions;
    }

    public void setUpcomingSession(Session upcomingSessions){
        this.upcomingSessions.add(upcomingSessions);
    }
}
