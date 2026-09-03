/**
 * JavaDBAccess handles database operations for the Office Automation System.
 * This class manages the connection to the MySQL database and provides methods
 * for CRUD operations across various tables including users, employees, attendance,
 * performance evaluations, budget, departments, messages, and knowledge base.
 * 
 * It encapsulates all database logic, providing methods to insert, update, delete, 
 * and retrieve data, ensuring that other classes interact with the database through 
 * this centralized access point.
 * 
 */
//package iaproductcode;

import java.sql.*;
import java.util.ArrayList;

public class JavaDBAccess
{

  private String dbName;
  private Connection dbConn;
  private ArrayList<ArrayList<String>> data;

  /**
   * Default constructor that initializes the database name and connection.
   */
  public JavaDBAccess()
  {
    this.dbName = "OfficeAutomationDB";
    setDBConn();
    data = null;
  }

  /**
   * Returns the name of the current database.
   * @return the database name as a String.
   */
  public String getDBName()
  {
    return dbName;
  }

  /**
   * Sets the database name.
   * @param dbName the name of the database to be used.
   */
  public void setDBName(String dbName)
  {
    this.dbName = dbName;
  }

  /**
   * Returns the current database connection object.
   * @return the database connection.
   */
  public Connection getDBConn()
  {
    return dbConn;
  }

  /**
   * Establishes the connection to the MySQL database using the set database name.
   */
  public void setDBConn()
  {
    String connectionURL = "jdbc:mysql://localhost:3306/" + this.dbName;
    this.dbConn = null;
    try
    {
      Class.forName("com.mysql.cj.jdbc.Driver");
      this.dbConn = DriverManager.getConnection(connectionURL, "root", "mysql1");
    }
    catch (ClassNotFoundException ex)
    {
      System.out.println("Driver not found, check library.");
    }
    catch (SQLException se)
    {
      System.out.println("SQL Connection error.");
    }
  }

  // Gets data from a specific table inside DB (1D ArrayList)
  /**
   * Retrieves data from a specified table and columns in the database.
   * @param tablename the name of the table to retrieve data from.
   * @param tableHeaders the columns to retrieve.
   * @return a 2D ArrayList of the table data.
   */
  public ArrayList<ArrayList<String>> getData(String tablename,
                                              String[] tableHeaders)
  {
    int columnCount = tableHeaders.length;
    Statement s = null;
    ResultSet rs = null;
    String dbQuery = "SELECT * FROM " + tablename;

    this.data = new ArrayList<>();
    try
    {
      s = this.dbConn.createStatement();
      rs = s.executeQuery(dbQuery);
      // Read data from table using rs and store in ArrayList<>
      while (rs.next())
      {
        // Row Object to hold one row of data from table
        ArrayList<String> row = new ArrayList<>();
        // go through the row and read each cell individually
        for (int i = 0; i < columnCount; i++)
        {
          // Getting the data from the cell in the row
          String cell = rs.getString(tableHeaders[i]);
          // Adding individual cell to the row.
          row.add(cell);
        }
        // Adding the row to whole data table
        this.data.add(row);
      }
    }
    catch (SQLException se)
    {
      System.out.println("SQL Error: Not able to get data.");
    }
    return this.data;
  }

  /**
   * Sets the internal data ArrayList for this class.
   * @param data the data to set.
   */
  public void setData(ArrayList<ArrayList<String>> data)
  {
    this.data = data;
  }

  // Convert datalist from table into 2D array for access for class
  /**
   * Converts a 2D ArrayList of Strings to a 2D Object array.
   * @param dataList the data to convert.
   * @return a 2D Object array representing the data.
   */
  public Object[][] to2dArray(ArrayList<ArrayList<String>> dataList)
  {
    if (dataList.isEmpty()) // if table is empty
    {
      Object[][] dataArray = new Object[0][0];
      return dataArray;
    }
    else
    {
      // Number of columns
      int columnCount = dataList.get(0).size();
      Object[][] dataArray = new Object[dataList.size()][columnCount];
      // Read each cell of each row into array
      for (int r = 0; r < dataList.size(); r++)
      {
        ArrayList<String> row = dataList.get(r); // Get the row
        for (int c = 0; c < columnCount; c++)
        {
          dataArray[r][c] = row.get(c); // Get the cell
        }
      }
      return dataArray;
    }
  }

  // Creating a DB
  /**
   * Creates a new database with the specified name if it does not already exist.
   * @param newDBName the name of the database to create.
   */
  public void createDB(String newDBName)
  {
    setDBName(newDBName);
    String connectionURL = "jdbc:mysql://localhost:3306/";
    String query = "CREATE DATABASE IF NOT EXISTS " + this.getDBName(); // avoid duplicate DB
    this.dbConn = null;
    try
    {
      Class.forName("com.mysql.cj.jdbc.Driver");
      this.dbConn = DriverManager.getConnection(connectionURL, "root", "mysql1");
      Statement s = this.dbConn.createStatement();
      s.executeUpdate(query);
      System.out.println("New Database " + this.dbName + " was created! (If did not exist)");

    }
    catch (ClassNotFoundException ex)
    {
      System.out.println("Driver not found, check library.");
    }
    catch (SQLException se)
    {
      System.out.println("SQL Connection error, DB was not created.");
    }
  }

