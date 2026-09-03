//package iaproductcode;

/**
 * The AttendanceCalculation class provides methods to handle 
 * attendance updates for employees in the system.
 * It interacts with the database to increment attendance 
 * records based on presence or absence.
 */
public class AttendanceCalculation
{

  /**
   * Updates the attendance record for a specific employee for a given year.
   * Increments either the present or absent count depending on the isPresent flag.
   *
   * @param employeeID the ID of the employee whose attendance is being updated
   * @param year the year for which attendance is being tracked
   * @param isPresent true if the employee is present, false if absent
   */
  public static void updateAttendance(int employeeID, String year, boolean isPresent)
  {
    JavaDBAccess dbAccess = new JavaDBAccess();
    dbAccess = new JavaDBAccess(); // Instanciate JavaDbAccess (used for later insert/update)
    int presentIncrement = isPresent ? 1 : 0; /* If else */
    int absentIncrement = isPresent ? 0 : 1;
    dbAccess.updateAttendance(employeeID, year, presentIncrement, absentIncrement);
  }
  /**
   * Main method for testing the attendance update functionality.
   * Demonstrates updating the attendance record of an example employee.
   *
   * @param args the command-line arguments (not used)
   */
  public static void main(String[] args)
  {
    int employeeID = 100;
    String year = "2025";
    boolean presence = false;
    updateAttendance(employeeID, year, presence);
  }

}
