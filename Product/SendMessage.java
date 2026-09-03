/**
 * The SendMessage class provides a GUI for Admins and Managers to post messages
 * or knowledge base content into the Office Automation system. It supports
 * input validation, category-based storage, and dynamic updates to the dashboards.
 * 
 * This class utilizes Java Swing for GUI components and Java LocalDate to timestamp posts.
 * It interacts with the backend database through JavaDBAccess for inserting records.
 */
//package iaproductcode;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import javax.swing.table.DefaultTableModel;

public class SendMessage extends JFrame implements ActionListener
{

  // Constants for styling
  public static final Font BUTTON_FONT = new Font("Helvetica", Font.BOLD, 16);
  public static final Font LABEL_FONT = new Font("Helvetica", Font.PLAIN, 14);

  // Title GUI Components
  private JPanel titlePanel;
  private JLabel titleLabel;

  // Main Panel GUI Components
  private JPanel mainPanel;
  private JLabel titleInputLabel, contentInputLabel, categoryLabel;
  private JTextField titleField;
  private JTextArea contentArea;
  private JComboBox<String> categoryDropdown;

  // Button Panel GUI Components
  private JPanel buttonPanel;
  private JButton postButton, closeButton;

  private JavaDBAccess dbAccess;

  /**
   * Constructs the SendMessage GUI window, initializing form fields for entering
   * post title, content, and category (Message or Knowledge Base).
   * Sets up layout and visual styling.
   */
  public SendMessage()
  {
    // Frame Title (GUI)
    this.setTitle("Create Post -" + Welcome.getCompanyName() + " OA System");

    // Structuring the frame
    this.setSize(500, 400);
    this.setLocationRelativeTo(null);
    this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    this.getContentPane().setBackground(Welcome.BG_COLOR);
    this.setLayout(new BorderLayout());

    // Title Panel
    this.titlePanel = new JPanel();
    titlePanel.setBackground(Welcome.BG_COLOR);
    this.titleLabel = new JLabel("Create a post");
    titleLabel.setFont(BUTTON_FONT);
    titlePanel.add(titleLabel);

    // Constructing Main Panel
    this.mainPanel = new JPanel();
    mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
    mainPanel.setBackground(Welcome.BG_COLOR);
    mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

    // Post Title Textfield
    this.titleInputLabel = new JLabel("Title:");
    titleInputLabel.setFont(LABEL_FONT);
    titleInputLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
    mainPanel.add(titleInputLabel);
    this.titleField = new JTextField(20);
    titleField.setFont(LABEL_FONT);
    titleField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
    mainPanel.add(titleField);

    mainPanel.add(Box.createRigidArea(new Dimension(0, 10))); // Spacing

    // Post Content Input Area
    this.contentInputLabel = new JLabel("Content:");
    contentInputLabel.setFont(LABEL_FONT);
    contentInputLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
    mainPanel.add(contentInputLabel);
    this.contentArea = new JTextArea(8, 30);
    contentArea.setFont(LABEL_FONT);
    contentArea.setLineWrap(true);
    contentArea.setWrapStyleWord(true);
    JScrollPane contentScrollPane = new JScrollPane(contentArea);
    contentScrollPane.setAlignmentX(Component.LEFT_ALIGNMENT);
    mainPanel.add(contentScrollPane);

    mainPanel.add(Box.createRigidArea(new Dimension(0, 10))); // Spacing

    // Category Dropdown
    this.categoryLabel = new JLabel("Category:");
    categoryLabel.setFont(LABEL_FONT);
    categoryLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
    mainPanel.add(categoryLabel);
    categoryDropdown = new JComboBox<>(new String[]
    {
      "Message", "Knowledge Base"
    });
    categoryDropdown.setFont(LABEL_FONT);
    categoryDropdown.setAlignmentX(Component.LEFT_ALIGNMENT);
    mainPanel.add(categoryDropdown);
    mainPanel.add(Box.createRigidArea(new Dimension(0, 20))); // Spacing

    // DB Access creation
    this.dbAccess = new JavaDBAccess();

    // Constructing Buttons Panel
    this.buttonPanel = new JPanel();
    buttonPanel.setBackground(Welcome.BG_COLOR);

    this.postButton = new JButton("Post");
    postButton.setFont(BUTTON_FONT);
    postButton.addActionListener(this);
    this.closeButton = new JButton("Close");
    closeButton.setFont(BUTTON_FONT);
    closeButton.addActionListener(this);

    buttonPanel.add(postButton);
    buttonPanel.add(closeButton);

    // Add components to frame
    this.add(titlePanel, BorderLayout.NORTH);
    this.add(mainPanel, BorderLayout.CENTER);
    this.add(buttonPanel, BorderLayout.SOUTH);

    // Set frame visible
    setVisible(true);
  }

  /**
   * Handles the actions triggered by the Post and Close buttons.
   * Validates form input and routes data to the correct table (message or knowledge base)
   * in the database. Clears the form and updates the appropriate dashboard upon success.
   */
  @Override
  public void actionPerformed(ActionEvent e)
  {
    if (e.getSource() == postButton)
    {
      // Initialize insert values
      String title = titleField.getText().trim(); // .trim to clear white spaces in front/after
      String content = contentArea.getText().trim();
      String username = LogIn.getUsername();
      String date = LocalDate.now().toString(); // To retrieve current date (reduce user input error)
      String category = (String) categoryDropdown.getSelectedItem(); // Receives whether to add to Messages OR KB

      if (title.isEmpty() || content.isEmpty())
      {
        JOptionPane.showMessageDialog(this, "Title and Content cannot be empty.", "Error Empty Field", JOptionPane.ERROR_MESSAGE);
        return;
      }
      if (category.equals("Message"))
      {
        dbAccess.insertMessage(username, title, content, date); // Insert into messageDB
      }
      else if (category.equals("Knowledge Base"))
      {
        dbAccess.insertKB(username, title, content, date); // Insert into knowledgebaseDB
      }
      // Clears field after insertion
      titleField.setText("");
      contentArea.setText("");
      JOptionPane.showMessageDialog(this, "Content posted successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
      if (LogIn.getRole().equals("Admin"))
      {
        AdminDashboard.updateTable();
      }
      else if (LogIn.getRole().equals("Manager"))
      {
        ManagerDashboard.updateTable();
      }
    }
    else if (e.getSource() == closeButton)
    {
      this.dispose(); // Close the popup
    }
  }

  /**
   * Main method for testing the SendMessage class in isolation.
   * Pre-sets login credentials and opens the message posting GUI.
   */
  public static void main(String[] args)
  {
    LogIn.setUsername("admin");
    LogIn.setRole("Admin");
    new SendMessage();
  }
}
