/**
 * The BudgetInput class provides a graphical user interface (GUI) for Admin users 
 * to input and update budget allocations and spent amounts for different departments 
 * and budget categories. It interacts with the database through JavaDBAccess 
 * to perform budget updates.
 * 
 * Features:
 * - Dropdown selection of departments and budget categories.
 * - Input fields for allocated and spent amounts.
 * - Updates budget data in the database with input validation.
 * - Filters budget categories based on the selected department.
 * 
 */
//package iaproductcode;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class BudgetInput extends JFrame implements ActionListener
{

  // Constants for styling
  private static final Font BUTTON_FONT = new Font("Helvetica", Font.BOLD, 16);
  private static final Font LABEL_FONT = new Font("Helvetica", Font.PLAIN, 14);

  // UI Components
  private JPanel mainPanel, buttonPanel;
  private JComboBox<String> departmentDropdown, categoryDropdown;
  private JTextField allocatedAmountField, spentAmountField;
  private JButton updateButton, cancelButton;

  private JavaDBAccess dbAccess;
  private Object[][] departmentData, categoryData;
  private int[] departmentIDs;
  private String[] departmentNames;

  /**
   * Constructor that initializes the BudgetInput GUI window. 
   * Loads department and budget category data from the database 
   * and sets up the form for budget input and update.
   */
  public BudgetInput()
  {
    // Frame Title (GUI)
    this.setTitle("Budget Input -" + Welcome.getCompanyName() + " OA System");

    // Structuring the frame
    this.setSize(500, 400);
    this.setLocationRelativeTo(null);
    this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    this.getContentPane().setBackground(Welcome.BG_COLOR);
    this.setLayout(new BorderLayout());

    // Database Access
    this.dbAccess = new JavaDBAccess();
    this.departmentData = dbAccess.getDepartmentData(); // Get dept info from DB
    this.categoryData = dbAccess.getBudgetData(); // Get budget from DB

    // Helps link department name with ID
    this.departmentNames = new String[departmentData.length];
    this.departmentIDs = new int[departmentData.length];
    for (int r = 0; r < departmentData.length; r++)
    {
      departmentIDs[r] = Integer.parseInt(departmentData[r][0].toString()); // Gets Department ID
      departmentNames[r] = departmentData[r][1].toString(); // Gets Department Name
    }

    // Get Budget Categories
    String[] categoryNames = new String[categoryData.length];
    for (int r = 0; r < categoryData.length; r++)
    {
      categoryNames[r] = categoryData[r][2].toString(); // Category Name
    }

    // Constructing Main Panel
    this.mainPanel = new JPanel();
    mainPanel.setLayout(new GridLayout(4, 2, 10, 10));
    mainPanel.setBackground(Welcome.BG_COLOR);
    mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50));

    // Constructing Department Dropdown
    JLabel departmentLabel = new JLabel("Select Department:");
    departmentLabel.setFont(LABEL_FONT);
    this.departmentDropdown = new JComboBox<>(departmentNames);
    departmentDropdown.setFont(LABEL_FONT);
    departmentDropdown.addActionListener(this);

    // Category Dropdown
    JLabel categoryLabel = new JLabel("Select Budget Category:");
    categoryLabel.setFont(LABEL_FONT);
    this.categoryDropdown = new JComboBox<>();
    int selectedDeptID = 1;
    ArrayList<String> filteredCategories = new ArrayList<>();
    for (int r = 0; r < categoryData.length; r++)
    {
      // If the departmentID selected is in this row
      if (Integer.parseInt(categoryData[r][1].toString()) == selectedDeptID)
      {
        filteredCategories.add(categoryData[r][2].toString()); // Adds the category from this row
      }
    }
    categoryDropdown.setModel(new DefaultComboBoxModel<>(filteredCategories.toArray(new String[0])));
    categoryDropdown.setFont(LABEL_FONT);

    // Allocated Amount Input
    JLabel allocatedLabel = new JLabel("New Allocated Amount ($):");
    allocatedLabel.setFont(LABEL_FONT);
    this.allocatedAmountField = new JTextField();
    allocatedAmountField.setFont(LABEL_FONT);

    // Spent Amount Input
    JLabel spentLabel = new JLabel("New Amount Spent ($):");
    spentLabel.setFont(LABEL_FONT);
    this.spentAmountField = new JTextField();
    spentAmountField.setFont(LABEL_FONT);

    // Add components to main panel
    mainPanel.add(departmentLabel);
    mainPanel.add(departmentDropdown);
    mainPanel.add(categoryLabel);
    mainPanel.add(categoryDropdown);
    mainPanel.add(allocatedLabel);
    mainPanel.add(allocatedAmountField);
    mainPanel.add(spentLabel);
    mainPanel.add(spentAmountField);

    // Buttons Panel
    this.buttonPanel = new JPanel();
    buttonPanel.setBackground(Welcome.BG_COLOR);

    this.updateButton = new JButton("Update Budget");
    updateButton.setFont(BUTTON_FONT);
    updateButton.addActionListener(this);

    this.cancelButton = new JButton("Cancel");
    cancelButton.setFont(BUTTON_FONT);
    cancelButton.addActionListener(this);

    buttonPanel.add(updateButton);
    buttonPanel.add(cancelButton);

    // Add panels to frame
    this.add(mainPanel, BorderLayout.CENTER);
    this.add(buttonPanel, BorderLayout.SOUTH);

    // Set frame visible
    this.setVisible(true);
  }

  /**
   * Handles user interactions with the GUI components, including:
   * - Updating the budget information upon clicking the update button.
   * - Filtering budget categories when the selected department changes.
   * - Closing the window when the cancel button is pressed.
   * 
   * @param e The action event triggered by the user.
   */
  @Override
  public void actionPerformed(ActionEvent e)
  {
    if (e.getSource() == updateButton)
    {
      try
      {
        // Retrieve Inputs
        int departmentIndex = departmentDropdown.getSelectedIndex();
        int departmentID = departmentIDs[departmentIndex]; // Gets Department ID from the corresponding dept. name in the dropdown
        String category = (String) categoryDropdown.getSelectedItem();
        double allocatedAmount = Double.parseDouble(allocatedAmountField.getText().trim());
        double spentAmount = Double.parseDouble(spentAmountField.getText().trim());

        // Ensure values are non-negative
        if (allocatedAmount < 0 || spentAmount < 0)
        {
          JOptionPane.showMessageDialog(this, "Values cannot be negative.", "Error Negative Value", JOptionPane.ERROR_MESSAGE);
          return;
        }

        // Update budget in DB
        dbAccess.updateBudget(departmentID, category, allocatedAmount, spentAmount);
        JOptionPane.showMessageDialog(this, "Budget updated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);

        // Clear fields after submission
        allocatedAmountField.setText("");
        spentAmountField.setText("");

      }
      catch (NumberFormatException ex)
      {
        JOptionPane.showMessageDialog(this, "Invalid input! Please enter numeric values.", "Error", JOptionPane.ERROR_MESSAGE);
      }
    }
    else if (e.getSource() == departmentDropdown)
    {
      int selectedDeptID = departmentIDs[departmentDropdown.getSelectedIndex()]; // Gets dept. ID from dept name
      ArrayList<String> filteredCategories = new ArrayList<>(); // Filters category based on department (Avoid cross department categories from appearing)

      // Clears all dropdown first
      categoryDropdown.removeAllItems();

      // Fetching for categories but with filter
      for (int r = 0; r < categoryData.length; r++)
      {
        // If the departmentID selected is in this row
        if (Integer.parseInt(categoryData[r][1].toString()) == selectedDeptID)
        {
          filteredCategories.add(categoryData[r][2].toString()); // Adds the category from this row
        }
      }

      // Changes dropdown to only include filtered categories (AKA the ones in the department)
      categoryDropdown.setModel(new DefaultComboBoxModel<>(filteredCategories.toArray(new String[0])));
    }

    else if (e.getSource() == cancelButton)
    {
      this.dispose();
    }
  }

  /**
   * Main method for standalone testing of the BudgetInput class.
   * Launches the budget input window.
   * 
   * @param args Command-line arguments (not used).
   */
  public static void main(String[] args)
  {
    new BudgetInput();
  }
}
