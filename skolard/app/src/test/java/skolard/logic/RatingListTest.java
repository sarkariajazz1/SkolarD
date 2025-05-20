package skolard.logic;//package solr.logic;

import skolard.logic.RatingList;
import skolard.objects.Session;
import skolard.objects.Tutor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class RatingListTest {
    private RatingList ratingList;

    @Before
    public void setUp() {
        ratingList = new RatingList();
    }

    @Test
    public void testSortByBestCourseRating() {
        // Arrange
        Tutor tutor1 = new Tutor("","Alice", "", "");
        tutor1.addCourseGrade("Math", "3.0");

        Tutor tutor2 = new Tutor("","Bob", "", "");
        tutor2.addCourseGrade("Math", "A");

        Tutor tutor3 = new Tutor("","Charlie", "", "");
        tutor3.addCourseGrade("Math", "5.0");

        Session session1 = new Session(null, tutor1, null, null, null, null);
        Session session2 = new Session(null, tutor2, null, null, null,null);
        Session session3 = new Session(null, tutor3, null, null, null,null);

        ratingList.addItem(session1);
        ratingList.addItem(session2);
        ratingList.addItem(session3);

        // Act
        List<Session> sorted = ratingList.sortByBestCourseRating("Math");

        // Assert
        assertEquals("Charlie", sorted.get(0).getTutor().getName()); // 5.0
        assertEquals("Alice", sorted.get(1).getTutor().getName());   // 3.0
        assertEquals("Bob", sorted.get(2).getTutor().getName());     // 1.0
    }
}
