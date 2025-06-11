package skolard.persistence.sqlite;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import skolard.objects.Feedback;

import java.sql.*;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class RatingDBTest {

    private Connection mockConn;
    private PreparedStatement mockStmt;
    private ResultSet mockRs;
    private RatingDB ratingDB;

    @BeforeEach
    void setup() throws SQLException {
        mockConn = mock(Connection.class);
        mockStmt = mock(PreparedStatement.class);
        mockRs = mock(ResultSet.class);
        ratingDB = new RatingDB(mockConn);
    }

    @Test
    void testSaveRating_success() throws SQLException {
        when(mockConn.prepareStatement(any())).thenReturn(mockStmt);
        ratingDB.saveRating("tutor@example.com", 1, "student@example.com", "Math", 5);
        verify(mockStmt).executeUpdate();
    }

    @Test
    void testSaveRating_exceptionHandled() throws SQLException {
        when(mockConn.prepareStatement(any())).thenThrow(SQLException.class);
        assertDoesNotThrow(() -> ratingDB.saveRating("t", 0, "s", "c", 1));
    }

    @Test
    void testGetAllFeedbackForTutor_success() throws SQLException {
        when(mockConn.prepareStatement(any())).thenReturn(mockStmt);
        when(mockStmt.executeQuery()).thenReturn(mockRs);
        when(mockRs.next()).thenReturn(true, false);
        when(mockRs.getInt("sessionId")).thenReturn(1);
        when(mockRs.getString("courseName")).thenReturn("Math");
        when(mockRs.getString("tutorEmail")).thenReturn("t@example.com");
        when(mockRs.getString("studentEmail")).thenReturn("s@example.com");
        when(mockRs.getInt("rating")).thenReturn(4);

        List<Feedback> feedbacks = ratingDB.getAllFeedbackForTutor("t");
        assertEquals(1, feedbacks.size());
        assertEquals("Math", feedbacks.get(0).getCourseName());
    }

    @Test
    void testGetAllFeedbackForTutor_sqlExceptionHandled() throws SQLException {
        when(mockConn.prepareStatement(any())).thenThrow(SQLException.class);
        List<Feedback> feedbacks = ratingDB.getAllFeedbackForTutor("t");
        assertTrue(feedbacks.isEmpty());
    }
}
