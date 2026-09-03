/**
 * HumanResources.java
 * 
 * This class represents the Human Resources dashboard for the Office Automation System.
 * It provides GUI functionality for HR-related operations including creating departments,
 * managing budget & salary, tracking attendance, and managing performance evaluations.
 * 
 * The class extends JFrame and implements ActionListener to handle user interactions.
 * It dynamically switches between different system panels and provides navigation through
 * a side function bar.
 */
//package iaproductcode;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class HumanResources extends JFrame implements ActionListener
{

  // Constants for styling
  public static final Font TITLE_FONT = new Font("Helvetica", Font.BOLD, 18);

  // Components for function bar
  private JPanel functionBar;
  private JButton homeButton, hrButton, employeeButton, knowledgeButton, sendMessageButton, logoutButton;
  private JLabel welcomeLabel, versionLabel;

  // Components for main HR Buttons Panel
  private JPanel mainPanel;
  private JLabel titleLabel;
  private JButton createDeptButton, moneyButton, attendanceButton, perfEvalButton;

  /**
   * Constructs the Human Resources dashboard GUI.
   * Initializes the layout, side function bar, main panel buttons,
   * and sets up action listeners for navigation between features.
   */
  public HumanResources()
  {
    // Frame Title (GUI)
    this.setTitle("Human Resources -" + Welcome.getCompanyName() + " OA System");

    // Structuring the frame
    this.setBounds(1920 / 2, 1080 / 2, 1920, 1080);
    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    this.getContentPane().setBackground(AdminDashboard.ADMIN_COLOR);
    this.setExtendedState(JFrame.MAXIMIZED_BOTH);
    this.setLayout(new BorderLayout());

    // Constructing Left Function Bar Panel
    this.functionBar = new JPanel();
    functionBar.setLayout(new BoxLayout(functionBar, BoxLayout.Y_AXIS));
    functionBar.setBackground(AdminDashboard.ADMIN_COLOR);
    functionBar.setPreferredSize(new Dimension(200, getHeight())); // set width 200, height relative

    // Constructing function bar buttons
    homeButton = newFuncButton("Home");
    hrButton = newPrimaryButton("Human Resources"); // Primary
    employeeButton = newFuncButton("Employee Manage");
    knowledgeButton = newFuncButton("Knowledgebase");
    sendMessageButton = newFuncButton("Send Message");
    logoutButton = newFuncButton("Logout");

    // Constructing version and welcome message
    this.welcomeLabel = new JLabel("Welcome, " + LogIn.getUsername());
    welcomeLabel.setFont(new Font("Helvetica", Font.BOLD, 16));
    welcomeLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
    this.versionLabel = new JLabel(Welcome.getVersionNumber());
    versionLabel.setFont(new Font("Helvetica", Font.PLAIN, 14));
    versionLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

    // Adding FB buttons
    functionBar.add(Box.createVerticalGlue()); // top pillar to center vert.
    functionBar.add(homeButton);
    functionBar.add(Box.createRigidArea(new Dimension(0, 20)));
    functionBar.add(hrButton);
    functionBar.add(Box.createRigidArea(new Dimension(0, 20)));
    functionBar.add(employeeButton);
    functionBar.add(Box.createRigidArea(new Dimension(0, 20)));
    functionBar.add(knowledgeButton);
    functionBar.add(Box.createRigidArea(new Dimension(0, 20)));
    functionBar.add(sendMessageButton);
    functionBar.add(Box.createRigidArea(new Dimension(0, 20)));
    functionBar.add(welcomeLabel);
    functionBar.add(versionLabel);
    functionBar.add(Box.createVerticalGlue()); // bottom pillar center vert.
    functionBar.add(logoutButton);

    // Constructing main panel
    this.mainPanel = new JPanel();
    mainPanel.setBackground(Welcome.BG_COLOR);
    mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));

    // Constructing title
    this.titleLabel = new JLabel("Human Resources", SwingConstants.CENTER);
    titleLabel.setFont(TITLE_FONT);
    titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

    // Buttons for HR functionalities
    this.createDeptButton = newPanelButton("Create Department");
    this.moneyButton = newPanelButton("Budget & Salary");
    this.attendanceButton = newPanelButton("Attendance");
    this.perfEvalButton = newPanelButton("Performance Evaluation");

    // Add buttons with spacing
    mainPanel.add(Box.createVerticalGlue()); // upper pillar
    mainPanel.add(titleLabel);
    mainPanel.add(Box.createRigidArea(new Dimension(0, 40))); // Spacing
    mainPanel.add(createDeptButton);
    mainPanel.add(Box.createRigidArea(new Dimension(0, 20))); // Spacing
    mainPanel.add(moneyButton);
    mainPanel.add(Box.createRigidArea(new Dimension(0, 20))); // Spacing
    mainPanel.add(attendanceButton);
    mainPanel.add(Box.createRigidArea(new Dimension(0, 20))); // Spacing
    mainPanel.add(perfEvalButton);
    mainPanel.add(Box.createVerticalGlue()); // bottom pillar

    // Add components to frame
    this.add(functionBar, BorderLayout.WEST);
    this.add(mainPanel, BorderLayout.CENTER);

    // Set frame visible
    this.setVisible(true);
  }

  /**
   * Helper method to create main panel buttons with consistent styling.
   * @param text The button label text.
   * @return JButton with configured properties.
   */
  private JButton newPanelButton(String text)
  {
    JButton button = new JButton(text);
    button.setFont(AdminDashboard.MENU_FONT);
    button.setAlignmentX(Component.CENTER_ALIGNMENT);
    button.setPreferredSize(new Dimension(180, 40));
    button.addActionListener(this);
    return button;
  }

  /**
   * Helper method to create standard function bar buttons (non-primary).
   * @param text The button label text.
   * @return JButton with configured properties.
   */
  private JButton newFuncButton(String text)
  {
    JButton button = new JButton(text);
    button.setFont(AdminDashboard.MENU_FONT);
    button.setAlignmentX(Component.CENTER_ALIGNMENT);
    button.setPreferredSize(new Dimension(180, 40));
    button.addActionListener(this);
    return button;
  }

  /**
   * Helper method to create the primary highlighted function bar button.
   * @param text The button label text.
   * @return JButton with configured properties.
   */
  private JButton newPrimaryButton(String text)
  {
    JButton pButton = new JButton(text);
    pButton.setFont(AdminDashboard.MENU_FONT);
    pButton.setAlignmentX(Component.CENTER_ALIGNMENT);
    pButton.setPreferredSize(new Dimension(180, 40));
    pButton.setForeground(AdminDashboard.PRIMARY_COLOR);
    pButton.addActionListener(this);
    return pButton;
  }

  /**
   * Handles button click events for all navigation and feature-related actions.
   * Depending on the clicked button, it opens the corresponding frame or executes actions like department creation.
   * @param e The ActionEvent triggered by the button click.
   */
  @Override
  public void actionPerformed(ActionEvent e)
  {
    Object source = e.getSource();

    // Main Panel
    if (source == createDeptButton)
    {
      String deptName = JOptionPane.showInputDialog(this, "Enter Department Name:");
      if (deptName != null) // Ensure user didn't press cancel
      {
        deptName = deptName.trim();

        if (!deptName.isEmpty()) // Ensure dept name has content
        {
          JavaDBAccess dbAccess = new JavaDBAccess();
          dbAccess.insertDeptData(deptName);
          JOptionPane.showMessageDialog(this, "Successfully created department: \"" + deptName + "\"", "Success", JOptionPane.INFORMATION_MESSAGE);
        }
        else
        {
          JOptionPane.showMessageDialog(this, "Department name cannot be empty.", "Error", JOptionPane.ERROR_MESSAGE);
        }
      }
    }
    else if (source == moneyButton)
    {
      new Money();
      this.dispose();
    }
    else if (source == attendanceButton)
    {
      new Attendance();
      this.dispose();
    }
    else if (source == perfEvalButton)
    {
      new PerformanceEvaluation();
      this.dispose();
    }

    // function bar
    else if (source == homeButton)
    {
      new AdminDashboard();
      this.dispose();
    }
    else if (source == hrButton)
    {
      // Current Page
    }
    else if (source == employeeButton)
    {
      new EmployeeManagement();
      this.dispose();
    }
    else if (source == knowledgeButton)
    {
      new Knowledgebase();
      this.dispose();
    }
    else if (source == sendMessageButton)
    {
      new SendMessage();
    }
    else if (source == logoutButton)
    {
      this.dispose();
      new Welcome();
    }
  }

  /**
   * Main method for standalone testing of the Human Resources dashboard.
   * Sets up a sample Admin login and launches the HR interface.
   * @param args Command-line arguments (not used).
   */
  public static void main(String[] args)
  {
    LogIn.setUsername("admin");
    LogIn.setRole("Admin");
    new HumanResources();
  }
}
