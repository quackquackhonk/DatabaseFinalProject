package edu.cs3200.musiclibrary;

public enum MusicLibraryCommand {
  // TODO: colors would be fun
  UPDATE("update"), CREATE("create"), DELETE("delete"),
  LIKE("like"), UNLIKE("unlike"), SHOW("show");

  private String cmd; // command line command for the particular enum

  /**
   * Constructor for this enum.
   *
   * @param cmd the command for the enum.
   */
  MusicLibraryCommand(String cmd) {
    this.cmd = cmd;
  }

  /**
   * Get the command line input for this enum.
   *
   * @return this.cmd
   */
  public String getCmd() {
    return this.cmd;
  }

  /**
   * determines if the given string is a valid command line command.
   *
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
   *
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
    out.append("IMPORTANT: Whenever entering a value that includes spaces, you must surround the " +
            "value with double quotes (\"\")\n\n");
    for (MusicLibraryCommand c : values()) {
      out.append(getHelpInfo(c));
      out.append("\n");
    }
    return out.toString().trim();
  }

  private static String getHelpInfo(MusicLibraryCommand command) {
    StringBuilder out;
    switch (command) {
      case UPDATE:
        out = new StringBuilder();
        out.append("KEYWORD: update\n");
        out.append("Updates the desired data entry based on the attribute you would like to " +
                "change. Command Usages are as follows:\n");
        out.append("update song <title> <artist> [title|artist|genre|length|ranking] <new " +
                "value>\n");
        out.append("update album <title> <artist> [title|artist] <new value>\n");
        out.append("update artist <name> [name|label] <new value>\n");
        out.append("update label <name> [name] <new value>\n");
        out.append("update user <username> [name] <new value>\n");
        return out.toString();
      case CREATE:
        out = new StringBuilder();
        out.append("KEYWORD: create\n");
        out.append("Allows for the creation of data entries of the desired type. Usage:\n");
        out.append("create [song|artist|album|label|user]\n");
        return out.toString();
      case DELETE:
        out = new StringBuilder();
        out.append("KEYWORD: delete\n");
        out.append("Allows for the deletion of data entries of the desired type. Usage:\n");
        out.append("delete [song|artist|album|label|user]\n");
        return out.toString();
      case LIKE:
        out = new StringBuilder();
        out.append("KEYWORD: like\n");
        out.append("Allows for the \'liking\' of a song for a specific user. Usage:\n");
        out.append("like <user name> <song title> <song artist>\n");
        return out.toString();
      case UNLIKE:
        out = new StringBuilder();
        out.append("KEYWORD: unlike\n");
        out.append("Allows for the \'unliking\' of a song for a specific user. Usage:\n");
        out.append("unlike <user name> <song title> <song artist>\n");
        return out.toString();
      case SHOW:
        out = new StringBuilder();
        out.append("KEYWORD: show\n");
        out.append("Method to read data from the database. Usage:\n");
        out.append("show all [song|artist|label|user|album]: shows all of the desired item in the" +
                " database\n");
        out.append("show user song: shows all of a users liked songs\n");
        out.append("show artist [song|album]: shows all of the desired types for an artist\n");
        out.append("NOTE: only for the command \'show all song\' the optional \'--orderby\' tag " +
                "exists.\nFollowing the tag with one of [title|artist|genre|length|rating] will " +
                "sort the results by the desired attribute.\nDefault sorting is by title.");
        return out.toString();
      default:
        return "Not a valid command";
    }
  }
}

