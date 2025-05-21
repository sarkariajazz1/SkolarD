package skolard.persistence;

import java.util.List;
import skolard.objects.Tutor;

/**
 * Interface that defines CRUD operations for tutor data.
 * Enables the system to manage tutors and their course information.
 */
public interface TutorPersistence {

    /**
     * Get all registered tutors in the system.
     *
     * @return list of all tutors
     */
    List<Tutor> getAllTutors();

    Tutor getTutorByEmail(String email);

    /**
     * Add a new tutor to the system.
     *
     * @param newTutor tutor to add
     * @return the added tutor (with any modifications applied)
     */
    Tutor addTutor(Tutor newTutor);

    /**
     * Remove a tutor from the system using their email.
     *
     * @param email email of the tutor to delete
     */
    void deleteTutorByEmail(String email);

    /**
     * Update the information of an existing tutor.
     *
     * @param updatedTutor updated tutor object
     */
    void updateTutor(Tutor updatedTutor);
}

