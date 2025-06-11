package skolard.persistence.sqlite;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import skolard.objects.*;
import skolard.persistence.SessionPersistence;
import skolard.persistence.StudentPersistence;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class RatingRequestDBTest {

    private Connection mockConn;
    private PreparedStatement mockStmt;
    private ResultSet mockRs;
    private Statement mockStatement;
    private StudentPersistence mockStudentPersistence;
    private SessionPersistence mockSessionPersistence;
    private RatingRequestDB ratingRequestDB;

    private Student student;
    private Session session;

    @BeforeEach
    void setup() {
        mockConn = mock(Connection.class);
        mockStmt = mock(PreparedStatement.class);
        mockRs = mock(ResultSet.class);
        mockStatement = mock(Statement.class);
        mockStudentPersistence = mock(StudentPersistence.class);
        mockSessionPersistence = mock(SessionPersistence.class);
        ratingRequestDB = new RatingRequestDB(mockConn, mockStudentPersistence, mockSessionPersistence);

        student = new Student("Alice", "alice@example.com", "hashed123");
        Tutor tutor = mock(Tutor.class); // you may replace with real Tutor if needed
        session = new Session(1, tutor, student, LocalDateTime.now(), LocalDateTime.now().plusHours(1), "Math");
    }

    @Test
    void testAddRequest_success() throws Exception {
        RatingRequest request = new RatingRequest(0, session, student, LocalDateTime.now(), false, false);

        when(mockConn.prepareStatement(any(), anyInt())).thenReturn(mockStmt);
        when(mockStmt.getGeneratedKeys()).thenReturn(mockRs);
        when(mockRs.next()).thenReturn(true);
        when(mockRs.getInt(1)).thenReturn(42);

        RatingRequest saved = ratingRequestDB.addRequest(request);
        assertEquals(42, saved.getId());
    }

    @Test
    void testAddRequest_noGeneratedKey() throws Exception {
        RatingRequest request = new RatingRequest(0, session, student, LocalDateTime.now(), false, false);

        when(mockConn.prepareStatement(any(), anyInt())).thenReturn(mockStmt);
        when(mockStmt.getGeneratedKeys()).thenReturn(mockRs);
        when(mockRs.next()).thenReturn(false);

        assertThrows(RuntimeException.class, () -> ratingRequestDB.addRequest(request));
    }

    @Test
    void testUpdateRequest_success() throws Exception {
        RatingRequest request = new RatingRequest(1, session, student, LocalDateTime.now(), true, true);

        when(mockConn.prepareStatement(any())).thenReturn(mockStmt);

        assertDoesNotThrow(() -> ratingRequestDB.updateRequest(request));
        verify(mockStmt).executeUpdate();
    }

    @Test
    void testGetAllRequests_success() throws Exception {
        when(mockConn.createStatement()).thenReturn(mockStatement);
        when(mockStatement.executeQuery(any())).thenReturn(mockRs);
        when(mockRs.next()).thenReturn(true, false);

        when(mockRs.getInt("id")).thenReturn(1);
        when(mockRs.getInt("sessionId")).thenReturn(1);
        when(mockRs.getString("studentEmail")).thenReturn(student.getEmail());
        when(mockRs.getString("createdAt")).thenReturn(LocalDateTime.now().toString());
        when(mockRs.getInt("completed")).thenReturn(1);
        when(mockRs.getInt("skipped")).thenReturn(0);

        when(mockStudentPersistence.getStudentByEmail(any())).thenReturn(student);
        when(mockSessionPersistence.getSessionById(anyInt())).thenReturn(session);

        List<RatingRequest> requests = ratingRequestDB.getAllRequests();
        assertEquals(1, requests.size());
        assertEquals("Math", requests.get(0).getSession().getCourseName());
    }

    @Test
    void testGetPendingRequestsForStudent_success() throws Exception {
        when(mockConn.prepareStatement(any())).thenReturn(mockStmt);
        when(mockStmt.executeQuery()).thenReturn(mockRs);
        when(mockRs.next()).thenReturn(true, false);

        when(mockRs.getInt("id")).thenReturn(1);
        when(mockRs.getInt("sessionId")).thenReturn(1);
        when(mockRs.getString("studentEmail")).thenReturn(student.getEmail());
        when(mockRs.getString("createdAt")).thenReturn(LocalDateTime.now().toString());
        when(mockRs.getInt("completed")).thenReturn(0);
        when(mockRs.getInt("skipped")).thenReturn(0);

        when(mockStudentPersistence.getStudentByEmail(any())).thenReturn(student);
        when(mockSessionPersistence.getSessionById(anyInt())).thenReturn(session);

        List<RatingRequest> result = ratingRequestDB.getPendingRequestsForStudent("alice@example.com");
        assertEquals(1, result.size());
        assertEquals(student.getEmail(), result.get(0).getStudent().getEmail());
    }
}
