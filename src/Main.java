import java.sql.SQLException;

import edu.cs3200.musiclibrary.MusicLibrary;

/**
 * Main class for running the application.
 */
public class Main {

  /**
   * Runs the application.
   *
   * @param args command line arguments.
   */
  public static void main(String[] args) {
    MusicLibrary app = new MusicLibrary();
    try {
      app.run();
    } catch (SQLException e) {
      System.out.println(e.getMessage());
    }
  }
}
