package skolard.objects;

/**
 * Represents a Frequently Asked Question (FAQ) with a question and its answer.
 */
public class FAQ {
    // The question being asked
    private final String question;

    // The answer to the question
    private final String answer;

    /**
     * Constructs an FAQ with the given question and answer.
     *
     * @param question the FAQ question
     * @param answer   the answer to the question
     */
    public FAQ(String question, String answer) {
        this.question = question;
        this.answer = answer;
    }

    /**
     * Returns the question text.
     *
     * @return the question
     */
    public String getQuestion() {
        return question;
    }

    /**
     * Returns the answer text.
     *
     * @return the answer
     */
    public String getAnswer() {
        return answer;
    }

    /**
     * Returns a formatted string representation of the FAQ.
     *
     * @return formatted question and answer
     */
    @Override
    public String toString() {
        return "Q: " + question + "\nA: " + answer;
    }
}