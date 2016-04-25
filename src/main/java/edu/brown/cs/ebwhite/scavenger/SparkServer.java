package edu.brown.cs.ebwhite.scavenger;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import spark.ModelAndView;
import spark.QueryParamsMap;
import spark.Request;
import spark.Response;
import spark.Route;
import spark.Spark;
import spark.TemplateViewRoute;
import spark.template.freemarker.FreeMarkerEngine;

import com.google.common.collect.ImmutableMap;
import com.google.gson.Gson;

import edu.brown.cs.ebwhite.database.TurtleQuery;
import edu.brown.cs.ebwhite.friends.Friend;
import edu.brown.cs.ebwhite.geo.LatLong;
import edu.brown.cs.ebwhite.note.Note;

public class SparkServer {
  Gson GSON;

  public SparkServer(int port) {
    GSON = new Gson();
    Spark.setPort(port);
    Spark.externalStaticFileLocation("src/main/resources/static");

  }

  public void run(){
    Spark.get("/", new HomeHandler(), new FreeMarkerEngine());
    Spark.get("/home", new HomeHandler(), new FreeMarkerEngine());
    Spark.post("/getNotes", new GetNotes());
    Spark.post("/updateNotes", new UpdateNotes());
    Spark.post("/postNote", new PostNote());
    Spark.post("/newUser", new NewUser());
    Spark.post("/checkUsername", new CheckUsername());
    Spark.post("/addFriend", new AddFriend());
    Spark.post("/removeFriend", new RemoveFriend());
    Spark.post("/getFriends", new GetFriends());

  }

  private class HomeHandler implements TemplateViewRoute {
    @Override
    public ModelAndView handle(Request req, Response res) {
      Map<String, Object> variables = ImmutableMap.of("title", "trtl");
      return new ModelAndView(variables, "home.ftl");
    }
  }

  private class GetNotes implements Route {
    @Override
    public Object handle(final Request req, final Response res) {
      QueryParamsMap qm = req.queryMap();
      String uIDstring = qm.value("userID");
      String latString = qm.value("lat");
      String lonString = qm.value("lon");
      String timeString = qm.value("timestamp");
      String minPostString = qm.value("minPost");
      String maxPostString = qm.value("maxPost");
      String radiusString = qm.value("radius");
      String message = "no-error";
      List<Note> notes = new ArrayList<>();
      try {
        int uID = Integer.parseInt(uIDstring);
        double lat = Double.parseDouble(latString);
        double lon = Double.parseDouble(lonString);
        long timestamp = Long.parseLong(timeString);
        int minPost = Integer.parseInt(minPostString);
        int maxPost = Integer.parseInt(maxPostString);
        double radius = Double.parseDouble(radiusString);
        notes = TurtleQuery.getNotesAnonymous(new LatLong(lat, lon), radius,
            minPost, maxPost, timestamp);

      } catch (NullPointerException np) {
        message = "Fields not filled. Something is null: " + np.getMessage();
      } catch (NumberFormatException nfe) {
        message = "Number Format Exception: " + nfe.getMessage();
      } catch (SQLException e) {
        // TODO Auto-generated catch block
        message = "SQL error when posting note: " + e.getMessage();
      }

      Map<String, Object> variables = new ImmutableMap.Builder()
          .put("notes", notes).put("error", message).build();

      return GSON.toJson(variables);
    }
  }

  private class UpdateNotes implements Route {
    @Override
    public Object handle(final Request req, final Response res) {
      QueryParamsMap qm = req.queryMap();
      String uIDstring = qm.value("userID");
      String latString = qm.value("lat");
      String lonString = qm.value("lon");
      String timeString = qm.value("timestamp");
      String minPostString = qm.value("minPost");
      String maxPostString = qm.value("maxPost");
      String radiusString = qm.value("radius");
      String message = "no-error";
      List<Note> notes = new ArrayList<>();
      try {
        int uID = Integer.parseInt(uIDstring);
        double lat = Double.parseDouble(latString);
        double lon = Double.parseDouble(lonString);
        long timestamp = Long.parseLong(timeString);
        int minPost = Integer.parseInt(minPostString);
        int maxPost = Integer.parseInt(maxPostString);
        double radius = Double.parseDouble(radiusString);

        notes = TurtleQuery.updateNotesAnonymous(new LatLong(lat, lon), radius,
            minPost, maxPost, timestamp);

      } catch (NullPointerException np) {
        message = "Fields not filled. smtn null.";
      } catch (NumberFormatException nfe) {
        message = "number format exception.";
      } catch (SQLException e) {
        // TODO Auto-generated catch block
        message = "SQL error.";
      }

      Map<String, Object> variables = new ImmutableMap.Builder()
          .put("notes", notes).put("error", message).build();

      return GSON.toJson(variables);
    }
  }

