package skolard.presentation;

import skolard.logic.ProfileHandler;
import skolard.objects.User;
import skolard.persistence.PersistenceFactory;
import skolard.persistence.PersistenceType;
import skolard.persistence.StudentPersistence;
import skolard.persistence.TutorPersistence;

import javax.swing.*;
import java.awt.*;

public class ProfileView extends JFrame {
    private final JTextField emailField = new JTextField(20);
    private final JTextArea profileArea = new JTextArea(15, 40);
    private final JButton fetchBtn = new JButton("View Profile");
    private final JButton updateBioBtn = new JButton("Update Bio");
    private final JButton addStudentBtn = new JButton("Add Student");
    private final JButton addTutorBtn = new JButton("Add Tutor");

    private final ProfileHandler handler;
    private User currentUser;

    public ProfileView() {
        super("SkolarD - Profile Viewer");

        StudentPersistence studentDao = PersistenceFactory.getStudentPersistence();
        TutorPersistence tutorDao = PersistenceFactory.getTutorPersistence();

        this.handler = new ProfileHandler(studentDao, tutorDao);

        setLayout(new BorderLayout(10, 10));

        // Top Panel (email + fetch)
        JPanel topPanel = new JPanel(new FlowLayout());
        topPanel.add(new JLabel("Email:"));
        topPanel.add(emailField);
        topPanel.add(fetchBtn);
        add(topPanel, BorderLayout.NORTH);

        // Center Panel (profile display)
        profileArea.setEditable(false);
        add(new JScrollPane(profileArea), BorderLayout.CENTER);

        // Bottom Panel (add + update)
        JPanel bottomPanel = new JPanel(new FlowLayout());
        bottomPanel.add(updateBioBtn);
        bottomPanel.add(addStudentBtn);
        bottomPanel.add(addTutorBtn);
        add(bottomPanel, BorderLayout.SOUTH);

        // Action: View profile
        fetchBtn.addActionListener(e -> {
            String email = emailField.getText().trim();
            if (!email.isEmpty()) {
                currentUser = handler.getUser(email);
                if (currentUser != null) {
                    profileArea.setText(handler.viewFullProfile(currentUser));
                } else {
                    profileArea.setText("No user found with that email.");
                }
            }
        });

        // Action: Update Tutor bio
        updateBioBtn.addActionListener(e -> {
            if (currentUser == null) {
                JOptionPane.showMessageDialog(this, "Load a tutor profile first.");
                return;
            }
            if (!(currentUser instanceof skolard.objects.Tutor)) {
                JOptionPane.showMessageDialog(this, "Only tutors can update bios.");
                return;
            }

            String newBio = JOptionPane.showInputDialog(this, "Enter new bio:");
            if (newBio != null && !newBio.isBlank()) {
                handler.updateBio(currentUser, newBio.trim());
                profileArea.setText(handler.viewFullProfile(currentUser)); // refresh
            }
        });

        // Action: Add Student
        addStudentBtn.addActionListener(e -> {
            JTextField nameField = new JTextField();
            JTextField emailField = new JTextField();
            Object[] fields = {
                    "Name:", nameField,
                    "Email:", emailField
            };

            int option = JOptionPane.showConfirmDialog(this, fields, "Add Student", JOptionPane.OK_CANCEL_OPTION);
            if (option == JOptionPane.OK_OPTION) {
                handler.addStudent(nameField.getText().trim(), emailField.getText().trim());
                JOptionPane.showMessageDialog(this, "Student added!");
            }
        });

        // Action: Add Tutor
        addTutorBtn.addActionListener(e -> {
            JTextField nameField = new JTextField();
            JTextField emailField = new JTextField();
            Object[] fields = {
                    "Name:", nameField,
                    "Email:", emailField
            };

            int option = JOptionPane.showConfirmDialog(this, fields, "Add Tutor", JOptionPane.OK_CANCEL_OPTION);
            if (option == JOptionPane.OK_OPTION) {
                handler.addTutor(nameField.getText().trim(), emailField.getText().trim());
                JOptionPane.showMessageDialog(this, "Tutor added!");
            }
        });

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }
}