  // Creates a new table inside DB
  /**
   * Creates a new table in the specified database.
   * @param newTable the SQL statement for table creation.
   * @param dbName the database name.
   * @param tableName the name of the table to create.
   */
  public void createTable(String newTable, String dbName, String tableName)
  {
    System.out.println("Table creation attempted.");
    setDBName(dbName);
    setDBConn();
    Statement s;
    try
    {
      s = this.dbConn.createStatement();
      s.execute(newTable);
      System.out.println("New Table " + tableName + " has been created (If did not exist)!");
      this.dbConn.close();
    }
    catch (SQLException se)
    {
      System.out.println("Error creating a table " + newTable);
    }
  }

  // Insert functions for DB Insert
  /* Users Table */
  /**
   * Inserts a new user into the users table.
   * @param username the username.
   * @param password the password.
   * @param role the user's role.
   * @return the generated user ID.
   */
  public int insertUsersData(String username, String password, String role) // To be implemented for account creation
  {
    String query = "INSERT INTO usersTable (username, password, role) VALUES (?, ?, ?)";
    int IDDisplay = 0;
    try
    {
      PreparedStatement ps = this.dbConn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
      ps.setString(1, username);
      ps.setString(2, password);
      ps.setString(3, role);
      ps.executeUpdate();
      ResultSet rs = ps.getGeneratedKeys();
      if (rs.next())
      {
        IDDisplay = rs.getInt(1); // Retrieves first column (UserID)
      }
      System.out.println("Account created with User ID: " + IDDisplay);
    }
    catch (SQLException se)
    {
      System.out.println("Account cannot be created into Database.");
    }
    return IDDisplay;
  }

  /* Messages Table */
  /**
   * Inserts a new message into the messages table.
   * @param username the username of the sender.
   * @param title the message title.
   * @param content the message content.
   * @param date the date the message was sent.
   */
  public void insertMessage(String username, String title, String content, String date)
  {
    String query = "INSERT INTO messagesTable (username, messageTitle, messageContent, sentDate) VALUES (?, ?, ?, ?)";
    try
    {
      PreparedStatement ps = this.dbConn.prepareStatement(query);
      ps.setString(1, username);
      ps.setString(2, title);
      ps.setString(3, content);
      ps.setString(4, date);
      ps.executeUpdate();
      System.out.println("Message has been posted!");
    }
    catch (SQLException se)
    {
      System.out.println("Message cannot be posted.");
    }
  }

  /* KB Table */
  /**
   * Inserts a new knowledge base entry.
   * @param username the username of the author.
   * @param kbTitle the title of the knowledge base entry.
   * @param kbContent the content of the entry.
   * @param publishedDate the publication date.
   */
  public void insertKB(String username, String kbTitle, String kbContent, String publishedDate)
  {
    String query = "INSERT INTO knowledgebaseTable (username, kbTitle, kbContent, publishedDate) VALUES (?, ?, ?, ?)";
    try
    {
      PreparedStatement ps = this.dbConn.prepareStatement(query);
      ps.setString(1, username);
      ps.setString(2, kbTitle);
      ps.setString(3, kbContent);
      ps.setString(4, publishedDate);
      ps.executeUpdate();
      System.out.println("Knowledge Base Content has been published!");
    }
    catch (SQLException se)
    {
      System.out.println("KB Content cannot be published.");
    }
  }

  /* Employees Table */
  /**
   * Inserts a new employee into the employees table.
   * @param userID the user ID of the employee.
   * @param firstName the employee's first name.
   * @param lastName the employee's last name.
   * @param departmentID the department ID.
   * @param DOB the date of birth.
   * @param salary the salary.
   */
  public void insertEmployeeData(int userID, String firstName, String lastName, int departmentID, String DOB, double salary)
  {
    String query = "INSERT INTO employeesTable (userID, firstName, lastName, departmentID, DOB, salary) VALUES (?, ?, ?, ?, ?, ?)";
    try
    {
      PreparedStatement ps = this.dbConn.prepareStatement(query);
      ps.setInt(1, userID);
      ps.setString(2, firstName);
      ps.setString(3, lastName);
      ps.setInt(4, departmentID);
      ps.setString(5, DOB);
      ps.setDouble(6, salary);
      ps.executeUpdate();
      System.out.println("A new employee has been created!");
    }
    catch (SQLException se)
    {
      System.out.println("Error creating employee.");
    }
  }

