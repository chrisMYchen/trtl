package edu.brown.cs.ebwhite.scavenger;

import freemarker.template.Configuration;
import spark.ModelAndView;
import spark.QueryParamsMap;
import spark.Request;
import spark.Response;
import spark.Route;
import spark.Spark;
import spark.TemplateViewRoute;
import spark.template.freemarker.FreeMarkerEngine;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.Set;
import java.util.Scanner;
import java.util.regex.Pattern;

import java.io.File;
import java.io.FileNotFoundException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.google.common.collect.ImmutableMap;
import com.google.gson.Gson;


public class SparkServer{
  Gson GSON;

  public SparkServer(int port){
    GSON = new Gson();
    Spark.setPort(port);
    Spark.externalStaticFileLocation("src/main/resources/static");
  }

  public void run(){
    Spark.get("/", new HomeHandler(), new FreeMarkerEngine());
    Spark.get("/home", new HomeHandler(), new FreeMarkerEngine());
  }

  private class HomeHandler implements TemplateViewRoute {
    @Override
    public ModelAndView handle(Request req, Response res) {
      Map<String, Object> variables = ImmutableMap.of(
      "title", "trtl"
      );
      return new ModelAndView(variables, "home.ftl");
    }
  }
}
