package edu.brown.cs.ebwhite.geo;

/**
 * LatLong class that holds a latitude and longitude double value.
 * 
 * @author TRTL team
 *
 */
public class LatLong {
  /**
   * The latitude value.
   */
  double lat;
  /**
   * The longitude value.
   */
  double lng;

  /**
   * Constructor for LatLong.
   * @param latitude a double representing a latitude value
   * @param longitude a double representing a longitude value
   */
  public LatLong(double latitude, double longitude) {
    this.lat = latitude;
    this.lng = longitude;
  }

  /**
   * Getter for latitude value.
   * @return the latitude value
   */
  public double getLat() {
    return lat;
  }

  /**
   * Getter for longitude value.
   * @return the longitude value
   */
  public double getLng() {
    return lng;
  }

  @Override
  public String toString() {
    return "LatLong [lat=" + lat + ", lng=" + lng + "]";
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    long temp;
    temp = Double.doubleToLongBits(lat);
    result = prime * result + (int) (temp ^ (temp >>> 32));
    temp = Double.doubleToLongBits(lng);
    result = prime * result + (int) (temp ^ (temp >>> 32));
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    LatLong other = (LatLong) obj;
    if (Double.doubleToLongBits(lat) != Double.doubleToLongBits(other.lat))
      return false;
    if (Double.doubleToLongBits(lng) != Double.doubleToLongBits(other.lng))
      return false;
    return true;
  }

  /**
   * Calculates the distance between two LatLong values.
   * @param l1 a LatLong value
   * @param l2 another LatLong value
   * @return The distance between l1 and l2
   */
  public static double distanceLatLong(LatLong l1, LatLong l2) {
    return Math.sqrt(Math.pow((l1.getLat() - l2.getLat()), 2)
        + Math.pow((l1.getLng() - l2.getLng()), 2));
  }

}