  /* Attendance Table */
  /**
   * Inserts an attendance record for an employee.
   * @param employeeID the employee's ID.
   * @param attendanceYear the year of attendance.
   * @param presentCount the number of days present.
   * @param absentCount the number of days absent.
   */
  public void insertAttendanceData(int employeeID, String attendanceYear, int presentCount, int absentCount)
  {
    String query = "INSERT INTO attendanceTable (employeeID, attendanceYear, presentCount, absentCount) VALUES (?, ?, ?, ?)";
    try
    {
      PreparedStatement ps = this.dbConn.prepareStatement(query);
      ps.setInt(1, employeeID);
      ps.setString(2, attendanceYear);
      ps.setInt(3, presentCount);
      ps.setInt(4, absentCount);
      ps.executeUpdate();
      System.out.println("Attendance record inserted!");
    }
    catch (SQLException se)
    {
      System.out.println("Error inserting attendance record.");
    }
  }

  /* PE Table */
  /**
   * Inserts a performance evaluation record for an employee.
   * @param employeeID the employee's ID.
   * @param username the username of the evaluator.
   * @param evaluationDate the date of evaluation.
   * @param evaluationContent the evaluation content.
   */
  public void insertPerformanceEvaluationData(int employeeID, String username, String evaluationDate, String evaluationContent)
  {
    String getDeptQuery = "SELECT departmentID FROM employeesTable WHERE employeeID = ?";
    String query = "INSERT INTO peTable (employeeID, username, departmentID, evaluationDate, evaluationContent) VALUES (?, ?, ?, ?, ?)";

    try
    {
      PreparedStatement deptStmt = this.dbConn.prepareStatement(getDeptQuery);
      deptStmt.setInt(1, employeeID);
      ResultSet rs = deptStmt.executeQuery(); // Retrieves the department of Employee being evaluated

      int departmentID = -1;
      if (rs.next())
      {
        departmentID = rs.getInt("departmentID"); // Assigns the value to employee's dept ID.
      }

      PreparedStatement ps = this.dbConn.prepareStatement(query);
      ps.setInt(1, employeeID);
      ps.setString(2, username);
      ps.setInt(3, departmentID);
      ps.setString(4, evaluationDate);
      ps.setString(5, evaluationContent);
      ps.executeUpdate();
      System.out.println("Performance Evaluation record inserted!");
    }
    catch (SQLException se)
    {
      System.out.println("Error inserting performance evaluation record.");
    }
  }

  /* Budget Table */
  /**
   * Inserts a new budget record for a department.
   * @param departmentID the department ID.
   * @param budgetCategory the budget category.
   * @param allocatedAmount the allocated amount.
   * @param amountSpent the amount spent.
   * @param budgetDate the date of the budget record (unused in query).
   */
  public void insertBudgetData(int departmentID, String budgetCategory, double allocatedAmount, double amountSpent, String budgetDate)
  {
    String query = "INSERT INTO budgetTable (departmentID, budgetCategory, allocatedAmount, amountSpent) VALUES (?, ?, ?, ?)";
    try
    {
      PreparedStatement ps = this.dbConn.prepareStatement(query);
      ps.setInt(1, departmentID);
      ps.setString(2, budgetCategory);
      ps.setDouble(3, allocatedAmount);
      ps.setDouble(4, amountSpent);
      ps.executeUpdate();
      System.out.println("Budget record inserted!");
    }
    catch (SQLException se)
    {
      System.out.println("Error inserting budget record.");
    }
  }

  /* Depts. Table */
  /**
   * Inserts a new department into the departments table if it does not already exist.
   * @param departmentName the name of the department.
   */
  public void insertDeptData(String departmentName)
  {
    String checkQuery = "SELECT COUNT(*) FROM departmentsTable WHERE LOWER(departmentName) = LOWER(?)";
    String insertQuery = "INSERT INTO departmentsTable (departmentName) VALUES (?)";

    try
    {
      PreparedStatement checkStmt = this.dbConn.prepareStatement(checkQuery);
      checkStmt.setString(1, departmentName);
      ResultSet rs = checkStmt.executeQuery();
      rs.next();
      int count = rs.getInt(1);

      if (count == 0) // Only insert if department doesn't exist
      {
        PreparedStatement insertStmt = this.dbConn.prepareStatement(insertQuery);
        insertStmt.setString(1, departmentName);
        insertStmt.executeUpdate();
        System.out.println("Department '" + departmentName + "' created successfully!");
      }
      else
      {
        System.out.println("Department already exists.");
      }
    }
    catch (SQLException se)
    {
      System.out.println("Error inserting department.");
    }
  }

  // Get data for table/other implementations
  /* Users Table */
  /**
   * Retrieves all user records as a 2D Object array.
   * @return users data as a 2D Object array.
   */
  public Object[][] getUsersData()
  {
    String[] columnNames =
    {
      "username", "password", "role"
    };
    return to2dArray(getData("usersTable", columnNames));
  }

