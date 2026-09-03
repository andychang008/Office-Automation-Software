
//package iaproductcode;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * The Knowledgebase class represents the GUI for displaying the knowledge base posts
 * within the Office Automation System. It retrieves data from the database and
 * presents it in a non-editable table format. This class handles navigation between
 * dashboards based on user roles and allows the user to log out or return to their
 * respective dashboard.
 *
 */
public class Knowledgebase extends JFrame implements ActionListener
{

  // Constants for styling
  public static final Font MENU_FONT = new Font("Helvetica", Font.BOLD, 16);

  // Components for function bar
  private JPanel bottomPanel;
  private JButton returnButton, logoutButton;

  // Components for knowledgebase table
  private JPanel knowledgePanel;
  private JTable knowledgeTable;
  private JScrollPane tableScrollPane;

  private JavaDBAccess dbAccess;

  /**
   * Constructor that initializes the Knowledgebase GUI, retrieves data from the database,
   * and populates the knowledge base table. It also sets up navigation buttons.
   */
  public Knowledgebase()
  {
    // Frame Title (GUI)
    this.setTitle("Knowledgebase -" + Welcome.getCompanyName() + " OA System");

    // Structuring the frame
    this.setBounds(1920 / 2, 1080 / 2, 1920, 1080);
    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    this.getContentPane().setBackground(Welcome.BG_COLOR);
    this.setExtendedState(JFrame.MAXIMIZED_BOTH);
    this.setLayout(new BorderLayout());

    // Constructing Bottom Button Bar Panel
    this.bottomPanel = new JPanel();
    bottomPanel.setBackground(Welcome.BG_COLOR);

    // Constructing function bar buttons
    returnButton = newBottomButton("Return to Home");
    logoutButton = newBottomButton("Logout");

    // Adding buttons to bottom panel
    bottomPanel.add(returnButton);
    bottomPanel.add(logoutButton);

    // KB Panel for KB Table
    this.knowledgePanel = new JPanel(new BorderLayout());
    knowledgePanel.setBackground(Welcome.BG_COLOR);

    // Constructing KB Table with KB Content
    this.dbAccess = new JavaDBAccess();
    Object[][] data = dbAccess.getKBData();
    String[] columnNames =
    {
      "Published By", "Title", "Content", "Published Date"
    };

    DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0)
    // column names and row number
    {
      @Override
      public boolean isCellEditable(int row, int column)
      {
        return false; // Makes all cells non-editable
      }
    };
    knowledgeTable = new JTable(tableModel);
    knowledgeTable.setRowHeight(25); // Adjust row height for better visibility

    // Add a border around the table
    tableScrollPane = new JScrollPane(knowledgeTable);
    tableScrollPane.setBorder(BorderFactory.createTitledBorder("Knowledgebase"));

    // KB Posts from DB
    for (int r = 0; r < data.length; r++)
    {
      tableModel.addRow(data[r]);
    }

    knowledgePanel.add(tableScrollPane, BorderLayout.CENTER);

    // Add components to frame
    this.add(bottomPanel, BorderLayout.SOUTH);
    this.add(knowledgePanel, BorderLayout.CENTER);

    // Set frame visible
    this.setVisible(true);
  }

  /**
   * Helper method to create a styled JButton for the bottom function bar.
   *
   * @param text The text to display on the button.
   * @return A styled JButton with the specified text.
   */
  private JButton newBottomButton(String text)
  {
    JButton button = new JButton(text);
    button.setFont(MENU_FONT);
    button.setAlignmentX(Component.CENTER_ALIGNMENT);
    button.setPreferredSize(new Dimension(180, 40));
    button.addActionListener(this);
    return button;
  }

  /**
   * Handles button click actions for the return and logout buttons.
   * Navigates the user back to the appropriate dashboard or logs out the user.
   *
   * @param e The ActionEvent triggered by button clicks.
   */
  @Override
  public void actionPerformed(ActionEvent e)
  {
    Object source = e.getSource();

    if (source == returnButton)
    {
      // User Direct Process
      if (LogIn.getRole().equals("Admin"))
      {
        new AdminDashboard();
        this.dispose();
      }
      else if (LogIn.getRole().equals("Manager"))
      {
        new ManagerDashboard();
        this.dispose();
      }
      else if (LogIn.getRole().equals("Employee"))
      {
        new EmployeeDashboard();
        this.dispose();
      }
      else
      {
        JOptionPane.showMessageDialog(this, "WARNING: YOU ARE ACCESSING WITHOUT PERMISSION."
            + "\nRESOURCES ON THIS PAGE IS RESTRICTED. PROPERTY OF " + Welcome.getCompanyName() + ".", "WARNING: ILLEGAL ACCESS", JOptionPane.ERROR_MESSAGE);
        System.exit(0);
      }
    }
    else if (source == logoutButton)
    {
      new Welcome();
      this.dispose();
    }
  }

  /**
   * Main method for testing the Knowledgebase GUI independently.
   * Sets default login credentials and launches the Knowledgebase window.
   *
   * @param args Command-line arguments (not used).
   */
  public static void main(String[] args)
  {
    LogIn.setUsername("admin");
    LogIn.setRole("Admin");
    new Knowledgebase();
  }
}
