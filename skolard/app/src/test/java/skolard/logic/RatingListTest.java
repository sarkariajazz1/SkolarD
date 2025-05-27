// package skolard.logic;

// import java.util.ArrayList;
// import java.util.List;

// import static org.junit.Assert.assertEquals;
// import static org.junit.Assert.assertNotNull;
// import static org.junit.Assert.assertTrue;
// import org.junit.Before;
// import org.junit.Test;

// import skolard.objects.Session;
// import skolard.objects.Tutor;

// public class RatingListTest {

//     private List<Session> sessionPool;

//     @Before
//     public void setUp() {
//         sessionPool = new ArrayList<>();
//     }

//     // Helper to create a Tutor with a single course and grade
//     private Tutor createTutor(String name, String course, String grade) {
//         Tutor tutor = new Tutor(name, name + "@mail.com", "Test Bio");
//         tutor.addCourseGrade(course, grade);
//         return tutor;
//     }

//     // Helper to create a Session object
//     private Session createSession(int id, Tutor tutor, String course) {
//         return new Session(id, tutor, null, null, null, course);
//     }

//     @Test
//     public void testSortByBestCourseRating_ValidAndInvalidGrades() {
//         Tutor tutor1 = createTutor("Alice", "Math", "3.0");
//         Tutor tutor2 = createTutor("Bob", "Math", "A");    // Non-numeric
//         Tutor tutor3 = createTutor("Charlie", "Math", "5.0");

//         sessionPool.add(createSession(1, tutor1, "Math"));
//         sessionPool.add(createSession(2, tutor2, "Math"));
//         sessionPool.add(createSession(3, tutor3, "Math"));

//         RatingList ratingList = new RatingList(sessionPool);
//         List<Session> sorted = ratingList.sortByBestCourseRating("Math");

//         assertEquals(3, sorted.size());
//         assertEquals("Charlie", sorted.get(0).getTutor().getName());
//         assertEquals("Alice", sorted.get(1).getTutor().getName());
//         assertEquals("Bob", sorted.get(2).getTutor().getName()); // fallback rating
//     }

//     @Test
//     public void testSortByBestCourseRating_MissingGradeFilteredOut() {
//         Tutor tutor1 = createTutor("Alice", "Math", "3.0");
//         Tutor tutor2 = createTutor("Bob", "Math", "N/A");

//         sessionPool.add(createSession(1, tutor1, "Math"));
//         sessionPool.add(createSession(2, tutor2, "Math"));

//         RatingList ratingList = new RatingList(sessionPool);
//         List<Session> sorted = ratingList.sortByBestCourseRating("Math");

//         assertEquals(1, sorted.size());
//         assertEquals("Alice", sorted.get(0).getTutor().getName());
//     }

//     @Test
//     public void testSortByBestCourseRating_AllInvalidGrades() {
//         Tutor tutor1 = createTutor("Alice", "Math", "B");
//         Tutor tutor2 = createTutor("Bob", "Math", "A");

//         sessionPool.add(createSession(1, tutor1, "Math"));
//         sessionPool.add(createSession(2, tutor2, "Math"));

//         RatingList ratingList = new RatingList(sessionPool);
//         List<Session> sorted = ratingList.sortByBestCourseRating("Math");

//         assertEquals(2, sorted.size());
//         assertEquals("Alice", sorted.get(0).getTutor().getName()); // original order preserved
//         assertEquals("Bob", sorted.get(1).getTutor().getName());
//     }

//     @Test
//     public void testSortByBestCourseRating_EmptyList() {
//         RatingList ratingList = new RatingList(sessionPool); // still empty
//         List<Session> sorted = ratingList.sortByBestCourseRating("Math");

//         assertNotNull(sorted);
//         assertTrue(sorted.isEmpty());
//     }

//     @Test
//     public void testSortByBestCourseRating_NoCourseMatch() {
//         Tutor tutor = createTutor("Alice", "COMP2140", "4.0");
//         sessionPool.add(createSession(1, tutor, "COMP2140"));

//         RatingList ratingList = new RatingList(sessionPool);
//         List<Session> sorted = ratingList.sortByBestCourseRating("Math");

//         assertTrue(sorted.isEmpty());
//     }

//     @Test
//     public void testSortByBestCourseRating_NullCourseName() {
//         Tutor tutor = createTutor("Alice", "Math", "4.0");
//         sessionPool.add(createSession(1, tutor, "Math"));

//         RatingList ratingList = new RatingList(sessionPool);
//         List<Session> sorted = ratingList.sortByBestCourseRating(null);

//         assertTrue(sorted.isEmpty());
//     }

//     @Test
//     public void testSortByBestCourseRating_EmptyCourseName() {
//         Tutor tutor = createTutor("Alice", "Math", "4.0");
//         sessionPool.add(createSession(1, tutor, "Math"));

//         RatingList ratingList = new RatingList(sessionPool);
//         List<Session> sorted = ratingList.sortByBestCourseRating("");

//         assertTrue(sorted.isEmpty());
//     }
// }
