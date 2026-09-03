//package iaproductcode;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * The AdminDashboard class represents the main dashboard interface for Admin users
 * in the Office Automation System. It allows navigation to various administrative
 * functions such as Human Resources management, Employee management, Knowledgebase access,
 * sending messages, and viewing the message table.
 *
 * Features:
 * - Displays messages sent in the system.
 * - Provides buttons for navigation to different management modules.
 * - Allows Admin users to interact with the database to retrieve and display information.
 *
 */
public class AdminDashboard extends JFrame implements ActionListener
{

  // Constants for styling
  public static final Color ADMIN_COLOR = new Color(103, 214, 238); // BLUE
  public static final Color PRIMARY_COLOR = new Color(80, 200, 120); // Green
  public static final Font MENU_FONT = new Font("Helvetica", Font.BOLD, 16);

  // Components for function bar
  private JPanel functionBar;
  private JButton homeButton, hrButton, employeeButton, knowledgeButton, sendMessageButton, logoutButton;
  private JLabel welcomeLabel, versionLabel;

  // Components for main content
  private JPanel messagePanel;
  private JTable messageTable;
  private JScrollPane tableScrollPane;
  private static DefaultTableModel tableModel;
  
  private JavaDBAccess dbAccess;

  /**
   * Constructs the AdminDashboard GUI, initializes the frame layout, 
   * navigation buttons, and loads messages from the database.
   */
  public AdminDashboard()
  {
    // Frame Title (GUI)
    this.setTitle("Dashboard (Admin) -" + Welcome.getCompanyName() + " OA System");

    // Structuring the frame
    this.setBounds(1920 / 2, 1080 / 2, 1920, 1080);
    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    this.getContentPane().setBackground(ADMIN_COLOR);
    this.setExtendedState(JFrame.MAXIMIZED_BOTH);
    this.setLayout(new BorderLayout());

    // Constructing Left Function Bar Panel
    functionBar = new JPanel();
    functionBar.setLayout(new BoxLayout(functionBar, BoxLayout.Y_AXIS));
    functionBar.setBackground(ADMIN_COLOR);
    functionBar.setPreferredSize(new Dimension(200, getHeight())); // set width 200, height relative

    // Constructing function bar buttons
    homeButton = newPrimaryButton("Home");
    hrButton = newFuncButton("Human Resources");
    employeeButton = newFuncButton("Employee Manage");
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

    this.tableModel = new DefaultTableModel(columnNames, 0)
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

  /**
   * Creates a styled function button for the function bar (non-primary).
   *
   * @param text The text to display on the button.
   * @return The configured JButton object.
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

  /**
   * Creates a styled primary function button for the function bar (highlighted).
   *
   * @param text The text to display on the button.
   * @return The configured primary JButton object.
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
   * Updates the message table with the latest data from the database.
   * Clears the current table rows and inserts refreshed data.
   */
  public static void updateTable()
  {
    // Fetch latest messages from DB
    JavaDBAccess dbAccess = new JavaDBAccess();
    Object[][] messageData = dbAccess.getMessagesData();

    // Clear existing table data
   tableModel.setRowCount(0);

    // Insert new data into the table
   for (int r = 0; r < messageData.length; r++)
    {
      tableModel.addRow(messageData[r]);
    }
  }
  
  /**
   * Handles action events triggered by the navigation buttons.
   * Opens corresponding windows or performs the appropriate action based on the clicked button.
   *
   * @param e The action event triggered by button clicks.
   */
  @Override
  public void actionPerformed(ActionEvent e)
  {
    Object source = e.getSource();

    if (source == homeButton)
    {
      // Current Page
    }
    else if (source == hrButton)
    {
      new HumanResources();
      this.dispose();
    }
    else if (source == employeeButton)
    {
      new EmployeeManagement();
      this.dispose();
    }
    else if (source == knowledgeButton)
    {
      new Knowledgebase();
      this.dispose();
    }
    else if (source == sendMessageButton)
    {
      new SendMessage();
    }
    else if (source == logoutButton)
    {
      new Welcome(); // Return back to Welcome Page
      this.dispose();
    }
  }

  /**
   * Main method for testing and running the AdminDashboard interface.
   * Sets the default logged-in user to Admin for demonstration purposes.
   *
   * @param args Command-line arguments (not used).
   */
  public static void main(String[] args)
  {
    LogIn.setUsername("admin");
    LogIn.setRole("Admin");
    new AdminDashboard();
  }
}
