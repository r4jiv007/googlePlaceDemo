package rajiv.imena.demo.activities;

import rajiv.imena.demo.adapters.PlaceCursorAdapter;
import rajiv.imena.demo.contentprovider.FavPlaceContentProvider;
import rajiv.imena.demo.db.FavPlacesTable;
import android.app.ListActivity;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

public class FavPlacesListActivity extends ListActivity implements
		LoaderManager.LoaderCallbacks<Cursor>  {

	private PlaceCursorAdapter mFavPlaceAdapter;
	private ListView mListView;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getLoaderManager().initLoader(0, null, this);
		mFavPlaceAdapter= new PlaceCursorAdapter(this, null);
	   mListView = getListView();
	   
	   setListener();
	}
	
	private void setListener(){
		mListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Cursor cursor = (Cursor) mFavPlaceAdapter.getItem(position);
				String placeId = cursor.getString(cursor.getColumnIndex(FavPlacesTable.COLUMN_PLACE_ID));
				Intent intent = new Intent(FavPlacesListActivity.this,ImageListActivity.class);
				intent.putExtra("placeid", placeId);
				intent.putExtra("fromfav", true);
				startActivity(intent);
			}
		});
	}
	@Override
	public Loader<Cursor> onCreateLoader(int id, Bundle args) {
		return new CursorLoader(getApplicationContext(),
				FavPlaceContentProvider.FAVPLACE_TABLE_CONTENT_URI, null, null,
				null, null);
	}

	@Override
	public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
		if(data==null || data.getCount()==0){
			Toast.makeText(this, "No Favourites to show", Toast.LENGTH_LONG).show();
		}
		mFavPlaceAdapter.swapCursor(data);
		setListAdapter(mFavPlaceAdapter);
	}

	@Override
	public void onLoaderReset(Loader<Cursor> loader) {
		mFavPlaceAdapter.swapCursor(null);
	}

}
