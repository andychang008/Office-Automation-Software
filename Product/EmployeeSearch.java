//package iaproductcode;

/**
 * EmployeeSearch class provides methods to search for employees
 * in the database by matching their first name, last name, or full name.
 * 
 * This class utilizes the JavaDBAccess class to interact with the database.
 * It includes a testable main method for standalone testing.
 * 
 */
public class EmployeeSearch
{
  /**
   * Searches for employees in the database based on the given search term.
   * The search term is matched against the first name, last name, or full name of the employee.
   * 
   * @param searchTerm The string to search for within employee names.
   * @return A 2D Object array containing employee records that match the search criteria.
   */
  public static Object[][] searchEmployees(String searchTerm)
  {
    JavaDBAccess dbAccess = new JavaDBAccess();
    Object[][] allEmployees = dbAccess.getEmployeeData();
    
    String allLower = searchTerm.toLowerCase().trim(); // Standardize search and remove space in front and after

    // Filter Employees Matching Search Term (Search by name)
     return java.util.Arrays.stream(allEmployees)
        .filter(row -> {
            String firstName = row[2].toString().toLowerCase();
            String lastName = row[3].toString().toLowerCase();
            String fullName = firstName + " " + lastName;

            return firstName.contains(allLower) || // Match first name
                   lastName.contains(allLower) || // Match last name
                   fullName.contains(allLower);   // Match full name
        })
        .toArray(Object[][]::new);
  }
  
  /**
   * Main method for testing the searchEmployees function.
   * Executes a sample search with the term "Test" and prints the results to the console.
   * 
   * @param args Command-line arguments (not used).
   */
  public static void main(String[] args)
  {
    String searchTerm = "Test";
    Object[][] results = searchEmployees(searchTerm);
    
    // Print the Results
    if (results.length > 0)
    {
        System.out.println("Search Results for: " + searchTerm);
        for (int r = 0; r<results.length; r++)
        {
            System.out.println(java.util.Arrays.toString(results[r]));
        }
    }
    else
    {
        System.out.println("No employees found matching: " + searchTerm);
    }

  }
  
}
