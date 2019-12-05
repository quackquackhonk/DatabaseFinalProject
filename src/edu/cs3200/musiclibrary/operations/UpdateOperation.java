package edu.cs3200.musiclibrary.operations;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Scanner;

public class UpdateOperation extends AbstractOperation implements MusicLibraryOperation {


  /**
   * Constructs an UpdateOperation.
   *
   * @param command the command
   * @param conn    the connection to the DB.
   * @param scan    the user input scanner.
   */
  public UpdateOperation(String command, Connection conn, Scanner scan) {
    super(command, conn, scan);
    this.args = this.getCommandArgs(this.command);
  }

  @Override
  public void run() {
    try {
      if (this.args.length < 5) {
        System.out.println("Update expects at least 4 arguments, found : " + (this.args.length - 1));
        return;
      }
      String name = this.removeQuotes(this.args[2]);
      String colName = this.removeQuotes(this.args[3]);
      String newValue = this.removeQuotes(this.args[4]);
      switch (this.args[1].toLowerCase()) {
        case "label":
          if (this.labelExists(name)) {
            this.updateLabel(name, colName, newValue);
          } else {
            System.out.println("Unable to update label: Label does not exist in the database");
          }
          break;
        case "album":
          if (this.args.length < 6) {
            System.out.println("Updating a album requires 5 arguments, found: "
                    + (this.args.length - 1));
            return;
          }
          String title = this.removeQuotes(this.args[2]);
          String artist = this.removeQuotes(this.args[3]);
          colName = this.removeQuotes(this.args[4]);
          newValue = this.removeQuotes(this.args[5]);
          if (!this.albumExists(title, artist)) {
            System.out.println("Unable to update album: Album does not exist in the database");
          } else {
            this.updateAlbum(title, artist, colName, newValue);
          }
          break;
        case "artist":
          if (this.artistExists(name)) {
            this.updateArtist(name, colName, newValue);
          } else {
            System.out.println("Unable to update artist: Artist does not exist in the database");
          }
          break;
        case "song":
          if (this.args.length < 6) {
            System.out.println("Updating a song requires 5 arguments, found: "
                    + (this.args.length - 1));
            return;
          }
          title = this.removeQuotes(this.args[2]);
          artist = this.removeQuotes(this.args[3]);
          colName = this.removeQuotes(this.args[4]);
          newValue = this.removeQuotes(this.args[5]);
          if (!this.songExists(title, artist)) {
            System.out.println("Unable to update song: Song does not exist in the database");
          } else {
            this.updateSong(title, artist, colName, newValue);
          }
          break;
        case "user":
          if (this.getUserId(name) == -1) {
            System.out.println("Unable to update user: User does not exist in the database");
          } else {
            this.updateUser(name, colName, newValue);
          }
          break;
        default:
          System.out.println("Cannot update database: " + this.args[1] + " is not a valid type of " +
                  "data to update");
      }
    } catch (SQLException e) {
      System.out.println("Error occured when updating database.");
      e.printStackTrace();
    }
  }

  /**
   * Updates the value of the label at the specified column.
   * @param name the name of the label to update.
   * @param attribute the column to update.
   * @param newValue the new value of the update.
   * @throws SQLException if an error occurs when updating the database.
   */
  private void updateLabel(String name, String attribute, String newValue) throws SQLException {
    String prepCall = "";
    switch (attribute) {
      case "name":
        prepCall = "CALL update_label_name(?,?)";
        break;
      default:
        System.out.println(attribute + " is not a valid attribute of the label table");
        return;
    }
    PreparedStatement updateLabelStatement = conn.prepareStatement(prepCall);
    updateLabelStatement.clearParameters();
    updateLabelStatement.setString(1, name);
    updateLabelStatement.setString(2, newValue);
    updateLabelStatement.executeQuery();
    System.out.println("Updated the " + attribute + " of " + name + " to " + newValue);
  }

