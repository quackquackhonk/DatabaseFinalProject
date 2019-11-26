package edu.cs3200.musiclibrary.operations;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LikeOperation implements MusicLibraryOperation {

  private String cmd;
  private Connection conn;

  public LikeOperation(String command, Connection conn) {
    this.cmd = command;
    this.conn = conn;
  }

  @Override
  public void run() {
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
      if (this.likeSongForUser(userId, songId)) {
        System.out.println("Successfully added song with id: " + songId + " to " + cmdSplit[1] +
                "\'s liked songs");
      } else {
        System.out.println(cmdSplit[1] + " already likes song with id:" + songId);
      }
    } catch (SQLException e) {
      System.out.println("Error occurred when updating database.");
      e.printStackTrace();
    }
  }

  /**
   * Adds a the given song to the given user's liked songs
   * @param userId the user liking the song
   * @param songId the song to like
   * @return if the song successfully liked
   */
  private boolean likeSongForUser(int userId, int songId) throws SQLException {
    if (doesUserLikeSong(userId, songId)) {
      return false;
    }

    String prepCall = "CALL like_song(?,?)";
    PreparedStatement likeSongStatement = conn.prepareStatement(prepCall);
    likeSongStatement.clearParameters();
    likeSongStatement.setInt(1, userId);
    likeSongStatement.setInt(2, songId);

    // Execute
    likeSongStatement.executeQuery();
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