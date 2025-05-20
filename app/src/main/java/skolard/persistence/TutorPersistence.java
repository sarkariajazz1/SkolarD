package skolard.persistence;

import java.util.List;

import skolard.objects.Tutor;

public interface TutorPersistence {
    List<Tutor> getAllTutors();
    Tutor addTutor(Tutor newTutor);
    void deleteTutorByEmail(String email);
    void updateTutor(Tutor updatedTutor);
}
