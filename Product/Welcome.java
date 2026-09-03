//package iaproductcode;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;
/**
 * Welcome.java
 * 
 * This class represents the Welcome screen of the Office Automation (OA) system.
 * It provides the entry point for the user to log into the system, displaying 
 * the company logo, welcome message, and version information.
 * 
 */
public class Welcome extends JFrame implements ActionListener
{

  public static final Color BG_COLOR = new Color(245, 245, 220); // Beige
  public static final Color FONT_COLOR = new Color(0, 0, 0); // black
  public static final Color FOOTER_COLOR = new Color(0, 0, 0); // black
  public static final Color WARNING_COLOR = new Color(255, 0, 0); // red
  public static final Font THEME_TITLE_FONT = new Font("Helvetica", Font.BOLD, 30);
  public static final Font THEME_DESCRIPTION_FONT = new Font("Helvetica", Font.PLAIN, 18);

  // Declaring Necessary components for welcome frame
  private JPanel mainPanel;
  private JPanel buttonPanel;

  private JLabel logoLabel;
  private JLabel welcomeText;
  private JLabel versionText;

  private JButton logInButton;
  
  // Logo Placeholder ICON declaration
  private final URL LOGO_PATH = getClass().getResource("logo.png");
  private final ImageIcon LOGOIMAGE = new ImageIcon
    (new ImageIcon(LOGO_PATH).getImage().getScaledInstance(
          512/4, 512/4, Image.SCALE_DEFAULT)
    );

  private static String companyName = "Example Company";
  private static final String versionNumber = "Version 0.1.1";

  /**
   * Constructs the Welcome GUI frame, including logo display, welcome text,
   * version number, and the log-in button to navigate to the login page.
   */
  public Welcome()
  {
    // Frame Title (GUI)
    this.setTitle("Welcome - " + this.companyName + " OA System");

    // Structuring the frame
    this.setBounds(1920 / 2, 1080 / 2, 1920, 1080);
    this.setDefaultCloseOperation(EXIT_ON_CLOSE);
    this.getContentPane().setBackground(BG_COLOR);
    this.setExtendedState(JFrame.MAXIMIZED_BOTH);
    this.setLayout(new BorderLayout());

    // Constructing Main Panel
    this.mainPanel = new JPanel();
    mainPanel.setBackground(BG_COLOR);
    mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));

    // Add the logo placeholder
    this.logoLabel = new JLabel(LOGOIMAGE, SwingConstants.CENTER);
    logoLabel.setFont(THEME_TITLE_FONT);
    logoLabel.setHorizontalAlignment(SwingConstants.CENTER);
    logoLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
    logoLabel.setBackground(Color.BLACK);

    this.welcomeText = new JLabel("Welcome to " + companyName + " OA System", SwingConstants.CENTER);
    welcomeText.setFont(THEME_TITLE_FONT);
    welcomeText.setAlignmentX(Component.CENTER_ALIGNMENT);

    this.versionText = new JLabel(versionNumber);
    versionText.setFont(THEME_DESCRIPTION_FONT);
    versionText.setAlignmentX(Component.CENTER_ALIGNMENT);

    // Adding Related Components to Panel
    mainPanel.add(logoLabel);
    mainPanel.add(Box.createRigidArea(new Dimension(0, 20)));
    mainPanel.add(welcomeText);
    mainPanel.add(Box.createRigidArea(new Dimension(0, 5)));
    mainPanel.add(versionText);

    // Component for buttonPanel
    this.logInButton = new JButton("Log-In");
    logInButton.setPreferredSize(new Dimension(200, 40));
    logInButton.addActionListener(this);

    this.buttonPanel = new JPanel();
    buttonPanel.setBackground(BG_COLOR);
    
    // Adding Buttons to ButtonPanel
    buttonPanel.add(logInButton);
    

    // Adding All Panels to Frame
    this.add(mainPanel, BorderLayout.CENTER);
    this.add(buttonPanel, BorderLayout.SOUTH);

    // Setting Frame Visbile
    this.setVisible(true);
  }
  
  /**
   * Sets the company name displayed on the welcome screen.
   * @param cName the company name to set
   */
  public static void setCompanyName(String cName)
  {
    companyName = cName;
  }
  
  /**
   * Retrieves the current company name.
   * @return the company name
   */
  public static String getCompanyName()
  {
    return companyName;
  }
  
  /**
   * Retrieves the version number of the OA system.
   * @return the version number
   */
  public static String getVersionNumber()
  {
    return versionNumber;
  }

  /**
   * Handles action events triggered by the GUI components.
   * Specifically, opens the Log-In page when the Log-In button is clicked.
   * @param e the action event object
   */
  public void actionPerformed(ActionEvent e)
  {
    Object component = e.getSource();

    if (component == logInButton)
    {
      new LogIn();
      this.dispose();
    }
  }
  
  /**
   * The main method to launch the Welcome screen.
   * @param args command-line arguments (not used)
   */
  public static void main(String[] args)
  {
    new Welcome();
  }
}
