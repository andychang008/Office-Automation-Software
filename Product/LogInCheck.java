//package iaproductcode;

/**
 * Class responsible for verifying login credentials by comparing the input
 * username and password against data retrieved from the database.
 * Sets the user's role upon successful authentication.
 *
 */
public class LogInCheck
{

  /**
   * Checks whether the provided username and password match any entry in the users table.
   * If a match is found, sets the user's role based on the database record.
   *
   * @param username the input username to check
   * @param password the input password to check
   * @param dbAccess the database access object used to retrieve user data
   * @return true if login credentials match a user entry, false otherwise
   */
  public static boolean checkLogIn(String username, String password, JavaDBAccess dbAccess)
  {
    Object[][] data = dbAccess.getUsersData();
    boolean match = false;
    int r = 0;
    String dbUsername = "";
    String dbPassword = "";

    while (r < data.length && match == false)
    {
      dbUsername = data[r][0].toString();
      dbPassword = data[r][1].toString();
      if (dbUsername.equals(username) && dbPassword.equals(password))
      {
        LogIn.setRole(data[r][2].toString());
        match = true;
      }
      r++;
    }
    return match;
  }
}
