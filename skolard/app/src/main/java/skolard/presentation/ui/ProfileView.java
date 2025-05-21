import javax.swing.*;
import java.awt.*;
import java.util.List;
import skolard.presentation.ProfileController;  // the façade we wrote
import skolard.objects.Student;

public class ProfileView extends JFrame {
    private final ProfileController ctrl = new ProfileController();
    private final DefaultListModel<String> listModel = new DefaultListModel<>();
    // ... other Swing components ...

    public ProfileView() {
        super("Edit Profiles");
        // init UI (BorderLayout, JList, JButton, etc.)
        loadList();
        attachListeners();
    }

    private void loadList() {
        for (Student s : ctrl.getAllStudents()) {
            listModel.addElement(s.getEmail());
        }
    }
    // ...
}
