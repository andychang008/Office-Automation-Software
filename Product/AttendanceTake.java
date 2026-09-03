
//package iaproductcode;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * The AttendanceTake class provides a GUI for administrators to take attendance
 * for employees. It allows selecting an employee, entering the year, and marking
 * them as present or absent. The class interacts with the database through
 * JavaDBAccess and updates the attendance records using AttendanceCalculation.
 */
public class AttendanceTake extends JFrame implements ActionListener
{

  private JComboBox<String> employeeDropdown;
  private JTextField yearField;
  private JButton submitButton, cancelButton;
  private JRadioButton presentButton, absentButton;
  private ButtonGroup statusGroup;

  private JavaDBAccess dbAccess;
  private Object[][] employeeData;

  /**
   * Constructs the AttendanceTake GUI window, initializes components,
   * and sets up event listeners for taking employee attendance.
   */
  public AttendanceTake()
  {
    // Frame Title (GUI)
    this.setTitle("Take Attendance -" + Welcome.getCompanyName() + " OA System");

    // Structuring the frame
    this.setSize(500, 400);
    this.setLocationRelativeTo(null);
    this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    this.getContentPane().setBackground(Welcome.BG_COLOR);
    this.setLayout(new GridLayout(5, 1, 10, 10));

    // Initialize DB Access
    this.dbAccess = new JavaDBAccess();
    this.employeeData = dbAccess.getEmployeeData();

    // Dropdown for Employee Selection
    String[] employeeNames = new String[employeeData.length];
    for (int i = 0; i < employeeData.length; i++)
    {
      employeeNames[i] = employeeData[i][2] + " " + employeeData[i][3]; // First and Last Name
    }
    this.employeeDropdown = new JComboBox<>(employeeNames);

    // Year Input Field
    this.yearField = new JTextField();
    this.yearField.setHorizontalAlignment(JTextField.CENTER);
    this.yearField.setBorder(BorderFactory.createTitledBorder("Enter Year"));

    // Attendance Status Selection
    this.presentButton = new JRadioButton("Present");
    this.absentButton = new JRadioButton("Absent");
    this.statusGroup = new ButtonGroup();
    statusGroup.add(presentButton);
    statusGroup.add(absentButton);

    // Buttons
    this.submitButton = new JButton("Submit");
    this.cancelButton = new JButton("Cancel");
    submitButton.addActionListener(this);
    cancelButton.addActionListener(this);

    // Add Components
    this.add(new JLabel("Select Employee:", SwingConstants.CENTER));
    this.add(employeeDropdown);
    this.add(yearField);
    JPanel statusPanel = new JPanel();
    statusPanel.add(presentButton);
    statusPanel.add(absentButton);
    this.add(statusPanel);
    JPanel buttonPanel = new JPanel();
    buttonPanel.add(submitButton);
    buttonPanel.add(cancelButton);
    this.add(buttonPanel);

    // Set Frame Visible
    this.setVisible(true);
  }

  /**
   * Handles button click events. If 'Submit' is clicked, it records the
   * attendance status for the selected employee. If 'Cancel' is clicked,
   * it closes the window.
   * 
   * @param e the ActionEvent triggered by button clicks
   */
  @Override
  public void actionPerformed(ActionEvent e)
  {
    if (e.getSource() == submitButton)
    {
      int employeeIndex = employeeDropdown.getSelectedIndex();
      if (employeeIndex < 0 || yearField.getText().trim().isEmpty() || (!presentButton.isSelected() && !absentButton.isSelected()))
      {
        JOptionPane.showMessageDialog(this, "Please fill all fields.", "Error Empty Field(s)", JOptionPane.ERROR_MESSAGE);
      }
      else
      {
        int employeeID = Integer.parseInt(employeeData[employeeIndex][0].toString()); // Get Employee ID from employee name selected
        String year = yearField.getText().trim();
        boolean isPresent = presentButton.isSelected(); // If present circle is ticked or not
        // Attendance Calculations & update attendance insert
        AttendanceCalculation.updateAttendance(employeeID, year, isPresent);
        // Close Current Window & Update Attendance.java table
        JOptionPane.showMessageDialog(this, "Attendance recorded successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
        Attendance.refreshTable();
        this.dispose();
      }
    }
    else if (e.getSource() == cancelButton)
    {
      this.dispose();
    }
  }

  /**
   * Main method to launch the AttendanceTake GUI independently for testing purposes.
   * 
   * @param args command-line arguments (not used)
   */
  public static void main(String[] args)
  {
    new AttendanceTake();
  }
}
