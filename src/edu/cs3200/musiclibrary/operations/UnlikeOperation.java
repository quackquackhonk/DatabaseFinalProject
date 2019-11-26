package edu.cs3200.musiclibrary.operations;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UnlikeOperation implements MusicLibraryOperation {

  private String cmd;
  private Connection conn;

  public UnlikeOperation(String command, Connection conn) {
    this.cmd = command;
    this.conn = conn;
  }

  @Override
  public void run() {
    // unlike_song(user id, song id)
    try {
      String[] cmdSplit = this.cmd.split(" ");
      if (cmdSplit.length < 3) {
        System.out.println(this.cmd + " does not have enough arguments. Needed 2, found "
                + (cmdSplit.length - 1));
        return;
      }

      int userId = this.getUserId(cmdSplit[1]);
      int songId;
      if (userId == -1) {
        System.out.println(cmdSplit[1] + " is not a valid user name in the database");
        return;
      }
      try {
        songId = Integer.parseInt(cmdSplit[2]);
      } catch (NumberFormatException e) {
        System.out.println(cmdSplit[2] + " is not a valid song id in the database");
        return;
      }
      if (this.unlikeSongForUser(userId, songId)) {
        System.out.println("Successfully removed song with id: " + songId + " from " + cmdSplit[1] +
                "\'s liked songs");
      } else {
        System.out.println(cmdSplit[1] + " does not like song with id: " + songId);
      }
    } catch (SQLException e) {
      System.out.println("Error occurred when updating database.");
      e.printStackTrace();
    }

  }

  /**
   * Removes a the given song from the given user's liked songs
   * @param userId the user liking the song
   * @param songId the song to like
   * @return if the song successfully unliked
   * @throws SQLException if problems occurred when interacting with the database
   */
  private boolean unlikeSongForUser(int userId, int songId) throws SQLException {
    if (!doesUserLikeSong(userId, songId)) {
      return false;
    }

    String prepCall = "CALL unlike_song(?,?)";
    PreparedStatement unlikeSongStatement = conn.prepareStatement(prepCall);
    unlikeSongStatement.clearParameters();
    unlikeSongStatement.setInt(1, userId);
    unlikeSongStatement.setInt(2, songId);

    // Execute
    unlikeSongStatement.executeQuery();
    return true;
  }

  /**
   * Does the user like the given song?
   * @param userId the user to check
   * @param songId the song to check
   * @return does the user like this song
   */
  private boolean doesUserLikeSong(int userId, int songId) throws SQLException {
    String prepCall = "SELECT likedSong_song FROM liked_song WHERE ? = likedSong_listener ORDER " +
            "BY likedSong_song";
    PreparedStatement userLikedSongs = conn.prepareStatement(prepCall);
    userLikedSongs.clearParameters();
    userLikedSongs.setInt(1, userId);

    ResultSet likedSongs = userLikedSongs.executeQuery();
    while (likedSongs.next()) {
      int toCheck = likedSongs.getInt("likedSong_song");
      if (toCheck == songId) {
        return true;
      }
    }

    return false;
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
}
