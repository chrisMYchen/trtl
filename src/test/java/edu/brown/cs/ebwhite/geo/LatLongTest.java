package edu.brown.cs.ebwhite.geo;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class LatLongTest {
  @BeforeClass
  public static void setUpClass() throws Exception {
    // (Optional) Code to run before any tests begin goes here.
  }

  @AfterClass
  public static void tearDownClass() throws Exception {
    // (Optional) Code to run after all tests finish goes here.
  }

  @Before
  public void setUp() {
    // (Optional) Code to run before each test case goes here.
  }

  @After
  public void tearDown() {
    // (Optional) CgedInTestode to run after each test case goes here.
  }

  @Test
  public void latLongTest() {
    LatLong ll = new LatLong(41.3, 27.8);
    
    assert (ll.getLat() == 41.3);
    assert (ll.getLng() == 27.8);
  }
}
