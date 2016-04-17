package edu.brown.cs.ebwhite.scavenger;

import java.util.List;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.Pattern;
import java.util.Map;

import java.io.File;
import java.io.FileNotFoundException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import joptsimple.OptionParser;
import joptsimple.OptionSet;
import joptsimple.OptionSpec;

public class Main {
  private Integer port;

  public static void main(String[] args) {
    new Main(args).run();
  }

  private String[] args;
  private Main(String[] args) {
    this.args = args;
  }

  private void run() {
    OptionParser parser = new OptionParser();
    parser.accepts("gui");
    parser.accepts("port").withRequiredArg().ofType(Integer.class);
    OptionSpec<String> stringSpec = parser.nonOptions().ofType(String.class);
    OptionSet options = parser.parse(args);

    /* List<String> input = options.valuesOf(stringSpec); */

    if(options.has("port")){
      port = (Integer) options.valueOf("port");
    }
    else{
      port = 2456;
    }

    if(options.has("gui")){
/*      if(input.size() != 1){
        System.out.println("ERROR: Wrong number of arguments");
        System.exit(1);
      } */
      /* Start spark, etc. */
      SparkServer s = new SparkServer(port);
      s.run();
    }
    else{
      return;
    }
  }

}
