package edu.brown.cs.ebwhite.scavenger;

import geo.LatLong;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import note.Note;
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

import database.TurtleQuery;

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
  }

  private class HomeHandler implements TemplateViewRoute {
    @Override
    public ModelAndView handle(Request req, Response res) {
      Map<String, Object> variables = ImmutableMap
          .of("title", "trtl");
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
      Map<String, Object> variables = new ImmutableMap.Builder().put(
          "notes", notes).put("error", message).build();
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
      Map<String, Object> variables = new ImmutableMap.Builder().put(
          "notes", notes).put("error", message).build();
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
      Map<String, Object> variables = new ImmutableMap.Builder().put("error", message).build();
      return GSON.toJson(variables);
    }
  }

}
