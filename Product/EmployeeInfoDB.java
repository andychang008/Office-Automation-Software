//package iaproductcode;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * EmployeeInfoDB.java
 *
 * This class represents the GUI interface for viewing and managing employee information 
 * within the Office Automation System. It allows Admin users to view, search, update, 
 * and delete employee records. Managers can only view and search employees in their 
 * own department with restricted permissions.
 *
 * Role-based access control ensures that only Admins can modify or delete records.
 * Managers are limited to viewing and searching within their department.
 *
 */
public class EmployeeInfoDB extends JFrame implements ActionListener
{

  // Constants for styling
  public static final Font BUTTON_FONT = new Font("Helvetica", Font.BOLD, 16);
  public static final Font TITLE_FONT = new Font("Helvetica", Font.BOLD, 18);

  // Components for DB Table
  private JPanel titlePanel, tablePanel;
  private JLabel titleLabel;
  private JTable employeeTable;
  private static DefaultTableModel tableModel;

  // Components for Button Panel
  private JPanel buttonPanel;
  private JButton returnButton, searchButton, updateButton, deleteButton, resetButton;

  private JavaDBAccess dbAccess;

  /**
   * Constructs the EmployeeInfoDB GUI window, setting up the table display and 
   * role-based controls for Admin and Manager users.
   * Initializes components and populates the table with employee data from the database.
   */
  public EmployeeInfoDB()
  {
    // Frame Title (GUI)
    this.setTitle("Employee Info Table -" + Welcome.getCompanyName() + " OA System");

    // Structuring the frame
    this.setBounds(1920 / 2, 1080 / 2, 1920, 1080);
    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    this.getContentPane().setBackground(Welcome.BG_COLOR);
    this.setExtendedState(JFrame.MAXIMIZED_BOTH);
    this.setLayout(new BorderLayout());

    // Role control
    String userRole = LogIn.getRole();

    // Constructing Title Panel
    this.titlePanel = new JPanel();
    titlePanel.setBackground(Welcome.BG_COLOR);
    this.titleLabel = new JLabel("Employee Information DB", SwingConstants.CENTER);
    titleLabel.setFont(TITLE_FONT);
    titlePanel.add(titleLabel);

    // Constructing Table Panel
    this.tablePanel = new JPanel();
    tablePanel.setLayout(new BorderLayout());
    tablePanel.setBackground(Welcome.BG_COLOR);

    // Columns for the table
    this.dbAccess = new JavaDBAccess();
    Object[][] data;
    if (LogIn.getRole().equals("Manager"))
    {
      int managerDeptID = dbAccess.getManagerDepartment(LogIn.getUsername());  // Get manager's department
      data = dbAccess.filterEmployeesByDepartment(managerDeptID); // Filter employees by department
    }
    else
    {
      data = dbAccess.getEmployeeData(); // Admins can view all employees
    }
    String[] columnNames =
    {
      "Employee ID", "User ID", "First Name", "Last Name", "Department ID", "DOB", "Salary"
    };
    tableModel = new DefaultTableModel(columnNames, 0)
    {
      @Override
      public boolean isCellEditable(int row, int column)
      {
        return false; // Makes all cells non-editable
      }
    };
    employeeTable = new JTable(tableModel);
    employeeTable.setRowHeight(25);

    // DB Data for employees
    for (int r = 0; r < data.length; r++)
    {
      tableModel.addRow(data[r]);
    }

    // Scroll Pane for the table
    JScrollPane tableScrollPane = new JScrollPane(employeeTable);
    tableScrollPane.setBorder(BorderFactory.createTitledBorder("Employee Information"));
    tablePanel.add(tableScrollPane, BorderLayout.CENTER);

    // Constructing Button Panel
    this.buttonPanel = new JPanel();
    buttonPanel.setBackground(Welcome.BG_COLOR);

    this.searchButton = new JButton("Search");
    searchButton.setFont(BUTTON_FONT);
    searchButton.setPreferredSize(new Dimension(180, 40));
    searchButton.addActionListener(this);
    buttonPanel.add(searchButton);
    this.resetButton = new JButton("Reset Filter");
    resetButton.setFont(BUTTON_FONT);
    resetButton.setPreferredSize(new Dimension(180, 40));
    resetButton.addActionListener(this);
    buttonPanel.add(resetButton);
    if (LogIn.getRole().equals("Admin"))
    {
      this.updateButton = new JButton("Update");
      updateButton.setFont(BUTTON_FONT);
      updateButton.setPreferredSize(new Dimension(180, 40));
      updateButton.addActionListener(this);
      buttonPanel.add(updateButton);
      this.deleteButton = new JButton("Delete");
      deleteButton.setFont(BUTTON_FONT);
      deleteButton.setPreferredSize(new Dimension(180, 40));
      deleteButton.addActionListener(this);
      buttonPanel.add(deleteButton);
    }
    this.returnButton = new JButton("Return");
    returnButton.setFont(BUTTON_FONT);
    returnButton.setPreferredSize(new Dimension(180, 40));
    returnButton.addActionListener(this);
    buttonPanel.add(returnButton);

    // Add panels to frame
    this.add(titlePanel, BorderLayout.NORTH);
    this.add(tablePanel, BorderLayout.CENTER);
    this.add(buttonPanel, BorderLayout.SOUTH);

    // Set frame visible
    this.setVisible(true);
  }

