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

  public void run() {
    Spark.get("/home", new HomeHandler(), new FreeMarkerEngine());
    Spark.post("/getNotes", new GetNotes());
    Spark.post("/updateNotes", new UpdateNotes());
    Spark.post("/postNote", new PostNote());
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
      String message = "";
      List<Note> notes = new ArrayList<>();
      try {
        int uID = Integer.parseInt(uIDstring);
        double lat = Double.parseDouble(latString);
        double lon = Double.parseDouble(lonString);
        int timestamp = Integer.parseInt(timeString);
        int minPost = Integer.parseInt(minPostString);
        int maxPost = Integer.parseInt(maxPostString);
        double radius = Double.parseDouble(radiusString);
        notes = TurtleQuery.getNotes(new LatLong(lat, lon), radius, minPost,
            maxPost, timestamp);

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
      String message = "";
      List<Note> notes = new ArrayList<>();
      try {
        int uID = Integer.parseInt(uIDstring);
        double lat = Double.parseDouble(latString);
        double lon = Double.parseDouble(lonString);
        int timestamp = Integer.parseInt(timeString);
        int minPost = Integer.parseInt(minPostString);
        int maxPost = Integer.parseInt(maxPostString);
        double radius = Double.parseDouble(radiusString);
        notes = TurtleQuery.updateNotes(new LatLong(lat, lon), radius, minPost,
            maxPost, timestamp);

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
      String message = "";

      try {
        int uID = Integer.parseInt(uIDstring);
        double lat = Double.parseDouble(latString);
        double lon = Double.parseDouble(lonString);
        int timestamp = Integer.parseInt(timeString);

        TurtleQuery.postNote(uID, timestamp, lat, lon, content);

      } catch (NullPointerException np) {
        message = "Fields not filled. smtn null.";
      } catch (NumberFormatException nfe) {
        message = "number format exception.";
      } catch (SQLException e) {
        // TODO Auto-generated catch block
        message = "SQL error when posting note.";
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
      String message = "";

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
      String message = "";

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
      String message = "";
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

}
