/*
 * 
 *  this class is meant to parse the json response from server
 *  date created :- 30 Jan 2014
 *  Last Edited :- 30 Jan 2014 
 * 
 * @author :- rajiv singh
 * 
 * 
 * 
 */
package rajiv.imena.demo.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import rajiv.imena.demo.app.MyApp;
import rajiv.imena.demo.models.MyLocation;
import rajiv.imena.demo.models.MyPhoto;
import rajiv.imena.demo.models.MyPlace;
import android.util.Log;


public class HttpCall {

	private static JSONArray jArray;
	private static JSONObject jObj;

	public static JSONObject doGet(String string, boolean docaching)
			throws IOException, JSONException {
		Log.i("Tag doGet", "making HTTP GET req");
		Log.i("Url :", string);
		// enableHttpCaching(context);
		URL url = new URL(string);
		HttpURLConnection con = (HttpURLConnection) url.openConnection();
		con.setUseCaches(docaching);
		con.setRequestMethod("GET");
		InputStream is = con.getInputStream();
		BufferedReader br = new BufferedReader(new InputStreamReader(is));
		StringBuilder sb = new StringBuilder();
		String input;
		while ((input = br.readLine()) != null) {
			sb.append(input);
		}
		Log.i("json", "" + sb.toString());
		return new JSONObject(sb.toString());
	}


	public static ArrayList<MyPlace> getNearByPlaces(MyLocation location)
			throws IOException, JSONException {
		String locationStr = location.getLat() + "," + location.getLongt();
		String placesUrl = ApiHelper.getPlaceNearByApiUrl(locationStr,
				MyApp.RADIUS);
		JSONObject jObj = doGet(placesUrl, false);
		JSONArray jArray = jObj.getJSONArray("results");
		int len = jArray.length();
		for (int i = 0; i < len; i++) {
			MyPlace mPlace = new MyPlace();
			JSONObject placeObj = jArray.getJSONObject(i);
			if (placeObj.has("photos")) {
				JSONObject geometry = placeObj.getJSONObject("geometry");
				JSONObject locationObj = geometry.getJSONObject("location");
				MyLocation mLocation = new MyLocation(
						locationObj.getDouble("lat"),
						locationObj.getDouble("lng"));
				mPlace.setmLocation(mLocation);
				if (placeObj.has("icon")) {
					mPlace.setmIcon(placeObj.getString("icon"));
				}
				if (placeObj.has("id")) {
					mPlace.setmId(placeObj.getString("id"));
				}
				if (placeObj.has("name")) {
					mPlace.setmName(placeObj.getString("name"));
				}
				if (placeObj.has("place_id")) {
					mPlace.setmPlaceid(placeObj.getString("place_id"));
				}
				
				
				MyApp.mPlaceIdnPlaceMap.put(mPlace.getmPlaceid(), mPlace);
				MyApp.mNearByPlaces.add(mPlace);
			}
			
			
		}
		return MyApp.mNearByPlaces;
	}
	
	public static boolean getPhotosForPlaces(String placeid) throws IOException, JSONException{
		boolean hasPhoto=false;
		String urlString = ApiHelper.getPlaceDetailApiUrl(placeid);
		JSONObject jObj = doGet(urlString, false);
		if(jObj.has("result")){
			JSONObject result = jObj.getJSONObject("result");
			MyPlace mPlace= MyApp.mPlaceIdnPlaceMap.get(placeid);
			if(result.has("photos")){
				hasPhoto=true;
				JSONArray imgAry = result.getJSONArray("photos");
				int numPics = imgAry.length();
				for (int j = 0; j < numPics; j++) {
					JSONObject picObj = imgAry.getJSONObject(j);
					String reference = picObj.getString("photo_reference");
					int height = picObj.getInt("height");
					int width = picObj.getInt("width");
					String url=ApiHelper.getPlacePhotoApiUrl(reference, MyApp.MAXWIDTH);
					MyPhoto mPic= new MyPhoto(height, width, reference,url);
					mPlace.addPhoto(mPic);
				}
			}
			if(result.has("formatted_address")){
				String address = result.getString("formatted_address");
				mPlace.setmAddress(address);
			}
		}
	 return hasPhoto;
	}
	
	public static MyPlace getPhotosForFavs(String placeid) throws IOException, JSONException{
		boolean hasPhoto=false;
		String urlString = ApiHelper.getPlaceDetailApiUrl(placeid);
		JSONObject jObj = doGet(urlString, false);
		MyPlace mPlace=  new MyPlace();
		if(jObj.has("result")){
			JSONObject result = jObj.getJSONObject("result");
			
			if(result.has("photos")){
				hasPhoto=true;
				JSONArray imgAry = result.getJSONArray("photos");
				int numPics = imgAry.length();
				for (int j = 0; j < numPics; j++) {
					JSONObject picObj = imgAry.getJSONObject(j);
					String reference = picObj.getString("photo_reference");
					int height = picObj.getInt("height");
					int width = picObj.getInt("width");
					String url=ApiHelper.getPlacePhotoApiUrl(reference, MyApp.MAXWIDTH);
					MyPhoto mPic= new MyPhoto(height, width, reference,url);
					mPlace.addPhoto(mPic);
				}
			}
			if(result.has("formatted_address")){
				String address = result.getString("formatted_address");
				mPlace.setmAddress(address);
			}
			JSONObject geometry = result.getJSONObject("geometry");
			JSONObject locationObj = geometry.getJSONObject("location");
			MyLocation mLocation = new MyLocation(
					locationObj.getDouble("lat"),
					locationObj.getDouble("lng"));
			mPlace.setmLocation(mLocation);
			if (result.has("icon")) {
				mPlace.setmIcon(result.getString("icon"));
			}
			if (result.has("id")) {
				mPlace.setmId(result.getString("id"));
			}
			if (result.has("name")) {
				mPlace.setmName(result.getString("name"));
			}
			if (result.has("place_id")) {
				mPlace.setmPlaceid(result.getString("place_id"));
			}
			
			
		}
	 return mPlace;
	}
}
