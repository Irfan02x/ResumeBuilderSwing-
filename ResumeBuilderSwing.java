import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class ResumeBuilderSwing {
    private static Resume[] resumeDatabase = new Resume[100];
    private static int resumeCount = 0;
    
    private static JFrame mainFrame;
    private static JPanel cardPanel;
    private static CardLayout cardLayout;
    
    // Colors for modern UI
    private static final Color PRIMARY_COLOR = new Color(70, 130, 180);
    private static final Color SECONDARY_COLOR = new Color(176, 196, 222);
    private static final Color ACCENT_COLOR = new Color(255, 165, 0);
    private static final Color BACKGROUND_COLOR = new Color(240, 248, 255);
    
    // Form components as instance variables
    private static JTextField nameField, emailField, phoneField, addressField;
    private static JTextField eduInstitutionField, eduDegreeField, eduFieldOfStudyField;
    private static JTextField eduStartDateField, eduEndDateField;
    private static JTextField expCompanyField, expPositionField;
    private static JTextField expStartDateField, expEndDateField;
    private static JTextArea expDescriptionArea;
    private static JTextField skillNameField;
    private static JComboBox<String> skillProficiencyCombo;
    
    public static void main(String[] args) {
        Thread.setDefaultUncaughtExceptionHandler((t, e) -> {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, 
                "Error: " + e.getMessage(), 
                "Critical Error", JOptionPane.ERROR_MESSAGE);
        });
        
        SwingUtilities.invokeLater(() -> {
            showSplashScreen();
        });
    }
    
    private static void showSplashScreen() {
        JWindow splashScreen = new JWindow();
        splashScreen.setSize(500, 300);
        splashScreen.setLocationRelativeTo(null);
        
        JPanel content = new JPanel(new BorderLayout());
        content.setBackground(BACKGROUND_COLOR);
        content.setBorder(BorderFactory.createLineBorder(PRIMARY_COLOR, 5));
        
        JLabel loadingLabel = new JLabel("Loading Resume Builder", SwingConstants.CENTER);
        loadingLabel.setFont(new Font("Arial", Font.BOLD, 24));
        loadingLabel.setForeground(PRIMARY_COLOR);
        
        JProgressBar progressBar = new JProgressBar();
        progressBar.setIndeterminate(true);
        progressBar.setForeground(ACCENT_COLOR);
        
        JLabel logoLabel = new JLabel(new ImageIcon(createGradientImage(400, 150)));
        logoLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        content.add(logoLabel, BorderLayout.CENTER);
        content.add(loadingLabel, BorderLayout.NORTH);
        content.add(progressBar, BorderLayout.SOUTH);
        
        splashScreen.setContentPane(content);
        splashScreen.setVisible(true);
        
        Timer timer = new Timer(3000, e -> {
            splashScreen.dispose();
            SwingUtilities.invokeLater(() -> {
                createAndShowGUI();
            });
        });
        timer.setRepeats(false);
        timer.start();
    }
    
    private static Image createGradientImage(int width, int height) {
        GradientPaint gradient = new GradientPaint(
            0, 0, PRIMARY_COLOR, 
            width, height, SECONDARY_COLOR);
        
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = image.createGraphics();
        g2d.setPaint(gradient);
        g2d.fillRect(0, 0, width, height);
        
        g2d.setColor(Color.BLUE);
        g2d.setFont(new Font("Arial", Font.BOLD, 36));
        FontMetrics fm = g2d.getFontMetrics();
        String text = "Resume Pro";
        int x = (width - fm.stringWidth(text)) / 2;
        int y = (height - fm.getHeight()) / 2 + fm.getAscent();
        g2d.drawString(text, x, y);
        
        g2d.dispose();
        return image;
    }
    
    private static void createAndShowGUI() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            UIManager.put("Button.foreground", Color.BLUE);
            UIManager.put("Button.background", PRIMARY_COLOR);
            UIManager.put("Button.select", ACCENT_COLOR);
            UIManager.put("Panel.background", BACKGROUND_COLOR);
            UIManager.put("Label.foreground", PRIMARY_COLOR);
            UIManager.put("TextField.border", BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(SECONDARY_COLOR, 1),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)));
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, 
                "Error setting look and feel: " + e.getMessage(), 
                "Error", JOptionPane.ERROR_MESSAGE);
        }
        
        initializeFormFields();
        
        mainFrame = new JFrame("Resume Builder Pro");
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setSize(900, 700);
        mainFrame.setMinimumSize(new Dimension(700, 500));
        
        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);
        cardPanel.setBackground(BACKGROUND_COLOR);
        
        cardPanel.add(createMenuPanel(), "Menu");
        cardPanel.add(createResumePanel(), "CreateResume");
        cardPanel.add(createViewResumesPanel(), "ViewResumes");
        cardPanel.add(createSearchPanel(), "Search");
        
        mainFrame.add(cardPanel);
        mainFrame.setLocationRelativeTo(null);
        mainFrame.setVisible(true);
        
        mainFrame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowOpened(WindowEvent e) {
                System.out.println("Main window opened successfully");
            }
            
            @Override
            public void windowClosing(WindowEvent e) {
                System.out.println("Main window closing");
            }
        });
    }
    
    private static void initializeFormFields() {
        nameField = new JTextField();
        emailField = new JTextField();
        phoneField = new JTextField();
        addressField = new JTextField();
        
        eduInstitutionField = new JTextField();
        eduDegreeField = new JTextField();
        eduFieldOfStudyField = new JTextField();
        eduStartDateField = new JTextField();
        eduEndDateField = new JTextField();
        
        expCompanyField = new JTextField();
        expPositionField = new JTextField();
        expStartDateField = new JTextField();
        expEndDateField = new JTextField();
        expDescriptionArea = new JTextArea(5, 20);
        expDescriptionArea.setLineWrap(true);
        expDescriptionArea.setWrapStyleWord(true);
        
        skillNameField = new JTextField();
        skillProficiencyCombo = new JComboBox<>(
            new String[]{"Beginner", "Intermediate", "Advanced", "Expert"});
    }
    
    private static JPanel createMenuPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(BACKGROUND_COLOR);
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(15, 50, 15, 50);
        
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(BACKGROUND_COLOR);
        
        JLabel titleLabel = new JLabel("Resume Builder Pro", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 32));
        titleLabel.setForeground(PRIMARY_COLOR);
        
        JLabel subtitleLabel = new JLabel("Create professional resumes with ease", SwingConstants.CENTER);
        subtitleLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        subtitleLabel.setForeground(SECONDARY_COLOR.darker());
        
        headerPanel.add(titleLabel, BorderLayout.NORTH);
        headerPanel.add(subtitleLabel, BorderLayout.CENTER);
        
        JButton createButton = createStyledButton("Create New Resume", "add-file.png");
        createButton.addActionListener(e -> cardLayout.show(cardPanel, "CreateResume"));
        
        JButton viewButton = createStyledButton("View All Resumes", "view-list.png");
        viewButton.addActionListener(e -> {
            updateResumeList();
            cardLayout.show(cardPanel, "ViewResumes");
        });
        
        JButton searchButton = createStyledButton("Search Resume by Name", "search.png");
        searchButton.addActionListener(e -> cardLayout.show(cardPanel, "Search"));
        
        JButton exitButton = createStyledButton("Exit", "exit.png");
        exitButton.addActionListener(e -> System.exit(0));
        
        panel.add(headerPanel, gbc);
        panel.add(Box.createVerticalStrut(30), gbc);
        panel.add(createButton, gbc);
        panel.add(viewButton, gbc);
        panel.add(searchButton, gbc);
        panel.add(Box.createVerticalStrut(30), gbc);
        panel.add(exitButton, gbc);
        
        return panel;
    }
    
    private static JButton createStyledButton(String text, String iconName) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 16));
        button.setBackground(PRIMARY_COLOR);
        button.setForeground(Color.BLUE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(PRIMARY_COLOR.darker(), 1),
            BorderFactory.createEmptyBorder(10, 25, 10, 25)));
        
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(PRIMARY_COLOR.brighter());
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(PRIMARY_COLOR);
            }
        });
        
        try {
            ImageIcon icon = new ImageIcon(createPlaceholderIcon(30, 30, PRIMARY_COLOR));
            button.setIcon(icon);
            button.setHorizontalAlignment(SwingConstants.LEFT);
            button.setIconTextGap(15);
        } catch (Exception e) {
        }
        
        return button;
    }
    
    private static Image createPlaceholderIcon(int width, int height, Color color) {
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = image.createGraphics();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        g2d.setColor(color);
        g2d.fillOval(5, 5, width-10, height-10);
        
        g2d.setColor(Color.BLUE);
        g2d.setFont(new Font("Arial", Font.BOLD, 12));
        FontMetrics fm = g2d.getFontMetrics();
        String text = "Icon";
        int x = (width - fm.stringWidth(text)) / 2;
        int y = (height - fm.getHeight()) / 2 + fm.getAscent();
        g2d.drawString(text, x, y);
        
        g2d.dispose();
        return image;
    }
    
    private static JPanel createResumePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(BACKGROUND_COLOR);
        
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(PRIMARY_COLOR);
        headerPanel.setBorder(new EmptyBorder(10, 15, 10, 15));
        
        JButton backButton = new JButton("← Back");
        backButton.setFont(new Font("Arial", Font.PLAIN, 14));
        backButton.setContentAreaFilled(false);
        backButton.setForeground(Color.BLUE);
        backButton.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        backButton.addActionListener(e -> cardLayout.show(cardPanel, "Menu"));
        
        JLabel titleLabel = new JLabel("Create New Resume", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setForeground(Color.BLUE);
        
        headerPanel.add(backButton, BorderLayout.WEST);
        headerPanel.add(titleLabel, BorderLayout.CENTER);
        
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("Arial", Font.BOLD, 14));
        tabbedPane.setBackground(BACKGROUND_COLOR);
        tabbedPane.setForeground(PRIMARY_COLOR);
        
        tabbedPane.addTab("Personal Info", createPersonalInfoTab());
        tabbedPane.addTab("Education", createEducationTab());
        tabbedPane.addTab("Experience", createExperienceTab());
        tabbedPane.addTab("Skills", createSkillsTab());
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 20));
        buttonPanel.setBackground(BACKGROUND_COLOR);
        
        JButton saveButton = createStyledButton("Save Resume", "save.png");
        JButton cancelButton = createStyledButton("Cancel", "cancel.png");
        
        saveButton.addActionListener(e -> saveResume());
        cancelButton.addActionListener(e -> {
            cardLayout.show(cardPanel, "Menu");
            clearAllFields();
        });
        
        buttonPanel.add(saveButton);
        buttonPanel.add(cancelButton);
        
        panel.add(headerPanel, BorderLayout.NORTH);
        panel.add(tabbedPane, BorderLayout.CENTER);
        panel.add(buttonPanel, BorderLayout.SOUTH);
        
        return panel;
    }
    
    private static JPanel createPersonalInfoTab() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(BACKGROUND_COLOR);
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));
        
        panel.add(createFormField("Full Name*", nameField));
        panel.add(Box.createVerticalStrut(15));
        panel.add(createFormField("Email*", emailField));
        panel.add(Box.createVerticalStrut(15));
        panel.add(createFormField("Phone", phoneField));
        panel.add(Box.createVerticalStrut(15));
        panel.add(createFormField("Address", addressField));
        
        return panel;
    }
    
    private static JPanel createEducationTab() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(BACKGROUND_COLOR);
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));
        
        panel.add(createFormField("Institution*", eduInstitutionField));
        panel.add(Box.createVerticalStrut(15));
        panel.add(createFormField("Degree", eduDegreeField));
        panel.add(Box.createVerticalStrut(15));
        panel.add(createFormField("Field of Study", eduFieldOfStudyField));
        panel.add(Box.createVerticalStrut(15));
        panel.add(createFormField("Start Date (YYYY-MM-DD)*", eduStartDateField));
        panel.add(Box.createVerticalStrut(15));
        panel.add(createFormField("End Date (YYYY-MM-DD)*", eduEndDateField));
        
        return panel;
    }
    
    private static JPanel createExperienceTab() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(BACKGROUND_COLOR);
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));
        
        panel.add(createFormField("Company*", expCompanyField));
        panel.add(Box.createVerticalStrut(15));
        panel.add(createFormField("Position", expPositionField));
        panel.add(Box.createVerticalStrut(15));
        panel.add(createFormField("Start Date (YYYY-MM-DD)*", expStartDateField));
        panel.add(Box.createVerticalStrut(15));
        panel.add(createFormField("End Date (YYYY-MM-DD)*", expEndDateField));
        panel.add(Box.createVerticalStrut(15));
        
        JLabel descLabel = new JLabel("Description:");
        descLabel.setFont(new Font("Arial", Font.BOLD, 14));
        descLabel.setForeground(PRIMARY_COLOR);
        descLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JScrollPane scrollPane = new JScrollPane(expDescriptionArea);
        scrollPane.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        panel.add(descLabel);
        panel.add(Box.createVerticalStrut(5));
        panel.add(scrollPane);
        
        return panel;
    }
    
    private static JPanel createSkillsTab() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(BACKGROUND_COLOR);
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));
        
        panel.add(createFormField("Skill Name*", skillNameField));
        panel.add(Box.createVerticalStrut(15));
        
        JLabel profLabel = new JLabel("Proficiency:");
        profLabel.setFont(new Font("Arial", Font.BOLD, 14));
        profLabel.setForeground(PRIMARY_COLOR);
        profLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        skillProficiencyCombo.setFont(new Font("Arial", Font.PLAIN, 14));
        skillProficiencyCombo.setAlignmentX(Component.LEFT_ALIGNMENT);
        skillProficiencyCombo.setMaximumSize(new Dimension(Integer.MAX_VALUE, 
            skillProficiencyCombo.getPreferredSize().height));
        
        panel.add(profLabel);
        panel.add(Box.createVerticalStrut(5));
        panel.add(skillProficiencyCombo);
        
        return panel;
    }
    
    private static JPanel createFormField(String labelText, JComponent input) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(BACKGROUND_COLOR);
        panel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JLabel label = new JLabel(labelText);
        label.setFont(new Font("Arial", Font.BOLD, 14));
        label.setForeground(PRIMARY_COLOR);
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        input.setFont(new Font("Arial", Font.PLAIN, 14));
        input.setMaximumSize(new Dimension(Integer.MAX_VALUE, input.getPreferredSize().height));
        
        panel.add(label);
        panel.add(Box.createVerticalStrut(5));
        panel.add(input);
        
        return panel;
    }
    
    private static void saveResume() {
        try {
            if (nameField.getText().trim().isEmpty() || 
                emailField.getText().trim().isEmpty() ||
                eduInstitutionField.getText().trim().isEmpty() ||
                expCompanyField.getText().trim().isEmpty() ||
                skillNameField.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(mainFrame, 
                    "Please fill in all required fields (marked with *)", 
                    "Validation Error", JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            Resume resume = new Resume();
            resume.fullName = nameField.getText();
            resume.email = emailField.getText();
            resume.phone = phoneField.getText();
            resume.address = addressField.getText();
            
            Education edu = new Education();
            edu.institution = eduInstitutionField.getText();
            edu.degree = eduDegreeField.getText();
            edu.fieldOfStudy = eduFieldOfStudyField.getText();
            edu.startDate = LocalDate.parse(eduStartDateField.getText());
            edu.endDate = LocalDate.parse(eduEndDateField.getText());
            resume.education = new Education[]{edu};
            
            Experience exp = new Experience();
            exp.company = expCompanyField.getText();
            exp.position = expPositionField.getText();
            exp.startDate = LocalDate.parse(expStartDateField.getText());
            exp.endDate = LocalDate.parse(expEndDateField.getText());
            exp.description = expDescriptionArea.getText();
            resume.experience = new Experience[]{exp};
            
            Skill skill = new Skill();
            skill.name = skillNameField.getText();
            skill.proficiency = (String)skillProficiencyCombo.getSelectedItem();
            resume.skills = new Skill[]{skill};
            
            if (resumeCount < resumeDatabase.length) {
                resumeDatabase[resumeCount++] = resume;
                showSaveSuccessAnimation();
                clearAllFields();
            } else {
                JOptionPane.showMessageDialog(mainFrame, 
                    "Database full! Cannot save more resumes.", 
                    "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (DateTimeParseException ex) {
            JOptionPane.showMessageDialog(mainFrame, 
                "Invalid date format. Please use YYYY-MM-DD.", 
                "Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(mainFrame, 
                "Error saving resume: " + ex.getMessage(), 
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private static void showSaveSuccessAnimation() {
        JDialog successDialog = new JDialog(mainFrame, "Success", true);
        successDialog.setSize(300, 200);
        successDialog.setLocationRelativeTo(mainFrame);
        successDialog.setLayout(new BorderLayout());
        
        JPanel content = new JPanel(new BorderLayout());
        content.setBackground(BACKGROUND_COLOR);
        content.setBorder(new EmptyBorder(20, 20, 20, 20));
        
        JLabel checkIcon = new JLabel(new ImageIcon(createCheckIcon(80, 80, Color.GREEN)));
        checkIcon.setHorizontalAlignment(SwingConstants.CENTER);
        
        JLabel messageLabel = new JLabel("Resume saved successfully!", SwingConstants.CENTER);
        messageLabel.setFont(new Font("Arial", Font.BOLD, 16));
        messageLabel.setForeground(PRIMARY_COLOR);
        
        content.add(checkIcon, BorderLayout.CENTER);
        content.add(messageLabel, BorderLayout.SOUTH);
        
        successDialog.add(content);
        
        Timer timer = new Timer(2000, e -> {
            successDialog.dispose();
            cardLayout.show(cardPanel, "Menu");
        });
        timer.setRepeats(false);
        timer.start();
        
        successDialog.setVisible(true);
    }
    
    private static Image createCheckIcon(int width, int height, Color color) {
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = image.createGraphics();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        g2d.setColor(color);
        g2d.fillOval(0, 0, width, height);
        
        g2d.setColor(Color.BLUE);
        g2d.setStroke(new BasicStroke(5));
        int[] xPoints = {width/4, width/2, width*3/4};
        int[] yPoints = {height/2, height*3/4, height/4};
        g2d.drawPolyline(xPoints, yPoints, 3);
        
        g2d.dispose();
        return image;
    }
    
    private static void clearAllFields() {
        nameField.setText("");
        emailField.setText("");
        phoneField.setText("");
        addressField.setText("");
        eduInstitutionField.setText("");
        eduDegreeField.setText("");
        eduFieldOfStudyField.setText("");
        eduStartDateField.setText("");
        eduEndDateField.setText("");
        expCompanyField.setText("");
        expPositionField.setText("");
        expStartDateField.setText("");
        expEndDateField.setText("");
        expDescriptionArea.setText("");
        skillNameField.setText("");
        skillProficiencyCombo.setSelectedIndex(0);
    }
    
    private static JPanel createViewResumesPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(BACKGROUND_COLOR);
        
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(PRIMARY_COLOR);
        headerPanel.setBorder(new EmptyBorder(10, 15, 10, 15));
        
        JButton backButton = new JButton("← Back");
        backButton.setFont(new Font("Arial", Font.PLAIN, 14));
        backButton.setContentAreaFilled(false);
        backButton.setForeground(Color.WHITE);
        backButton.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        backButton.addActionListener(e -> cardLayout.show(cardPanel, "Menu"));
        
        JLabel titleLabel = new JLabel("Your Resumes", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setForeground(Color.BLUE);
        
        JButton refreshButton = new JButton("↻ Refresh");
        refreshButton.setFont(new Font("Arial", Font.PLAIN, 14));
        refreshButton.setContentAreaFilled(false);
        refreshButton.setForeground(Color.WHITE);
        refreshButton.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        refreshButton.addActionListener(e -> updateResumeList());
        
        headerPanel.add(backButton, BorderLayout.WEST);
        headerPanel.add(titleLabel, BorderLayout.CENTER);
        headerPanel.add(refreshButton, BorderLayout.EAST);
        
        DefaultListModel<Resume> listModel = new DefaultListModel<>();
        JList<Resume> resumeList = new JList<>(listModel);
        resumeList.setCellRenderer(new ResumeListRenderer());
        resumeList.setBackground(Color.WHITE);
        resumeList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        resumeList.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent evt) {
                if (evt.getClickCount() == 2) {
                    Resume selected = resumeList.getSelectedValue();
                    if (selected != null) {
                        displayResume(selected);
                    }
                }
            }
        });
        
        JScrollPane scrollPane = new JScrollPane(resumeList);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 20));
        buttonPanel.setBackground(BACKGROUND_COLOR);
        
        JButton viewButton = createStyledButton("View Selected Resume", "view.png");
        JButton deleteButton = createStyledButton("Delete Resume", "delete.png");
        JButton exportButton = createStyledButton("Export to PDF", "pdf.png");
        JButton backToMenuButton = createStyledButton("Back to Menu", "home.png");
        
        viewButton.addActionListener(e -> {
            Resume selected = resumeList.getSelectedValue();
            if (selected != null) {
                displayResume(selected);
            } else {
                JOptionPane.showMessageDialog(mainFrame, 
                    "Please select a resume to view.", 
                    "No Selection", JOptionPane.WARNING_MESSAGE);
            }
        });
        
        deleteButton.addActionListener(e -> {
            int selectedIndex = resumeList.getSelectedIndex();
            if (selectedIndex != -1) {
                int confirm = JOptionPane.showConfirmDialog(mainFrame, 
                    "Are you sure you want to delete this resume?", 
                    "Confirm Delete", JOptionPane.YES_NO_OPTION);
                
                if (confirm == JOptionPane.YES_OPTION) {
                    System.arraycopy(resumeDatabase, selectedIndex + 1, resumeDatabase, selectedIndex, resumeCount - selectedIndex - 1);
                    resumeDatabase[--resumeCount] = null;
                    listModel.remove(selectedIndex);
                    
                    JOptionPane.showMessageDialog(mainFrame, 
                        "Resume deleted successfully.", 
                        "Success", JOptionPane.INFORMATION_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(mainFrame, 
                    "Please select a resume to delete.", 
                    "No Selection", JOptionPane.WARNING_MESSAGE);
            }
        });
        
        exportButton.addActionListener(e -> {
            Resume selected = resumeList.getSelectedValue();
            if (selected != null) {
                JOptionPane.showMessageDialog(mainFrame, 
                    "PDF export would be implemented here.\n" +
                    "Resume for " + selected.fullName + " would be exported.", 
                    "Export Functionality", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(mainFrame, 
                    "Please select a resume to export.", 
                    "No Selection", JOptionPane.WARNING_MESSAGE);
            }
        });
        
        backToMenuButton.addActionListener(e -> cardLayout.show(cardPanel, "Menu"));
        
        buttonPanel.add(viewButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(exportButton);
        buttonPanel.add(backToMenuButton);
        
        updateResumeList(listModel);
        
        panel.add(headerPanel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(buttonPanel, BorderLayout.SOUTH);
        
        return panel;
    }
    
    private static JPanel createSearchPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(BACKGROUND_COLOR);
        
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(PRIMARY_COLOR);
        headerPanel.setBorder(new EmptyBorder(10, 15, 10, 15));
        
        JButton backButton = new JButton("← Back");
        backButton.setFont(new Font("Arial", Font.PLAIN, 14));
        backButton.setContentAreaFilled(false);
        backButton.setForeground(Color.BLUE);
        backButton.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        backButton.addActionListener(e -> cardLayout.show(cardPanel, "Menu"));
        
        JLabel titleLabel = new JLabel("Search Resumes", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setForeground(Color.BLUE);
        
        headerPanel.add(backButton, BorderLayout.WEST);
        headerPanel.add(titleLabel, BorderLayout.CENTER);
        
        JPanel searchPanel = new JPanel(new BorderLayout(10, 10));
        searchPanel.setBackground(BACKGROUND_COLOR);
        searchPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        
        JPanel inputPanel = new JPanel(new BorderLayout(10, 10));
        inputPanel.setBackground(BACKGROUND_COLOR);
        
        JLabel searchLabel = new JLabel("Search by name:");
        searchLabel.setFont(new Font("Arial", Font.BOLD, 14));
        searchLabel.setForeground(PRIMARY_COLOR);
        
        JTextField searchField = new JTextField();
        searchField.setFont(new Font("Arial", Font.PLAIN, 14));
        
        JButton searchButton = createStyledButton("Search", "search.png");
        
        inputPanel.add(searchLabel, BorderLayout.WEST);
        inputPanel.add(searchField, BorderLayout.CENTER);
        inputPanel.add(searchButton, BorderLayout.EAST);
        
        DefaultListModel<Resume> resultModel = new DefaultListModel<>();
        JList<Resume> resultList = new JList<>(resultModel);
        resultList.setCellRenderer(new ResumeListRenderer());
        resultList.setBackground(Color.BLUE);
        
        JScrollPane resultScroll = new JScrollPane(resultList);
        resultScroll.setBorder(BorderFactory.createEmptyBorder());
        
        searchPanel.add(inputPanel, BorderLayout.NORTH);
        searchPanel.add(resultScroll, BorderLayout.CENTER);
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 20));
        buttonPanel.setBackground(BACKGROUND_COLOR);
        
        JButton viewButton = createStyledButton("View Selected", "view.png");
        JButton clearButton = createStyledButton("Clear Results", "clear.png");
        
        viewButton.addActionListener(e -> {
            Resume selected = resultList.getSelectedValue();
            if (selected != null) {
                displayResume(selected);
            } else {
                JOptionPane.showMessageDialog(mainFrame, 
                    "Please select a resume to view.", 
                    "No Selection", JOptionPane.WARNING_MESSAGE);
            }
        });
        
        clearButton.addActionListener(e -> {
            searchField.setText("");
            resultModel.clear();
        });
        
        buttonPanel.add(viewButton);
        buttonPanel.add(clearButton);
        
        searchButton.addActionListener(e -> {
            String searchTerm = searchField.getText().trim().toLowerCase();
            resultModel.clear();
            
            if (searchTerm.isEmpty()) {
                JOptionPane.showMessageDialog(mainFrame, 
                    "Please enter a name to search.", 
                    "Error", JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            boolean found = false;
            for (int i = 0; i < resumeCount; i++) {
                if (resumeDatabase[i].fullName.toLowerCase().contains(searchTerm)) {
                    resultModel.addElement(resumeDatabase[i]);
                    found = true;
                }
            }
            
            if (!found) {
                JOptionPane.showMessageDialog(mainFrame, 
                    "No resumes found matching: " + searchTerm, 
                    "No Results", JOptionPane.INFORMATION_MESSAGE);
            }
        });
        
        resultList.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent evt) {
                if (evt.getClickCount() == 2) {
                    int index = resultList.locationToIndex(evt.getPoint());
                    if (index != -1) {
                        displayResume(resultModel.get(index));
                    }
                }
            }
        });
        
        panel.add(headerPanel, BorderLayout.NORTH);
        panel.add(searchPanel, BorderLayout.CENTER);
        panel.add(buttonPanel, BorderLayout.SOUTH);
        
        return panel;
    }
    
    private static void updateResumeList() {
        JPanel viewPanel = (JPanel)cardPanel.getComponent(2);
        JScrollPane scrollPane = (JScrollPane)viewPanel.getComponent(1);
        JList<Resume> resumeList = (JList<Resume>)scrollPane.getViewport().getView();
        DefaultListModel<Resume> model = (DefaultListModel<Resume>)resumeList.getModel();
        updateResumeList(model);
    }
    
    private static void updateResumeList(DefaultListModel<Resume> model) {
        model.clear();
        for (int i = 0; i < resumeCount; i++) {
            model.addElement(resumeDatabase[i]);
        }
    }
    private static void displayResume(Resume resume) {
        JDialog dialog = new JDialog(mainFrame, "Resume: " + resume.fullName, true);
        dialog.setSize(700, 600);
        dialog.setLocationRelativeTo(mainFrame);
        
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("Arial", Font.BOLD, 14));
        
        // Personal Info Tab
        JPanel personalPanel = new JPanel(new GridLayout(0, 2, 10, 5));
        personalPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        
        addResumeField(personalPanel, "Full Name:", resume.fullName);
        addResumeField(personalPanel, "Email:", resume.email);
        addResumeField(personalPanel, "Phone:", resume.phone);
        addResumeField(personalPanel, "Address:", resume.address);
        
        tabbedPane.addTab("Personal Info", new JScrollPane(personalPanel));
        
        // Education Tab
        JPanel educationPanel = new JPanel();
        educationPanel.setLayout(new BoxLayout(educationPanel, BoxLayout.Y_AXIS));
        educationPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        
        for (Education edu : resume.education) {
            JPanel eduPanel = new JPanel(new GridLayout(0, 2, 10, 5));
            eduPanel.setBorder(BorderFactory.createTitledBorder("Education Entry"));
            
            addResumeField(eduPanel, "Institution:", edu.institution);
            addResumeField(eduPanel, "Degree:", edu.degree);
            addResumeField(eduPanel, "Field of Study:", edu.fieldOfStudy);
            addResumeField(eduPanel, "Start Date:", edu.startDate.toString());
            addResumeField(eduPanel, "End Date:", edu.endDate.toString());
            
            educationPanel.add(eduPanel);
            educationPanel.add(Box.createVerticalStrut(15));
        }
        
        tabbedPane.addTab("Education", new JScrollPane(educationPanel));
        
        // Experience Tab
        JPanel experiencePanel = new JPanel();
        experiencePanel.setLayout(new BoxLayout(experiencePanel, BoxLayout.Y_AXIS));
        experiencePanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        
        for (Experience exp : resume.experience) {
            JPanel expPanel = new JPanel(new BorderLayout(10, 5));
            expPanel.setBorder(BorderFactory.createTitledBorder("Experience Entry"));
            
            JPanel topPanel = new JPanel(new GridLayout(0, 2, 10, 5));
            addResumeField(topPanel, "Company:", exp.company);
            addResumeField(topPanel, "Position:", exp.position);
            addResumeField(topPanel, "Start Date:", exp.startDate.toString());
            addResumeField(topPanel, "End Date:", exp.endDate.toString());
            
            JTextArea descArea = new JTextArea(exp.description);
            descArea.setEditable(false);
            descArea.setLineWrap(true);
            descArea.setWrapStyleWord(true);
            descArea.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
            
            expPanel.add(topPanel, BorderLayout.NORTH);
            expPanel.add(new JScrollPane(descArea), BorderLayout.CENTER);
            
            experiencePanel.add(expPanel);
            experiencePanel.add(Box.createVerticalStrut(15));
        }
        
        tabbedPane.addTab("Experience", new JScrollPane(experiencePanel));
        
        // Skills Tab
        JPanel skillsPanel = new JPanel(new GridLayout(0, 2, 10, 5));
        skillsPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        
        for (Skill skill : resume.skills) {
            addResumeField(skillsPanel, "Skill:", skill.name);
            addResumeField(skillsPanel, "Proficiency:", skill.proficiency);
        }
        
        tabbedPane.addTab("Skills", new JScrollPane(skillsPanel));
        
        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        JButton closeButton = createStyledButton("Close", "close.png");
        closeButton.addActionListener(e -> dialog.dispose());
        buttonPanel.add(closeButton);
        
        dialog.add(tabbedPane, BorderLayout.CENTER);
        dialog.add(buttonPanel, BorderLayout.SOUTH);
        
        dialog.setVisible(true);
    }
    
    private static void addResumeField(JPanel panel, String label, String value) {
        JLabel lbl = new JLabel(label);
        lbl.setFont(new Font("Arial", Font.BOLD, 12));
        lbl.setForeground(PRIMARY_COLOR);
        
        JTextField txt = new JTextField(value);
        txt.setEditable(false);
        txt.setBorder(BorderFactory.createEmptyBorder());
        txt.setFont(new Font("Arial", Font.PLAIN, 12));
        
        panel.add(lbl);
        panel.add(txt);
    }
    
    private static void clearAllFields(JTextField... fields) {
        for (JTextField field : fields) {
            field.setText("");
        }
    }
    
    private static void clearAllFields(JTextField nameField, JTextField emailField, 
                                     JTextField phoneField, JTextField addressField,
                                     JTextField eduInstitutionField, JTextField eduDegreeField, 
                                     JTextField eduFieldOfStudyField, JTextField eduStartDateField,
                                     JTextField eduEndDateField, JTextField expCompanyField,
                                     JTextField expPositionField, JTextField expStartDateField,
                                     JTextField expEndDateField, JTextArea expDescriptionArea,
                                     JTextField skillNameField, JComboBox<String> skillProficiencyCombo) {
        nameField.setText("");
        emailField.setText("");
        phoneField.setText("");
        addressField.setText("");
        eduInstitutionField.setText("");
        eduDegreeField.setText("");
        eduFieldOfStudyField.setText("");
        eduStartDateField.setText("");
        eduEndDateField.setText("");
        expCompanyField.setText("");
        expPositionField.setText("");
        expStartDateField.setText("");
        expEndDateField.setText("");
        expDescriptionArea.setText("");
        skillNameField.setText("");
        skillProficiencyCombo.setSelectedIndex(0);
    }
    
    // Custom list renderer for resumes
    static class ResumeListRenderer extends JPanel implements ListCellRenderer<Resume> {
        private JLabel nameLabel;
        private JLabel emailLabel;
        private JLabel dateLabel;
        
        public ResumeListRenderer() {
            setLayout(new BorderLayout(10, 5));
            setBorder(new EmptyBorder(10, 10, 10, 10));
            setOpaque(true);
            
            nameLabel = new JLabel();
            nameLabel.setFont(new Font("Arial", Font.BOLD, 16));
            nameLabel.setForeground(PRIMARY_COLOR);
            
            emailLabel = new JLabel();
            emailLabel.setFont(new Font("Arial", Font.PLAIN, 12));
            
            dateLabel = new JLabel();
            dateLabel.setFont(new Font("Arial", Font.ITALIC, 10));
            dateLabel.setHorizontalAlignment(SwingConstants.RIGHT);
            
            JPanel leftPanel = new JPanel(new BorderLayout());
            leftPanel.add(nameLabel, BorderLayout.NORTH);
            leftPanel.add(emailLabel, BorderLayout.CENTER);
            leftPanel.setOpaque(false);
            
            add(leftPanel, BorderLayout.CENTER);
            add(dateLabel, BorderLayout.EAST);
        }
        
        @Override
        public Component getListCellRendererComponent(JList<? extends Resume> list, 
                Resume resume, int index, boolean isSelected, boolean cellHasFocus) {
            
            nameLabel.setText(resume.fullName);
            emailLabel.setText(resume.email);
            
            // Get the most recent date from education or experience
            LocalDate latestDate = null;
            if (resume.education != null && resume.education.length > 0) {
                latestDate = resume.education[0].endDate;
            }
            if (resume.experience != null && resume.experience.length > 0) {
                LocalDate expDate = resume.experience[0].endDate;
                if (latestDate == null || expDate.isAfter(latestDate)) {
                    latestDate = expDate;
                }
            }
            
            dateLabel.setText(latestDate != null ? "Last updated: " + latestDate.toString() : "");
            
            if (isSelected) {
                setBackground(SECONDARY_COLOR);
            } else {
                setBackground(index % 2 == 0 ? Color.BLUE : new Color(240, 240, 240));
            }
            
            return this;
        }
    }
    
    // Inner classes for resume components
    static class Resume {
        String fullName;
        String email;
        String phone;
        String address;
        Education[] education;
        Experience[] experience;
        Skill[] skills;
    }
    
    static class Education {
        String institution;
        String degree;
        String fieldOfStudy;
        LocalDate startDate;
        LocalDate endDate;
    }
    
    static class Experience {
        String company;
        String position;
        LocalDate startDate;
        LocalDate endDate;
        String description;
    }
    
    static class Skill {
        String name;
        String proficiency;
    }
}