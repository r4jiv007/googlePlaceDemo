package rajiv.imena.demo.activities;

import java.io.IOException;

import org.json.JSONException;

import rajiv.imena.demo.R;
import rajiv.imena.demo.adapters.PlaceImageAdapter;
import rajiv.imena.demo.app.MyApp;
import rajiv.imena.demo.contentprovider.FavPlaceContentProvider;
import rajiv.imena.demo.db.FavPlacesTable;
import rajiv.imena.demo.models.MyPhoto;
import rajiv.imena.demo.models.MyPlace;
import rajiv.imena.demo.utils.ApiHelper;
import rajiv.imena.demo.utils.HttpCall;
import rajiv.imena.demo.utils.Utils;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;

public class ImageListActivity extends ListActivity {

	private String mPlaceId;
	private MyPlace mPlace;
	private PlaceImageAdapter mImageAadapter;
	private AddFavTask mAddFavTask;
	private RemoveFavTask mRemoveFavTask;
	private ListView mListView;

	private Menu mMenu;
	private boolean isFav = false;;
	private boolean fromFav = false;
	
	private ProgressDialog mProgressDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		mListView = getListView();
		mAddFavTask = new AddFavTask();
		mRemoveFavTask = new RemoveFavTask();
		mPlaceId = getIntent().getStringExtra("placeid");
		fromFav = getIntent().getBooleanExtra("fromfav", false);
		setListener();
		if (!fromFav) {

			mPlace = MyApp.mPlaceIdnPlaceMap.get(mPlaceId);

			new CheckFavTask().execute();
			mImageAadapter = new PlaceImageAdapter(this, mPlace);
			setListAdapter(mImageAadapter);
			
		} else {
			isFav = true;
			invalidateOptionsMenu();
			if(MyApp.mPlaceIdnPlaceMap.containsKey(mPlaceId)){
				mPlace = MyApp.mPlaceIdnPlaceMap.get(mPlaceId);
				mImageAadapter = new PlaceImageAdapter(this, mPlace);
				setListAdapter(mImageAadapter);
				
			}else{

				new FetchFavTask().execute();
			}
		}
	}

	private void setListener() {
		mListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				if(Utils.isNetworkAvailable(ImageListActivity.this)){
					String placeid = mPlaceId;
					MyPhoto photo = (MyPhoto) getListAdapter().getItem(position);
					int maxWidth = photo.getmWidth();
					String reference = photo.getmPhotoReference();
					String url = ApiHelper.getPlacePhotoApiUrl(reference, maxWidth);
					Intent intent = new Intent(ImageListActivity.this,
							ImageDetailActivity.class);
					intent.putExtra("place", mPlace);
					intent.putExtra("placeid", placeid);
					intent.putExtra("url", url);
					startActivity(intent);
				}else{

					Toast.makeText(ImageListActivity.this, "No Network Access", Toast.LENGTH_LONG).show();
				}

			}
		});
	}

	@Override
	protected void onResume() {
		super.onResume();

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.place_menu, menu);
		mMenu = menu;
		return true;
	}

	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		if (isFav) {
			menu.findItem(R.id.mRemove).setVisible(true);
			menu.findItem(R.id.mfav).setVisible(false);
		} else {
			menu.findItem(R.id.mRemove).setVisible(false);
			menu.findItem(R.id.mfav).setVisible(true);
		}
		return super.onPrepareOptionsMenu(menu);
	}

	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.mfav:
			// Toast.makeText(this, "Option1", Toast.LENGTH_SHORT).show();
			mAddFavTask.execute();

			return true;
		case R.id.mRemove:
			mRemoveFavTask.execute();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	private boolean addToFav(MyPlace place) {
		ContentValues cv = new ContentValues();

		cv.put(FavPlacesTable.COLUMN_PID, mPlace.getmId());
		cv.put(FavPlacesTable.COLUMN_PLACE_ID, mPlace.getmPlaceid());
		cv.put(FavPlacesTable.COLUMN_PLACE_NAME, mPlace.getmName());
		cv.put(FavPlacesTable.COLUMN_PLACE_ADDRESS, mPlace.getmAddress());
		cv.put(FavPlacesTable.COLUMN_PLACE_ICON, mPlace.getmIcon());
		cv.put(FavPlacesTable.COLUMN_PLACE_LAT,
				String.valueOf(mPlace.getmLocation().getLat()));
		cv.put(FavPlacesTable.COLUMN_PLACE_LNG,
				String.valueOf(mPlace.getmLocation().getLongt()));

		Uri favUri = getContentResolver().insert(
				FavPlaceContentProvider.FAVPLACE_TABLE_CONTENT_URI, cv);
		Log.d("favUri", favUri.toString() + "");
		int val = Integer.valueOf(favUri.getLastPathSegment());
		return val > 0;
	}

	private boolean removeFromFav(MyPlace place) {
		String mSelectionClause = FavPlacesTable.COLUMN_PLACE_ID + "=?";
		String[] mSelectionArgs = { place.getmPlaceid() };
		int numDeletedRows = getContentResolver().delete(
				FavPlaceContentProvider.FAVPLACE_TABLE_CONTENT_URI,
				mSelectionClause, mSelectionArgs);

		return numDeletedRows > 0;
	}

	private boolean checkFav(MyPlace place) {
		String[] projection = { FavPlacesTable.COLUMN__ID,
				FavPlacesTable.COLUMN_PLACE_ID,
				FavPlacesTable.COLUMN_PLACE_NAME };
		String selection = FavPlacesTable.COLUMN_PLACE_ID + "=?";
		String[] selectionArgs = { place.getmPlaceid() };
		Cursor cursor = getContentResolver().query(
				FavPlaceContentProvider.FAVPLACE_TABLE_CONTENT_URI, projection,
				selection, selectionArgs, null);
		if (cursor == null) {
			return false;
		} else if (cursor.getCount() < 1) {
			return false;
		} else {
			return true;
		}

	}

	private class AddFavTask extends AsyncTask<Void, Void, Void> {

		private boolean isAdded = false;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			mProgressDialog = ProgressDialog.show(ImageListActivity.this,
					"Wait", "Adding to Favourites...");
			mProgressDialog.setCancelable(false);
		}

		@Override
		protected Void doInBackground(Void... params) {
			isAdded = addToFav(mPlace);
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			if(null != mProgressDialog){
				mProgressDialog.dismiss();
			}
			if (isAdded) {
				Toast.makeText(ImageListActivity.this,
						" Sucessfully Added to Favourites", Toast.LENGTH_LONG)
						.show();
				// mMenu.findItem(R.id.mRemove).setVisible(true);
				// mMenu.findItem(R.id.mfav).setVisible(false);
				isFav = true;
				ImageListActivity.this.invalidateOptionsMenu();
			} else {
				Toast.makeText(ImageListActivity.this,
						"Failed to add to Favourites", Toast.LENGTH_LONG)
						.show();
				isFav = false;
				ImageListActivity.this.invalidateOptionsMenu();
			}
		}

	}

	private class RemoveFavTask extends AsyncTask<Void, Void, Void> {

		private boolean isRemoved = false;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			mProgressDialog = ProgressDialog.show(ImageListActivity.this,
					"Wait", "Removing from Favourites...");
			mProgressDialog.setCancelable(false);
		
		}

		@Override
		protected Void doInBackground(Void... params) {
			isRemoved = removeFromFav(mPlace);
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			if(null!=mProgressDialog){
				mProgressDialog.dismiss();
			}
			if (isRemoved) {
				Toast.makeText(ImageListActivity.this,
						"Sucessfully Removed from Favourites",
						Toast.LENGTH_LONG).show();
				// mMenu.findItem(R.id.mRemove).setVisible(false);
				// mMenu.findItem(R.id.mfav).setVisible(true);
				isFav = false;
				ImageListActivity.this.invalidateOptionsMenu();
			} else {
				Toast.makeText(ImageListActivity.this,
						"Failed to Remove from Favourites", Toast.LENGTH_LONG)
						.show();
				isFav = true;
				ImageListActivity.this.invalidateOptionsMenu();
			}
		}

	}

	private class CheckFavTask extends AsyncTask<Void, Void, Void> {

		private boolean isFavs = false;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			mProgressDialog = ProgressDialog.show(ImageListActivity.this,
					"Wait", "Checking if Favourites...");
			mProgressDialog.setCancelable(false);
		
			
		}

		@Override
		protected Void doInBackground(Void... params) {
			isFavs = checkFav(mPlace);
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			if(null != mProgressDialog){
				mProgressDialog.dismiss();
			}
			if (isFavs) {
				ImageListActivity.this.isFav = true;

				ImageListActivity.this.invalidateOptionsMenu();
			} else {
				// mMenu.findItem(R.id.mRemove).setVisible(false);
				// mMenu.findItem(R.id.mfav).setVisible(true);

				ImageListActivity.this.isFav = false;
				ImageListActivity.this.invalidateOptionsMenu();
			}
		}

	}

	private class FetchFavTask extends AsyncTask<Void, Void, Void> {


		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			mProgressDialog = ProgressDialog.show(ImageListActivity.this,
					"Wait", "Fetching Value for Favs....");
			mProgressDialog.setCancelable(false);
		
		}

		@Override
		protected Void doInBackground(Void... params) {
			try {
				mPlace = HttpCall.getPhotosForFavs(mPlaceId);
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
			if(null!=mProgressDialog){
				mProgressDialog.dismiss();
			}
			if (null != mPlace) {
				mImageAadapter = new PlaceImageAdapter(ImageListActivity.this, mPlace);
				setListAdapter(mImageAadapter);
			} else {
				Toast.makeText(ImageListActivity.this,
						"Failed to Fetch Images for Favourites", Toast.LENGTH_LONG)
						.show();
			}
		}

	}
}
