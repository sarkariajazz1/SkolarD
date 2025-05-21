package skolard.presentation.ui;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import skolard.presentation.ProfileController;
import skolard.objects.Student;
import skolard.objects.Tutor;

public class ProfileView extends JFrame {
    private final ProfileController ctrl = new ProfileController();

    private final DefaultListModel<String> studentModel = new DefaultListModel<>();
    private final DefaultListModel<String> tutorModel = new DefaultListModel<>();

    public ProfileView() {
        super("Profile Manager");
        initModels();
        setContentPane(createMainPane());
        pack();
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setVisible(true);
    }

    private void initModels() {
        // Load students
        List<Student> students = ctrl.getAllStudents();
        for (Student s : students) {
            studentModel.addElement(formatStudent(s));
        }
        // Load tutors
        List<Tutor> tutors = ctrl.getAllTutors();
        for (Tutor t : tutors) {
            tutorModel.addElement(formatTutor(t));
        }
    }

    private JPanel createMainPane() {
        JTabbedPane tabs = new JTabbedPane();
        tabs.addTab("Students", createListPanel(studentModel, "Student"));
        tabs.addTab("Tutors", createListPanel(tutorModel, "Tutor"));

        JPanel root = new JPanel(new BorderLayout());
        root.add(tabs, BorderLayout.CENTER);
        return root;
    }

    private JPanel createListPanel(DefaultListModel<String> model, String type) {
        JList<String> list = new JList<>(model);
        JScrollPane scroll = new JScrollPane(list);

        JButton refresh = new JButton("Refresh " + type + " List");
        refresh.addActionListener(e -> refreshModel(model, type));

        JPanel panel = new JPanel(new BorderLayout(8, 8));
        panel.add(scroll, BorderLayout.CENTER);
        panel.add(refresh, BorderLayout.SOUTH);
        return panel;
    }

    private void refreshModel(DefaultListModel<String> model, String type) {
        model.clear();
        if ("Student".equals(type)) {
            for (Student s : ctrl.getAllStudents()) {
                model.addElement(formatStudent(s));
            }
        } else {
            for (Tutor t : ctrl.getAllTutors()) {
                model.addElement(formatTutor(t));
            }
        }
    }

    private String formatStudent(Student s) {
        return String.format("[%s] %s <%s>", s.getId(), s.getName(), s.getEmail());
    }

    private String formatTutor(Tutor t) {
        return String.format("[%s] %s <%s> Subjects: %s", t.getId(), t.getName(), t.getEmail(), t.getCourses());
    }
}
