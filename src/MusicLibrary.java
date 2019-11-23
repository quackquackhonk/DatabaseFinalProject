import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import java.util.Scanner;

/**
 * Main class for running the MusicLibrary database. Holds functions for running, as well as
 * calling server-side procedures and functions.
 */
public class MusicLibrary {

  /**
   * The name of the MySQL account to use (or empty for anonymous)
   */
  private final String USER_NAME = "root";

  /**
   * The password for the MySQL account (or empty for anonymous)
   */
  private final String PASSWORD = "root";

  /**
   * The name of the computer running MySQL
   */
  private final String SERVER_NAME = "localhost";

  /**
   * The port of the MySQL server (default is 3306)
   */
  private final int PORT = 3306;

  /**
   * The name of the database we are testing with (this default is installed with MySQL)
   */
  private final String DB_NAME = "lotrfinal";

  /**
   * Get a new database connection
   *
   * @return
   * @throws SQLException
   */
  public Connection getConnection() throws SQLException {
    Connection conn = null;
    Properties connectionProps = new Properties();
    connectionProps.put("user", this.USER_NAME);
    connectionProps.put("password", this.PASSWORD);

    conn = DriverManager.getConnection("jdbc:mysql://"
                    + this.SERVER_NAME + ":" + this.PORT + "/" + this.DB_NAME,
            connectionProps);

    return conn;
  }

  /**
   * Connects to the database and does what the assignment wants me to do.
   */
  public void run() throws SQLException {
    // prompt the user for login information
    String inputUser = "";
    String inputPassword = "";
    Scanner scan = new Scanner(System.in);
    System.out.print("Enter a username: ");
    inputUser = scan.nextLine();
    System.out.print("Enter a password: ");
    inputPassword = scan.nextLine();
    if (inputUser.equals(this.USER_NAME) && inputPassword.equals(this.PASSWORD)) {
      System.out.println("Login Successful!");
    } else {
      System.out.println("Invalid login credentials.");
      System.exit(-1);
    }

    // Connect to MySQL
    Connection conn = null;
    try {
      conn = this.getConnection();
      System.out.println("Connected to database");
    } catch (SQLException e) {
      System.out.println("ERROR: Could not connect to the database");
      e.printStackTrace();
      return;
    }
  }
}
