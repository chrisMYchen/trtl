package edu.brown.cs.ebwhite.scavenger;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.imageio.ImageIO;
import javax.servlet.MultipartConfigElement;
import javax.servlet.ServletException;
import javax.servlet.http.Part;

import spark.ModelAndView;
import spark.QueryParamsMap;
import spark.Request;
import spark.Response;
import spark.Route;
import spark.Spark;
import spark.TemplateViewRoute;
import spark.template.freemarker.FreeMarkerEngine;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMap.Builder;
import com.google.gson.Gson;

import com.amazonaws.AmazonClientException;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.services.s3.transfer.TransferManager;
import com.amazonaws.services.s3.transfer.Upload;
import com.amazonaws.services.s3.model.ObjectMetadata;

import edu.brown.cs.ebwhite.database.TurtleQuery;
import edu.brown.cs.ebwhite.friends.Friend;
import edu.brown.cs.ebwhite.geo.LatLong;
import edu.brown.cs.ebwhite.note.Note;
import edu.brown.cs.ebwhite.note.NoteRanker;
import edu.brown.cs.ebwhite.user.User;
import edu.brown.cs.ebwhite.user.UserProxy;

public class SparkServer {
  Gson GSON;
  String imagepath;
  boolean external;

  public SparkServer(int port, String keystore, String keypass) {
    GSON = new Gson();
    Spark.port(port);
    Spark.externalStaticFileLocation("src/main/resources/static");
    Spark.setSecure(keystore, keypass, null, null);
    imagepath = "https://s3-us-west-2.amazonaws.com/trtl-images";
    external = true;
  }

  public SparkServer(int port) {
    GSON = new Gson();
    Spark.setPort(port);
    Spark.externalStaticFileLocation("src/main/resources/static");
    imagepath = "src/main/resources/static/images/";
    external = false;
  }

  public void run() {
    Spark.get("/", new HomeHandler(), new FreeMarkerEngine());
    Spark.get("/home", new HomeHandler(), new FreeMarkerEngine());
    Spark.post("/getNotes", new GetNotes());
    Spark.post("/updateNotes", new UpdateNotes());
    Spark.post("/postNote", new PostNote());
    Spark.post("/newUser", new NewUser());
    Spark.post("/login", new Login());
    Spark.post("/checkUsername", new CheckUsername());
    Spark.post("/acceptFollower", new AcceptFollower());
    Spark.post("/requestFollow", new RequestFollow());
    Spark.post("/unfollow", new Unfollow());
    Spark.post("/userInfo", new UserInfo());
    Spark.post("/getUser", new GetUserInfoFromId());
    Spark.post("/myInfo", new MyInfo());
    Spark.post("/postNoteImage", "multipart/form-data", new PostNoteImage());
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
      String message = "no-error";
      List<Note> notes = new ArrayList<>();
      try {
        // userID is -1 when anonymous
        int uID = Integer.parseInt(uIDstring);
        double lat = Double.parseDouble(latString);
        double lon = Double.parseDouble(lonString);
        long timestamp = Long.parseLong(timeString);
        int minPost = Integer.parseInt(minPostString);
        int maxPost = Integer.parseInt(maxPostString);
        double radius = Double.parseDouble(radiusString);
        notes = TurtleQuery.getNotes(uID, new LatLong(lat, lon),
        radius, minPost, maxPost, timestamp);
        if (uID != -1) {
          NoteRanker noteRank = new NoteRanker();
          noteRank.setCurrentUser(uID);
          Collections.sort(notes, noteRank);
        }

      } catch (NullPointerException np) {
        message = "Fields not filled. Something is null: "
        + np.getMessage();
      } catch (NumberFormatException nfe) {
        message = "Number Format Exception: " + nfe.getMessage();
      } catch (SQLException e) {
        // TODO Auto-generated catch block
        message = "SQL error when getting note: " + e.getMessage();
      }
      // for (Note n : notes) {
      // System.out.println(n)
      // }
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
      String message = "no-error";
      List<Note> notes = new ArrayList<>();
      try {
        // uID is -1 if not logged in
        int uID = Integer.parseInt(uIDstring);
        double lat = Double.parseDouble(latString);
        double lon = Double.parseDouble(lonString);
        long timestamp = Long.parseLong(timeString);
        int minPost = Integer.parseInt(minPostString);
        int maxPost = Integer.parseInt(maxPostString);
        double radius = Double.parseDouble(radiusString);

        notes = TurtleQuery.updateNotes(uID, new LatLong(lat, lon),
        radius, minPost, maxPost, timestamp);
        if (uID != -1) {
          NoteRanker noteRank = new NoteRanker();
          noteRank.setCurrentUser(uID);
          Collections.sort(notes, noteRank);
        }

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
      String privacy = qm.value("private");
      String message = "no-error";

      if (content != null && !content.equals("")) {
        try {
          int uID = Integer.parseInt(uIDstring);
          double lat = Double.parseDouble(latString);
          double lon = Double.parseDouble(lonString);
          long timestamp = Long.parseLong(timeString);
          int privacyVal = Integer.parseInt(privacy);
          TurtleQuery.postNote(uID, timestamp, lat, lon, content,
          privacyVal);

        } catch (NullPointerException np) {
          message = "Fields not filled. Something is null: "
          + np.getMessage();
        } catch (NumberFormatException nfe) {
          message = "Number Format Exception: " + nfe.getMessage();
        } catch (SQLException e) {
          // TODO Auto-generated catch block
          message = "SQL error when posting note: " + e.getMessage();
        }
      } else {
        message = "content is empty or null";
      }
      Map<String, Object> variables = new ImmutableMap.Builder().put(
      "error", message).build();
      return GSON.toJson(variables);
    }
  }

