/**
 * BudgetCategoryCreate class handles the GUI for creating new budget categories
 * within selected departments. Allows users to select a department and input a
 * new category name, then inserts the entry into the database.
 * 
 * This class utilizes Java Swing for the user interface and connects to the 
 * database via JavaDBAccess.
 * 
 */
//package iaproductcode;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class BudgetCategoryCreate extends JFrame implements ActionListener
{

  private JComboBox<String> departmentDropdown;
  private JTextField categoryField;
  private JButton createButton, closeButton;
  private JavaDBAccess dbAccess;
  private Object[][] departmentData;

  /**
   * Constructs the BudgetCategoryCreate GUI, initializes components, sets up
   * the layout, and prepares database access to retrieve department data.
   */
  public BudgetCategoryCreate()
  {
    // Frame Title (GUI)
    this.setTitle("Create Budget Category -" + Welcome.getCompanyName() + " OA System");

    // Structuring the frame
    this.setSize(500, 400);
    this.setLocationRelativeTo(null);
    this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    this.getContentPane().setBackground(Welcome.BG_COLOR);
    this.setLayout(new BorderLayout());
    
    this.setLayout(new GridBagLayout());
    GridBagConstraints gbc = new GridBagConstraints(); // Helps position component location
    gbc.insets = new Insets(10, 10, 10, 10); // Padding GridBagLayout
    gbc.fill = GridBagConstraints.HORIZONTAL;

    // DB Access
    this.dbAccess = new JavaDBAccess();
    this.departmentData = dbAccess.getDepartmentData(); // Fetch Departments

    // Department Dropdown
    String[] departmentNames = new String[departmentData.length];
    for (int i = 0; i < departmentData.length; i++)
    {
      departmentNames[i] = departmentData[i][1].toString(); // Department Name
    }

    JLabel deptLabel = new JLabel("Select Department:");
    deptLabel.setFont(new Font("Helvetica", Font.BOLD, 14));
    gbc.gridx = 0;
    gbc.gridy = 0;
    this.add(deptLabel, gbc);

    this.departmentDropdown = new JComboBox<>(departmentNames);
    gbc.gridx = 1;
    this.add(departmentDropdown, gbc);

    // Category Input
    JLabel categoryLabel = new JLabel("Category Name:");
    categoryLabel.setFont(new Font("Helvetica", Font.BOLD, 14));
    gbc.gridx = 0;
    gbc.gridy = 1;
    this.add(categoryLabel, gbc);

    this.categoryField = new JTextField(15);
    gbc.gridx = 1;
    this.add(categoryField, gbc);

    // Buttons
    this.createButton = new JButton("Create");
    createButton.addActionListener(this);
    gbc.gridx = 0;
    gbc.gridy = 2;
    this.add(createButton, gbc);

    this.closeButton = new JButton("Close");
    closeButton.addActionListener(this);
    gbc.gridx = 1;
    this.add(closeButton, gbc);

    // Make visible
    this.setVisible(true);
  }

  /**
   * Handles button actions for creating a budget category or closing the window.
   * Validates input and performs database insertion if the category name is provided.
   *
   * @param e the ActionEvent triggered by the user's interaction
   */
  @Override
  public void actionPerformed(ActionEvent e)
  {
    if (e.getSource() == createButton)
    {
      int selectedDeptIndex = departmentDropdown.getSelectedIndex();
      int departmentID = Integer.parseInt(departmentData[selectedDeptIndex][0].toString()); // Retrieve Dept ID
      String category = categoryField.getText().trim();

      if (category.isEmpty())
      {
        JOptionPane.showMessageDialog(this, "Category name cannot be empty!", "Error", JOptionPane.ERROR_MESSAGE);
        return;
      }

      dbAccess.createBudgetCategory(departmentID, category); // Insert into DB
      JOptionPane.showMessageDialog(this, "Category created successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
      categoryField.setText(""); // Clear field after insert
    }
    else if (e.getSource() == closeButton)
    {
      this.dispose(); // Close window
    }
  }

  /**
   * Main method for testing and launching the BudgetCategoryCreate GUI independently.
   *
   * @param args command-line arguments (not used)
   */
  public static void main(String[] args)
  {
    new BudgetCategoryCreate();
  }
}
