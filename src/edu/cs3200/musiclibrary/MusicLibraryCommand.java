package edu.cs3200.musiclibrary;

import com.mysql.cj.x.protobuf.MysqlxDatatypes;

public enum MusicLibraryCommand {
  // TODO: colors would be fun
  ADD("add"), REMOVE("remove"), UPDATE("update"),
  CREATE("create"), DELETE("delete"), LIKE("like"),
  UNLIKE("unlike"), SHOW("show");

  private String cmd; // command line command for the particular enum

  /**
   * Constructor for this enum.
   * @param cmd the command for the enum.
   */
  MusicLibraryCommand(String cmd) {
    this.cmd = cmd;
  }

  /**
   * Get the command line input for this enum.
   * @return this.cmd
   */
  public String getCmd() {
    return this.cmd;
  }

  /**
   * determines if the given string is a valid command line command.
   * @param s the string to check.
   * @return is s a valid command line command.
   */
  public static boolean isCommand(String s) {
    s = s.toLowerCase();
    for (MusicLibraryCommand c : values()) {
      if (s.startsWith(c.getCmd())) {
        return true;
      }
    }
    return false;
  }

  /**
   * Returns the correct MusicLibraryCommand given the String prefix
   * @param pre the command-line prefix
   * @return a MusicLibraryCommand, null if the prefix is invalid.
   */
  public static MusicLibraryCommand commandFromPrefix(String pre) {
    for (MusicLibraryCommand c : values()) {
      if (c.getCmd().equalsIgnoreCase(pre)) {
        return c;
      }
    }

    return null;
  }

  public static String helpInformation() {
    StringBuilder out = new StringBuilder();
    for (MusicLibraryCommand c : values()) {
      out.append(getHelpInfo(c));
      out.append("\n");
    }
    return out.toString().trim();
  }

  private static String getHelpInfo(MusicLibraryCommand command) {
    switch (command) {
      case ADD:
        return "add [song|artist|album]: adds a tuple in the database corresponding to the " +
                "desired type";
      case REMOVE:
        return "remove [song|artist|album] <name>: removes the data item of the given type that " +
                "has the specified id";
      case UPDATE:
        return "update [song|artist|album] <id> <column_name> <new_value>: updates specified " +
                "field of the data item with the desired type and specified id";
      case CREATE:
        return "create [user|playlist] <args>: creates a user or a playlist with the specified " +
                "arguments";
      case DELETE:
        return "delete [user|playlist] <id>: deletes the user or playlist with the specified id " +
                "and updates the database accordingly";
      case LIKE:
        return "like <user> <song>: adds the given song to the user's liked songs.";
      case UNLIKE:
        return "unlike <user> <song>: unlikes the given song for the specified user.";
      case SHOW:
        return "show [all|user|artist] [song|playlist|album|label]: displays requested " +
                "information.";
      default:
        return "Not a valid command";
    }
  }
}