  private class PostNoteImage implements Route {
    @Override
    public Object handle(final Request req, final Response res) {
      MultipartConfigElement multipartConfigElement = new MultipartConfigElement(
      "/tmp");
      req.raw().setAttribute("org.eclipse.jetty.multipartConfig",
      multipartConfigElement);
      String message = "no-error";
      Part filePart;
      System.err.println(req.raw().getParameterMap());

      try{
        /* Get request data (different format for multipart) */
        filePart = req.raw().getPart("pic");
        String uIDstring = req.raw().getParameter("userID");
        String latString = req.raw().getParameter("lat");
        String lonString = req.raw().getParameter("lon");
        String timeString = req.raw().getParameter("timestamp");
        String content = req.raw().getParameter("text");
        String privacy = req.raw().getParameter("private");

        /* Parse request data */
        int uID = Integer.parseInt(uIDstring);
        double lat = Double.parseDouble(latString);
        double lon = Double.parseDouble(lonString);
        long timestamp = Long.parseLong(timeString);
        int privacyVal = Integer.parseInt(privacy);
        int noteid = TurtleQuery.postNote(uID, timestamp, lat, lon, content,
        privacyVal);

        /* Connect imageid to noteid */
        int newImageID = TurtleQuery.addImage(noteid, imagepath);
        String path = imagepath + newImageID + ".jpg";

        /* Try to write image to file */
        InputStream is = filePart.getInputStream();
        if(external){
          try {
            String bucket = "trtl-images";
            String key = newImageID + ".jpg";

            TransferManager tm = new TransferManager(new ProfileCredentialsProvider());
            ObjectMetadata meta = new ObjectMetadata();
            meta.setContentLength(filePart.getSize());
            meta.setContentType(filePart.getContentType());
            Upload upload = tm.upload(bucket, key, is, meta);
          	upload.waitForCompletion();
          } catch (AmazonClientException | InterruptedException e) {
          	message = "Unable to upload file, upload was aborted.";
          	e.printStackTrace();
          }
        } else{
          BufferedImage image = ImageIO.read(is);
          File outputfile = new File(imagepath + newImageID + ".jpg");
          ImageIO.write(image, "jpg", outputfile);
        }

        /* Set path for image (won't happen if write fails)*/
        TurtleQuery.setImagePath(newImageID, path);

      } catch (IOException | ServletException e) {
        System.out.println("ERROR: WEIRD ERROR");
        e.printStackTrace();
      } catch (NullPointerException np) {
        message = "Fields not filled. Something is null: "
        + np.getMessage();
      } catch (NumberFormatException nfe) {
        message = "Number Format Exception: " + nfe.getMessage();
      } catch (SQLException e) {
        // TODO Auto-generated catch block
        message = "SQL error when posting note: " + e.getMessage();
      }

      Map<String, Object> variables = new ImmutableMap.Builder().put(
      "error", message).build();
      return GSON.toJson(variables);
    }
  }

