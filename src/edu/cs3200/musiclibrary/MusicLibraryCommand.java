package edu.cs3200.musiclibrary;

import com.mysql.cj.x.protobuf.MysqlxDatatypes;

public enum MusicLibraryCommand {
  ADD, REMOVE, UPDATE, CREATE, DELETE, LIKE, UNLIKE;

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
        return "rmv [song|artist|album] <id>: removes the data item of the given type that has " +
                "the specified id";
      case UPDATE:
        return "upd [song|artist|album] <id> <column_name> <new_value>: updates specified field " +
                "of the data item with the desired type and specified id";
      case CREATE:
        return "crt [user|playlist] <args>: creates a user or a playlist with the specified " +
                "arguments";
      case DELETE:
        return "del [user|playlist] <id>: deletes the user or playlist with the specified id and " +
                "updates the database accordingly";
      case LIKE:
        return "like <user> <song>: adds the given song to the user's liked songs";
      case UNLIKE:
        return "unlike <user> <song>: unlikes the given song for the specified user.";
      default:
        return "Not a valid command";
    }
  }
}

