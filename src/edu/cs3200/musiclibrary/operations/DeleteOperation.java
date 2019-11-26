package edu.cs3200.musiclibrary.operations;

import java.sql.Connection;

public class DeleteOperation implements MusicLibraryOperation {

  private String cmd;
  private Connection conn;

  public DeleteOperation(String command, Connection conn) {
    this.cmd = command;
    this.conn = conn;
  }

  @Override
  public void run() {
    // deleting users and songs, but songs is useless.

  }
}
