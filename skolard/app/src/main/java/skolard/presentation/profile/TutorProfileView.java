
package skolard.presentation.profile;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;

import skolard.logic.profile.ProfileHandler;
import skolard.objects.Tutor;

/**
 * Specialized profile management view for tutors.
 * Allows tutors to view and edit their profile information, bio, and courses.
 */
public class TutorProfileView extends JFrame {

    private final ProfileHandler profileHandler;
    private final Tutor currentTutor;

    // UI Components
    private final JTextArea profileInfoArea = new JTextArea(10, 50);
    private final JButton editBioBtn = new JButton("Edit Bio");
    private final JButton addCourseBtn = new JButton("Add Course");
    private final JButton removeCourseBtn = new JButton("Remove Course");
    private final JButton refreshProfileBtn = new JButton("Refresh Profile");
    private final JButton backBtn = new JButton("Back");
    private final JLabel statusLabel = new JLabel("Profile loaded successfully", SwingConstants.CENTER);

    // Course management components
    private final DefaultListModel<String> courseListModel = new DefaultListModel<>();
    private final JList<String> courseList = new JList<>(courseListModel);

    public TutorProfileView(ProfileHandler profileHandler, Tutor tutor) {
        super("SkolarD - Manage My Profile (Tutor)");

        this.profileHandler = profileHandler;
        this.currentTutor = tutor;

        initializeUI();
        setupEventHandlers();
        loadProfileData();

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void initializeUI() {
        setLayout(new BorderLayout(10, 10));

        // Title
        JLabel titleLabel = new JLabel("Manage My Profile - " + currentTutor.getName(), SwingConstants.CENTER);
        titleLabel.setFont(titleLabel.getFont().deriveFont(Font.BOLD, 16f));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        add(titleLabel, BorderLayout.NORTH);

        // Main content panel
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Profile info panel (left side)
        JPanel profilePanel = createProfileInfoPanel();
        mainPanel.add(profilePanel, BorderLayout.CENTER);

        // Course management panel (right side)
        JPanel coursePanel = createCourseManagementPanel();
        mainPanel.add(coursePanel, BorderLayout.EAST);

        // Action buttons panel (bottom)
        JPanel actionPanel = createActionPanel();
        mainPanel.add(actionPanel, BorderLayout.SOUTH);

        add(mainPanel, BorderLayout.CENTER);

        // Status label at bottom
        statusLabel.setBorder(BorderFactory.createEmptyBorder(5, 10, 10, 10));
        add(statusLabel, BorderLayout.SOUTH);
    }

    private JPanel createProfileInfoPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Profile Information"));
        panel.setPreferredSize(new java.awt.Dimension(400, 300));

        profileInfoArea.setEditable(false);
        profileInfoArea.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
        profileInfoArea.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        panel.add(new JScrollPane(profileInfoArea), BorderLayout.CENTER);

        return panel;
    }

