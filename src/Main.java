import java.sql.SQLException;

/**
 * Main class for running the application.
 */
public class Main {

  public static void main(String[] args) {
    MusicLibrary app = new MusicLibrary();
    try {
      app.run();
    } catch (SQLException e) {
      System.out.println(e.getMessage());
    }
  }

}