  private class PostNote implements Route {
    @Override
    public Object handle(final Request req, final Response res) {
      QueryParamsMap qm = req.queryMap();
      String uIDstring = qm.value("userID");
      String latString = qm.value("lat");
      String lonString = qm.value("lon");
      String timeString = qm.value("timestamp");
      String content = qm.value("text");
      String privacy = qm.value("private");
      String message = "no-error";

      try {
        int uID = Integer.parseInt(uIDstring);
        double lat = Double.parseDouble(latString);
        double lon = Double.parseDouble(lonString);
        long timestamp = Long.parseLong(timeString);
        int privacyVal = Integer.parseInt(privacy);
        TurtleQuery.postNote(uID, timestamp, lat, lon, content, privacyVal);

      } catch (NullPointerException np) {
        message = "Fields not filled. Something is null: " + np.getMessage();
      } catch (NumberFormatException nfe) {
        message = "Number Format Exception: " + nfe.getMessage();
      } catch (SQLException e) {
        // TODO Auto-generated catch block
        message = "SQL error when posting note: " + e.getMessage();
      }
      Map<String, Object> variables = new ImmutableMap.Builder().put("error",
          message).build();
      return GSON.toJson(variables);
    }
  }

  private class AddFriend implements Route {
    @Override
    public Object handle(final Request req, final Response res) {
      QueryParamsMap qm = req.queryMap();
      String userIDstring = qm.value("userID");
      String friendIDstring = qm.value("friendID");
      String message = "no-error";

      try {
        int userID = Integer.parseInt(userIDstring);
        int friendID = Integer.parseInt(friendIDstring);

        Friend.addFriend(userID, friendID);
      } catch (NullPointerException np) {
        message = "Fields not filled. smtn null.";
      } catch (NumberFormatException nfe) {
        message = "number format exception.";
      } catch (SQLException e) {
        message = "SQL error when adding friend.";
      }
      Map<String, Object> variables = new ImmutableMap.Builder().put("error",
          message).build();
      return GSON.toJson(variables);
    }
  }

  private class RemoveFriend implements Route {
    @Override
    public Object handle(final Request req, final Response res) {
      QueryParamsMap qm = req.queryMap();
      String userIDstring = qm.value("userID");
      String friendIDstring = qm.value("friendID");
      String message = "no-error";

      try {
        int userID = Integer.parseInt(userIDstring);
        int friendID = Integer.parseInt(friendIDstring);

        Friend.removeFriend(userID, friendID);
      } catch (NullPointerException np) {
        message = "Fields not filled. smtn null.";
      } catch (NumberFormatException nfe) {
        message = "number format exception.";
      } catch (SQLException e) {
        message = "SQL error when adding friend.";
      }
      Map<String, Object> variables = new ImmutableMap.Builder().put("error",
          message).build();

      return GSON.toJson(variables);
    }
  }

  private class GetFriends implements Route {
    @Override
    public Object handle(final Request req, final Response res) {
      QueryParamsMap qm = req.queryMap();
      String userIDstring = qm.value("userID");
      String message = "no-error";
      List<Integer> friends = new ArrayList<>();

      try {
        int userID = Integer.parseInt(userIDstring);

        friends = Friend.getFriends(userID);

      } catch (NullPointerException np) {
        message = "Fields not filled. smtn null.";
      } catch (NumberFormatException nfe) {
        message = "number format exception.";
      } catch (SQLException e) {
        message = "SQL error when adding friend.";
      }
      Map<String, Object> variables = new ImmutableMap.Builder()
          .put("friends", friends).put("error", message).build();

      return GSON.toJson(variables);
    }
  }

  private class NewUser implements Route {
    @Override
    public Object handle(final Request req, final Response res) {
      QueryParamsMap qm = req.queryMap();
      String firstname = qm.value("firstname");
      String lastname = qm.value("lastname");
      String username = qm.value("username");
      String password = qm.value("password");
      String email = qm.value("email");
      String phoneString = qm.value("phone");

      String message = "no-error";
      int userID = -1;
      int phone = -1;
      try {
        phone = Integer.parseInt(phoneString);

      } catch (NullPointerException np) {
        message = "Fields not filled. smtn null.";
      } catch (NumberFormatException nfe) {
        message = "number format exception for phone.";
      }
      if (firstname != null && username != null && password != null
          && email != null) {
        try {
          userID = TurtleQuery.addUser(username, password, firstname, lastname,
              email, phone);
          if (userID == -1) {
            message = "Failed to create new user.";
          }
        } catch (SQLException e) {
          // TODO Auto-generated catch block
          message = "SQL error.";
        }
      } else {
        message = "there is a null field being sent.";
      }

      Map<String, Object> variables = new ImmutableMap.Builder()
          .put("error", message).put("userID", userID).build();
      return GSON.toJson(variables);
    }
  }

  private class CheckUsername implements Route {
    @Override
    public Object handle(final Request req, final Response res) {
      QueryParamsMap qm = req.queryMap();
      String username = qm.value("username");

      String message = "no-error";
      boolean exists = false;

      try {
        int userID = TurtleQuery.getUserID(username);
        if (userID != -1) {
          exists = true;
        }
      } catch (SQLException e) {
        message = "SQL error.";
      }

      Map<String, Object> variables = new ImmutableMap.Builder().put("error",
          message).put("exists", exists).build();
      return GSON.toJson(variables);
    }
  }

}
