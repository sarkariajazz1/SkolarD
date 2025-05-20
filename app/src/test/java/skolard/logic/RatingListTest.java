import java.util.List;

import skolard.logic.RatingList;
import skolard.objects.Session;
import skolard.objects.Tutor;

import static org.junit.Assert.*;

public class RatingListTest {
    private RatingList ratingList;

    @Before
    void setUp() {
        ratingList = new RatingList();
    }

    @Test
    void testSortByBestCourseRating() {
        // Arrange
        Tutor tutor1 = new Tutor("","Alice", "", "", null, null);
        tutor1.addCourseGrade("Math", "3.0");

        Tutor tutor2 = new Tutor("","Bob", "", "", null, null);
        tutor2.addCourseGrade("Math", "A+");

        Tutor tutor3 = new Tutor("","Charlie", "", "", null, null);
        tutor3.addCourseGrade("Math", "5.0");


        ratingList.addItem(tutor1);
        ratingList.addItem(tutor2);
        ratingList.addItem(tutor3);

        // Act
        List<Tutor> sorted = ratingList.sortByBestCourseRating("Math");

        // Assert
        assertEquals("Charlie", sorted.get(0).getName()); // 5.0
        assertEquals("Alice", sorted.get(1).getName());   // 3.0
        assertEquals("Bob", sorted.get(2).getName());     // 0.0
    }
}
