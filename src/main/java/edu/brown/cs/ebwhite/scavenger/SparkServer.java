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

import com.amazonaws.AmazonClientException;
import com.amazonaws.auth.InstanceProfileCredentialsProvider;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.transfer.TransferManager;
import com.amazonaws.services.s3.transfer.Upload;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMap.Builder;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import edu.brown.cs.ebwhite.database.TurtleQuery;
import edu.brown.cs.ebwhite.friends.Friend;
import edu.brown.cs.ebwhite.geo.LatLong;
import edu.brown.cs.ebwhite.note.Note;
import edu.brown.cs.ebwhite.note.NoteRanker;
import edu.brown.cs.ebwhite.user.User;
import edu.brown.cs.ebwhite.user.UserProxy;
import edu.brown.cs.ebwhite.user.UserSerializer;

public class SparkServer {
  Gson GSON;
  String imagepath;
  boolean external;

  public SparkServer(int port, String keystore, String keypass) {
    GsonBuilder gb = new GsonBuilder();
    gb.registerTypeAdapter(User.class, new UserSerializer());
    GSON = gb.create();
    Spark.port(port);
    Spark.externalStaticFileLocation("src/main/resources/static");
    Spark.secure(keystore, keypass, null, null);
    imagepath = "https://s3-us-west-2.amazonaws.com/trtl-images/";
    external = true;
  }

