package rajiv.imena.demo.fragments;

import rajiv.imena.demo.activities.ImageListActivity;
import rajiv.imena.demo.adapters.PlaceAdapter;
import rajiv.imena.demo.app.MyApp;
import rajiv.imena.demo.models.MyPlace;
import rajiv.imena.demo.utils.Utils;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;

public class NearPlacesFragment extends Fragment {

	private PlaceAdapter mAdapter;
	private ListView mListView;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mListView = new ListView(getActivity());
		
		mAdapter = new PlaceAdapter(getActivity(), MyApp.mNearByPlaces);
		mListView.setAdapter(mAdapter);
		setListener();
		return mListView;
	}
	
	private void setListener(){
		mListView.setOnItemClickListener(new  OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				if(Utils.isNetworkAvailable(getActivity())){
				MyPlace mPlace= MyApp.mNearByPlaces.get(position);
				String placeId = mPlace.getmPlaceid();
				Intent intent = new Intent(getActivity(),ImageListActivity.class);
				intent.putExtra("placeid", placeId);
				startActivity(intent);
				}else{
					Toast.makeText(getActivity(), "No Network Access", Toast.LENGTH_LONG).show();
				}
			}
		});
	}

}
