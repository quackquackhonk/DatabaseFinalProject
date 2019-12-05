package edu.cs3200.musiclibrary.operations;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Scanner;

public class UnlikeOperation extends AbstractOperation implements MusicLibraryOperation {

  /**
   * Constructs an UnlikeOperation.
   *
   * @param command the command
   * @param conn    the connection to the DB.
   * @param scan    the user input scanner.
   */
  public UnlikeOperation(String command, Connection conn, Scanner scan) {
    super(command, conn, scan);
    this.args = this.getCommandArgs(this.command);
  }

  @Override
  public void run() {
    // unlike_song(user id, song id)
    try {
      if (this.args.length < 4) {
        System.out.println(this.command + " does not have enough arguments. Needed 3, found "
                + (this.args.length - 1));
        return;
      }

      int userId = this.getUserId(this.args[1]);
      String title = this.removeQuotes(this.args[2]);
      String artist = this.removeQuotes(this.args[3]);

      if (!songExists(title, artist)) {
        System.out.println(title + " by " + artist + " is not a song in the database");
        return;
      }

      if (unlikeSongForUser(userId, title, artist)) {
        System.out.println("Successfully removed song " + title + " by " + artist + " from "
                + this.args[1] +"'s liked songs");
      } else {
        System.out.println("Unable to unlike song " + title + ". User does not like that song.");
      }


    } catch (SQLException e) {
      System.out.println("Error occurred when updating database.");
      e.printStackTrace();
    }

  }

  /**
   * Removes a the given song from the given user's liked songs
   * @param userId the user liking the song
   * @param title the song to like
   * @param artist the artist
   * @return if the song successfully unliked
   * @throws SQLException if problems occurred when interacting with the database
   */
  private boolean unlikeSongForUser(int userId, String title, String artist) throws SQLException {
    if (!doesUserLikeSong(userId, title, artist)) {
      return false;
    }
    String prepCall = "CALL unlike_song(?,?,?)";
    PreparedStatement unlikeSongStatement = conn.prepareStatement(prepCall);
    unlikeSongStatement.clearParameters();
    unlikeSongStatement.setInt(1, userId);
    unlikeSongStatement.setString(2, title);
    unlikeSongStatement.setString(3, artist);
    // Execute
    unlikeSongStatement.executeQuery();
    return true;
  }
  /**
   * Does the user like the given song?
   * @param userId the user to check
   * @param song the song to check
   * @param artist the artist the song is by
   * @return does the user like this song
   */
  private boolean doesUserLikeSong(int userId, String song, String artist) throws SQLException {
    String prepCall = "SELECT likedSong_song_title, likedSong_song_artist FROM liked_song WHERE ?" +
            " = likedSong_listener ORDER BY likedSong_song_title";
    PreparedStatement userLikedSongs = conn.prepareStatement(prepCall);
    userLikedSongs.clearParameters();
    userLikedSongs.setInt(1, userId);

    ResultSet likedSongs = userLikedSongs.executeQuery();
    while (likedSongs.next()) {
      String st = likedSongs.getString("likedSong_song_title");
      String sa = likedSongs.getString("likedSong_song_artist");
      if (st.equalsIgnoreCase(song) && sa.equalsIgnoreCase(artist)) {
        return true;
      }
    }
    return false;
  }
}