  public SparkServer(int port) {
    GsonBuilder gb = new GsonBuilder();
    gb.registerTypeAdapter(User.class, new UserSerializer());
    GSON = gb.create();
    Spark.port(port);
    Spark.externalStaticFileLocation("src/main/resources/static");
    imagepath = "src/main/resources/static/";
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
    Spark.post("/removeFollower", new RemoveFollower());
    Spark.post("/userInfo", new UserInfo());
    Spark.post("/getUser", new GetUserInfoFromId());
    Spark.post("/myInfo", new MyInfo());
    Spark.post("/postNoteImage", "multipart/form-data", new PostNoteImage());
    Spark.post("/removeNote", new RemoveNote());
    Spark.post("/upvote", new Upvote());
    Spark.post("/removeUpvote", new RemoveUpvote());
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
      String username = qm.value("username");
      String latString = qm.value("lat");
      String lonString = qm.value("lon");
      String timeString = qm.value("timestamp");
      String minPostString = qm.value("minPost");
      String maxPostString = qm.value("maxPost");
      String radiusString = qm.value("radius");
      String filterString = qm.value("filter");
      String message = "no-error";
      List<Note> notes = new ArrayList<>();
      try {
        // userID is -1 when anonymous
        int uID = Integer.parseInt(uIDstring);
        int profileID = -1;
        if (username != null) {
          // to see a specific user's posts : get filter = 3 with username
          profileID = UserProxy.ofName(username).getId();
        }
        double lat = Double.parseDouble(latString);
        double lon = Double.parseDouble(lonString);
        long timestamp = Long.parseLong(timeString);
        int minPost = Integer.parseInt(minPostString);
        int maxPost = Integer.parseInt(maxPostString);
        int filter = Integer.parseInt(filterString);
        double radius = Double.parseDouble(radiusString);
        LatLong curr_loc = new LatLong(lat, lon);
        notes = TurtleQuery.getNotes(uID, curr_loc, radius, minPost, maxPost,
            timestamp, filter, profileID);

        NoteRanker noteRank = new NoteRanker();
        if (uID != -1) {
          noteRank.setCurrentUser(uID);
        }
        noteRank.setCurrentLocation(curr_loc);
        Collections.sort(notes, noteRank);
        notes = notes.subList(Math.min((notes.size()), minPost),
            Math.min(notes.size(), maxPost));

      } catch (NullPointerException np) {
        message = "Fields not filled. Something is null: " + np.getMessage();
        np.printStackTrace();
      } catch (NumberFormatException nfe) {
        message = "Number Format Exception: " + nfe.getMessage();
      } catch (SQLException e) {
        // TODO Auto-generated catch block
        message = "SQL error when getting note: " + e.getMessage();
      }
      // for (Note n : notes) {
      // System.out.println(n);
      // }
      Map<String, Object> variables = new ImmutableMap.Builder<String, Object>()
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
      String starttimeString = qm.value("start_time");
      String endtimeString = qm.value("end_time");
      String minPostString = qm.value("minPost");
      String maxPostString = qm.value("maxPost");
      String radiusString = qm.value("radius");
      String filterString = qm.value("filter");
      String message = "no-error";
      List<Note> notes = new ArrayList<>();
      try {
        // uID is -1 if not logged in
        int uID = Integer.parseInt(uIDstring);
        double lat = Double.parseDouble(latString);
        double lon = Double.parseDouble(lonString);
        long start_time = Long.parseLong(starttimeString);
        long end_time = Long.parseLong(endtimeString);
        int minPost = Integer.parseInt(minPostString);
        int maxPost = Integer.parseInt(maxPostString);
        double radius = Double.parseDouble(radiusString);
        int filter = Integer.parseInt(filterString);
        LatLong curr_loc = new LatLong(lat, lon);

        notes = TurtleQuery.updateNotes(uID, curr_loc, radius, minPost,
            maxPost, start_time, end_time, filter);
        NoteRanker noteRank = new NoteRanker();
        if (uID != -1) {
          noteRank.setCurrentUser(uID);
        }
        noteRank.setCurrentLocation(curr_loc);
        Collections.sort(notes, noteRank);


      } catch (NullPointerException np) {
        message = "Fields not filled. smtn null.";
      } catch (NumberFormatException nfe) {
        message = "number format exception.";
      } catch (SQLException e) {
        // TODO Auto-generated catch block
        message = "SQL error.";
      }

      Map<String, Object> variables = new ImmutableMap.Builder<String, Object>()
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

      if (content != null && !content.equals("")) {
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
      } else {
        message = "content is empty or null";
      }

      Map<String, Object> variables = new ImmutableMap.Builder<String, Object>()
          .put("error", message).build();
      return GSON.toJson(variables);
    }
  }

  private class PostNoteImage implements Route {
    @Override
    public Object handle(final Request req, final Response res) {
      long maxFileSize = 10000000;
      long maxRequestSize = 10000000;
      int fileSizeThreshold = 10000000;
      MultipartConfigElement multipartConfigElement = new MultipartConfigElement(
          "/tmp", maxFileSize, maxRequestSize, fileSizeThreshold);
      req.raw().setAttribute("org.eclipse.jetty.multipartConfig",
          multipartConfigElement);
      String message = "no-error";
      Part filePart;
      System.err.println(req.raw().getParameterMap());

      try {
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
        if (external) {
          try {
            String bucket = "trtl-images";
            String key = newImageID + ".jpg";

            TransferManager tm = new TransferManager(
                new InstanceProfileCredentialsProvider());
            ObjectMetadata meta = new ObjectMetadata();
            meta.setContentLength(filePart.getSize());
            meta.setContentType(filePart.getContentType());
            Upload upload = tm.upload(bucket, key, is, meta);
            upload.waitForCompletion();
          } catch (AmazonClientException | InterruptedException e) {
            message = "Unable to upload file, upload was aborted.";
            e.printStackTrace();
            TurtleQuery.removeNote(noteid, uID);
          }
        } else {
          BufferedImage image = ImageIO.read(is);
          File outputfile = new File(imagepath + newImageID + ".jpg");
          ImageIO.write(image, "jpg", outputfile);
        }

      } catch (IOException | ServletException e) {
        System.out.println("ERROR: WEIRD ERROR");
        e.printStackTrace();
      } catch (NullPointerException np) {
        message = "Fields not filled. Something is null: " + np.getMessage();
      } catch (NumberFormatException nfe) {
        message = "Number Format Exception: " + nfe.getMessage();
      } catch (SQLException e) {
        // TODO Auto-generated catch block
        message = "SQL error when posting note: " + e.getMessage();
      }

      Map<String, Object> variables = new ImmutableMap.Builder<String, Object>()
          .put("error", message).build();
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
        int friendID = TurtleQuery.getUserID(friendUsername);
        if(friendID == -1){
          message = "User with username " + friendUsername + " doesn't exist";
        } else {
          boolean success = Friend.requestFollow(userID, friendID);
          if(!success){
            message = "You are already following " +friendUsername;
          }
        }
      } catch (NullPointerException np) {
        message = "Fields not filled. smtn null.";
      } catch (NumberFormatException nfe) {
        message = "number format exception.";
      } catch (SQLException e) {
        message = "SQL error when adding friend.";
        if (e.getErrorCode() == 19) {
          message = "Your request to follow " + friendUsername
              + " is already pending!";
        }
      } catch (IllegalArgumentException i) {
        message = i.getMessage();
      }
      Map<String, Object> variables = new ImmutableMap.Builder<String, Object>()
          .put("error", message).build();
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
          message = "User with username " + friendUsername + " doesn't exist";
        }
      } catch (NullPointerException np) {
        message = "Fields not filled. smtn null.";
      } catch (NumberFormatException nfe) {
        message = "number format exception.";
      } catch (SQLException e) {
        message = "SQL error when adding friend.";
        if (e.getErrorCode() == 19) {
          message = "Your already follow " + friendUsername + "!";
        }
      }
      Map<String, Object> variables = new ImmutableMap.Builder<String, Object>()
          .put("error", message).build();
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
        int friendID = TurtleQuery.getUserID(friendUsername);
        if (friendID == -1) {
          message = "Friend with username doesn't exist";
        } else {
          Friend.unfollow(userID, friendID);
        }
      } catch (NullPointerException np) {
        message = "Fields not filled. smtn null.";
      } catch (NumberFormatException nfe) {
        message = "number format exception.";
      } catch (SQLException e) {
        message = "SQL error when adding friend.";
      }
      Map<String, Object> variables = new ImmutableMap.Builder<String, Object>()
          .put("error", message).build();

      return GSON.toJson(variables);
    }
  }

  private class RemoveFollower implements Route {
    @Override
    public Object handle(final Request req, final Response res) {
      QueryParamsMap qm = req.queryMap();
      String userIDstring = qm.value("userID");
      String friendUsername = qm.value("friendUsername");
      String message = "no-error";
      try {
        int userID = Integer.parseInt(userIDstring);
        int friendID = TurtleQuery.getUserID(friendUsername);
        if (friendID == -1) {
          message = "Friend with username doesn't exist";
        } else{
          Friend.removeFollower(userID, friendID);
        }

      } catch (NullPointerException np) {
        message = "Fields not filled. smtn null.";
      } catch (NumberFormatException nfe) {
        message = "number format exception.";
      } catch (SQLException e) {
        message = "SQL error when adding friend.";
      }
      Map<String, Object> variables = new ImmutableMap.Builder<String, Object>()
          .put("error", message).build();

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
            userID = TurtleQuery.addUser(username, password, firstname,
                lastname, email, phone);
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

      Map<String, Object> variables = new ImmutableMap.Builder<String, Object>()
          .put("error", message).put("userID", userID).build();
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

      Map<String, Object> variables = new ImmutableMap.Builder<String, Object>()
          .put("error", message).put("userID", uID).build();

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

      Map<String, Object> variables = new ImmutableMap.Builder<String, Object>()
          .put("error", message).put("exists", exists).build();
      return GSON.toJson(variables);
    }
  }

  private class UserInfo implements Route {
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

      Builder<String, Object> variables = new ImmutableMap.Builder<String, Object>()
          .put("error", message);

      if (user != null) {

        variables.put("firstname", user.getFirstName()).put("lastname",
            user.getLastName());
        /*
         * variables.put("firstname", user.getFirstName()).put("lastname",
         * user.getLastName());
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
    @Override
    public Object handle(final Request req, final Response res) {
      QueryParamsMap qm = req.queryMap();
      String userIDstring = qm.value("userID");

      String message = "no-error";
      User user = null;
      int userID = -1;

      try {
        userID = Integer.parseInt(userIDstring);
        user = new UserProxy(userID);
      } catch (NullPointerException np) {
        message = "Fields not filled. smtn null.";
      } catch (NumberFormatException nfe) {
        message = "number format exception.";
      }

      Builder<String, Object> variables = new ImmutableMap.Builder<String, Object>()
          .put("error", message);

      if (user != null && userID != -1) {
        variables.put("firstname", user.getFirstName())
            .put("lastname", user.getLastName()).put("email", user.getEmail())
            .put("username", user.getUsername());

        Set<String> followers = new HashSet<>();
        for (int f : user.getFollowers()) {
          User friend = new UserProxy(f);
          followers.add(friend.getUsername());
        }
        variables.put("followers", followers);

        Set<String> following = new HashSet<>();
        for (int f : user.getFollowing()) {
          User friend = new UserProxy(f);
          following.add(friend.getUsername());
        }
        variables.put("following", following);

        Set<String> pending = new HashSet<>();
        for (int f : user.getPending()) {
          User pend = new UserProxy(f);
          pending.add(pend.getUsername());
        }
        variables.put("pending_followers", pending);

        Set<String> pendingFollowing = new HashSet<>();
        for (int f : user.getPendingFollowing()) {
          User pendFoll = new UserProxy(f);
          pendingFollowing.add(pendFoll.getUsername());
        }
        variables.put("pending_following", pendingFollowing);
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

      Builder<String, Object> variables = new ImmutableMap.Builder<String, Object>()
          .put("error", message);
      if (user != null) {
        variables.put("username", user.getUsername());
      }

      Map<String, Object> map = variables.build();

      return GSON.toJson(map);
    }
  }

  private class RemoveNote implements Route {
    @Override
    public Object handle(final Request req, final Response res) {
      QueryParamsMap qm = req.queryMap();
      String noteIDString = qm.value("noteID");
      String userIDString = qm.value("userID");
      String message = "no-error";

      try {
        int noteID = Integer.parseInt(noteIDString);
        int userID = Integer.parseInt(userIDString);
        TurtleQuery.removeNote(noteID, userID);
      } catch (NumberFormatException nfe) {
        message = "noteID or userID not a number";
      } catch (SQLException e) {
        message = "SQL error in removing note from database: ";
        message += e.getMessage();
      }

      Builder<String, Object> variables = new ImmutableMap.Builder<String, Object>()
          .put("error", message);

      Map<String, Object> map = variables.build();

      return GSON.toJson(map);
    }
  }

  private class Upvote implements Route {
    @Override
    public Object handle(final Request req, final Response res) {
      QueryParamsMap qm = req.queryMap();
      String noteIDString = qm.value("noteID");
      String userIDString = qm.value("userID");
      String message = "no-error";

      try {
        int noteID = Integer.parseInt(noteIDString);
        int userID = Integer.parseInt(userIDString);
        TurtleQuery.upvote(noteID, userID);
      } catch (NumberFormatException nfe) {
        message = "noteID or userID not a number";
      } catch (SQLException e) {
        message = "SQL error in adding vote to database: ";
        message += e.getMessage();
      }

      Builder<String, Object> variables = new ImmutableMap.Builder<String, Object>()
          .put("error", message);

      Map<String, Object> map = variables.build();

      return GSON.toJson(map);
    }
  }

  private class RemoveUpvote implements Route {
    @Override
    public Object handle(final Request req, final Response res) {
      QueryParamsMap qm = req.queryMap();
      String noteIDString = qm.value("noteID");
      String userIDString = qm.value("userID");
      String message = "no-error";

      try {
        int noteID = Integer.parseInt(noteIDString);
        int userID = Integer.parseInt(userIDString);
        TurtleQuery.removeUpvote(noteID, userID);
      } catch (NumberFormatException nfe) {
        message = "noteID or userID not a number";
      } catch (SQLException e) {
        message = "SQL error in removing vote from database: ";
        message += e.getMessage();
      }

      Builder<String, Object> variables = new ImmutableMap.Builder<String, Object>()
          .put("error", message);

      Map<String, Object> map = variables.build();

      return GSON.toJson(map);
    }
  }

}
