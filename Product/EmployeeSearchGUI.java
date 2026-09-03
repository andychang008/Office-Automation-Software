/**
 * GUI class for searching employees by name within the OA System.
 * Provides user interface components to input search queries, execute searches, and handle results.
 * Connects with EmployeeSearch logic and updates the EmployeeInfoDB table upon search.
 *
 */
//package iaproductcode;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class EmployeeSearchGUI extends JFrame implements ActionListener
{
  private JPanel inputPanel, buttonPanel;
  private JLabel searchLabel;
  private JTextField searchField;
  private JButton searchButton, cancelButton;
  private EmployeeInfoDB employeeInfoDB;

  /**
   * Constructor to initialize the Employee Search GUI.
   * Sets up the frame, input fields, and search/cancel buttons.
   */
  public EmployeeSearchGUI()
  {

    // Frame Title (GUI)
    this.setTitle("Employee Search -" + Welcome.getCompanyName() + " OA System");

    // Structuring the frame
    this.setSize(500, 400);
    this.setLocationRelativeTo(null);
    this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    this.getContentPane().setBackground(Welcome.BG_COLOR);
    this.setLayout(new BorderLayout());

    // Input Panel
    this.inputPanel = new JPanel();
    inputPanel.setLayout(new FlowLayout());
    inputPanel.setBackground(Welcome.BG_COLOR);
    
    this.searchLabel = new JLabel("Enter Employee Name:");
    searchField = new JTextField(20);
    
    inputPanel.add(searchLabel);
    inputPanel.add(searchField);

    // Button Panel
    this.buttonPanel = new JPanel();
    
    this.searchButton = new JButton("Search");
    searchButton.addActionListener(this);
    
    this.cancelButton = new JButton("Cancel");
    cancelButton.addActionListener(this);

    buttonPanel.add(searchButton);
    buttonPanel.add(cancelButton);

    // Add components to frame
    this.add(inputPanel, BorderLayout.CENTER);
    this.add(buttonPanel, BorderLayout.SOUTH);

    // Set frame visible
    this.setVisible(true);
  }

  /**
   * Handles button actions for searching and cancelling.
   * Executes employee search using EmployeeSearch class and updates the EmployeeInfoDB table if matches are found.
   *
   * @param e The ActionEvent triggered by button clicks.
   */
  @Override
  public void actionPerformed(ActionEvent e)
  {
    if (e.getSource() == searchButton)
    {
      String searchTerm = searchField.getText().trim();
      
      if (searchTerm.isEmpty())
      {
        JOptionPane.showMessageDialog(this, "Please enter a name!", "Error", JOptionPane.ERROR_MESSAGE);
        return;
      }

      Object[][] filteredData = EmployeeSearch.searchEmployees(searchTerm); // ✅ CHANGED CODE (Uses Separate Calculation Class)

      if (filteredData.length > 0)
      {
        EmployeeInfoDB.refreshTable(filteredData);
      }
      else
      {
        JOptionPane.showMessageDialog(this, "No employee found.", "Search Results", JOptionPane.INFORMATION_MESSAGE);
      }
      
      this.dispose();
    }
    else if (e.getSource() == cancelButton)
    {
      this.dispose();
    }
  }

  /**
   * Main method for testing the Employee Search GUI independently.
   * Launches the search window.
   *
   * @param args Command-line arguments (not used).
   */
  public static void main(String[] args)
  {
    new EmployeeSearchGUI();
  }
}
