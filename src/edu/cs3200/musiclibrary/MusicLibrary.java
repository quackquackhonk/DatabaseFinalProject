package edu.cs3200.musiclibrary;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import java.util.Scanner;

import edu.cs3200.musiclibrary.operations.AddOperation;
import edu.cs3200.musiclibrary.operations.CreateOperation;
import edu.cs3200.musiclibrary.operations.DeleteOperation;
import edu.cs3200.musiclibrary.operations.LikeOperation;
import edu.cs3200.musiclibrary.operations.MusicLibraryOperation;
import edu.cs3200.musiclibrary.operations.RemoveOperation;
import edu.cs3200.musiclibrary.operations.ShowOperation;
import edu.cs3200.musiclibrary.operations.UnlikeOperation;
import edu.cs3200.musiclibrary.operations.UpdateOperation;

/**
 * Main class for running the edu.cs3200.musiclibrary.MusicLibrary database. Holds functions for
 * running, as well as calling server-side procedures and functions.
 */
public class MusicLibrary {

  private Scanner scan = new Scanner(System.in);

  /**
   * The name of the MySQL account to use (or empty for anonymous)
   */
  private String USER_NAME = "root";

  /**
   * The password for the MySQL account (or empty for anonymous)
   */
  private String PASSWORD = "root";

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
  private final String DB_NAME = "music_final_project";

  /**
   * Get a new database connection
   *
   * @return a connection to the database.
   * @throws SQLException
   */
  public Connection getConnection(String user, String pass) throws SQLException {
    Connection conn = null;
    Properties connectionProps = new Properties();
    connectionProps.put("user", user);
    connectionProps.put("password", pass);

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
    System.out.print("Enter a username: ");
    inputUser = scan.nextLine();
    System.out.print("Enter a password: ");
    inputPassword = scan.nextLine();
    Connection conn = null;
    // Connect to MySQL
    try {
      conn = this.getConnection(inputUser, inputPassword);
      System.out.println("Connected to database: " + DB_NAME);
    } catch (SQLException e) {
      System.out.println("Invalid login information");
      return;
    }
    // update stored un and pass, for use in other functions.
    this.USER_NAME = inputUser;
    this.PASSWORD = inputPassword;

    // Loop until the user logsout
    while (true) {
      System.out.print("$> ");
      String command = scan.nextLine().toLowerCase();
      switch (command) {
        case "logout":
          this.logout(conn);
        case "help":
          System.out.println(MusicLibraryCommand.helpInformation());
          break;
        default:
          if (MusicLibraryCommand.isCommand(command)) {
            this.processCommand(command);
            break;
          }
          System.out.println("Invalid command, enter a valid command or type \"help\" for a list " +
                  "of commands.");
          break;
      }
    }
  }

  /**
   * Processes the given valid command from the command line
   *
   * @param command the command to process
   */
  private void processCommand(String command) {
    Connection conn = null;
    try {
      conn = this.getConnection(this.USER_NAME, this.PASSWORD);
    } catch (SQLException e) {
      System.out.println("Failure to obtain connection when processing command");
      return;
    }
    String prefix = command.split(" ")[0];
    MusicLibraryOperation op;
    MusicLibraryCommand oper = MusicLibraryCommand.commandFromPrefix(prefix);
    if (oper == null) {
      return;
    }
    switch (MusicLibraryCommand.commandFromPrefix(prefix)) {
      case ADD:
        op = new AddOperation(command, conn, scan);
        break;
      case REMOVE:
        op = new RemoveOperation(command, conn);
        break;
      case UPDATE:
        op = new UpdateOperation(command, conn, scan);
        break;
      case CREATE:
        op = new CreateOperation(command, conn, scan);
        break;
      case DELETE:
        op = new DeleteOperation(command, conn, scan);
        break;
      case LIKE:
        op = new LikeOperation(command, conn, scan);
        break;
      case UNLIKE:
        op = new UnlikeOperation(command, conn, scan);
        break;
      case SHOW:
        op = new ShowOperation(command, conn, scan);
        break;
      default:
        return;
    }
    // run the operation
    op.run();
  }

  /**
   * Closes the connection and ends the application.
   *
   * @param conn the connection to close.
   * @throws SQLException if closing the connection fails.
   */
  private void logout(Connection conn) throws SQLException {
    System.out.println("Closing connection...");
    conn.close();
    System.out.println("Application ending.");
    System.exit(0);
  }
}
