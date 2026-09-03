
//package iaproductcode;

/**
 * InstallDB is responsible for initializing and setting up the database 
 * structure for the Office Automation System. It creates the database 
 * and necessary tables such as users, employees, departments, messages, 
 * attendance, performance evaluations, budgets, and knowledge base.
 * 
 * This class should be run once during the initial setup of the application 
 * to ensure the database schema is properly established.
 * 
 */
public class InstallDB
{

  /**
   * The main method executes the database installation process. It:
   * - Creates the main database.
   * - Defines the SQL table creation queries for each required table.
   * - Executes the creation of each table within the database.
   * 
   * @param args Command-line arguments (not used).
   */
  public static void main(String[] args)
  {
    String newDBName = "OfficeAutomationDB";
    JavaDBAccess objDb = new JavaDBAccess();
    objDb.createDB(newDBName);
    // Table creation queries
    String newDepartmentsTable = "CREATE TABLE IF NOT EXISTS departmentsTable("
        + "departmentID INT AUTO_INCREMENT PRIMARY KEY, "
        + "departmentName VARCHAR(50))";

    String newUsersTable = "CREATE TABLE IF NOT EXISTS usersTable("
        + "userID INT AUTO_INCREMENT PRIMARY KEY,"
        + "username VARCHAR(20) UNIQUE NOT NULL,"
        + "password VARCHAR(255) NOT NULL, "
        + "role VARCHAR(10) NOT NULL)";

    String newMessagesTable = "CREATE TABLE IF NOT EXISTS messagesTable ("
        + "messageID INT PRIMARY KEY AUTO_INCREMENT, "
        + "username VARCHAR(20), "
        + "messageTitle VARCHAR(255), "
        + "messageContent TEXT, " // Long String
        + "sentDate VARCHAR(15), "
        + "FOREIGN KEY (username) REFERENCES usersTable(username))";

    String newKBTable = "CREATE TABLE IF NOT EXISTS knowledgebaseTable ("
        + "articleID INT PRIMARY KEY AUTO_INCREMENT, "
        + "username VARCHAR(50), "
        + "kbTitle VARCHAR(255), "
        + "kbContent TEXT, "
        + "publishedDate VARCHAR(15), "
        + "FOREIGN KEY (username) REFERENCES usersTable(username))";

    String newEmployeesTable = "CREATE TABLE IF NOT EXISTS employeesTable ("
        + "employeeID INT AUTO_INCREMENT PRIMARY KEY, "
        + "userID INT, "
        + "firstName VARCHAR(50), "
        + "lastName VARCHAR(50), "
        + "departmentID INT, "
        + "DOB VARCHAR(15), "
        + "salary DOUBLE, "
        + "FOREIGN KEY (userID) REFERENCES usersTable(userID), "
        + "FOREIGN KEY (departmentID) REFERENCES departmentsTable(departmentID))";

    String newAttendanceTable = "CREATE TABLE IF NOT EXISTS attendanceTable ("
        + "employeeID INT NOT NULL, "
        + "attendanceYear VARCHAR(10) NOT NULL, "
        + "presentCount INT DEFAULT 0, "
        + "absentCount INT DEFAULT 0, "
        + "PRIMARY KEY (employeeID, attendanceYear), "
        + "FOREIGN KEY (employeeID) REFERENCES employeesTable(employeeID) ON DELETE CASCADE)";

    String newPETable = "CREATE TABLE IF NOT EXISTS peTable ("
        + "evaluationID INT AUTO_INCREMENT PRIMARY KEY, "
        + "employeeID INT NOT NULL, "
        + "username VARCHAR(50) NOT NULL, "
        + "departmentID INT NOT NULL, "
        + "evaluationDate VARCHAR(15), "
        + "evaluationContent TEXT, "
        + "FOREIGN KEY (employeeID) REFERENCES employeesTable(employeeID) ON DELETE CASCADE, "
        + "FOREIGN KEY (username) REFERENCES usersTable(username) ON DELETE CASCADE, "
        + "FOREIGN KEY (departmentID) REFERENCES departmentsTable(departmentID) ON DELETE CASCADE)";

    String newBudgetTable = "CREATE TABLE IF NOT EXISTS budgetTable ("
        + "budgetID INT AUTO_INCREMENT PRIMARY KEY, "
        + "departmentID INT NOT NULL, "
        + "budgetCategory VARCHAR(255), "
        + "allocatedAmount DOUBLE, "
        + "amountSpent DOUBLE, "
        + "FOREIGN KEY (departmentID) REFERENCES departmentsTable(departmentID) ON DELETE CASCADE)";

    // Add table to DB
    objDb.createTable(newDepartmentsTable, newDBName, "DepartmentsTable");
    objDb.createTable(newUsersTable, newDBName, "UsersTable");
    objDb.createTable(newMessagesTable, newDBName, "MessagesTable");
    objDb.createTable(newKBTable, newDBName, "KnowledgeBaseTable");
    objDb.createTable(newEmployeesTable, newDBName, "EmployeesTable");
    objDb.createTable(newAttendanceTable, newDBName, "AttendanceTable");
    objDb.createTable(newPETable, newDBName, "PerformanceEvaluationTable");
    objDb.createTable(newBudgetTable, newDBName, "BudgetTable");
  }
}
