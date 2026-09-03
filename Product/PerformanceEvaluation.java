/**
 * The PerformanceEvaluation class provides a graphical interface for 
 * administrators and managers to submit performance evaluations for employees.
 * - Admins can evaluate all employees.
 * - Managers can only evaluate employees in their department (excluding themselves).
 * Evaluation data is stored in the database with associated metadata.
 */

//package iaproductcode;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.util.ArrayList;

public class PerformanceEvaluation extends JFrame implements ActionListener
{

  // Constants for styling
  public static final Font TITLE_FONT = new Font("Helvetica", Font.BOLD, 20);
  public static final Font BUTTON_FONT = new Font("Helvetica", Font.BOLD, 16);
  public static final Font LABEL_FONT = new Font("Helvetica", Font.PLAIN, 14);

  // Title GUI Components
  private JPanel titlePanel;
  private JLabel titleLabel;

  // Main GUI Components
  private JPanel mainPanel, namePanel;
  private JComboBox<String> employeeDropdown;
  private JLabel nameLabel, evaluationLabel;
  private JTextArea evaluationTextArea;

  // Button Panel GUI Components
  private JPanel buttonPanel;
  private JButton storeButton, returnButton, viewDBButton;

  private JavaDBAccess dbAccess;
  private Object[][] employeeData;
  private int[] employeeIDs;

