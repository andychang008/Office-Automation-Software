/**
 * EmployeeManagement.java
 * 
 * This class represents the Employee Management GUI of the Office Automation System.
 * It allows Admin users to create new employee accounts, view the employee database,
 * and navigate between different management features via the function bar.
 *
 * Functionalities:
 * - Account creation for Employees, Managers, and Admins
 * - Employee information entry including validation of input fields
 * - Navigation between system features via function bar buttons
 * - Role-based account setup (includes department and salary configuration)
 * 
 */
//package iaproductcode;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class EmployeeManagement extends JFrame implements ActionListener
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
  private JButton createAccountButton, viewDBButton;

  private JavaDBAccess dbAccess;

  /**
   * Constructs the Employee Management GUI frame.
   * Initializes the layout, navigation buttons, and main panel components.
   */
  public EmployeeManagement()
  {
    // Frame Title (GUI)
    this.setTitle("Employee Management -" + Welcome.getCompanyName() + " OA System");

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
    hrButton = newFuncButton("Human Resources");
    employeeButton = newPrimaryButton("Employee Manage"); // Primary
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
    this.titleLabel = new JLabel("Employee Management", SwingConstants.CENTER);
    titleLabel.setFont(TITLE_FONT);
    titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

    // Buttons for HR functionalities
    createAccountButton = newPanelButton("Create Account");
    viewDBButton = newPanelButton("View Database");

    // Add buttons with spacing
    mainPanel.add(Box.createVerticalGlue()); // upper pillar
    mainPanel.add(titleLabel);
    mainPanel.add(Box.createRigidArea(new Dimension(0, 40))); // Spacing
    mainPanel.add(createAccountButton);
    mainPanel.add(Box.createRigidArea(new Dimension(0, 20))); // Spacing
    mainPanel.add(viewDBButton);
    mainPanel.add(Box.createVerticalGlue()); // bottom pillar

    // Add components to frame
    this.add(functionBar, BorderLayout.WEST);
    this.add(mainPanel, BorderLayout.CENTER);

    // Set frame visible
    this.setVisible(true);
  }

  /**
   * Helper method to create styled buttons for the main panel.
   * 
   * @param text The text to be displayed on the button.
   * @return A configured JButton instance.
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
   * Helper method to create styled buttons for the function bar.
   * 
   * @param text The text to be displayed on the button.
   * @return A configured JButton instance.
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
   * Helper method to create the primary styled button for the function bar.
   * Highlights the current active section.
   * 
   * @param text The text to be displayed on the button.
   * @return A configured primary JButton instance.
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
   * Adds a new employee entry into the employeesTable.
   * Performs input validation for first name, last name, date of birth, and salary.
   * Ensures that valid department selection is made.
   * 
   * @param userID The user ID associated with the created account.
   */
  public void addEmployee(int userID)
  {
    String firstName = "";
    String lastName = "";
    // Makes sure that an employee entry is created if the user is an employee.
    boolean employeeCreated = false;
    // Makes sure that the name is always not empty
    boolean nameEmpty = true;
    // Continue to iterate until employee is created successfully into employeesTable
    while (employeeCreated == false)
    {
      while (nameEmpty)
      {
        firstName = JOptionPane.showInputDialog(this, "Enter Employee's First Name: ");
        lastName = JOptionPane.showInputDialog(this, "Enter Employee's Last Name: ");
        if (firstName != null && lastName != null && !firstName.trim().isEmpty() && !lastName.trim().isEmpty()
            && firstName.matches("[a-zA-Z]+") && lastName.matches("[a-zA-Z]+"))
        {
          nameEmpty = false;
        }
        else
        {
          JOptionPane.showMessageDialog(this, "First/Last Name cannot be empty and must contain only letters!", "Error Invalid Name",
              JOptionPane.ERROR_MESSAGE);
        }
      }
      Object[][] departmentData = dbAccess.getDepartmentData();
      String[] departmentNames = new String[departmentData.length];
      int[] departmentIDs = new int[departmentData.length];

      // Finds data for dropdown and to convert later on
      for (int r = 0; r < departmentData.length; r++)
      {
        departmentIDs[r] = Integer.parseInt(departmentData[r][0].toString());
        departmentNames[r] = departmentData[r][1].toString();
      }

      // Initialize department selection
      String selectedDepartment = null;
      int departmentID = -1;

      // Keep prompting the user to pick until a valid department is selected
      while (departmentID == -1)
      {
        selectedDepartment = (String) JOptionPane.showInputDialog(
            this,
            "Select Employee's Department:",
            "Department Selection",
            JOptionPane.QUESTION_MESSAGE,
            null, // if null then select first
            departmentNames,
            departmentNames[0]
        );

        // Search for the selected department name in the list
        for (int i = 0; i < departmentNames.length; i++)
        {
          if (departmentNames[i].equals(selectedDepartment))
          {
            departmentID = departmentIDs[i]; // Assign valid department ID to add
          }
        }
      }

      String dob = "";
      boolean validDOB = false;
      while (!validDOB)
      {
        dob = JOptionPane.showInputDialog(this, "Enter Employee's Date of Birth (YYYY-MM-DD): ");
        try
        {
          java.time.LocalDate birthDate = java.time.LocalDate.parse(dob);
          java.time.LocalDate today = java.time.LocalDate.now();
          int age = java.time.Period.between(birthDate, today).getYears();

          if (birthDate.isAfter(today))
          {
            JOptionPane.showMessageDialog(this, "Date of birth cannot be in the future!", "Error Invalid DOB", JOptionPane.ERROR_MESSAGE);
          }
          else if (age < 0 || age > 130)
          {
            JOptionPane.showMessageDialog(this, "Employee must be of a real age.", "Error Invalid Age", JOptionPane.ERROR_MESSAGE);
          }
          else
          {
            validDOB = true;
          }
        }
        catch (Exception ex)
        {
          JOptionPane.showMessageDialog(this, "Invalid date format! Please use YYYY-MM-DD.", "Error Invalid Format", JOptionPane.ERROR_MESSAGE);
        }
      }

      boolean validSalary = false;
      while (!validSalary)
      {
        String salaryStr = JOptionPane.showInputDialog(this, "Enter Employee's Salary (Without Special Characters; Ex: 100000): ");
        try
        {
          double salary = Double.parseDouble(salaryStr);
          if (salary <= 0)
          {
            JOptionPane.showMessageDialog(this, "Salary must be a positive number.", "Error Invalid Salary", JOptionPane.ERROR_MESSAGE);
          }
          else
          {
            dbAccess.insertEmployeeData(userID, firstName, lastName, departmentID, dob, salary);
            JOptionPane.showMessageDialog(this, "Employee successfully added.");
            employeeCreated = true; // Exit loop since employee is now created
            validSalary = true;
          }
        }
        catch (NumberFormatException ex)
        {
          JOptionPane.showMessageDialog(this, "Invalid input! Please enter numeric values for salary.",
              "Error Input Value", JOptionPane.ERROR_MESSAGE);
        }
      }
    }
  }

  /**
   * Handles action events for the buttons in the Employee Management GUI.
   * Directs navigation and triggers account creation or viewing employee data.
   * 
   * @param e The ActionEvent triggered by a user interaction.
   */
  @Override
  public void actionPerformed(ActionEvent e)
  {
    Object source = e.getSource();
    this.dbAccess = new JavaDBAccess();

    String username = "";
    String password = "";
    String role = "";
    String[] roles =
    {
      "Employee", "Manager", "Admin"
    };

    // Main Panel Buttons
    if (source == createAccountButton)
    {
      boolean acctFieldEmtpy = true;
      while (acctFieldEmtpy)
      {
        username = JOptionPane.showInputDialog(this, "Enter Username:");
        password = JOptionPane.showInputDialog(this, "Enter Password:");
        role = (String) JOptionPane.showInputDialog(this, "Select Role:",
            "Role Selection", JOptionPane.QUESTION_MESSAGE, null, roles, roles[0]); // selects first if role is null
        if (username != null && password != null && role != null)
        {
          acctFieldEmtpy = false;
        }
        else
        {
          JOptionPane.showMessageDialog(this, "Some/all fields are empty! Try again", "Error Blank Field",
              JOptionPane.ERROR_MESSAGE);
        }
      }
      int userID = dbAccess.insertUsersData(username, password, role); // Returns userID
      if (userID != 0) // Starts at 1 so if not 0 meaning created correctly.
      {
        // If employee account created, auto jump to employee creation to avoid missing DB Entries
        if (role.equals("Employee") || role.equals("Manager"))
        {
          addEmployee(userID); // pre-defined method to add employee to DB
        }
        JOptionPane.showMessageDialog(this, "Account successfully created with User ID: " + userID);
      }
      else
      {
        JOptionPane.showMessageDialog(this, "Error creating account.", "Error Account Creation",
            JOptionPane.ERROR_MESSAGE);
      }

    }

    else if (source == viewDBButton)
    {
      new EmployeeInfoDB();
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
      new HumanResources();
      this.dispose();
    }
    else if (source == employeeButton)
    {
      // Current Page
    }
    else if (source == knowledgeButton)
    {
      new Knowledgebase();
      this.dispose();
    }
    else if (source == sendMessageButton)
    {
      JOptionPane.showMessageDialog(this, "Button to insert into a database."); // DB: Message OR KnowledgeBase
    }
    else if (source == logoutButton)
    {
      this.dispose();
      new Welcome();
    }
  }

  /**
   * Main method to launch the Employee Management GUI for testing.
   * Sets up a default Admin login session.
   * 
   * @param args Command-line arguments (not used).
   */
  public static void main(String[] args)
  {
    LogIn.setUsername("admin");
    LogIn.setRole("Admin");
    new EmployeeManagement();
  }
}
