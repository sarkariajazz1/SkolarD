package skolard.utils;

import skolard.objects.Message;

public final class MessageUtil {
    
    public static boolean validMessage(Message message) {
        String studentEmail = message.getStudentEmail();
        String tutorEmail = message.getTutorEmail();
        String senderEmail = message.getSenderEmail();

        boolean valid = !studentEmail.equalsIgnoreCase(tutorEmail) &&
            (studentEmail.equals(senderEmail) || tutorEmail.equals(senderEmail)) &&
            (message.getMessage().length() > 0) && 
            EmailUtil.isValid(studentEmail) && EmailUtil.isValid(tutorEmail);

        return valid;
    }  
}