    private JPanel createCourseManagementPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder("My Courses"));
        panel.setPreferredSize(new java.awt.Dimension(250, 300));

        courseList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        courseList.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        JScrollPane scrollPane = new JScrollPane(courseList);
        panel.add(scrollPane, BorderLayout.CENTER);

        // Course action buttons
        JPanel courseButtonPanel = new JPanel(new FlowLayout());
        courseButtonPanel.add(addCourseBtn);
        courseButtonPanel.add(removeCourseBtn);
        panel.add(courseButtonPanel, BorderLayout.SOUTH);

        return panel;
    }

    private JPanel createActionPanel() {
        JPanel panel = new JPanel(new FlowLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Profile Actions"));

        panel.add(editBioBtn);
        panel.add(refreshProfileBtn);
        panel.add(backBtn);

        return panel;
    }

    private void setupEventHandlers() {
        editBioBtn.addActionListener(e -> editBio());
        addCourseBtn.addActionListener(e -> addCourse());
        removeCourseBtn.addActionListener(e -> removeCourse());
        refreshProfileBtn.addActionListener(e -> loadProfileData());
        backBtn.addActionListener(e -> dispose());

        // Enable/disable remove button based on selection
        courseList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                removeCourseBtn.setEnabled(courseList.getSelectedIndex() != -1);
            }
        });
    }

    private void loadProfileData() {
        try {
            // Load updated tutor profile
            Tutor updatedTutor = profileHandler.getTutor(currentTutor.getEmail());
            if (updatedTutor != null) {
                // Update profile info display
                profileInfoArea.setText(profileHandler.viewFullProfile(updatedTutor));

                // Update course list
                courseListModel.clear();
                for (String course : updatedTutor.getCourses()) {
                    Double grade = updatedTutor.getCourseGrades().get(course);
                    String displayText = course;
                    if (grade != null) {
                        displayText += " (Grade: " + grade + ")";
                    }
                    courseListModel.addElement(displayText);
                }

                statusLabel.setText("Profile refreshed successfully");
            } else {
                statusLabel.setText("Error: Could not load profile data");
            }
        } catch (Exception ex) {
            showError("Error loading profile: " + ex.getMessage());
        }
    }

    private void editBio() {
        String currentBio = currentTutor.getBio() != null ? currentTutor.getBio() : "";

        JTextArea bioArea = new JTextArea(8, 40);
        bioArea.setText(currentBio);
        bioArea.setLineWrap(true);
        bioArea.setWrapStyleWord(true);
        bioArea.setBorder(BorderFactory.createLoweredBevelBorder());

        JScrollPane scrollPane = new JScrollPane(bioArea);
        Object[] message = {
                "Edit your bio:",
                scrollPane,
                "",
                "Your bio helps students understand your teaching style and experience."
        };

        int option = JOptionPane.showConfirmDialog(this, message, "Edit Bio",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (option == JOptionPane.OK_OPTION) {
            String newBio = bioArea.getText().trim();
            if (!newBio.isEmpty()) {
                try {
                    profileHandler.updateBio(currentTutor, newBio);
                    loadProfileData(); // Refresh display
                    statusLabel.setText("Bio updated successfully");
                    JOptionPane.showMessageDialog(this, "Bio updated successfully!",
                            "Success", JOptionPane.INFORMATION_MESSAGE);
                } catch (Exception ex) {
                    showError("Error updating bio: " + ex.getMessage());
                }
            } else {
                JOptionPane.showMessageDialog(this, "Bio cannot be empty.",
                        "Invalid Input", JOptionPane.WARNING_MESSAGE);
            }
        }
    }

    private void addCourse() {
        JTextField courseField = new JTextField(20);
        JTextField gradeField = new JTextField(10);

        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;

        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(new JLabel("Course Code:"), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL;
        panel.add(courseField, gbc);

        gbc.gridx = 0; gbc.gridy = 1; gbc.fill = GridBagConstraints.NONE;
        panel.add(new JLabel("Grade (optional):"), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL;
        panel.add(gradeField, gbc);

        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 2;
        panel.add(new JLabel("Example: COMP3350, MATH1500, etc."), gbc);

        int option = JOptionPane.showConfirmDialog(this, panel, "Add Course",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (option == JOptionPane.OK_OPTION) {
            String course = courseField.getText().trim().toLowerCase();
            String gradeText = gradeField.getText().trim();

            if (course.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Course code cannot be empty.",
                        "Invalid Input", JOptionPane.WARNING_MESSAGE);
                return;
            }

            if (currentTutor.getCourses().contains(course)) {
                JOptionPane.showMessageDialog(this, "You already have this course in your profile.",
                        "Course Already Exists", JOptionPane.WARNING_MESSAGE);
                return;
            }

            Double grade = null;
            if (!gradeText.isEmpty()) {
                try {
                    grade = Double.parseDouble(gradeText);
                    if (grade < 0 || grade > 4.5) {
                        JOptionPane.showMessageDialog(this, "Grade must be between 0 and 4.5",
                                "Invalid Grade", JOptionPane.WARNING_MESSAGE);
                        return;
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(this, "Grade must be a valid number.",
                            "Invalid Grade", JOptionPane.WARNING_MESSAGE);
                    return;
                }
            }

            try {
                profileHandler.addCourse(currentTutor, course, grade);
                loadProfileData(); // Refresh display
                statusLabel.setText("Course added successfully");
                JOptionPane.showMessageDialog(this, "Course '" + course.toUpperCase() + "' added successfully!",
                        "Success", JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception ex) {
                showError("Error adding course: " + ex.getMessage());
            }
        }
    }

    private void removeCourse() {
        int selectedIndex = courseList.getSelectedIndex();
        if (selectedIndex == -1) {
            JOptionPane.showMessageDialog(this, "Please select a course to remove.",
                    "No Course Selected", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String selectedItem = courseListModel.getElementAt(selectedIndex);
        String courseName = selectedItem.split(" \\(")[0]; // Extract course name before grade info

        int option = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to remove '" + courseName.toUpperCase() + "' from your profile?",
                "Confirm Removal", JOptionPane.YES_NO_OPTION);

        if (option == JOptionPane.YES_OPTION) {
            try {
                profileHandler.removeCourse(currentTutor, courseName);
                loadProfileData(); // Refresh display
                statusLabel.setText("Course removed successfully");
                JOptionPane.showMessageDialog(this, "Course '" + courseName.toUpperCase() + "' removed successfully!",
                        "Success", JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception ex) {
                showError("Error removing course: " + ex.getMessage());
            }
        }
    }

    private void showError(String message) {
        statusLabel.setText("Error occurred");
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }
}