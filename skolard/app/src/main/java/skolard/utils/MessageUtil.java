package skolard.utils;

import skolard.objects.Message;

public final class MessageUtil {

    /**
     * Validates a {@link Message} object to ensure it meets specific criteria for a valid SkolarD message.
     * A message is considered valid if:
     * <ul>
     * <li>The student's email and tutor's email are not the same (case-insensitive).</li>
     * <li>The sender's email is either the student's email or the tutor's email.</li>
     * <li>The message content is not empty.</li>
     * <li>Both the student's and tutor's emails are valid according to {@link EmailUtil#isValid(String)}.</li>
     * </ul>
     *
     * @param message The {@link Message} object to be validated.
     * @return {@code true} if the message is valid according to the criteria, {@code false} otherwise.
     */
    public static boolean validMessage(Message message) {
        // Extract relevant email addresses and message content from the Message object.
        String studentEmail = message.getStudentEmail();
        String tutorEmail = message.getTutorEmail();
        String senderEmail = message.getSenderEmail();

        // Perform validation checks:
        // 1. Student and tutor emails must be different (case-insensitive comparison).
        // 2. The sender must be either the student or the tutor.
        // 3. The message content must not be empty.
        // 4. Both student and tutor emails must be in a valid format using EmailUtil.
        boolean valid = !studentEmail.equalsIgnoreCase(tutorEmail) &&
            (studentEmail.equals(senderEmail) || tutorEmail.equals(senderEmail)) &&
            (message.getMessage().length() > 0) &&
            EmailUtil.isValid(studentEmail) && EmailUtil.isValid(tutorEmail);

        return valid; // Return the result of the validation.
    }
}