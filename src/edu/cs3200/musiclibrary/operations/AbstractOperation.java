package edu.cs3200.musiclibrary.operations;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Scanner;

import javax.xml.transform.Result;

/**
 * Abstract class, holding information for the songs.
 */
public abstract class AbstractOperation implements MusicLibraryOperation {

  protected String command;
  protected Connection conn;
  protected String[] args;
  protected Scanner scan;

  /**
   * Constructs an AbstractOperation.
   *
   * @param command the command
   * @param conn    the connection to the DB.
   * @param scan    the user input scanner.
   */
  public AbstractOperation(String command, Connection conn, Scanner scan) {
    this.command = command;
    this.conn = conn;
    this.args = command.split(" ");
    this.scan = scan;
  }

  /**
   * Gets the user id of the given user.
   *
   * @param user the username
   * @return the user id of the given user, -1 if invalid user.
   */
  protected int getUserId(String user) throws SQLException {
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
   *
   * @param title  the title
   * @param artist the artist
   * @return is the song in the database
   * @throws SQLException if something goes wrong
   */
  protected boolean songExists(String title, String artist) throws SQLException {
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

  /**
   * Checks if an artist with the given name exists in the database.
   *
   * @param name the name to check
   * @return is there an artist with that name?
   * @throws SQLException if something goes wrong with the database.
   */
  protected boolean artistExists(String name) throws SQLException {
    String prepCall = "SELECT * FROM artist WHERE artist_name = ?";
    PreparedStatement exist = conn.prepareStatement(prepCall);
    exist.clearParameters();
    exist.setString(1, name);
    ResultSet artists = exist.executeQuery();
    return artists.next();
  }

  /**
   * Checks if the label with the given name exists.
   *
   * @param name the name to check
   * @return is there a label with that name?
   * @throws SQLException if something goes wrong reading from the db.
   */
  protected boolean labelExists(String name) throws SQLException {
    String prepCall = "SELECT * FROM label";
    PreparedStatement labels = conn.prepareStatement(prepCall);
    ResultSet allLabels = labels.executeQuery();
    while (allLabels.next()) {
      if (allLabels.getString("label_name").equalsIgnoreCase(name)) {
        return true;
      }
    }
    return false;
  }

  /**
   * Determines if the given album exists
   * @param name the name of the album
   * @param artist the artist of the album
   * @return does the album exist?
   * @throws SQLException error happened when reading.
   */
  protected boolean albumExists(String name, String artist) throws SQLException {
    String prepCall = "SELECT * FROM album";
    PreparedStatement albumStatement = conn.prepareStatement(prepCall);
    ResultSet albums = albumStatement.executeQuery();

    while (albums.next()) {
      if (name.equalsIgnoreCase(albums.getString("album_name"))
              && artist.equalsIgnoreCase(albums.getString("album_artist"))) {
        return true;
      }
    }
    return false;
  }

  /**
   * Gets the command arguments based on the tags --title and --artist.
   *
   * @param command the command to process.
   */
  protected String[] getCommandArgs(String command) {
    ArrayList<String> ret = new ArrayList<>();
    Scanner reader = new Scanner(command);
    String regex = "[^\"\\s]+|\"(\\\\.|[^\\\\\"])*\"";
    while (reader.hasNext()) {
      ret.add(reader.findInLine(regex));
    }
    String[] arr = new String[ret.size()];
    return ret.toArray(arr);
  }

  /**
   * Removes quotes from the start and end of a given string.
   *
   * @param s the string to check
   * @return the new string
   */
  protected String removeQuotes(String s) {
    if (s.startsWith("\"")) {
      s = s.substring(1);
    }

    if (s.endsWith("\"")) {
      s = s.substring(0, s.length() - 1);
    }

    return s;
  }

  /**
   * Checks if the given string is a number.
   *
   * @param check the string to check.
   * @return is the string a number?
   */
  protected boolean isNumber(String check) {
    try {
      Integer.parseInt(check);
    } catch (NumberFormatException e) {
      return false;
    }
    return true;
  }
}
