/**
 * The EmployeeUpdate class provides a graphical user interface (GUI) for updating
 * employee information in the database. It allows the user to modify an employee's
 * first name, last name, department, and salary, with input validation and automatic
 * refresh of the employee information table upon successful update.
 *
 * This class connects to the database via the JavaDBAccess class and ensures that
 * department data is properly retrieved and displayed for selection.
 *
 */
//package iaproductcode;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class EmployeeUpdate extends JFrame implements ActionListener
{

  private int employeeID;
  private JPanel mainPanel, buttonPanel;
  private JLabel firstNameLabel, lastNameLabel, deptLabel, salaryLabel;
  private JTextField firstNameField, lastNameField, salaryField;
  private JComboBox<String> deptDropdown;
  private JButton updateButton, cancelButton;
  private JavaDBAccess dbAccess;
  private Object[][] departmentData;
  private int[] departmentIDs;
  private String[] departmentNames;

  /**
   * Constructs the EmployeeUpdate frame for the specified employee ID.
   * Initializes the GUI components, retrieves existing employee data,
   * and populates the input fields for editing.
   *
   * @param employeeID The ID of the employee whose data will be updated.
   */
  public EmployeeUpdate(int employeeID)
  {
    this.employeeID = employeeID; // initialize for later update

    // Frame Title (GUI)
    this.setTitle("Employee Update -" + Welcome.getCompanyName() + " OA System");

    // Structuring the frame
    this.setSize(500, 400);
    this.setLocationRelativeTo(null);
    this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    this.getContentPane().setBackground(Welcome.BG_COLOR);
    this.setLayout(new BorderLayout());

    // Constructing main panel
    this.mainPanel = new JPanel(new GridLayout(4, 2, 10, 10)); 
    mainPanel.setBackground(Welcome.BG_COLOR);
    mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50));
    
    this.dbAccess = new JavaDBAccess();

    // Get Employee Data & Initialize Values
    Object[][] employeeData = dbAccess.getEmployeeData();
    String firstName = "", lastName = "";
    int departmentID = 0;
    double salary = 0;

    // Retrieves first, last name, dept, and salary of the employee
    for (int r = 0; r < employeeData.length; r++)
    {
      if (Integer.parseInt(employeeData[r][0].toString()) == employeeID)  // Match Employee ID
      {
        firstName = employeeData[r][2].toString();
        lastName = employeeData[r][3].toString();
        departmentID = Integer.parseInt(employeeData[r][4].toString());
        salary = Double.parseDouble(employeeData[r][6].toString());
      }
    }

    // Get Department Data (Name and ID for easy update)
    this.departmentData = dbAccess.getDepartmentData();
    this.departmentNames = new String[departmentData.length];
    this.departmentIDs = new int[departmentData.length];

    for (int r = 0; r < departmentData.length; r++)
    {
      departmentIDs[r] = Integer.parseInt(departmentData[r][0].toString());
      departmentNames[r] = departmentData[r][1].toString();
    }
    
    // Input fields
    this.firstNameLabel = new JLabel("First Name:");
    firstNameField = new JTextField(firstName); // Sets value based on what was selected in DB display
    mainPanel.add(firstNameLabel);
    mainPanel.add(firstNameField);

    this.lastNameLabel= new JLabel("Last Name:");
    lastNameField = new JTextField(lastName);
    mainPanel.add(lastNameLabel);
    mainPanel.add(lastNameField);

    this.deptLabel = new JLabel("Department:");
    deptDropdown = new JComboBox<>(departmentNames);
    deptDropdown.setSelectedIndex(getDepartmentIndex(departmentID));
    mainPanel.add(deptLabel);
    mainPanel.add(deptDropdown);

    this.salaryLabel = new JLabel("Salary:");
    salaryField = new JTextField(String.valueOf(salary));
    mainPanel.add(salaryLabel);
    mainPanel.add(salaryField);

    // Button Panel & Components
    this.buttonPanel = new JPanel();
    buttonPanel.setBackground(Welcome.BG_COLOR);
    
    this.updateButton = new JButton("Update");
    updateButton.addActionListener(this);
    buttonPanel.add(updateButton);

    this.cancelButton = new JButton("Cancel");
    cancelButton.addActionListener(this);
    buttonPanel.add(cancelButton);

    this.add(mainPanel, BorderLayout.CENTER);
    this.add(buttonPanel, BorderLayout.SOUTH);
    
    // Set frame visible
    this.setVisible(true);
  }

  /**
   * Finds the index of a department in the departmentIDs array for dropdown selection.
   *
   * @param departmentID The department ID to locate.
   * @return The index of the department in the dropdown list.
   */
  private int getDepartmentIndex(int departmentID)
  {
    for (int i = 0; i < departmentIDs.length; i++)
    {
      if (departmentIDs[i] == departmentID)
      {
        return i;
      }
    }
    return 0;
  }

  /**
   * Handles the actions for the update and cancel buttons.
   * Validates input fields and performs the update operation on the database.
   *
   * @param e The ActionEvent triggered by button clicks.
   */
  @Override
  public void actionPerformed(ActionEvent e)
  {
    if (e.getSource() == updateButton)
    {
      try
      {
        String firstName = firstNameField.getText().trim();
        String lastName = lastNameField.getText().trim();
        double salary = Double.parseDouble(salaryField.getText().trim());
        int departmentID = departmentIDs[deptDropdown.getSelectedIndex()];

        if (firstName.isEmpty() || lastName.isEmpty() || salary < 0)
        {
          JOptionPane.showMessageDialog(this, "Invalid input!", "Error Invalid Input", 
              JOptionPane.ERROR_MESSAGE);
          return;
        }

        dbAccess.updateEmployee(employeeID, firstName, lastName, departmentID, salary);
        JOptionPane.showMessageDialog(this, "Employee updated successfully!", "Success", 
            JOptionPane.INFORMATION_MESSAGE);
        this.dispose();
        EmployeeInfoDB.refreshTable(dbAccess.getEmployeeData()); // Refresh Table
      }
      catch (NumberFormatException ex)
      {
        JOptionPane.showMessageDialog(this, "Invalid salary input!", "Error Invalid Number", 
            JOptionPane.ERROR_MESSAGE);
      }
    }
    else if (e.getSource() == cancelButton)
    {
      this.dispose();
    }
  }

  /**
   * Main method for testing the EmployeeUpdate frame independently.
   * Used for development testing purposes.
   *
   * @param args Command-line arguments (not used).
   */
  public static void main(String[] args)
  {
    int deptID = 1;
    new EmployeeUpdate(deptID);
  }
}