  /* Messages Table */
  /**
   * Retrieves all messages as a 2D Object array.
   * @return messages data as a 2D Object array.
   */
  public Object[][] getMessagesData()
  {
    String[] columnNames =
    {
      "username", "messageTitle", "messageContent", "sentDate"
    };
    return to2dArray(getData("messagesTable", columnNames));
  }

  /* KB Table */
  /**
   * Retrieves all knowledge base records as a 2D Object array.
   * @return knowledge base data as a 2D Object array.
   */
  public Object[][] getKBData()
  {
    String[] columnNames =
    {
      "username", "kbTitle", "kbContent", "publishedDate"
    };
    return to2dArray(getData("knowledgebaseTable", columnNames));
  }

  /* Employees Table */
  /**
   * Retrieves all employee records as a 2D Object array.
   * @return employee data as a 2D Object array.
   */
  public Object[][] getEmployeeData()
  {
    String[] columnNames =
    {
      "employeeID", "userID", "firstName", "lastName", "departmentID", "DOB", "salary"
    };
    return to2dArray(getData("employeesTable", columnNames));
  }

  /* Attenadance Table */
  /**
   * Retrieves all attendance records as a 2D Object array.
   * @return attendance data as a 2D Object array.
   */
  public Object[][] getAttendanceData()
  {
    String[] columnNames =
    {
      "employeeID", "attendanceYear", "presentCount", "absentCount"
    };
    return to2dArray(getData("attendanceTable", columnNames));
  }

  /* PE Table */
  /**
   * Retrieves all performance evaluation records as a 2D Object array.
   * @return performance evaluation data as a 2D Object array.
   */
  public Object[][] getPerformanceEvaluationData()
  {
    String[] columnNames =
    {
      "evaluationID", "employeeID", "username", "departmentID", "evaluationDate", "evaluationContent"
    };
    return to2dArray(getData("peTable", columnNames));
  }

  // Special PE extraction for managers
  /**
   * Retrieves performance evaluation records for a department, excluding a specific user.
   * @param departmentID the department ID to filter by.
   * @param excludeUserID the user ID to exclude from results.
   * @return filtered performance evaluation data as a 2D Object array.
   */
  public Object[][] getPerformanceEvaluationDataFiltered(int departmentID, int excludeUserID)
  {
    String[] columnNames =
    {
      "evaluationID", "employeeID", "username", "departmentID", "evaluationDate", "evaluationContent"
    };
    ArrayList<ArrayList<String>> dataList = new ArrayList<>();
    // Extracts from same department but filters out manager userID.
    String query = "SELECT * FROM peTable WHERE departmentID = ? AND employeeID NOT IN "
        + "(SELECT employeeID FROM employeesTable WHERE userID = ?)";

    try
    {
      PreparedStatement ps = this.dbConn.prepareStatement(query);
      ps.setInt(1, departmentID);
      ps.setInt(2, excludeUserID);
      ResultSet rs = ps.executeQuery();

      while (rs.next())
      {
        ArrayList<String> row = new ArrayList<>();
        for (String col : columnNames)
        {
          row.add(rs.getString(col));
        }
        dataList.add(row);
      }
    }
    catch (SQLException se)
    {
      System.out.println("SQL Error: Unable to filter PE data.");
    }
    return to2dArray(dataList);
  }

  /* Budget Table */
  /**
   * Retrieves all budget records as a 2D Object array.
   * @return budget data as a 2D Object array.
   */
  public Object[][] getBudgetData()
  {
    String[] columnNames =
    {
      "budgetID", "departmentID", "budgetCategory", "allocatedAmount", "amountSpent"
    };
    return to2dArray(getData("budgetTable", columnNames));
  }

  /* Department Table */
  /**
   * Retrieves all department records as a 2D Object array.
   * @return department data as a 2D Object array.
   */
  public Object[][] getDepartmentData()
  {
    String[] columnNames =
    {
      "departmentID", "departmentName"
    };
    return to2dArray(getData("departmentsTable", columnNames));
  }

