package edu.cs3200.musiclibrary.operations;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class CreateOperation extends AbstractOperation implements MusicLibraryOperation {


  /**
   * Constructs an AbstractOperation.
   *
   * @param command the command
   * @param conn    the connection to the DB.
   * @param scan    the user input scanner.
   */
  public CreateOperation(String command, Connection conn, Scanner scan) {
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
          this.createSong();
          break;
        case "album":
          this.createAlbum();
          break;
        case "artist":
          this.createArtist();
          break;
        case "label":
          this.createLabel();
          break;
        case "user":
          this.createUser();
          break;
        default:
          System.out.println(this.args[1] + " is not a valid item to add to the database");
      }
    } catch (SQLException e) {
      System.out.println("Exception occurred when interacting with database.");
      e.printStackTrace();
    }
  }

  private void createSong() throws SQLException {
    System.out.println("Enter the new song title:");
    System.out.print("$> ");
    String title = scan.nextLine();
    System.out.println("Enter the new artist name:");
    System.out.print("$> ");
    String artist = scan.nextLine();
    String genre = "";
    int length = -1;
    int rating = -1;
    if (songExists(title, artist)) {
      System.out.println("This song is already in the database");
      this.createSong();
    } else {
      System.out.println("Enter " + title + "\'s genre:");
      System.out.print("$> ");
      genre = scan.nextLine();
      System.out.println("Enter " + title + "\'s length (in seconds):");
      System.out.print("$> ");
      String len = scan.nextLine();
      while(!this.isNumber(len)) {
        System.out.println(len + " is not a valid length");
        System.out.println("Enter " + title + "\'s length (in seconds)");
        len = scan.nextLine();
      }
      length = Integer.parseInt(len);
      System.out.println("Enter " + title + "\'s peak rating:");
      System.out.print("$> ");
      String rat = scan.nextLine();
      while(!this.isNumber(rat)) {
        System.out.println(rat + " is not a valid rating");
        System.out.println("Enter " + title + "\'s peak rating:");
        rat = scan.nextLine();
      }
      rating = Integer.parseInt(rat);
    }
    String prepCall = "CALL add_song(?,?,?,?,?)";
    PreparedStatement addSong = conn.prepareStatement(prepCall);
    addSong.clearParameters();
    addSong.setString(1, title);
    addSong.setString(2, artist);
    addSong.setString(3, genre);
    addSong.setInt(4, length);
    addSong.setInt(5, rating);
    addSong.executeQuery();
  }

  private void createAlbum() throws SQLException {
    System.out.println("Enter the new album name:");
    System.out.print("$> ");
    String name = scan.nextLine();
    System.out.println("Enter the artist who made the album:");
    System.out.print("$> ");
    String artist = scan.nextLine();
    String prepCall = "CALL add_album(?,?)";
    PreparedStatement addAlbum = conn.prepareStatement(prepCall);
    addAlbum.clearParameters();
    addAlbum.setString(1, name);
    addAlbum.setString(2, artist);
    addAlbum.executeQuery();
    System.out.println("Successfully added new album: " + name + " by " + artist);
    //System.out.println("Use the \'add\' command to add songs to this album");
  }

  private void createArtist() throws SQLException {
    System.out.println("Enter the new artist's name:");
    System.out.print("$> ");
    String name = scan.nextLine();
    String prepCall = "CALL add_artist(?)";
    PreparedStatement addArtist = conn.prepareStatement(prepCall);
    addArtist.clearParameters();
    addArtist.setString(1, name);
    addArtist.executeQuery();
    System.out.println("Added " + name + " to the database");
  }

  private void createLabel() throws SQLException {
    System.out.println("Enter the new labels's name:");
    System.out.print("$> ");
    String name = scan.nextLine();
    String prepCall = "CALL add_label(?)";
    PreparedStatement addLabel = conn.prepareStatement(prepCall);
    addLabel.clearParameters();
    addLabel.setString(1, name);
    addLabel.executeQuery();
    System.out.println("Added " + name + " to the database");
  }

  private void createUser() throws SQLException {
    System.out.println("Please enter a new username for the new user: ");
    System.out.print("$> ");
    String un = this.scan.nextLine();
    if (this.getUserId(un) != -1) {
      System.out.println("That username is taken.");
      this.createUser();
    } else {
      String prepCall = "CALL add_user(?)";
      PreparedStatement addUserStatement = conn.prepareStatement(prepCall);
      addUserStatement.clearParameters();
      addUserStatement.setString(1, un);
      addUserStatement.executeQuery();
      System.out.println("Added " + un + " to the database.");
    }
  }

  /**
   * Checks if the given string is a number.
   * @param check the string to check.
   * @return is the string a number?
   */
  private boolean isNumber(String check) {
    try {
      Integer.parseInt(check);
    } catch (NumberFormatException e) {
      return false;
    }
    return true;
  }
}