  private class RequestFollow implements Route {
    @Override
    public Object handle(final Request req, final Response res) {
      QueryParamsMap qm = req.queryMap();
      String userIDstring = qm.value("userID");
      String friendUsername = qm.value("friendUsername");
      String message = "no-error";

      try {
        int userID = Integer.parseInt(userIDstring);
        if (!Friend.requestFollow(userID, friendUsername)) {
          message = "User with username " + friendUsername
          + " doesn't exist";
        }
      } catch (NullPointerException np) {
        message = "Fields not filled. smtn null.";
      } catch (NumberFormatException nfe) {
        message = "number format exception.";
      } catch (SQLException e) {
        message = "SQL error when adding friend.";
        if (e.getErrorCode() == 19) {
          message = "You're request to follow " + friendUsername
          + " is already pending!";
        }
      }
      Map<String, Object> variables = new ImmutableMap.Builder().put(
      "error", message).build();
      return GSON.toJson(variables);
    }
  }

  private class AcceptFollower implements Route {
    @Override
    public Object handle(final Request req, final Response res) {
      QueryParamsMap qm = req.queryMap();
      String userIDstring = qm.value("userID");
      String friendUsername = qm.value("friendUsername");
      String message = "no-error";

      try {
        int userID = Integer.parseInt(userIDstring);
        if (!Friend.acceptPendingRequest(userID, friendUsername)) {
          message = "User with username " + friendUsername
          + " doesn't exist";
        }
      } catch (NullPointerException np) {
        message = "Fields not filled. smtn null.";
      } catch (NumberFormatException nfe) {
        message = "number format exception.";
      } catch (SQLException e) {
        message = "SQL error when adding friend.";
        if (e.getErrorCode() == 19) {
          message = "You're already follow " + friendUsername + "!";
        }
      }
      Map<String, Object> variables = new ImmutableMap.Builder().put(
      "error", message).build();
      return GSON.toJson(variables);
    }
  }

  private class Unfollow implements Route {
    @Override
    public Object handle(final Request req, final Response res) {
      QueryParamsMap qm = req.queryMap();
      String userIDstring = qm.value("userID");
      String friendUsername = qm.value("friendUsername");
      String message = "no-error";
      try {
        int userID = Integer.parseInt(userIDstring);
        if (Friend.unfollow(userID, friendUsername)) {
          message = "Friend with username doesn't exist";
        }
      } catch (NullPointerException np) {
        message = "Fields not filled. smtn null.";
      } catch (NumberFormatException nfe) {
        message = "number format exception.";
      } catch (SQLException e) {
        message = "SQL error when adding friend.";
      }
      Map<String, Object> variables = new ImmutableMap.Builder().put(
      "error", message).build();

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
      } catch (NullPointerException | NumberFormatException e) {
        phone = -1;
      }
      if (firstname != null && username != null && password != null
      && email != null) {
        try {
          userID = TurtleQuery.getUserID(username);
          if (userID == -1) {
            userID = TurtleQuery.addUser(username, password,
            firstname, lastname, email, phone);
          } else {
            message = "That username already exists. Cannot create account.";
          }
          if (userID == -1) {
            message = "Failed to create new user.";
          }
        } catch (SQLException e) {
          // TODO Auto-generated catch block
          message = "SQL error.";
          message += e.getMessage();
        }
      } else {
        message = "Please fill all required fields";
      }

