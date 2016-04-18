	
	public class TurtleQuery {
		//ask rohan or look up autoincrement and how it works with.
	String userID;
	int time;
	double lat;
	double lng;
	double coslat = Math.cos(deg2rad(lat));
	double sinlat = Math.sin(deg2rad(lat));
	double coslng;
	double sinlng;
	String text;
	
	   String post = "INSERT INTO notes VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
	   //- UserID, TIMESTAMP, Lat, Lon, coslat, sinlat, coslng, sinlng, Text
	   
	   
	   //how to do radius distance with lat /lon
	   //calc this out later
	double cos_allowed_distance = 1;
	   String getNotes = "SELECT * FROM notes WHERE CUR_sin_lat * sin_lat + CUR_cos_lat * cos_lat * (cos_lng* CUR_cos_lng + sin_lng * CUR_sin_lng) > cos_allowed_distance;";
	double inputLat;
	double inputLng;
	double CUR_sin_lat = Math.sin(deg2rad(inputLat));
	double CUR_cos_lat = Math.cos(deg2rad(inputLat));
	double CUR_sin_lng = Math.sin(deg2rad(inputLng));
	double CUR_cos_lng = Math.cos(deg2rad(inputLng));
	String someting =    "";
				   
	   public static double deg2rad(double deg) {
		    return (deg * Math.PI / 180.0);
	   }
	   
	   
	}
