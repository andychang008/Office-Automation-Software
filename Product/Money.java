
/**
 * This class represents the Money module of the Office Automation System.
 * It provides budget visualization per department and allows HR/Admin users
 * to modify budget data or calculate employee bonuses.
 * It integrates with the database to fetch department and employee data, and
 * visually renders pie charts for budget analysis.
 *
 * Core functionalities include:
 * - Viewing and selecting department budgets
 * - Creating budget categories
 * - Modifying budget allocations
 * - Calculating bonus for selected employees
 * 
 * This class is implemented using Swing and adheres to the MVC structure with
 * chart rendering and database access delegated to other classes.
 */
//package iaproductcode;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class Money extends JFrame implements ActionListener
{

  // Constants for styling
  public static final Font BUTTON_FONT = new Font("Helvetica", Font.BOLD, 16);
  public static final Font LABEL_FONT = new Font("Helvetica", Font.PLAIN, 14);

  // Components for left panel
  private JComboBox<String> departmentDropdown;
  private BudgetChartRender chartRender;
  private int[] departmentIDs;
  private String[] departmentNames;
  private JPanel leftPanel, buttonOnLeftPanel;
  private JButton modifyButton, createCategoryButton;

  // Components for right panel
  private JPanel rightPanel;
  private JComboBox<String> employeeDropdown;
  private JLabel employeeLabel, salaryLabel;
  private JButton bonusCalcButton;

  // Components for bottom panel
  private JPanel bottomPanel;
  private JButton returnButton;

  private JavaDBAccess dbAccess;
  private Object[][] employeeData;
  private Object[][] departmentData;

  /**
   * Constructs the Money window GUI which displays budget data and provides
   * salary and bonus functionality for HR purposes.
   */
  public Money()
  {
    // Frame Title (GUI)
    this.setTitle("Money -" + Welcome.getCompanyName() + " OA System");

    // Structuring the frame
    this.setBounds(1920 / 2, 1080 / 2, 1920, 1080);
    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    this.getContentPane().setBackground(Welcome.BG_COLOR);
    this.setExtendedState(JFrame.MAXIMIZED_BOTH);
    this.setLayout(new BorderLayout());

    // Constructing Left Panel (Graph)
    this.leftPanel = new JPanel();
    leftPanel.setBackground(Welcome.BG_COLOR);
    leftPanel.setLayout(new BorderLayout());
    leftPanel.setPreferredSize(new Dimension(600, 800)); // width and height

    // Retrieving information from DB for dept. dropdown & Initializing
    this.dbAccess = new JavaDBAccess();
    this.departmentData = dbAccess.getDepartmentData();
    this.departmentNames = new String[departmentData.length]; // Will use to store dept. name
    this.departmentIDs = new int[departmentData.length]; // Dept. ID Array

    for (int r = 0; r < departmentData.length; r++)
    {
      departmentIDs[r] = Integer.parseInt(departmentData[r][0].toString()); // Gets Department ID
      departmentNames[r] = departmentData[r][1].toString(); // Gets Department Name
    }
    
    // Dropdown for departments
    this.departmentDropdown = new JComboBox<>(departmentNames);
    departmentDropdown.setFont(LABEL_FONT);
    departmentDropdown.setAlignmentX(Component.CENTER_ALIGNMENT);
    departmentDropdown.addActionListener(this);
    
    // Create chart for default department (#1)
    this.chartRender = new BudgetChartRender(departmentIDs[0]);
    
    // Constructing Left Panel buttons
    this.buttonOnLeftPanel = new JPanel();
    buttonOnLeftPanel.setLayout(new FlowLayout()); // Help button placement (side by side)
    buttonOnLeftPanel.setBackground(Welcome.BG_COLOR);
    this.modifyButton = new JButton("Modify");
    modifyButton.setPreferredSize(new Dimension(180, 40));
    modifyButton.setFont(BUTTON_FONT);
    modifyButton.addActionListener(this);
    this.createCategoryButton = new JButton("Create Category");
    createCategoryButton.setPreferredSize(new Dimension(180, 40));
    createCategoryButton.setFont(BUTTON_FONT);
    createCategoryButton.addActionListener(this);
    buttonOnLeftPanel.add(modifyButton);
    buttonOnLeftPanel.add(createCategoryButton);

    leftPanel.add(departmentDropdown, BorderLayout.NORTH);
    leftPanel.add(chartRender, BorderLayout.CENTER);
    leftPanel.add(buttonOnLeftPanel, BorderLayout.SOUTH);

    // Constructing Right Panel 
    this.rightPanel = new JPanel();
    rightPanel.setBackground(Welcome.BG_COLOR);
    rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
    rightPanel.setPreferredSize(new Dimension(600, 800)); // width and height
    // Title label
    this.employeeLabel = new JLabel("Employee Salary & Bonus");
    employeeLabel.setFont(BUTTON_FONT);
    employeeLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
    rightPanel.add(employeeLabel);
    // Employee List Dropdown

    this.dbAccess = new JavaDBAccess();
    this.employeeData = dbAccess.getEmployeeData();
    String[] employeeNames = new String[employeeData.length]; // Retrieve only names from all employee data
    for (int i = 0; i < employeeData.length; i++)
    {
      employeeNames[i] = employeeData[i][2] + " " + employeeData[i][3]; // First and last name combined
    }

    // Create actual dropdown
    this.employeeDropdown = new JComboBox<>(employeeNames);
    employeeDropdown.setFont(LABEL_FONT);
    employeeDropdown.setAlignmentX(Component.CENTER_ALIGNMENT);
    employeeDropdown.addActionListener(this);
    rightPanel.add(employeeDropdown);

    // Employee Salary Label (Starts with first employee)
    double defaultSalary = Double.parseDouble(employeeData[0][6].toString()); // Retrieves from column 6 in employeesDB (Salary)
    String formattedSalary = String.format("%.2f", defaultSalary); // converts to 2 decimal place for money
    this.salaryLabel = new JLabel("Current Salary: $" + formattedSalary);
    salaryLabel.setFont(LABEL_FONT);
    salaryLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
    rightPanel.add(salaryLabel);

    rightPanel.add(Box.createRigidArea(new Dimension(0, 20))); // For separation from buttons
    this.bonusCalcButton = new JButton("Bonus Calculation");
    bonusCalcButton.setFont(BUTTON_FONT);
    bonusCalcButton.setPreferredSize(new Dimension(180, 40));
    bonusCalcButton.setAlignmentX(Component.CENTER_ALIGNMENT);
    bonusCalcButton.addActionListener(this);
    rightPanel.add(bonusCalcButton);
    rightPanel.add(Box.createRigidArea(new Dimension(0, 400))); // Moves button more centered

    // Bottom Navigation Panel
    this.bottomPanel = new JPanel();
    bottomPanel.setBackground(AdminDashboard.ADMIN_COLOR);

    returnButton = new JButton("Return to HR");
    returnButton.setFont(BUTTON_FONT);
    returnButton.setPreferredSize(new Dimension(180, 40));
    returnButton.addActionListener(this);
    bottomPanel.add(returnButton);

    // Add components to frame
    this.add(leftPanel, BorderLayout.WEST);
    this.add(rightPanel, BorderLayout.CENTER);
    this.add(bottomPanel, BorderLayout.SOUTH);

    // Set frame visible
    this.setVisible(true);

  }

  /**
   * Handles all action events triggered by the GUI buttons and dropdowns,
   * including department selection, budget modifications, bonus calculation,
   * and navigating back to the HR panel.
   *
   * @param e the event triggered by user interaction
   */
  @Override
  public void actionPerformed(ActionEvent e)
  {
    if (e.getSource() == modifyButton)
    {
      new BudgetInput();
      
    }
    else if (e.getSource() == createCategoryButton) // Direct to budget category popup
    {
      new BudgetCategoryCreate();
    }
    else if (e.getSource() == bonusCalcButton)
    {
      // Get selected employee index from the dropdown
      int selectedEmployee = employeeDropdown.getSelectedIndex();
      if (selectedEmployee >= 0)
      {
        // Retrieve salary from employeeData
        double salary = Double.parseDouble(employeeData[selectedEmployee][6].toString()); // Salary Column (EmployeeDB) converted to double

        // Prompt user to enter bonus percentage
        String input = JOptionPane.showInputDialog(this, "Enter Bonus Percentage (%):");

        try
        {
          double bonusPercentage = Double.parseDouble(input);
          double bonusAmount = BonusCalculation.calculateBonus(salary, bonusPercentage);
          String formattedBonus = String.format("%.2f", bonusAmount); // 2 decimal places
          JOptionPane.showMessageDialog(this, "Bonus Amount: $" + formattedBonus, "Bonus Calculation", JOptionPane.INFORMATION_MESSAGE);
        }
        catch (NumberFormatException ex)
        {
          JOptionPane.showMessageDialog(this, "Invalid input! Be sure to enter without % sign", "Error Invalid Input", JOptionPane.ERROR_MESSAGE);
        }
      }
    }
    else if (e.getSource() == returnButton)
    {
      new HumanResources();
      this.dispose();
    }
    // Update when new dropdown item selected.
    else if (e.getSource() == departmentDropdown) 
    {
      int selectedIndex = departmentDropdown.getSelectedIndex();
      if (selectedIndex >= 0)
      {
        int selectedDeptID = departmentIDs[selectedIndex]; // Get department ID
        chartRender = new BudgetChartRender(selectedDeptID); // Create new chart
        leftPanel.removeAll(); // Removes everything
        leftPanel.add(departmentDropdown, BorderLayout.NORTH); // Re-add dropdown
        leftPanel.add(chartRender, BorderLayout.CENTER);
        leftPanel.add(buttonOnLeftPanel, BorderLayout.SOUTH);
        leftPanel.revalidate();
        leftPanel.repaint();
      }
    }
    else if (e.getSource() == employeeDropdown)
    {
      int employeeIndex = employeeDropdown.getSelectedIndex();
      // Retrieve and update salary based on selected employee
      if (employeeIndex >= 0)
      {
        // Retrieves from column 6 in employeesDB (Salary)
        double salary = Double.parseDouble(employeeData[employeeIndex][6].toString());
        // converts to 2 decimal place for money
        String formattedSalary = String.format("%.2f", salary);
        salaryLabel.setText("Current Salary: $" + formattedSalary);
        // Text Update after new selection
        salaryLabel.revalidate();
        salaryLabel.repaint();
      }
    }
  }

  /**
   * Entry point for testing the Money class independently.
   * Sets the user context and launches the Money frame.
   *
   * @param args the command line arguments (not used)
   */
  public static void main(String[] args)
  {
    LogIn.setUsername("admin");
    LogIn.setRole("Admin");
    new Money();
  }
}