  /**
   * Handles the action events for the various buttons in the GUI such as Return, 
   * Search, Reset Filter, Update, and Delete. Functionality varies based on user role.
   *
   * @param e The ActionEvent triggered by button interaction.
   */
  @Override
  public void actionPerformed(ActionEvent e)
  {
    if (e.getSource() == returnButton)
    {
      // User Direct Process
      if (LogIn.getRole().equals("Admin"))
      {
        new EmployeeManagement();
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

    else if (e.getSource() == searchButton)
    {
      if (LogIn.getRole().equals("Admin"))
      {
        new EmployeeSearchGUI();
      }
      else if (LogIn.getRole().equals("Manager"))
      {
        int managerDeptID = dbAccess.getManagerDepartment(LogIn.getUsername()); // Get manager's department
        // Manager specific search function
        String searchQuery = JOptionPane.showInputDialog(this, "Enter First or Last Name to Search:");
        if (searchQuery != null && !searchQuery.trim().isEmpty())
        {
          // Only allow search result from the same department
          Object[][] searchResults = dbAccess.searchEmployeesAfterFilter(managerDeptID, searchQuery);
          refreshTable(searchResults); // Update Table
        }
      }
      else
      {
        JOptionPane.showMessageDialog(this, "Access Restricted. You do not have permission to search DB.", "Error Access Restricted.", JOptionPane.ERROR_MESSAGE);
      }
    }

    else if (e.getSource() == resetButton)
    {
      if (LogIn.getRole().equals("Admin"))
      {
        Object[][] data = dbAccess.getEmployeeData();
        this.refreshTable(data);
      }
      else if (LogIn.getRole().equals("Manager"))
      {
        int managerDeptID = dbAccess.getManagerDepartment(LogIn.getUsername());  // Get manager's department
        Object[][] departmentData = dbAccess.filterEmployeesByDepartment(managerDeptID); // Restrict to department
        this.refreshTable(departmentData);
      }
      else
      {
        new LogIn();
        this.dispose();
      }

    }

    else if (e.getSource() == updateButton)
    {
      if (LogIn.getRole().equals("Admin"))
      {
        int selectedRow = employeeTable.getSelectedRow();
        if (selectedRow != -1)  // Ensure a row is selected
        {
          int employeeID = Integer.parseInt(tableModel.getValueAt(selectedRow, 0).toString()); // Gets Employee ID of selected row
          new EmployeeUpdate(employeeID); // Opens Update GUI
        }
        else
        {
          JOptionPane.showMessageDialog(this, "Please select an employee to update.", "Error No Selection", JOptionPane.ERROR_MESSAGE);
        }
      }
      else
      {
        JOptionPane.showMessageDialog(this, "Access Restricted. You do not have permission to modify.", "Error Access Restricted", JOptionPane.ERROR_MESSAGE);
      }
    }
    else if (e.getSource() == deleteButton)
    {
      if (LogIn.getRole().equals("Admin"))
      {
        int selectedRow = employeeTable.getSelectedRow();
        if (selectedRow != -1)  // Ensure a row is selected
        {
          int employeeID = Integer.parseInt(tableModel.getValueAt(selectedRow, 0).toString()); // Gets Employee ID of selected row

          // Confirm deletion with user
          int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete Employee ID " + employeeID + "?",
              "Confirm Deletion", JOptionPane.YES_NO_OPTION);
          if (confirm == JOptionPane.YES_OPTION)
          {
            dbAccess.deleteEmployee(employeeID); // Deletes employee and update accordingly
            JOptionPane.showMessageDialog(this, "Employee deleted successfully!", "Success Delete", JOptionPane.INFORMATION_MESSAGE);
            this.refreshTable(dbAccess.getEmployeeData());  // Refreshes table after deletion
          }
        }
        else
        {
          JOptionPane.showMessageDialog(this, "Please select an employee to delete.", "Error No Selection", JOptionPane.ERROR_MESSAGE);
        }
      }
      else
      {
        JOptionPane.showMessageDialog(this, "Access Restricted. You do not have permission to modify.", "Error Access Restricted", JOptionPane.ERROR_MESSAGE);
      }
    }
  }

  /**
   * Refreshes and updates the JTable with the latest employee data provided.
   * Clears existing table rows and repopulates with new data.
   *
   * @param newData A 2D array containing the updated employee data to display.
   */
  public static void refreshTable(Object[][] newData)
  {
    tableModel.setRowCount(0); // Clears all existing table data

    // Fetch updated data from the database
    JavaDBAccess dbAccess = new JavaDBAccess();

    // Update data row by row from database
    for (int r = 0; r < newData.length; r++)
    {
      tableModel.addRow(newData[r]);
    }
  }

  /**
   * Main method for standalone testing of the EmployeeInfoDB interface.
   * Initializes the login state as Admin for testing purposes.
   *
   * @param args Command-line arguments (not used).
   */
  public static void main(String[] args)
  {
    LogIn.setUsername("Admin");
    LogIn.setRole("Admin");
    new EmployeeInfoDB();
  }
}
