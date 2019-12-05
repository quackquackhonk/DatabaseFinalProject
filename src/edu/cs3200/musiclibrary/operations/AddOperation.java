package edu.cs3200.musiclibrary.operations;

import java.sql.Connection;
import java.util.Scanner;

public class AddOperation extends AbstractOperation implements MusicLibraryOperation {


  /**
   * Constructs an AddOperation.
   *
   * @param command the command
   * @param conn    the connection to the DB.
   * @param scan    the user input scanner.
   */
  public AddOperation(String command, Connection conn, Scanner scan) {
    super(command, conn, scan);
  }

  @Override
  public void run() {

  }
}
