package rajiv.imena.demo.activities;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.json.JSONException;

import rajiv.imena.demo.R;
import rajiv.imena.demo.app.MyApp;
import rajiv.imena.demo.models.MyLocation;
import rajiv.imena.demo.models.MyPlace;
import rajiv.imena.demo.utils.HttpCall;
import rajiv.imena.demo.utils.Utils;
import android.app.Activity;
import android.content.Intent;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Place;
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

public class StartupActivity extends Activity implements ConnectionCallbacks,
		OnConnectionFailedListener {

	protected static final String TAG = "basic-location-sample";

	/**
	 * Provides the entry point to Google Play services.
	 */
	protected GoogleApiClient mGoogleApiClient;

	/**
	 * Represents a geographical location.
	 */
	protected Location mLastLocation;

	protected TextView mLatitudeText;
	protected TextView mLongitudeText;

	private ProgressBar mLoadingView;
	private PlaceFinderTask mPlaceFinder;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		if (!Utils.isNetworkAvailable(this)) {
			Toast.makeText(this, "Network Not Available", Toast.LENGTH_LONG)
					.show();
			this.finish();
		}
		MyApp.mNearByPlaces.clear();
		MyApp.mPlaceIdnPlaceMap.clear();
		initImageLoader();
		setContentView(R.layout.startup_activity);
		initView();
		mPlaceFinder = new PlaceFinderTask();
		buildGoogleApiClient();
		// String uri =
		// "https://maps.googleapis.com/maps/api/place/photo?maxwidth=400&photoreference=CoQBcQAAAORNmbdWLY1CJ4NHw4qqm_V57DAcSkBNXoUR9Surw8s0zm4nPUigOVUlkpyTg4AEmN4X6nTIeuspgN-vq-3W_0t7WOt3Yb1KFtWbPiho3HMiVuBvlP-Q18RNUwQFZnxjFIfpSar-9COFcLVGkNO9rXEmV5ExIP0zQdjuVDl6oGf3EhDlRISPHFYtOVa9AL8NB-chGhT3hBT7D3S-sOiP6XEKCFEsj93s8Q&key=AIzaSyAPtCBYbNom7rg35QlP3VvOgqQx22hJTbE";
		// mImageLoader.displayImage(uri, mImageView);
	}

	private void initView() {
		mLoadingView = (ProgressBar) findViewById(R.id.pbLoadingView);
	}

	/**
	 * Builds a GoogleApiClient. Uses the addApi() method to request the
	 * LocationServices API.
	 */
	protected synchronized void buildGoogleApiClient() {
		mGoogleApiClient = new GoogleApiClient.Builder(this)
				.addConnectionCallbacks(this)
				.addOnConnectionFailedListener(this)
				.addApi(LocationServices.API).build();
	}

	@Override
	protected void onStart() {
		super.onStart();
		mGoogleApiClient.connect();
	}

	@Override
	protected void onStop() {
		super.onStop();
		if (mGoogleApiClient.isConnected()) {
			mGoogleApiClient.disconnect();
		}
	}

	/**
	 * Runs when a GoogleApiClient object successfully connects.
	 */
	@Override
	public void onConnected(Bundle connectionHint) {
		mLastLocation = LocationServices.FusedLocationApi
				.getLastLocation(mGoogleApiClient);
		if (mLastLocation != null) {
			MyApp.mCurrentLoc = mLastLocation;
		} else {
			Toast.makeText(this, R.string.no_location_detected,
					Toast.LENGTH_LONG).show();
		}

//		Thread mThread = new Thread(new Runnable() {
//
//			@SuppressWarnings("unused")
//			@Override
//			public void run() {
//				try {
//					ArrayList<MyPlace> mPlaces = HttpCall
//							.getNearByPlaces(new MyLocation(mLastLocation
//									.getLatitude(), mLastLocation
//									.getLongitude()));
//					for (MyPlace places : mPlaces) {
//						HttpCall.getPhotosForPlaces(places.getmPlaceid());
//					}
//					for (MyPlace places : mPlaces) {
//						Log.d("nearby", "" + places.getPhotos().size());
//					}
//					Intent in = new Intent(StartupActivity.this,
//							HomeActivity.class);
//					startActivity(in);
//					StartupActivity.this.finish();
//				} catch (IOException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				} catch (JSONException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//			}
//		});
		// mThread.start();
			mPlaceFinder.execute();
	}

	Place lastPlace;
	String mString;

	@Override
	public void onConnectionFailed(ConnectionResult result) {
		Log.i(TAG, "Connection failed: ConnectionResult.getErrorCode() = "
				+ result.getErrorCode());
	}

	@Override
	public void onConnectionSuspended(int cause) {
		Log.i(TAG, "Connection suspended");
		mGoogleApiClient.connect();
	}

	private class PlaceFinderTask extends AsyncTask<Void, Void, Void> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			if (mLoadingView.getVisibility() != View.VISIBLE)
				mLoadingView.setVisibility(View.VISIBLE);
		}

		@Override
		protected Void doInBackground(Void... params) {
			try {
				
				ArrayList<MyPlace> mPlaces = HttpCall
						.getNearByPlaces(new MyLocation(mLastLocation
								.getLatitude(), mLastLocation.getLongitude()));
				for (MyPlace places : mPlaces) {
					HttpCall.getPhotosForPlaces(places.getmPlaceid());
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			if(MyApp.mNearByPlaces.size()>0){
			Intent in = new Intent(StartupActivity.this, HomeActivity.class);
			startActivity(in);
			StartupActivity.this.finish();
			}else{
				Toast.makeText(StartupActivity.this, "Failed to Download Information", Toast.LENGTH_LONG).show();
			}
		}

	}

	private void initImageLoader() {
		File cacheDir = StorageUtils.getCacheDirectory(this);
		DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
				.cacheOnDisk(true).cacheInMemory(true)
				.displayer(new FadeInBitmapDisplayer(100))
				.showImageOnLoading(R.drawable.ic_launcher)
				.showImageForEmptyUri(R.drawable.ic_launcher)
				.showImageOnFail(R.drawable.ic_launcher).build();

		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
				this).memoryCacheExtraOptions(480, 800)
				.diskCacheExtraOptions(480, 800, null).threadPoolSize(3)
				.threadPriority(Thread.NORM_PRIORITY - 2)
				.tasksProcessingOrder(QueueProcessingType.FIFO)
				.denyCacheImageMultipleSizesInMemory()
				.memoryCache(new LruMemoryCache(2 * 1024 * 1024))
				.memoryCacheSize(2 * 1024 * 1024).memoryCacheSizePercentage(13)
				.diskCache(new UnlimitedDiscCache(cacheDir))
				.diskCacheSize(50 * 1024 * 1024).diskCacheFileCount(100)
				.diskCacheFileNameGenerator(new HashCodeFileNameGenerator()) // default
				.imageDownloader(new BaseImageDownloader(this)) // default
				.imageDecoder(new BaseImageDecoder(true)) // default
				.defaultDisplayImageOptions(defaultOptions) // default
				.writeDebugLogs().build();
		MyApp.mImageLoader = ImageLoader.getInstance();
		MyApp.mImageLoader.init(config);
	}
}