package edu.cs3200.musiclibrary.operations;

import java.sql.Connection;
import java.util.Scanner;

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
   * @param command the command
   * @param conn the connection to the DB.
   * @param scan the user input scanner.
   */
  public AbstractOperation(String command, Connection conn, Scanner scan) {
    this.command = command;
    this.conn = conn;
    this.args = command.split(" ");
    this.scan = scan;
  }
}
