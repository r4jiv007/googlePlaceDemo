package rajiv.imena.demo.utils;

import rajiv.imena.demo.app.MyApp;

public class ApiHelper {
 
	public static final String LOCATION="location";
	public static final String KEY="key";
	public static final String RADIUS="radius";
	public static final String PLACEID="placeid";
	public static final String PHOTOREFERENCE="photoreference";
	public static final String MAXHEIGHT="maxheight";
	public static final String MAXWIDTH="maxwidth";
	
	public static final String PLACE_NEARBY_API="https://maps.googleapis.com/maps/api/place/nearbysearch/json?";
	public static final String PLACE_DETAIL_API="https://maps.googleapis.com/maps/api/place/details/json?";
	public static final String PLACE_PHOTO_API="https://maps.googleapis.com/maps/api/place/photo?";
	
	public static String getPlaceNearByApiUrl(String location,int radius){
		String urlString = PLACE_NEARBY_API+LOCATION+"="+location+"&"+RADIUS+"="+radius+"&"+KEY+"="+MyApp.APPLICATION_KEY;
		return urlString;
	}
	
	public static String getPlaceDetailApiUrl(String placeid){
		String urlString = PLACE_DETAIL_API+PLACEID+"="+placeid+"&"+KEY+"="+MyApp.APPLICATION_KEY;
		return urlString;
	}
	
	public static String getPlacePhotoApiUrl(String photoreference,int maxwidth){
		String urlString = PLACE_PHOTO_API+MAXWIDTH+"="+maxwidth+"&"+PHOTOREFERENCE+"="+photoreference+"&"+KEY+"="+MyApp.APPLICATION_KEY;
		return urlString;
	}
	
}