  // Calculation Related Methods
  // Update Budget/Insert new entry
  /**
   * Updates or inserts a budget record for a department and category.
   * @param departmentID the department ID.
   * @param category the budget category.
   * @param allocatedAmount the allocated amount.
   * @param spentAmount the amount spent.
   */
  public void updateBudget(int departmentID, String category, double allocatedAmount, double spentAmount)
  {
    String checkQuery = "SELECT COUNT(*) FROM budgetTable WHERE departmentID = ? AND budgetCategory = ?";
    String updateQuery = "UPDATE budgetTable SET allocatedAmount = ?, amountSpent = ? WHERE departmentID = ? AND budgetCategory = ?";
    String insertQuery = "INSERT INTO budgetTable (departmentID, budgetCategory, allocatedAmount, amountSpent) VALUES (?, ?, ?, ?)";

    try
    {
      // Checks whether an entry from selected dept and category already exists
      PreparedStatement checkStmt = this.dbConn.prepareStatement(checkQuery);
      checkStmt.setInt(1, departmentID);
      checkStmt.setString(2, category);
      ResultSet rs = checkStmt.executeQuery();
      rs.next();
      int count = rs.getInt(1); // If exist should be 1

      if (count > 0)
      {
        // Update the entry if department and category exists
        PreparedStatement updateStmt = this.dbConn.prepareStatement(updateQuery);
        updateStmt.setDouble(1, allocatedAmount);
        updateStmt.setDouble(2, spentAmount);
        updateStmt.setInt(3, departmentID);
        updateStmt.setString(4, category);
        updateStmt.executeUpdate();
        System.out.println("Budget updated successfully!");
      }
      else
      {
        // Insert into the table if category does not exist in the department
        PreparedStatement insertStmt = this.dbConn.prepareStatement(insertQuery);
        insertStmt.setInt(1, departmentID);
        insertStmt.setString(2, category);
        insertStmt.setDouble(3, allocatedAmount);
        insertStmt.setDouble(4, spentAmount);
        insertStmt.executeUpdate();
        System.out.println("New budget entry created!");
      }
    }
    catch (SQLException se)
    {
      System.out.println("Error updating/inserting budget record.");
    }
  }

  // Updating attendance (OR INSERT IF NOT EXIST)
  /**
   * Updates or inserts an attendance record for an employee and year.
   * @param employeeID the employee's ID.
   * @param attendanceYear the attendance year.
   * @param presentIncrement the number of days to add to present count.
   * @param absentIncrement the number of days to add to absent count.
   */
  public void updateAttendance(int employeeID, String attendanceYear, int presentIncrement, int absentIncrement)
  {
    // Check if record exist for employee durig a given year
    String checkQuery = "SELECT COUNT(*) FROM attendanceTable WHERE employeeID = ? AND attendanceYear = ?";

    try (PreparedStatement checkPs = this.dbConn.prepareStatement(checkQuery))
    {
      checkPs.setInt(1, employeeID);
      checkPs.setString(2, attendanceYear);
      ResultSet rs = checkPs.executeQuery(); // default rs at 0, db start at 1.

      if (rs.next() && rs.getInt(1) > 0) // Count > 0 meaning entries found
      {
        // Update attendance count when entry already exist
        String updateQuery = "UPDATE attendanceTable SET presentCount = presentCount + ?, absentCount = absentCount + ? WHERE employeeID = ? AND attendanceYear = ?";
        try (PreparedStatement updatePs = this.dbConn.prepareStatement(updateQuery))
        {
          updatePs.setInt(1, presentIncrement);
          updatePs.setInt(2, absentIncrement);
          updatePs.setInt(3, employeeID);
          updatePs.setString(4, attendanceYear);
          updatePs.executeUpdate();
          System.out.println("Attendance updated successfully!");
        }
      }
      else
      {
        // If no record exists, create a new entry by inserting
        String insertQuery = "INSERT INTO attendanceTable (employeeID, attendanceYear, presentCount, absentCount) VALUES (?, ?, ?, ?)";
        try (PreparedStatement insertPs = this.dbConn.prepareStatement(insertQuery))
        {
          insertPs.setInt(1, employeeID);
          insertPs.setString(2, attendanceYear);
          insertPs.setInt(3, presentIncrement);
          insertPs.setInt(4, absentIncrement);
          insertPs.executeUpdate();
          System.out.println("New attendance record inserted!");
        }
      }
    }
    catch (SQLException se)
    {
      System.out.println("Error handling attendance record.");
    }
  }

  // Updating Employee
  /**
   * Updates an employee's information.
   * @param employeeID the employee's ID.
   * @param firstName the new first name.
   * @param lastName the new last name.
   * @param departmentID the new department ID.
   * @param salary the new salary.
   */
  public void updateEmployee(int employeeID, String firstName, String lastName, int departmentID, double salary)
  {
    String query = "UPDATE employeesTable SET firstName = ?, lastName = ?, departmentID = ?, salary = ? WHERE employeeID = ?";
    try
    {
      PreparedStatement ps = this.dbConn.prepareStatement(query);
      ps.setString(1, firstName);
      ps.setString(2, lastName);
      ps.setInt(3, departmentID);
      ps.setDouble(4, salary);
      ps.setInt(5, employeeID);
      ps.executeUpdate();
      System.out.println("Employee with ID " + employeeID + " updated successfully!");
    }
    catch (SQLException se)
    {
      System.out.println("Error updating employee.");
    }
  }