  /**
   * Updates the album based on the attribute and the new Value
   * @param title the title of the album
   * @param artist the artist on the album
   * @param attribute the attribute to update
   * @param newValue the new value to give to the album
   * @throws SQLException if an error occurs when updating the database
   */
  private void updateAlbum(String title, String artist, String attribute, String newValue)
          throws SQLException {
    String prepCall = "";
    switch(attribute) {
      case "title":
        prepCall = "CALL update_album_name(?,?,?)";
        break;
      case "artist":
        prepCall = "CALL update_album_artist(?,?,?)";
        break;
      default:
        System.out.println(attribute + " is not a valid attribute of the album table");
    }
    PreparedStatement updateAlbum = conn.prepareStatement(prepCall);
    updateAlbum.clearParameters();
    updateAlbum.setString(1, title);
    updateAlbum.setString(2, artist);
    updateAlbum.setString(3, newValue);
    updateAlbum.executeQuery();
    System.out.println("Updated the " + attribute + " of " + title + " to " + newValue);
  }

  /**
   * Updates the attribute of the given artist to the new value
   * @param name the name of the artist
   * @param attribute the attribute to update
   * @param newValue the new value to give to the artist
   * @throws SQLException if an error occurs when updating the database
   */
  private void updateArtist(String name, String attribute, String newValue) throws SQLException {
    String prepCall = "";
    switch(attribute) {
      case "name":
        prepCall = "CALL update_artist_name(?,?)";
        break;
      case "label":
        prepCall = "CALL update_artist_label(?,?)";
        break;
      default:
        System.out.println(attribute + " is not a valid attribute of the artist table");
        return;
    }

    PreparedStatement updateArtist = conn.prepareStatement(prepCall);
    updateArtist.clearParameters();
    updateArtist.setString(1, name);
    updateArtist.setString(2, newValue);
    updateArtist.executeQuery();
    System.out.println("Updated the " + attribute + " of " + name + " to " + newValue);
  }

  /**
   * Updates a song based on the given attribute and new value
   * @param title the title of the song
   * @param artist the artist of the song
   * @param attribute the attribute to update
   * @param newValue the new value oft he specified attribute
   * @throws SQLException if an error occurs when updating the database.
   */
  private void updateSong(String title, String artist, String attribute, String newValue)
    throws SQLException {
    String prepCall = "";
    switch (attribute) {
      case "title":
        prepCall = "CALL update_song_title(?,?,?)";
        break;
      case "artist":
        prepCall = "CALL update_song_artist(?,?,?)";
        break;
      case "genre":
        prepCall = "CALL update_song_genre(?,?,?)";
        break;
      case "length":
        prepCall = "CALL update_song_length(?,?,?)";
        break;
      case "rating":
        prepCall = "Call update_song_rating(?,?,?)";
        break;
      default:
        System.out.println(attribute + " is not a valid attribute of the song table");
        return;
    }
    PreparedStatement updateSong = conn.prepareStatement(prepCall);
    updateSong.clearParameters();
    updateSong.setString(1, title);
    updateSong.setString(2, artist);
    if (attribute.equalsIgnoreCase("length") || attribute.equalsIgnoreCase("rating")) {
      if (this.isNumber(newValue)) {
        updateSong.setInt(3, Integer.parseInt(newValue));
      } else {
        System.out.println("New values for " + attribute + " must be numeric");
        return;
      }
    } else {
      updateSong.setString(3, newValue);
    }
    updateSong.executeQuery();
    System.out.println("Updated the " + attribute + " of " + title + " to " + newValue);
  }

  private void updateUser(String name, String attribute, String newValue) throws SQLException {
    String prepCall = "";
    switch (attribute) {
      case "name":
        prepCall = "CALL update_listener_name(?,?)";
      default:
        System.out.println(attribute + " is not a valid attribute of the user table");
    }
    PreparedStatement updateUser = conn.prepareStatement(prepCall);
    updateUser.clearParameters();
    updateUser.setString(1, name);
    updateUser.setString(2, newValue);
    updateUser.executeQuery();
    System.out.println("Updated the " + attribute + " of " + name + " to " + newValue);
  }
}
