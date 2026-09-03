/**
 * Attendance class handles the display and management of the employee attendance records.
 * It retrieves attendance data from the database and provides a GUI for viewing and taking attendance.
 * 
 * This class uses Java Swing components for the graphical user interface and connects to the database 
 * through the JavaDBAccess class to fetch and update attendance data.
 */
//package iaproductcode;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Attendance extends JFrame implements ActionListener
{

  // Constants for styling
  public static final Font BUTTON_FONT = new Font("Helvetica", Font.BOLD, 16);
  public static final Font LABEL_FONT = new Font("Helvetica", Font.PLAIN, 14);

  // All GUI Components
  private JPanel titlePanel, tablePanel, bottomPanel;
  private JLabel titleLabel;
  private JTable attendanceTable;
  private static DefaultTableModel tableModel;
  private JButton returnButton, takeAttendanceButton;

  private JavaDBAccess dbAccess;

  /**
   * Constructs the Attendance GUI window.
   * Sets up the layout, retrieves attendance data from the database, 
   * and initializes the table and navigation components.
   */
  public Attendance()
  {
    // Frame Title (GUI)
    this.setTitle("Attendance -" + Welcome.getCompanyName() + " OA System");

    // Structuring the frame
    this.setBounds(1920 / 2, 1080 / 2, 1920, 1080);
    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    this.getContentPane().setBackground(Welcome.BG_COLOR);
    this.setExtendedState(JFrame.MAXIMIZED_BOTH);
    this.setLayout(new BorderLayout());

    // Title Panel
    this.titlePanel = new JPanel();
    titlePanel.setBackground(Welcome.BG_COLOR);
    this.titleLabel = new JLabel("Attendance");
    titleLabel.setFont(BUTTON_FONT);
    titlePanel.add(titleLabel);

    // Constructing Table Panel
    this.tablePanel = new JPanel();
    tablePanel.setLayout(new BorderLayout());
    tablePanel.setBackground(Welcome.BG_COLOR);

    // Table columns
    this.dbAccess = new JavaDBAccess();
    Object[][] data = dbAccess.getAttendanceData();

    String[] columnNames =
    {
      "Employee ID", "Year", "Present Days", "Absent Days"
    };
    tableModel = new DefaultTableModel(columnNames, 0);
    attendanceTable = new JTable(tableModel);

    // Adding data to the table (All rows above)
    for (int r = 0; r < data.length; r++)
    {
      tableModel.addRow(data[r]);
    }

    attendanceTable = new JTable(tableModel)
    {
      @Override
      public boolean isCellEditable(int row, int column)
      {
        // Making the 3rd one editable only
        return false;
      }
    };

    // Present/Absent Dropdown
    JComboBox<String> attendanceOptions = new JComboBox<>(new String[]
    {
      "Present", "Absent"
    });
    // Setting column 2 as the editable column
    attendanceTable.getColumnModel().getColumn(2).setCellEditor(new DefaultCellEditor(attendanceOptions));
    // Setting row height for better visibility
    attendanceTable.setRowHeight(25);
    // Add scroll pane for the table
    JScrollPane tableScrollPane = new JScrollPane(attendanceTable);
    tableScrollPane.setBorder(BorderFactory.createTitledBorder("Employee Attendance"));
    tablePanel.add(tableScrollPane, BorderLayout.CENTER);

    // Bottom Navigation Panel
    this.bottomPanel = new JPanel();
    bottomPanel.setBackground(Welcome.BG_COLOR);

    this.takeAttendanceButton = new JButton("Take Attendance");
    takeAttendanceButton.setFont(BUTTON_FONT);
    takeAttendanceButton.setPreferredSize(new Dimension(180, 40));
    takeAttendanceButton.addActionListener(this);
    bottomPanel.add(takeAttendanceButton);

    this.returnButton = new JButton("Return to HR");
    returnButton.setFont(BUTTON_FONT);
    returnButton.setPreferredSize(new Dimension(180, 40));
    returnButton.addActionListener(this);
    bottomPanel.add(returnButton);

    // Add components to frame
    this.add(titlePanel, BorderLayout.NORTH);
    this.add(tablePanel, BorderLayout.CENTER);
    this.add(bottomPanel, BorderLayout.SOUTH);

    // Set frame visible
    this.setVisible(true);
  }

  /**
   * Refreshes the attendance table data.
   * Clears existing data and reloads updated attendance records from the database.
   */
  public static void refreshTable()
  {
    tableModel.setRowCount(0); // Clears all existing table data

    // Fetch updated data from the database
    JavaDBAccess dbAccess = new JavaDBAccess();
    Object[][] updatedData = dbAccess.getAttendanceData();

    // Update data row by row from database
    for (int r = 0; r < updatedData.length; r++)
    {
      tableModel.addRow(updatedData[r]);
    }
  }

  /**
   * Handles button actions for returning to the HR dashboard or taking attendance.
   * Opens the corresponding GUI window based on the user's selection.
   *
   * @param e the ActionEvent triggered by button clicks.
   */
  @Override
  public void actionPerformed(ActionEvent e)
  {
    if (e.getSource() == returnButton)
    {
      new HumanResources();
      this.dispose();
    }
    else if (e.getSource() == takeAttendanceButton)
    {
      new AttendanceTake();
    }
  }

  /**
   * Main method for testing and launching the Attendance window directly.
   * Sets up the login role as Admin for testing purposes.
   *
   * @param args the command-line arguments (not used).
   */
  public static void main(String[] args)
  {
    LogIn.setUsername("Admin");
    LogIn.setRole("Admin");
    new Attendance();
  }
}