  // Delete Employee
  /**
   * Deletes an employee and related records, and adjusts IDs and auto-increment values.
   * @param employeeID the employee's ID to delete.
   */
  public void deleteEmployee(int employeeID)
  {
    try
    {
      // Disable foreign key checks temporarily to make amendments
      String disableFK = "SET FOREIGN_KEY_CHECKS=0;";
      PreparedStatement disableFKStmt = this.dbConn.prepareStatement(disableFK);
      disableFKStmt.executeUpdate();

      // Retrieves the to-be-deleted user's ID
      String getUserIDQuery = "SELECT userID FROM employeesTable WHERE employeeID = ?";
      PreparedStatement getUserIDStmt = this.dbConn.prepareStatement(getUserIDQuery);
      getUserIDStmt.setInt(1, employeeID);
      ResultSet rsUser1 = getUserIDStmt.executeQuery();
      int userID = -1; // Default if no user found

      if (rsUser1.next())
      {
        userID = rsUser1.getInt("userID");
      }

      // If user is found, deletes the user from the usersTable
      if (userID != -1)
      {
        String deleteUserQuery = "DELETE FROM usersTable WHERE userID = ?";
        PreparedStatement deleteUserStmt = this.dbConn.prepareStatement(deleteUserQuery);
        deleteUserStmt.setInt(1, userID);
        deleteUserStmt.executeUpdate();
        System.out.println("User with ID " + userID + " deleted.");

        // Shifts userID in UsersTable up 1 for everything after deleted userID
        String shiftUserQuery = "UPDATE usersTable SET userID = userID - 1 WHERE userID > ?";
        PreparedStatement shiftUserStmt = this.dbConn.prepareStatement(shiftUserQuery);
        shiftUserStmt.setInt(1, userID);
        shiftUserStmt.executeUpdate();
        System.out.println("User IDs adjusted.");

        // Retrieves max in users and changes auto increment for user creation
        String getMaxUserIDQuery = "SELECT MAX(userID) FROM usersTable";
        PreparedStatement getMaxUserIDStmt = this.dbConn.prepareStatement(getMaxUserIDQuery);
        ResultSet rsUser = getMaxUserIDStmt.executeQuery();

        int newUserAutoIncrement = 1; // Default if table is empty
        if (rsUser.next() && rsUser.getObject(1) != null)
        {
          newUserAutoIncrement = rsUser.getInt(1) + 1; // Set to max +1
        }
        // Resets the users auto increment to the new auto increment
        String resetUserAIQuery = "ALTER TABLE usersTable AUTO_INCREMENT = ?";
        PreparedStatement resetUserAIStmt = this.dbConn.prepareStatement(resetUserAIQuery);
        resetUserAIStmt.setInt(1, newUserAutoIncrement);
        resetUserAIStmt.executeUpdate();
        System.out.println("User Auto-increment reset to " + newUserAutoIncrement);
      }

      // Delete employee from the query when selected
      String deleteQuery = "DELETE FROM employeesTable WHERE employeeID = ?";
      PreparedStatement deleteStmt = this.dbConn.prepareStatement(deleteQuery);
      deleteStmt.setInt(1, employeeID);
      deleteStmt.executeUpdate();
      System.out.println("Employee with ID " + employeeID + " deleted.");

      // Perform shift for ALL EmployeeID in employeesTable after the deleted employee ID
      String shiftQuery = "UPDATE employeesTable SET employeeID = employeeID - 1 WHERE employeeID > ?";
      PreparedStatement shiftStmt = this.dbConn.prepareStatement(shiftQuery);
      shiftStmt.setInt(1, employeeID);
      shiftStmt.executeUpdate();
      System.out.println("Employee IDs adjusted.");

      // Perform shift for ALL UserID in FK related tables
      String updateAttendanceQuery = "UPDATE attendanceTable SET employeeID = employeeID - 1 WHERE employeeID > ?";
      PreparedStatement updateAttendanceStmt = this.dbConn.prepareStatement(updateAttendanceQuery);
      updateAttendanceStmt.setInt(1, employeeID);
      updateAttendanceStmt.executeUpdate();
      System.out.println("Attendance table updated.");

      String updatePEQuery = "UPDATE peTable SET employeeID = employeeID - 1 WHERE employeeID > ?";
      PreparedStatement updatePEStmt = this.dbConn.prepareStatement(updatePEQuery);
      updatePEStmt.setInt(1, employeeID);
      updatePEStmt.executeUpdate();
      System.out.println("Performance evaluation table updated.");

      // Get latest employee ID from employees table
      String getMaxIDQuery = "SELECT MAX(employeeID) FROM employeesTable";
      PreparedStatement getMaxIDStmt = this.dbConn.prepareStatement(getMaxIDQuery);
      ResultSet rs = getMaxIDStmt.executeQuery();
      int newAutoIncrement = 1; // Default if table is empty
      if (rs.next() && rs.getObject(1) != null)
      {
        newAutoIncrement = rs.getInt(1) + 1; // Set to max +1
      }

      // Resets the employee auto increment to the new auto increment
      String resetEmployeeAIQuery = "ALTER TABLE employeesTable AUTO_INCREMENT = ?";
      PreparedStatement resetEmployeeAIStmt = this.dbConn.prepareStatement(resetEmployeeAIQuery);
      resetEmployeeAIStmt.setInt(1, newAutoIncrement);
      resetEmployeeAIStmt.executeUpdate();
      System.out.println("Employee Auto-increment reset to " + newAutoIncrement);

      // Re-enable foreign key checks to continue checking for errors
      String enableFK = "SET FOREIGN_KEY_CHECKS=1;";
      PreparedStatement enableStmt = this.dbConn.prepareStatement(enableFK);
      enableStmt.executeUpdate();
    }
    catch (SQLException se)
    {
      System.out.println("Error deleting employee." + se.getMessage());
      se.printStackTrace();
    }
  }

