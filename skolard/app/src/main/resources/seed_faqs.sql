BEGIN TRANSACTION;

INSERT INTO faq (question, answer) VALUES 
("How do I book a session?", "Go to the session view and choose an available time slot."),
("How can I contact a tutor?", "Use the messaging view to send a direct message."),
("What if I need to cancel a session?", "You can cancel from your session view before it starts."),
("Is there a way to rate tutors?", "Yes, after a session, you can rate your tutor using the rating panel."),
("Can I sign up as both a tutor and a student?", "Yes, you can sign up for both roles with the same account."),
("What do I do if I forgot my password?", "Use the password recovery option on the login screen or contact support."),
("How do I update my profile?", "Go to the profile view and click 'Edit Profile'."),
("Are sessions recorded?", "No, sessions are not recorded for privacy reasons."),
("How do I give feedback?", "Use the rating panel after a session or contact support."),
("What if I don't find a suitable tutor?", "You can post a session request and tutors will reach out to you.");

COMMIT;
