package skolard.logic;

public class ProfileHandlerIntegrationTest {

    private ProfileHandler profileHandler;

    // @Before
    // public void setUp() throws IOException {
    //     // Use test or stubbed persistence layer
    //     PersistenceFactory.initialize(PersistenceType.TEST, false);
    //     profileHandler = new ProfileHandler(
    //         PersistenceFactory.getStudentPersistence(),
    //         PersistenceFactory.getTutorPersistence()
    //     );
    // }

    // @After
    // public void tearDown() throws IOException {
    //     PersistenceFactory.reset();
    // }

    // @Test
    // public void testViewBasicAndFullTutorProfile() {
    //     // Add a tutor
    //     profileHandler.addTutor("Jane Doe", "jane@skolard.ca");

    //     Tutor tutor = profileHandler.getTutor("jane@skolard.ca");
    //     assertNotNull(tutor);
    //     assertEquals("Jane Doe", tutor.getName());

    //     // Update profile with some course and grades
    //     tutor.setBio("Experienced Java developer");
    //     tutor.setCourses(Arrays.asList("comp2150", "comp1010"));
    //     tutor.setCourseGrades(new HashMap<String, Double>() {{
    //         put("comp2150", 4.5);
    //         put("comp1010", 4.2);
    //     }});
    //     //tutor.addRating(5); // one 5-star rating

    //     profileHandler.updateTutor(tutor);

    //     // View basic profile
    //     String basic = profileHandler.viewBasicProfile(tutor);
    //     assertTrue(basic.contains("Name: Jane Doe"));
    //     assertTrue(basic.contains("Email: jane@skolard.ca"));

    //     // View full profile
    //     String full = profileHandler.viewFullProfile(tutor);
    //     assertTrue(full.contains("Bio: Experienced Java developer"));
    //     assertTrue(full.contains("Courses: comp2150, comp1010"));
    //     assertTrue(full.contains("comp2150: 4.5"));
    //     assertTrue(full.contains("Avg Rating: 5.0"));
    // }

    // @Test
    // public void testViewBasicAndFullStudentProfile() {
    //     // Add a student
    //     profileHandler.addStudent("Alice", "alice@skolard.ca");

    //     Student student = profileHandler.getStudent("alice@skolard.ca");
    //     assertNotNull(student);
    //     assertEquals("Alice", student.getName());

    //     // View basic profile
    //     String basic = profileHandler.viewBasicProfile(student);
    //     assertTrue(basic.contains("Name: Alice"));
    //     assertTrue(basic.contains("Email: alice@skolard.ca"));

    //     // View full profile (session counts should be 0)
    //     String full = profileHandler.viewFullProfile(student);
    //     assertTrue(full.contains("Upcoming: 0"));
    //     assertTrue(full.contains("Past: 0"));
    // }
}