  // Manager Department & Evaluations
  /**
   * Retrieves the department ID for a manager given their username.
   * @param username the manager's username.
   * @return the department ID, or -1 if not found.
   */
  public int getManagerDepartment(String username)
  {
    String query = "SELECT departmentID FROM employeesTable WHERE userID = (SELECT userID FROM usersTable WHERE username = ?)";

    try (PreparedStatement ps = this.dbConn.prepareStatement(query))
    {
      ps.setString(1, username);
      ResultSet rs = ps.executeQuery();
      if (rs.next())
      {
        return rs.getInt("departmentID"); // Return the department ID of the manager
      }
    }
    catch (SQLException se)
    {
      System.out.println("Error retrieving manager's department.");
    }
    return -1; // Default value if department not found
  }

  // Allows only access to specific employee data in a department (used for managers)
  /**
   * Retrieves employees in a specific department as a 2D Object array.
   * @param departmentID the department ID to filter by.
   * @return employee data as a 2D Object array.
   */
  public Object[][] filterEmployeesByDepartment(int departmentID)
  {
    String[] columnNames =
    {
      "employeeID", "userID", "firstName", "lastName", "departmentID", "DOB", "salary"
    };
    return to2dArray(getDataFiltered("employeesTable", columnNames, "departmentID", departmentID));
  }

  // Allows filter out manager username from evaluation
  /**
   * Retrieves the user ID for a given username.
   * @param username the username to search for.
   * @return the user ID, or -1 if not found.
   */
  public int getUserIDByUsername(String username)
  {
    String query = "SELECT userID FROM usersTable WHERE username = ?";
    try
    {
      PreparedStatement ps = this.dbConn.prepareStatement(query);
      ps.setString(1, username);
      ResultSet rs = ps.executeQuery();
      if (rs.next())
      {
        return rs.getInt("userID");
      }
    }
    catch (SQLException se)
    {
      System.out.println("Error retrieving user ID by username.");
    }
    return -1; // Default if not found
  }

  // Allows search for employees by managers
  /**
   * Searches for employees in a department matching a search term by name.
   * @param departmentID the department ID to search within.
   * @param searchTerm the term to search for in employee names.
   * @return filtered employee data as a 2D Object array.
   */
  public Object[][] searchEmployeesAfterFilter(int departmentID, String searchTerm)
  {
    // Get all employees in the manager's department
    Object[][] departmentEmployees = filterEmployeesByDepartment(departmentID);
    // Create a list to store filtered employees
    ArrayList<Object[]> filteredList = new ArrayList<>();
    // Case insensitive, same format as search function
    String allLower = searchTerm.toLowerCase().trim(); // Standardize search and remove space in front and after
    for (int i = 0; i < departmentEmployees.length; i++)
    {
      String firstName = departmentEmployees[i][2].toString().toLowerCase(); // Column index 2 = First Name
      String lastName = departmentEmployees[i][3].toString().toLowerCase();  // Column index 3 = Last Name
      String fullName = firstName + " " + lastName;

      if (firstName.contains(allLower) || lastName.contains(allLower) || fullName.contains(allLower))
      {
        filteredList.add(departmentEmployees[i]);
      }
    }
    // Convert the filtered list to a 2D array
    Object[][] filteredArray = new Object[filteredList.size()][];
    return filteredList.toArray(filteredArray);
  }

