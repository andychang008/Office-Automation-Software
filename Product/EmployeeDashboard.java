//package iaproductcode;
/**
 * EmployeeDashboard.java
 *
 * This class represents the Employee Dashboard GUI for the Office Automation System.
 * It displays messages to employees and provides navigation buttons to the knowledgebase
 * and logout functionality. The dashboard retrieves and presents message data
 * from the database using the JavaDBAccess class.
 *
 * The EmployeeDashboard restricts employee users to viewing messages only and
 * does not provide access to other admin or manager functionalities.
 */

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class EmployeeDashboard extends JFrame implements ActionListener
{

  // Constants for styling
  public static final Color PRIMARY_COLOR = new Color(80, 200, 120); // Green
  public static final Font MENU_FONT = new Font("Helvetica", Font.BOLD, 16);

  // Components for function bar
  private JPanel functionBar, messagePanel;
  private JButton homeButton, knowledgeButton, logoutButton;
  private JLabel welcomeLabel, versionLabel;

  // Components for main content
  private JTable messageTable;
  private JScrollPane tableScrollPane;
  
  private JavaDBAccess dbAccess;

  /**
   * Constructs the EmployeeDashboard GUI, including the navigation panel,
   * welcome message, and message table populated from the database.
   */
  public EmployeeDashboard()
  {
    // Frame Title (GUI)
    this.setTitle("Dashboard -" + Welcome.getCompanyName() + " OA System");

    // Structuring the frame
    this.setBounds(1920 / 2, 1080 / 2, 1920, 1080);
    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    this.getContentPane().setBackground(Welcome.BG_COLOR);
    this.setExtendedState(JFrame.MAXIMIZED_BOTH);
    this.setLayout(new BorderLayout());

    // Constructing Left Function Bar Panel
    functionBar = new JPanel();
    functionBar.setLayout(new BoxLayout(functionBar, BoxLayout.Y_AXIS));
    functionBar.setBackground(Welcome.BG_COLOR);
    functionBar.setPreferredSize(new Dimension(200, getHeight()));

    // Constructing function bar buttons
    homeButton = newPrimaryButton("Home");
    knowledgeButton = newFuncButton("Knowledgebase");
    logoutButton = newFuncButton("Logout");

    // Constructing version and welcome message
    welcomeLabel = new JLabel("Welcome, " + LogIn.getUsername());
    welcomeLabel.setFont(new Font("Helvetica", Font.BOLD, 16));
    welcomeLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
    versionLabel = new JLabel(Welcome.getVersionNumber());
    versionLabel.setFont(new Font("Helvetica", Font.PLAIN, 14));
    versionLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

    // Adding FB buttons
    functionBar.add(Box.createVerticalGlue()); // top pillar to center vert.
    functionBar.add(homeButton);
    functionBar.add(Box.createRigidArea(new Dimension(0, 20)));
    functionBar.add(knowledgeButton);
    functionBar.add(Box.createRigidArea(new Dimension(0, 20)));
    functionBar.add(welcomeLabel);
    functionBar.add(versionLabel);
    functionBar.add(Box.createVerticalGlue()); // bottom pillar center vert.
    functionBar.add(logoutButton);

    // Message Panel for table with messages
    this.messagePanel = new JPanel(new BorderLayout());
    messagePanel.setBackground(Welcome.BG_COLOR);

    // Constructing Messages Table with DB
    this.dbAccess = new JavaDBAccess();
    Object[][] data = dbAccess.getMessagesData();
    String[] columnNames =
    {
      "Sender", "Title", "Message Content", "Sent Date"
    };

    DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0)
    // columns and starting row number
    {
      @Override
      public boolean isCellEditable(int row, int column)
      {
        return false; // Makes all cells non-editable
      }
    };
    
    messageTable = new JTable(tableModel);
    messageTable.setRowHeight(25); // Adjust row height for better visibility

    // Add a border around the table
    tableScrollPane = new JScrollPane(messageTable);
    tableScrollPane.setBorder(BorderFactory.createTitledBorder("Message Table"));

    // DB Messages
    for (int r = 0; r < data.length; r++)
    {
      tableModel.addRow(data[r]);
    }
    messagePanel.add(tableScrollPane, BorderLayout.CENTER);

    // Add components to frame
    this.add(functionBar, BorderLayout.WEST);
    this.add(messagePanel, BorderLayout.CENTER);

    // Set frame visible
    this.setVisible(true);
  }

  // Styling Function for function bar buttons (except primary)
  /**
   * Creates a styled function bar button (non-primary).
   *
   * @param text The text displayed on the button.
   * @return A configured JButton instance.
   */
  private JButton newFuncButton(String text)
  {
    JButton button = new JButton(text);
    button.setFont(MENU_FONT);
    button.setAlignmentX(Component.CENTER_ALIGNMENT);
    button.setPreferredSize(new Dimension(180, 40));
    button.addActionListener(this);
    return button;
  }

  // Styling function for primary function button
  /**
   * Creates a styled primary function bar button.
   *
   * @param text The text displayed on the button.
   * @return A configured JButton instance with primary styling.
   */
  private JButton newPrimaryButton(String text)
  {
    JButton pButton = new JButton(text);
    pButton.setFont(MENU_FONT);
    pButton.setAlignmentX(Component.CENTER_ALIGNMENT);
    pButton.setPreferredSize(new Dimension(180, 40));
    pButton.setForeground(PRIMARY_COLOR);
    pButton.addActionListener(this);
    return pButton;
  }

  /**
   * Handles the actions triggered by the navigation buttons.
   * Redirects to the appropriate pages based on the user's selection.
   *
   * @param e The ActionEvent triggered by user interaction.
   */
  @Override
  public void actionPerformed(ActionEvent e)
  {
    Object source = e.getSource();

    if (source == homeButton)
    {
      // Current Page
    }
    else if (source == knowledgeButton)
    {
      new Knowledgebase();
      this.dispose();
    }
    else if (source == logoutButton)
    {
      new Welcome(); // Return back to Welcome Page
      this.dispose();
    }
  }

  /**
   * Main method to test the EmployeeDashboard independently.
   * Sets dummy login credentials for testing purposes.
   *
   * @param args Command line arguments (not used).
   */
  public static void main(String[] args)
  {
    LogIn.setUsername("employee");
    LogIn.setRole("Employee");
    new EmployeeDashboard();
  }
}
