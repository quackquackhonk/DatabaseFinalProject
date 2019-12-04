package edu.cs3200.musiclibrary.operations;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class AddOperation extends AbstractOperation implements MusicLibraryOperation {


  /**
   * Constructs an AddOperation.
   *
   * @param command the command
   * @param conn    the connection to the DB.
   * @param scan    the user input scanner.
   */
  public AddOperation(String command, Connection conn, Scanner scan) {
    super(command, conn, scan);
  }

  @Override
  public void run() {
    if (this.args.length < 2) {
      System.out.println("Adds expects 2 arguments. Found 1");
      return;
    }

    try {
      switch(this.args[1]) {
        case "song":
          this.addSong();
          break;
        case "album":
          this.addAlbum();
          break;
        case "artist":
          this.addArtist();
          break;
        case "label":
          this.addLabel();
          break;
        case "user":
          this.addUser();
          break;
        default:
          System.out.println(this.args[1] + " is not a valid item to add to the database");
      }
    } catch (SQLException e) {
      System.out.println("Exception occurred when interacting with database.");
      e.printStackTrace();
    }
  }

  private void addSong() throws SQLException {


  }

  private void addAlbum() throws SQLException {

  }

  private void addArtist() throws SQLException {

  }

  private void addLabel() throws SQLException {

  }

  private void addUser() throws SQLException {
    System.out.println("Please enter a new username for the new user: ");
    System.out.print("$> ");
    String un = this.scan.nextLine();
    if (this.getUserId(un) != -1) {
      System.out.println("That username is taken.");
      this.addUser();
    } else {
     String prepCall = "CALL add_user(?)";
     PreparedStatement addUserStaetment = conn.prepareStatement(prepCall);
     addUserStaetment.clearParameters();
     addUserStaetment.setString(1, un);
     addUserStaetment.executeQuery();
     System.out.println("Added " + un + " to the database.");
    }
  }

  /**
   * Gets the user id of the given user.
   * @param user the username
   * @return the user id of the given user, -1 if invalid user.
   */
  private int getUserId(String user) throws SQLException {
    String prepCall = "CALL get_user_id(?)";
    PreparedStatement getUserIdStatement = conn.prepareStatement(prepCall);
    getUserIdStatement.clearParameters();
    getUserIdStatement.setString(1, user);

    ResultSet userId = getUserIdStatement.executeQuery();
    if (userId.next()) {
      return userId.getInt("listener_id");
    }
    return -1;
  }

  /**
   * Checks if the given song is in the database.
   * @param title the title
   * @param artist the artist
   * @return is the song in the database
   * @throws SQLException if something goes wrong
   */
  private boolean songExists(String title, String artist) throws SQLException {
    String prepCall = "SELECT * FROM song";
    PreparedStatement allSongs = conn.prepareStatement(prepCall);

    ResultSet allSongsRS = allSongs.executeQuery();
    while (allSongsRS.next()) {
      String st = allSongsRS.getString("song_title");
      String sa = allSongsRS.getString("song_artist");
      if (st.equalsIgnoreCase(title) && sa.equalsIgnoreCase(artist)) {
        return true;
      }
    }
    return false;
  }
}
