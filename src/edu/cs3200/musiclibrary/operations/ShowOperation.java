package edu.cs3200.musiclibrary.operations;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;


/**
 * Operation to handle the showing (reading) of data from the database.
 */
public class ShowOperation extends AbstractOperation implements MusicLibraryOperation {

  private String cmd;
  private Connection conn;

  /**
   * Constructs an Operation.
   *
   * @param command the command
   * @param conn    the connection to the DB.
   * @param scan    the user input scanner.
   */
  public ShowOperation(String command, Connection conn, Scanner scan) {
    super(command, conn, scan);
  }

  @Override
  public void run() {
    try {
      if (this.args.length < 3) {
        System.out.println(this.cmd + " does not have enough arguments. Needed 2, found "
                + (this.args.length - 1));
      }
      // getting type to return
      switch (this.args[1].toLowerCase()) {
        case "all":
          switch (this.args[2].toLowerCase()) {
            case "song":
              if (this.args.length >= 5 && this.args[3].equals("--orderby")) {
                this.showAllSongs(this.args[4]);
              } else {
                this.showAllSongs("title");
              }
              break;
            case "artist":
              this.showAllArtist();
              break;
            case "label":
              this.showAllLabels();
              break;
            case "user":
              this.showAllUsers();
              break;
            default:
              System.out.println(this.args[2] + " is not a valid identifier for keyword \"all\"");
              return;
          }
          break;
        case "user":
          // check if a username is given.
          if (this.args.length != 4) {
            System.out.println("Need 4 arguments to display information about a particular user");
          } else {
            switch (this.args[2]) {
              case "song":
                this.showUserLikedSongs(this.args[3]);
              case "playlist":
                // TODO: add ability to display user playlists
                this.showUserPlaylists(this.args[3]);
            }
          }
          break;
        case "artist": // TODO: finish functionality for reading artist data
          break;
        default:
          System.out.println(this.args[1] + " is not a valid second argument");
      }
    } catch (SQLException e) {
      System.out.println("Exception occurred when reading from database.");
      e.printStackTrace();
    }
  }

  /**
   * Shows all users in the database.
   */
  private void showAllUsers() throws SQLException {
    // prepare the procedure call
    String prepCall = "CALL music_final_project.read_all_users()";
    PreparedStatement showAllUsersStatement = conn.prepareStatement(prepCall);

    // execute the query;
    ResultSet usersRS = showAllUsersStatement.executeQuery();
    while (usersRS.next()) {
      StringBuilder toPrint = new StringBuilder();
      toPrint.append("ID: ");
      toPrint.append(usersRS.getInt("listener_id"));
      toPrint.append(", Username: ");
      toPrint.append(usersRS.getString("listener_username"));
      System.out.println(toPrint.toString());
    }
  }

  /**
   * Shows all the labels.
   */
  private void showAllLabels() throws SQLException {
    String prepCall = "CALL music_final_project.read_all_labels()";
    PreparedStatement showAllLabelStatement = conn.prepareStatement(prepCall);

    ResultSet labels = showAllLabelStatement.executeQuery();
    while(labels.next()) {
      StringBuilder toPrint = new StringBuilder();
      toPrint.append("ID: ");
      toPrint.append(labels.getString("label_id"));
      toPrint.append(", Label Name: ");
      toPrint.append(labels.getString("label_name"));
      System.out.println(toPrint.toString());
    }
  }

  /**
   * Displays all artist information.
   */
  private void showAllArtist() throws SQLException {
    String prepCall = "CALL music_final_project.read_all_artists()";
    PreparedStatement showAllArtistStatement = conn.prepareStatement(prepCall);

    ResultSet artists = showAllArtistStatement.executeQuery();
    while(artists.next()) {
      StringBuilder toPrint = new StringBuilder();
      toPrint.append("ID: ");
      toPrint.append(artists.getInt("artist_id"));
      toPrint.append(", Name: ");
      toPrint.append(artists.getString("artist_name"));
      toPrint.append(", Label: ");
      int labelid = artists.getInt("artist_label");
      toPrint.append(this.getLabelName(labelid, conn));
      System.out.println(toPrint.toString());
    }
    // read_all_artists
  }

  /**
   * Displays all the songs in the database.
   * @param order the desired ordering of the song, default is by title.
   */
  private void showAllSongs(String order) throws SQLException {
    String prepCall = "CALL read_songs_by_title()";
    switch (order) {
      case "artist":
        prepCall = "CALL read_songs_by_artist()";
        break;
      case "genre":
        prepCall = "CALL read_songs_by_genre()";
        break;
      case "length":
        prepCall = "CALL read_songs_by_length()";
        break;
      case "rating":
        prepCall = "CALL read_songs_by_rating()";
        break;
      default:
        break;
    }
    PreparedStatement readAllSongStatement = conn.prepareStatement(prepCall);

    ResultSet songs = readAllSongStatement.executeQuery();

    while (songs.next()) {
      StringBuilder toPrint = new StringBuilder();
      toPrint.append("Title: ").append(songs.getString("song_title"));
      toPrint.append(", Artist: ").append(songs.getString("song_artist"));
      toPrint.append(", Genre: ").append(songs.getString("song_genre"));
      toPrint.append(", Length: ").append(songs.getInt("song_length"));
      toPrint.append(", Peak Rating: ").append(songs.getString("song_peaked_rating"));
      System.out.println(toPrint.toString());
    }
    // read_all_songs
  }

  /**
   * Returns all of a users songs
   * @param user the username
   */
  private void showUserPlaylists(String user) {

  }

  /**
   * Displays all the liked songs of the given user.
   * @param user the username of the user to see the songs.
   */
  private void showUserLikedSongs(String user) throws SQLException {
    int userId = this.getUserId(user);
    if (userId == -1) {
      System.out.println(user + " is not a valid user in the database. Please enter a valid " +
              "username");
      return;
    }

    String prepCall = "CALL read_users_liked(?)";
    PreparedStatement usersLikedSongsStatement = conn.prepareStatement(prepCall);
    usersLikedSongsStatement.clearParameters();
    usersLikedSongsStatement.setInt(1, userId);

    ResultSet likedSongs = usersLikedSongsStatement.executeQuery();
    while (likedSongs.next()) {
      StringBuilder toPrint = new StringBuilder();
      toPrint.append("Title: ").append(likedSongs.getString("song_title"));
      toPrint.append(", Artist: ").append(likedSongs.getString("song_artist"));
      toPrint.append(", Genre: ").append(likedSongs.getString("song_genre"));
      toPrint.append(", Length: ").append(likedSongs.getInt("song_length"));
      toPrint.append(", Peak Rating: ").append(likedSongs.getString("song_peaked_rating"));
      System.out.println(toPrint.toString());
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
   * Gets a label name from a label id.
   * @param id the label id
   * @return the label name
   */
  private String getLabelName(int id, Connection conn) throws SQLException {
    String prepCall = "CALL get_label_name(?)";
    PreparedStatement getLabelNameStatement = conn.prepareStatement(prepCall);
    getLabelNameStatement.clearParameters();
    getLabelNameStatement.setInt(1, id);

    ResultSet idRS = getLabelNameStatement.executeQuery();
    if (idRS.next()) {
      return idRS.getString("label_name");
    }
    return null;
  }
}
