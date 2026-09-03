/**
 * GUI class that displays performance evaluations in a JTable.
 * Admins can view all evaluations, while Managers see only those
 * within their department, excluding their own submissions.
 * Integrates with JavaDBAccess for data retrieval and uses Java Swing for UI rendering.
 */
//package iaproductcode;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class PerformanceEvaluationDB extends JFrame implements ActionListener
{

  // Constants for styling
  public static final Font BUTTON_FONT = new Font("Helvetica", Font.BOLD, 16);
  public static final Font TITLE_FONT = new Font("Helvetica", Font.BOLD, 18);

  // Components for DB Table
  private JPanel titlePanel, tablePanel;
  private JLabel titleLabel;
  private JTable PETable;
  private DefaultTableModel tableModel;

  // Components for Button Panel
  private JPanel buttonPanel;
  private JButton returnButton;

  private JavaDBAccess dbAccess;

  /**
   * Constructs the PerformanceEvaluationDB frame, initializes UI components,
   * fetches and displays performance evaluations based on the user's role.
   */
  public PerformanceEvaluationDB()
  {
    // Frame Title (GUI)
    this.setTitle("Performance Evaluation Table -" + Welcome.getCompanyName() + " OA System");

    // Structuring the frame
    this.setBounds(1920 / 2, 1080 / 2, 1920, 1080);
    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    this.getContentPane().setBackground(Welcome.BG_COLOR);
    this.setExtendedState(JFrame.MAXIMIZED_BOTH);
    this.setLayout(new BorderLayout());

    // Constructing Title Panel
    this.titlePanel = new JPanel();
    titlePanel.setBackground(Welcome.BG_COLOR);
    this.titleLabel = new JLabel("Performance Evaluation DB", SwingConstants.CENTER);
    titleLabel.setFont(TITLE_FONT);
    titlePanel.add(titleLabel);

    // Constructing Table Panel
    this.tablePanel = new JPanel();
    tablePanel.setLayout(new BorderLayout());
    tablePanel.setBackground(Welcome.BG_COLOR);

    // Data and Column Formatting
    this.dbAccess = new JavaDBAccess();
    Object[][] data;
    
    // Access control component
    if (LogIn.getRole().equals("Admin"))
    {
      data = dbAccess.getPerformanceEvaluationData(); // Admin sees all evalutaions
    }
    else if (LogIn.getRole().equals("Manager"))
    {
      int managerDeptID = dbAccess.getManagerDepartment(LogIn.getUsername());
      int managerUserID = dbAccess.getUserIDByUsername(LogIn.getUsername());
      // Filters out performance evaluation from other departments & manager itself
      data = dbAccess.getPerformanceEvaluationDataFiltered(managerDeptID, managerUserID);
    }
    else
    {
      data = new Object[0][0]; // Just in case for safety
    }
    String[] columnNames =
    {
      "Evaluation ID", "Employee ID", "Evaluator", "Department ID", "Evaluation Date", "Evaluation Content"
    };
    tableModel = new DefaultTableModel(columnNames, 0)
    {
      @Override
      public boolean isCellEditable(int row, int column)
      {
        return false; // Makes all cells non-editable
      }
    };
    PETable = new JTable(tableModel);
    PETable.setRowHeight(25);

    // DB data for all Performance Evaluations
    for (int r = 0; r < data.length; r++)
    {
      tableModel.addRow(data[r]); // adds whole row for table
    }

    // Scroll Pane for the table
    JScrollPane tableScrollPane = new JScrollPane(PETable);
    tableScrollPane.setBorder(BorderFactory.createTitledBorder("Employee Information"));
    tablePanel.add(tableScrollPane, BorderLayout.CENTER);

    // Constructing Button Panel
    this.buttonPanel = new JPanel();
    buttonPanel.setBackground(Welcome.BG_COLOR);

    returnButton = new JButton("Return");
    returnButton.setFont(BUTTON_FONT);
    returnButton.setPreferredSize(new Dimension(300, 40));
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
   * Handles actions from UI components. Redirects users based on their role
   * when the return button is clicked. Restricts access for unauthorized users.
   */
  @Override
  public void actionPerformed(ActionEvent e)
  {
    if (e.getSource() == returnButton)
    {
      // User Direct Process
      if (LogIn.getRole().equals("Admin"))
      {
        new PerformanceEvaluation();
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
  }

  /**
   * Main method for standalone testing. Sets default login credentials and launches the GUI.
   */
  public static void main(String[] args)
  {
    LogIn.setUsername("admin");
    LogIn.setRole("Admin");
    new PerformanceEvaluationDB();
  }
}
