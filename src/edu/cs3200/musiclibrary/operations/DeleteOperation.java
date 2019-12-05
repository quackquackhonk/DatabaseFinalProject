package edu.cs3200.musiclibrary.operations;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Scanner;

public class DeleteOperation extends AbstractOperation implements MusicLibraryOperation {


  /**
   * Constructs an DeleteOperation
   *
   * @param command the command
   * @param conn    the connection to the DB.
   * @param scan    the user input scanner.
   */
  public DeleteOperation(String command, Connection conn, Scanner scan) {
    super(command, conn, scan);
    this.args = this.getCommandArgs(this.command);
  }

  @Override
  public void run() {
    try {
      if (this.args.length < 2) {
        System.out.println("Need minimum of 2 arguments to delete an entry. Found 1");
      } else {
        switch (this.args[1]) {
          case "song":
            System.out.println("Enter the song name to delete: ");
            System.out.print("$> ");
            String title = scan.nextLine();
            System.out.println("Enter the artist of that song:");
            System.out.print("$> ");
            String artist = scan.nextLine();
            if (this.songExists(title, artist)) {
              this.deleteSong(title, artist);
            } else {
              System.out.println("Unable to delete song: Song does not exist");
            }
            break;
          case "artist":
            System.out.println("Enter the name of the artist to delete:");
            System.out.print("$> ");
            String name = scan.nextLine();
            if (this.artistExists(name)) {
              this.deleteArtist(name);
            } else {
              System.out.println("Unable to delete artist: No artist exists with the given name");
            }
            break;
          case "label":
            System.out.println("Enter the name of the label to delete:");
            System.out.print("$> ");
            String label = scan.nextLine();
            if (this.labelExists(label)) {
              this.deleteLabel(label);
            } else {
              System.out.println("Unable to delete label: No label exists with the given name");
            }
            break;
          case "user":
            System.out.println("Enter the username of the user to delete:");
            System.out.print("$> ");
            String un = scan.nextLine();
            if (this.getUserId(un) != -1) {
              this.deleteUser(un);
              System.out.println("Successfully deleted user with username " + un + " from the " +
                      "database");
            } else {
              System.out.println("Unable to delete user: No user exists with the given name");
            }
            break;
          case "playlist":
            //this.deletePlaylist(-1);
            break;
          default:
            System.out.println(this.args[1] + " is not a valid type to delete from the database");
        }
      }
    } catch (SQLException e) {
      System.out.println("Error occurred when updating the database.");
      e.printStackTrace();
    }
  }

  /**
   * Deletes the given song from the database
   *
   * @param title  the title of the song.
   * @param artist the artist of the song.
   * @throws SQLException if the deletion goes wrong
   */
  private void deleteSong(String title, String artist) throws SQLException {
    String prepCall = "CALL delete_song(?,?)";
    PreparedStatement deleteSong = conn.prepareStatement(prepCall);
    deleteSong.clearParameters();
    deleteSong.setString(1, title);
    deleteSong.setString(2, artist);
    deleteSong.executeQuery();
    System.out.println("Deleted song " + title + " by " + artist + " from the database");
  }

  private void deleteArtist(String name) throws SQLException {
    String prepCall = "CALL delete_artist(?)";
    PreparedStatement deleteArtist = conn.prepareStatement(prepCall);
    deleteArtist.clearParameters();
    deleteArtist.setString(1, name);
    deleteArtist.executeQuery();
    System.out.println("Deleted the artist " + name + " from the database");
  }

  private void deleteLabel(String name) throws SQLException {
    String prepCall = "CALL delete_label(?)";
    PreparedStatement deleteLabel = conn.prepareStatement(prepCall);
    deleteLabel.clearParameters();
    deleteLabel.setString(1, name);
    deleteLabel.executeQuery();
    System.out.println("Deleted the label " + name + " from the database");
  }

  /**
   * Deletes a user from the database.
   *
   * @param userName the id of the user to delete.
   */
  private void deleteUser(String userName) throws SQLException {
    String prepCall = "CALL delete_user(?)";
    PreparedStatement deleteUserStatement = conn.prepareStatement(prepCall);
    deleteUserStatement.clearParameters();
    deleteUserStatement.setString(1, userName);
    deleteUserStatement.executeQuery();
  }

  /**
   * Deletes a playlist from the database, but does not delete any of the songs or user
   * information.
   *
   * @param playlistId the id of the playlist to delete.
   */
  private void deletePlaylist(int playlistId) {
    // delete!
  }
}
