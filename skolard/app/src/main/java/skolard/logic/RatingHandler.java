// package skolard.logic;

// import skolard.objects.*;
// import skolard.persistence.SessionPersistence;
// import skolard.persistence.TutorPersistence;

// import java.util.ArrayList;
// import java.util.List;

// /**
//  * Handles rating submission and processing.
//  * Responsible for updating tutor/session records and posting feedback.
//  */
// public class RatingHandler {
//     private final List<RatingRequest> ratingRequests;
//     private final SessionPersistence sessionDB;
//     private final TutorPersistence tutorDB;

//     public RatingHandler(SessionPersistence sessionDB, TutorPersistence tutorDB) {
//         this.ratingRequests = new ArrayList<>();
//         this.sessionDB = sessionDB;
//         this.tutorDB = tutorDB;
//     }

//     /**
//      * Creates and stores a new rating request.
//      */
//     public void createRatingRequest(Session session, Student student) {
//         RatingRequest request = new RatingRequest(session, student);
//         ratingRequests.add(request);
//         // UI/notification logic can be triggered elsewhere
//     }

//     /**
//      * Processes a submitted rating by the student.
//      */
//     public void processRatingSubmission(RatingRequest request, int tutorRating, int courseRating, String feedback) {
//         request.submit(tutorRating, courseRating, feedback);

//         Session session = request.getSession();
//         Tutor tutor = session.getTutor();

//         // Update models
//         session.setRating(courseRating);
//         tutor.addRating(tutorRating);
//         tutor.addCourseFeedback(session.getCourseName(), feedback);

//         // Persist updated models
//         sessionDB.updateSession(session);
//         tutorDB.updateTutor(tutor);

//         // Save feedback as a persistent record
//         Feedback feedbackRecord = new Feedback(
//             session.getId(),
//             session.getCourseName(),
//             tutor.getId(),
//             request.getStudent().getId(),
//             courseRating,
//             feedback
//         );
//         sessionDB.saveFeedback(feedbackRecord);
//     }

//     /**
//      * Marks a rating request as skipped.
//      */
//     public void processRatingSkip(RatingRequest request) {
//         request.skip();
//     }

//     /**
//      * Returns all in-memory rating requests.
//      */
//     public List<RatingRequest> getAllRequests() {
//         return new ArrayList<>(ratingRequests);
//     }
// }