  /**
   * Constructs the GUI for entering performance evaluations.
   * Populates the employee dropdown based on user role.
   */
  public PerformanceEvaluation()
  {
    // Frame Title (GUI)
    this.setTitle("Performance Evaluation -" + Welcome.getCompanyName() + " OA System");

    // Structuring the frame
    this.setBounds(1920 / 2, 1080 / 2, 1920, 1080);
    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    this.getContentPane().setBackground(Welcome.BG_COLOR);
    this.setExtendedState(JFrame.MAXIMIZED_BOTH);
    this.setLayout(new BorderLayout());

    // Title Panel
    this.titlePanel = new JPanel();
    titlePanel.setBackground(Welcome.BG_COLOR);
    this.titleLabel = new JLabel("Employee Performance Evaluation");
    titleLabel.setFont(TITLE_FONT);
    titlePanel.add(titleLabel);

    // Constructing Main Panel
    this.mainPanel = new JPanel();
    mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
    mainPanel.setBackground(Welcome.BG_COLOR);
    mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50)); // Padding

    // Employee Dropdown
    this.dbAccess = new JavaDBAccess();
    // access employee data from DB with restrictions
    int loggedInUserID = dbAccess.getUserIDByUsername(LogIn.getUsername());

    if (LogIn.getRole().equals("Admin"))
    {
      this.employeeData = dbAccess.getEmployeeData();
    }
    else // Manager
    {
      Object[][] deptEmployees = dbAccess.filterEmployeesByDepartment(dbAccess.getManagerDepartment(LogIn.getUsername()));
      ArrayList<Object[]> filteredEmployees = new ArrayList<>();

      for (int i = 0; i < deptEmployees.length; i++)
      {
        int empUserID = Integer.parseInt(deptEmployees[i][1].toString()); // Column 1 = userID
        if (empUserID != loggedInUserID) // Filter out the logged-in manager
        {
          filteredEmployees.add(deptEmployees[i]);
        }
      }
      this.employeeData = filteredEmployees.toArray(new Object[0][0]);
    }

    String[] employeeInfo = new String[employeeData.length]; // names for employee drop down
    this.employeeIDs = new int[employeeData.length];
    for (int r = 0; r < employeeData.length; r++)
    {
      int empID = Integer.parseInt(employeeData[r][0].toString()); // Employee ID
      String firstName = employeeData[r][2].toString();
      String lastName = employeeData[r][3].toString();

      this.employeeIDs[r] = empID;
      employeeInfo[r] = firstName + " " + lastName; // Receives First and Last Name
    }
    this.nameLabel = new JLabel("Name:");
    nameLabel.setFont(LABEL_FONT);
    nameLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
    this.employeeDropdown = new JComboBox<>(employeeInfo); // display list retrieved from db
    employeeDropdown.setFont(LABEL_FONT);
    employeeDropdown.setAlignmentX(Component.LEFT_ALIGNMENT);
    employeeDropdown.addActionListener(this);
    this.namePanel = new JPanel();
    namePanel.setBackground(Welcome.BG_COLOR);
    namePanel.setLayout(new FlowLayout(FlowLayout.LEFT));
    namePanel.add(nameLabel);
    namePanel.add(employeeDropdown);
    mainPanel.add(namePanel);

    // Evaluation TextArea
    this.evaluationLabel = new JLabel("Evaluation:");
    evaluationLabel.setFont(LABEL_FONT);
    evaluationLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

    evaluationTextArea = new JTextArea(8, 40);
    evaluationTextArea.setFont(LABEL_FONT);
    evaluationTextArea.setLineWrap(true);
    evaluationTextArea.setWrapStyleWord(true);
    JScrollPane evaluationScrollPane = new JScrollPane(evaluationTextArea);
    evaluationScrollPane.setAlignmentX(Component.LEFT_ALIGNMENT);

    mainPanel.add(Box.createRigidArea(new Dimension(0, 20))); // Spacing
    mainPanel.add(evaluationLabel);
    mainPanel.add(evaluationScrollPane);

    // Buttons Panel
    this.buttonPanel = new JPanel();
    buttonPanel.setBackground(Welcome.BG_COLOR);

    this.storeButton = new JButton("Store");
    storeButton.setFont(BUTTON_FONT);
    storeButton.setPreferredSize(new Dimension(180, 40));
    storeButton.addActionListener(this);

    this.returnButton = new JButton("Return");
    returnButton.setFont(BUTTON_FONT);
    returnButton.setPreferredSize(new Dimension(180, 40));
    returnButton.addActionListener(this);

    this.viewDBButton = new JButton("View DB");
    viewDBButton.setFont(BUTTON_FONT);
    viewDBButton.setPreferredSize(new Dimension(180, 40));
    viewDBButton.addActionListener(this);

    buttonPanel.add(storeButton);
    buttonPanel.add(returnButton);
    buttonPanel.add(viewDBButton);

    // Add components to frame
    this.add(titlePanel, BorderLayout.NORTH);
    this.add(mainPanel, BorderLayout.CENTER);
    this.add(buttonPanel, BorderLayout.SOUTH);

    // Set frame visible
    this.setVisible(true);

  }

  /**
   * Handles button actions for storing evaluations, returning to the dashboard,
   * or viewing the performance evaluation database.
   */
  @Override
  public void actionPerformed(ActionEvent e)
  {
    if (e.getSource() == storeButton)
    {
      int selectedIndex = employeeDropdown.getSelectedIndex();
      int employeeID = employeeIDs[selectedIndex];
      String evaluation = evaluationTextArea.getText().trim();
      String date = LocalDate.now().toString();
      if (evaluation.isEmpty())
      {
        JOptionPane.showMessageDialog(this, "Evaluation cannot be empty.", "Error", JOptionPane.ERROR_MESSAGE);
        return;
      }

      dbAccess.insertPerformanceEvaluationData(employeeID, LogIn.getUsername(), date, evaluation);
      JOptionPane.showMessageDialog(this, "Evaluation stored successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
      evaluationTextArea.setText("");
    }
    else if (e.getSource() == returnButton)
    {
      // User Direct Process
      if (LogIn.getRole().equals("Admin"))
      {
        new HumanResources();
        this.dispose();
      }
      else if (LogIn.getRole().equals("Manager"))
      {
        new ManagerDashboard();
        this.dispose();
      }
      else
      {
        JOptionPane.showMessageDialog(this, "WARNING: YOU ARE ACCESSING WITHOUT PERMISSION."
            + "\nRESOURCES ON THIS PAGE IS RESTRICTED. PROPERTY OF " + Welcome.getCompanyName() + ".", "WARNING: ILLEGAL ACCESS", JOptionPane.ERROR_MESSAGE);
        System.exit(0);
      }
    }
    else if (e.getSource() == viewDBButton)
    {
      new PerformanceEvaluationDB();
      this.dispose();
    }
  }

  /**
   * Launches the PerformanceEvaluation frame as an Admin (for testing purposes).
   */
  public static void main(String[] args)
  {
    LogIn.setUsername("Admin");
    LogIn.setRole("Admin");
    new PerformanceEvaluation();
  }
}
