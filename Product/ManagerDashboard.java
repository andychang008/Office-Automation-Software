//package iaproductcode;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
/**
 * ManagerDashboard is the main dashboard interface for users with the "Manager" role.
 * It displays messages posted by Admins/Managers and provides access to other features 
 * such as performance evaluation, employee viewing, knowledge base, and messaging.
 * It includes a function bar and a central message table populated from the database.
 */
public class ManagerDashboard extends JFrame implements ActionListener
{

  // Constants for styling
  public static final Color MANAGER_COLOR = new Color(143, 22, 0); // Red
  public static final Color PRIMARY_COLOR = new Color(80, 200, 120); // Green
  public static final Font MENU_FONT = new Font("Helvetica", Font.BOLD, 16);

  // Components for function bar
  private JPanel functionBar;
  private JButton homeButton, peButton, employeeButton, knowledgeButton, sendMessageButton, logoutButton;
  private JLabel welcomeLabel, versionLabel;

  // Components for main content
  private JTable messageTable;
  private JScrollPane tableScrollPane;
  private static DefaultTableModel tableModel;

  public ManagerDashboard(JPanel functionBar, JButton homeButton, JButton peButton, JButton employeeButton, JButton knowledgeButton, JButton sendMessageButton, JButton logoutButton, JLabel welcomeLabel, JLabel versionLabel, JTable messageTable, JScrollPane tableScrollPane, JavaDBAccess dbAccess) throws HeadlessException
  {
    this.functionBar = functionBar;
    this.homeButton = homeButton;
    this.peButton = peButton;
    this.employeeButton = employeeButton;
    this.knowledgeButton = knowledgeButton;
    this.sendMessageButton = sendMessageButton;
    this.logoutButton = logoutButton;
    this.welcomeLabel = welcomeLabel;
    this.versionLabel = versionLabel;
    this.messageTable = messageTable;
    this.tableScrollPane = tableScrollPane;
    this.dbAccess = dbAccess;
  }
  
  
  private JavaDBAccess dbAccess;

  /**
   * Constructs the ManagerDashboard GUI frame, sets up layout, function bar, 
   * welcome/version labels, and message table with content from the database.
   */
  public ManagerDashboard()
  {
    // Frame Title (GUI)
    this.setTitle("Dashboard (Manager) -" + Welcome.getCompanyName() + " OA System");

    // Structuring the frame
    this.setBounds(1920 / 2, 1080 / 2, 1920, 1080);
    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    this.getContentPane().setBackground(MANAGER_COLOR);
    this.setExtendedState(JFrame.MAXIMIZED_BOTH);
    this.setLayout(new BorderLayout());

    // Constructing Left Function Bar Panel
    functionBar = new JPanel();
    functionBar.setLayout(new BoxLayout(functionBar, BoxLayout.Y_AXIS));
    functionBar.setBackground(MANAGER_COLOR);
    functionBar.setPreferredSize(new Dimension(200, getHeight()));

    // Constructing function bar buttons
    homeButton = newPrimaryButton("Home");
    peButton = newFuncButton("Performance Evaluation");
    employeeButton = newFuncButton("Employee View");
    knowledgeButton = newFuncButton("Knowledgebase");
    sendMessageButton = newFuncButton("Send Message");
    logoutButton = newFuncButton("Logout");

    // Constructing version and welcome message
    welcomeLabel = new JLabel("Welcome, " + LogIn.getUsername());
    welcomeLabel.setFont(new Font("Helvetica", Font.BOLD, 16));
    welcomeLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
    welcomeLabel.setForeground(Color.WHITE);
    versionLabel = new JLabel(Welcome.getVersionNumber());
    versionLabel.setFont(new Font("Helvetica", Font.PLAIN, 14));
    versionLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
    versionLabel.setForeground(Color.WHITE);

    // Adding FB buttons
    functionBar.add(Box.createVerticalGlue()); // top pillar to center vert.
    functionBar.add(homeButton);
    functionBar.add(Box.createRigidArea(new Dimension(0, 20)));
    functionBar.add(peButton);
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
    JPanel messagePanel = new JPanel(new BorderLayout());
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

  // Styling Function for all function bar buttons (except primary)
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
   * Refreshes the message table with updated data from the database.
   * Called after sending a message to reflect the changes in the UI.
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
   * Handles button click events from the function bar and navigates to the corresponding screens.
   * It also updates the message table when the Send Message button is used.
   */
  @Override
  public void actionPerformed(ActionEvent e)
  {
    Object source = e.getSource();

    if (source == homeButton)
    {
      // Current Page
    }
    else if (source == peButton)
    {
      new PerformanceEvaluation();
      this.dispose();
    }
    else if (source == employeeButton)
    {
      new EmployeeInfoDB();
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
      updateTable();
    }
    else if (source == logoutButton)
    {
      new Welcome(); // Return back to Welcome Page
      this.dispose();
    }
  }

  /**
   * Main method for testing the ManagerDashboard independently.
   * Sets the login user as a manager and displays the dashboard.
   */
  public static void main(String[] args)
  {
    LogIn.setUsername("manager");
    LogIn.setRole("Manager");
    new ManagerDashboard();
  }
}
