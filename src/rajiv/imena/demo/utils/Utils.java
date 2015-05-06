package rajiv.imena.demo.utils;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.DisplayMetrics;
import android.util.Log;

public class Utils {

	private static String TAG="NetworkConn";
	public static boolean hasInternetAccess(Context context) {
	    if (isNetworkAvailable(context)) {
	        try {
	            HttpURLConnection urlc = (HttpURLConnection) 
	                (new URL("http://clients3.google.com/generate_204")
	                .openConnection());
	            urlc.setRequestProperty("User-Agent", "Android");
	            urlc.setRequestProperty("Connection", "close");
	            urlc.setConnectTimeout(1500); 
	            urlc.connect();
	            return (urlc.getResponseCode() == 204 &&
	                        urlc.getContentLength() == 0);
	        } catch (IOException e) {
	            Log.e(TAG, "Error checking internet connection", e);
	        }
	    } else {
	        Log.d(TAG, "No network available!");
	    }
	    return false;
	}
	
	public static boolean isNetworkAvailable(Context context) {
	    ConnectivityManager connectivityManager 
	         = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
	    NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
	    return activeNetworkInfo != null;
	}
	
	public static int[] getDimentions(Context context){
		DisplayMetrics metrics = context.getResources().getDisplayMetrics();
		int width = metrics.widthPixels;
		int height = metrics.heightPixels;
		
		return new int[]{width,height};
	}
}