      Map<String, Object> variables = new ImmutableMap.Builder().put(
      "error", message).put("userID", userID).build();
      return GSON.toJson(variables);
    }
  }

  private class Login implements Route {
    @Override
    public Object handle(final Request req, final Response res) {
      QueryParamsMap qm = req.queryMap();
      String username = qm.value("username");
      String password = qm.value("password");
      String message = "no-error";
      int uID = -1;
      try {
        uID = TurtleQuery.loginValid(username, password);
      } catch (SQLException e) {
        // TODO Auto-generated catch block
        message = e.getMessage();
      }
      if (uID == -1) {
        message = "Login failed : Invalid username password combination.";
      }

      Map<String, Object> variables = new ImmutableMap.Builder().put(
      "error", message).put("userID", uID).build();

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
        message += e.getMessage();
      }

      Map<String, Object> variables = new ImmutableMap.Builder().put(
      "error", message).put("exists", exists).build();
      return GSON.toJson(variables);
    }
  }

  private class UserInfo implements Route {
    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
    public Object handle(final Request req, final Response res) {
      QueryParamsMap qm = req.queryMap();
      String username = qm.value("username");

      String message = "no-error";
      User user = null;

      try {
        user = UserProxy.ofName(username);
      } catch (SQLException e) {
        message = "SQL error, username might be MIA in DB.";
        message += e.getMessage();
      }

      Builder variables = new ImmutableMap.Builder().put("error",
      message);

      if (user != null) {

        variables.put("firstname", user.getFirstName()).put(
        "lastname", user.getLastName());
        /*
        variables.put("firstname", user.getFirstName()).put("lastname",
        user.getLastName());
        */

        Set<String> followers = new HashSet<>();
        for (int f : user.getFollowers()) {
          User friend = new UserProxy(f);
          followers.add(friend.getUsername());
        }
        variables.put("followers", followers);
      }

      Map<String, Object> map = variables.build();

      return GSON.toJson(map);
    }
  }

  private class MyInfo implements Route {
    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
    public Object handle(final Request req, final Response res) {
      QueryParamsMap qm = req.queryMap();
      String userIDstring = qm.value("userID");

      String message = "no-error";
      User user = null;

      try {
        int userID = Integer.parseInt(userIDstring);
        user = new UserProxy(userID);
      } catch (NullPointerException np) {
        message = "Fields not filled. smtn null.";
      } catch (NumberFormatException nfe) {
        message = "number format exception.";
      }

      Builder variables = new ImmutableMap.Builder().put("error",
      message);

      if (user != null) {
        variables.put("firstname", user.getFirstName()).put(
        "lastname", user.getLastName()).put("email",
        user.getEmail()).put("username", user.getUsername());

        Set<String> followers = new HashSet<>();
        for (int f : user.getFollowers()) {
          User friend = new UserProxy(f);
          followers.add(friend.getUsername());
        }
        variables.put("followers", followers);

        Set<String> pending = new HashSet<>();
        for (int f : user.getPending()) {
          User pend = new UserProxy(f);
          pending.add(pend.getUsername());
        }
        variables.put("pending", pending);
      }

      Map<String, Object> map = variables.build();

      return GSON.toJson(map);
    }
  }

  private class GetUserInfoFromId implements Route {
    @Override
    public Object handle(final Request req, final Response res) {
      QueryParamsMap qm = req.queryMap();
      String userIDString = qm.value("userID");
      String message = "no-error";
      User user = null;
      try {
        int userID = Integer.parseInt(userIDString);
        user = new UserProxy(userID);
      } catch (NumberFormatException nfe) {
        message = "userID not a number";
      }

      Builder variables = new ImmutableMap.Builder().put("error",
      message);
      if (user != null) {
        variables.put("username", user.getUsername());
      }

      Map<String, Object> map = variables.build();

      return GSON.toJson(map);
    }
  }

}