  // Filter method for DB
  /**
   * Retrieves filtered data from a table where a column matches a specific value.
   * @param tableName the table to query.
   * @param tableHeaders the columns to retrieve.
   * @param filterColumn the column to filter by.
   * @param filterValue the value to filter with.
   * @return a 2D ArrayList of filtered data.
   */
  private ArrayList<ArrayList<String>> getDataFiltered(String tableName, String[] tableHeaders, String filterColumn, int filterValue)
  {
    int columnCount = tableHeaders.length; // Resuable for all table filter
    Statement s;
    ResultSet rs;
    String query = "SELECT * FROM " + tableName + " WHERE " + filterColumn + " = " + filterValue;

    this.data = new ArrayList<>();
    try
    {
      s = this.dbConn.createStatement();
      rs = s.executeQuery(query);

      while (rs.next())
      {
        ArrayList<String> row = new ArrayList<>();
        for (int i = 0; i < columnCount; i++)
        {
          row.add(rs.getString(tableHeaders[i]));
        }
        this.data.add(row);
      }
    }
    catch (SQLException se)
    {
      System.out.println("SQL Error: Unable to filter data.");
    }
    return this.data;
  }

  // Create and initialize new category in a department (IF NOT EXIST ALREADY)
  /**
   * Creates a new budget category for a department if it does not already exist.
   * @param departmentID the department ID.
   * @param category the budget category to create.
   */
  public void createBudgetCategory(int departmentID, String category)
  {
    String checkQuery = "SELECT COUNT(*) FROM budgetTable WHERE departmentID = ? AND LOWER(budgetCategory) = LOWER(?)"; // Check whether Category already exists (Ignores Case)
    String insertQuery = "INSERT INTO budgetTable (departmentID, budgetCategory, allocatedAmount, amountSpent) VALUES (?, ?, 0, 0)"; // zero allocated and amt spent

    try
    {
      PreparedStatement checkStmt = this.dbConn.prepareStatement(checkQuery);
      checkStmt.setInt(1, departmentID);
      checkStmt.setString(2, category);
      ResultSet rs = checkStmt.executeQuery();
      rs.next();
      int count = rs.getInt(1);

      if (count == 0)
      {
        PreparedStatement insertStmt = this.dbConn.prepareStatement(insertQuery);
        insertStmt.setInt(1, departmentID);
        insertStmt.setString(2, category);
        insertStmt.executeUpdate();
        System.out.println("Budget category created successfully!");
      }
      else
      {
        System.out.println("Category already exists for this department.");
      }
    }
    catch (SQLException se)
    {
      System.out.println("Error creating budget category.");
    }
  }

  // Create Budget Category
  /**
   * Main method for testing database operations and table insertions.
   * @param args command line arguments.
   */
  public static void main(String[] args)
  {
    // Inserting into DB
    String dbName = "OfficeAutomationDB";

    /* Department Table */
    String deptTableName = "departmentsTable";
    String deptDBQuery = "INSERT IGNORE INTO departmentsTable VALUES (?, ?)"; // Avoid duplicate entry
    // Dummy Values
    int departmentID = 1;
    String departmentName = "Human Resources";

    /* User Table */
    String usersTableName = "usersTable";
    String usersDBQuery = "INSERT IGNORE INTO usersTable VALUES (?, ?, ?, ?)";
    int userID = 1;
    String username = "admin";
    String password = "password";
    String role = "Admin";

    // Create object and make connection to the database
    JavaDBAccess objAccess = new JavaDBAccess();
    // Getting database connection
    Connection myDBConn = objAccess.getDBConn();

    // Insert the dummy values into DB
    try
    {
      // Create blank statement
      PreparedStatement deptPS = myDBConn.prepareStatement(deptDBQuery);
      // Supply values
      deptPS.setInt(1, departmentID);
      deptPS.setString(2, departmentName);
      deptPS.executeUpdate();
      System.out.println("Data is inserted into " + deptTableName + ".");

      PreparedStatement usersPS = myDBConn.prepareStatement(usersDBQuery);
      usersPS.setInt(1, userID);
      usersPS.setString(2, username);
      usersPS.setString(3, password);
      usersPS.setString(4, role);
      usersPS.executeUpdate();
      System.out.println("Data is inserted into " + usersTableName + ".");
    }
    catch (SQLException se)
    {
      System.out.println("Error inserting data.");
    }

    // Reading data from table
    // Setting column names to read from
    String[] deptColumnNames =
    {
      "departmentID", "departmentName"
    };
    String[] usersColumnName =
    {
      "userID", "username", "password", "role"
    };

    ArrayList<ArrayList<String>> demoData = objAccess.getData(deptTableName, deptColumnNames);
    System.out.println("DepartmentsTable Data: " + demoData);
    ArrayList<ArrayList<String>> demoData2 = objAccess.getData(usersTableName, usersColumnName);
    System.out.println("UsersTable Data: " + demoData2);

    // Testing to2DArray (ONLY DEPT TABLE)
    Object[][] demoDataArray = objAccess.to2dArray(demoData); // converts current demo data to 2D Array
    // accessing each individual cell in 2D array
    for (int r = 0; r < demoDataArray.length; r++)
    {
      for (int c = 0; c < demoDataArray[0].length; c++)
      {
        System.out.print(demoDataArray[r][c]); // Print out value in the cell
      }
      System.out.println();
    }

  }
}
