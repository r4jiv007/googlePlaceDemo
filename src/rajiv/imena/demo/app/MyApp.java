package rajiv.imena.demo.app;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import rajiv.imena.demo.R;
import rajiv.imena.demo.models.MyPhoto;
import rajiv.imena.demo.models.MyPlace;
import android.app.Application;
import android.location.Location;

import com.google.android.gms.maps.model.LatLng;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiscCache;
import com.nostra13.universalimageloader.cache.disc.naming.HashCodeFileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.decode.BaseImageDecoder;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;
import com.nostra13.universalimageloader.utils.StorageUtils;

public class MyApp extends Application {
  
	public static LatLng mPosition ;
	public static final int RADIUS = 500;
	public static final int MAXWIDTH=400;
	public static Location mCurrentLoc;
	public static final String APPLICATION_KEY= "AIzaSyA_3sWYXT9QmnAHr_Se2uL4P_xWUOwlF_0";//"AIzaSyAPtCBYbNom7rg35QlP3VvOgqQx22hJTbE";//"AIzaSyC6cgXu2G4SDjlQGE063sYlIwCRqa1E0mc";//"";//;// old key 
	public static HashMap<String , ArrayList<MyPhoto>> mPlacePhotoMap = new HashMap<String , ArrayList<MyPhoto>>();
	public static ArrayList<MyPlace>mNearByPlaces= new ArrayList<MyPlace>();
	public static HashMap<String , MyPlace> mPlaceIdnPlaceMap = new HashMap<String , MyPlace>();
	
	public static ImageLoader mImageLoader;
	@Override
	public void onCreate() {
		super.onCreate();
		
	
	}
}
