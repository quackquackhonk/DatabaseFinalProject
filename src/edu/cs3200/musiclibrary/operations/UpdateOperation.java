package edu.cs3200.musiclibrary.operations;

import java.sql.Connection;

public class UpdateOperation implements MusicLibraryOperation {

  private String cmd;
  private Connection conn;

  public UpdateOperation(String command, Connection conn) {
    this.cmd = command;
    this.conn = conn;
  }

  @Override
  public void run() {

  }
}
