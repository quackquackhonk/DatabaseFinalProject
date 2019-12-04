package edu.cs3200.musiclibrary.operations;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DeleteOperation implements MusicLibraryOperation {

  private String cmd;
  private Connection conn;

  public DeleteOperation(String command, Connection conn) {
    this.cmd = command;
    this.conn = conn;
  }

  @Override
  public void run() {
    try {
      String[] cmdSplit = this.cmd.split(" ");
      if (cmdSplit.length < 3) {
        System.out.println("Need 3 arguments to delete something from the database, found: "
                + cmdSplit.length);
        return;
      }
      switch (cmdSplit[1]) {
        case "user":
          int userId = this.getUserId(cmdSplit[2]);
          if (userId == -1) {
            System.out.println(cmdSplit[2] + " is not a valid username in the database");
            return;
          }
          this.deleteUser(userId);
          System.out.println("Successfully deleted " + cmdSplit[2] + " from the database.");
          break;
        case "playlist":
          int plId = 0; // will be playlist ID
          this.deletePlaylist(plId);
          break;
        default:
          System.out.println(cmdSplit[1] + " is not a valid item to delete.");
      }
    } catch (SQLException e) {
      System.out.println("Error occurred when updating the database.");
      e.printStackTrace();
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
   * Deletes a user from the database.
   * @param userId the id of the user to delete.
   */
  private void deleteUser(int userId) throws SQLException {
    String prepCall = "CALL delete_user(?)";
    PreparedStatement deleteUserStatement = conn.prepareStatement(prepCall);
    deleteUserStatement.clearParameters();
    deleteUserStatement.setInt(1, userId);
    deleteUserStatement.executeQuery();
  }

  /**
   * Deletes a playlist from the database, but does not delete any of the songs or user information.
   * @param playlistId the id of the playlist to delete.
   */
  private void deletePlaylist(int playlistId) {
    // delete!
  }
}
