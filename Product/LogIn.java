/**
 * The LogIn class handles the user authentication process for the Office Automation System.
 * It provides a graphical user interface (GUI) for users to enter their username and password,
 * checks the login credentials against the database, and routes the user to the appropriate dashboard
 * based on their role (Admin, Manager, or Employee). It also provides a help request option.
 *
 * This class uses Java Swing components to build the GUI and relies on JavaDBAccess and LogInCheck
 * for database connection and login verification.
 */
//package iaproductcode;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LogIn extends JFrame implements ActionListener
{

  // Declaration of color and fonts
  public static final Font LABEL_FONT = new Font("Helvetica", Font.PLAIN, 18);
  public static final Font BUTTON_FONT = new Font("Helvetica", Font.BOLD, 16);

  // Declaring necessary components for Frame
  private JPanel mainPanel;
  private JPanel buttonPanel;

  private JLabel usernameLabel;
  private JLabel passwordLabel;
  private JTextField usernameField;
  private JPasswordField passwordField;

  private JButton logInButton;
  private JButton requestHelpButton;

  private JavaDBAccess dbAccess;
  private static String username;
  private static String role = "";
  private String password;

  /**
   * Constructs the LogIn GUI frame where users can input their username and password.
   * Initializes all GUI components and sets up the action listeners for the buttons.
   */
  public LogIn()
  {
    // Frame Title (GUI)
    this.setTitle("Log In -" + Welcome.getCompanyName() + " OA System");

    // Structuring the frame
    this.setSize(500, 300);
    this.setLocationRelativeTo(null);
    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    this.getContentPane().setBackground(Welcome.BG_COLOR);
    this.setLayout(new BorderLayout());

    // Constructing main panel
    mainPanel = new JPanel();
    mainPanel.setBackground(Welcome.BG_COLOR);
    mainPanel.setLayout(new GridBagLayout());
    GridBagConstraints gbc = new GridBagConstraints();

    // Spacing between components
    gbc.insets = new Insets(10, 10, 10, 10);
    gbc.fill = GridBagConstraints.HORIZONTAL;

    // Constructing Username and Password Fields
    usernameLabel = new JLabel("Username:");
    usernameLabel.setFont(LABEL_FONT);
    gbc.gridx = 0;
    gbc.gridy = 0;
    mainPanel.add(usernameLabel, gbc);
    usernameField = new JTextField(15);
    usernameField.setFont(LABEL_FONT);
    gbc.gridx = 1;
    mainPanel.add(usernameField, gbc);
    passwordLabel = new JLabel("Password:");
    passwordLabel.setFont(LABEL_FONT);
    gbc.gridx = 0;
    gbc.gridy = 1;
    mainPanel.add(passwordLabel, gbc);
    passwordField = new JPasswordField(15);
    passwordField.setFont(LABEL_FONT);
    gbc.gridx = 1;
    mainPanel.add(passwordField, gbc);

    // Constructing Button panel
    buttonPanel = new JPanel();
    buttonPanel.setBackground(Welcome.BG_COLOR);

    // Constructing Log-In Button
    logInButton = new JButton("Log-In");
    logInButton.setFont(BUTTON_FONT);
    logInButton.setPreferredSize(new Dimension(200, 40));
    logInButton.addActionListener(this);
    buttonPanel.add(logInButton);

    //Constructing Request Help Button
    requestHelpButton = new JButton("Request Help?");
    requestHelpButton.setFont(BUTTON_FONT);
    requestHelpButton.setPreferredSize(new Dimension(200, 40));
    requestHelpButton.addActionListener(this);
    buttonPanel.add(requestHelpButton);

    // Add panels to frame
    this.add(mainPanel, BorderLayout.CENTER);
    this.add(buttonPanel, BorderLayout.SOUTH);

    // Set the frame visible
    this.setVisible(true);
  }

  /**
   * Handles the actions triggered by the GUI buttons.
   * If the login button is clicked, it checks the provided username and password against the database.
   * If the request help button is clicked, it displays a help message dialog.
   *
   * @param e the ActionEvent triggered by user interaction
   */
  @Override
  public void actionPerformed(ActionEvent e)
  {
    Object source = e.getSource();

    if (source == logInButton)
    {
      this.username = usernameField.getText();
      this.password = String.valueOf(passwordField.getPassword());
      this.dbAccess = new JavaDBAccess();
      
      boolean userExist = false;
      userExist = LogInCheck.checkLogIn(username, password, dbAccess);

      if (userExist)
      {
        if (role.equals("Admin"))
        {
          new AdminDashboard();
          this.dispose();
        }
        else if (role.equals("Manager"))
        {
          new ManagerDashboard();
          this.dispose();
        }
        else if (role.equals("Employee"))
        {
          new EmployeeDashboard();
          this.dispose();
        }
      }
      else
      {
        JOptionPane.showMessageDialog(this, "Invalid Username or Password! "
            + "\nPlease try again or request help.", "Error", JOptionPane.ERROR_MESSAGE);
      }

    }

    if (source == requestHelpButton)
    {
      JOptionPane.showMessageDialog(this, "Contact your office software "
          + "administrator for more information"
          + " on your account. \nIf you are an administrator,"
          + " double check your log in information or reset your"
          + " account. ", "Request Help", JOptionPane.INFORMATION_MESSAGE);
    }
  }
  
  /* Encapsulation Accessor (For use across application) */
  /**
   * Sets the static username for the current session.
   *
   * @param u the username to set
   */
  public static void setUsername(String u)
  {
    username = u;
  }
  
  /**
   * Retrieves the static username for the current session.
   *
   * @return the username
   */
  public static String getUsername()
  {
    return username;
  }
  
  /**
   * Sets the static role for the current session.
   *
   * @param r the role to set (Admin, Manager, or Employee)
   */
  public static void setRole(String r)
  {
    role = r;
  }
  
  /**
   * Retrieves the static role for the current session.
   *
   * @return the role
   */
  public static String getRole()
  {
    return role;
  }

  /**
   * Main method to launch the LogIn frame independently for testing purposes.
   *
   * @param args command-line arguments (not used)
   */
  public static void main(String[] args)
  {
    new LogIn();
  }
}
