BEGIN TRANSACTION;

INSERT OR IGNORE INTO faq (question, answer) VALUES 
("How do I book a tutoring session?",
 "After logging in as a student, navigate to the 'Sessions' tab, choose an available time slot from a tutor's schedule, and click 'Book'."),
 
("How do I contact my tutor?",
 "Use the 'Messages' section to find your tutor and send a direct message. Communication is done entirely within the app."),

("Can I cancel a session I booked?",
 "Currently, session cancellations must be manually arranged with the tutor via messaging. A proper cancellation system is being considered for future updates."),

("How do I become a tutor on the platform?",
 "You must register as a tutor and provide your course history along with past grades. Once approved, you can start listing your available sessions."),

("Can tutors message students?",
 "Yes. Tutors can view and respond to messages sent by students. Communication is always logged in the Messages view."),

("How do tutors list available sessions?",
 "After logging in, tutors can go to the 'Sessions' section and add new time slots they are available for tutoring."),

("Is there a rating system for tutors?",
 "Yes. After a session, students can rate their tutor. This helps maintain quality and trust in the platform."),

("What do I do if I have a technical issue?",
 "Use the Support tab to submit a support ticket. Our support staff will respond as soon as possible."),

("What payment methods are accepted?",
 "Currently, we accept major credit cards, which can be added under the 'Card' section in your profile settings."),

("Do I need to create separate accounts to be a tutor and a student?",
 "No. A single account can act as both a tutor and a student, depending on your role during a session."),

("How do I view my upcoming sessions?",
 "Go to the 'Sessions' tab. All confirmed sessions will appear there with details on date, time, and participants."),

("What happens if I miss a session?",
 "Please notify the other party through the messaging system. Repeated no-shows may affect your account status.");

COMMIT;
