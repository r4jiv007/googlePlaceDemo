package rajiv.imena.demo.fragments;

import java.util.ArrayList;
import java.util.HashMap;

import rajiv.imena.demo.R;
import rajiv.imena.demo.activities.ImageListActivity;
import rajiv.imena.demo.app.MyApp;
import rajiv.imena.demo.models.MyPlace;
import rajiv.imena.demo.utils.Utils;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

/**
 * A fragment that launches other parts of the demo application.
 */
public class MyMapFragment extends Fragment {

	MapView mMapView;
	private GoogleMap googleMap;
    private ArrayList<MarkerOptions>mMarkers = new ArrayList<MarkerOptions>();
    private HashMap<String, MyPlace>mMarkernPlaceMap= new HashMap<String, MyPlace>();
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// inflat and return the layout
		View v = inflater.inflate(R.layout.fragment_location_info, container,
				false);
		mMapView = (MapView) v.findViewById(R.id.mapView);
		mMapView.onCreate(savedInstanceState);

		mMapView.onResume();// needed to get the map to display immediately

		try {
			MapsInitializer.initialize(getActivity().getApplicationContext());
		} catch (Exception e) {
			e.printStackTrace();
		}

		googleMap = mMapView.getMap();
		fillMarkerList();
		// latitude and longitude
		double latitude = MyApp.mCurrentLoc.getLatitude();
		double longitude = MyApp.mCurrentLoc.getLongitude();

		// create marker
		MarkerOptions marker = new MarkerOptions().position(
				new LatLng(latitude, longitude)).title("Current Location");

		// Changing marker icon
		marker.icon(BitmapDescriptorFactory
				.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));

		// adding marker
		googleMap.addMarker(marker).showInfoWindow();;
		for(MarkerOptions m:mMarkers){
			 googleMap.addMarker(m);
			
		}
		
		CameraPosition cameraPosition = new CameraPosition.Builder()
				.target(new LatLng(latitude, longitude)).zoom(15).build();
		googleMap.animateCamera(CameraUpdateFactory
				.newCameraPosition(cameraPosition));
		
		googleMap.setOnMarkerClickListener(new OnMarkerClickListener() {
			
			@Override
			public boolean onMarkerClick(Marker marker) {

				if(Utils.isNetworkAvailable(getActivity())){
				String id = marker.getTitle();
				MyPlace place = mMarkernPlaceMap.get(id);
				if(place!=null){
					String placeId = place.getmPlaceid();
					Intent intent = new Intent(getActivity(),ImageListActivity.class);
					intent.putExtra("placeid", placeId);
					startActivity(intent);
				}
				}else{
					Toast.makeText(getActivity(), "No Network Access", Toast.LENGTH_LONG).show();
				}
				return false;
			}
		});
		// Perform any camera updates here
		return v;
	}

	private void fillMarkerList(){
		for(MyPlace place:MyApp.mNearByPlaces){
			// create marker
			MarkerOptions marker = new MarkerOptions().position(
					new LatLng(place.getmLocation().getLat(), place.getmLocation().getLongt())).title(place.getmName());

			// Changing marker icon
			marker.snippet(place.getmAddress());
			marker.icon(BitmapDescriptorFactory
					.defaultMarker(BitmapDescriptorFactory.HUE_ROSE));
			mMarkers.add(marker);
			mMarkernPlaceMap.put(marker.getTitle(), place);
		}
	}
	@Override
	public void onResume() {
		super.onResume();
		mMapView.onResume();
	}

	@Override
	public void onPause() {
		super.onPause();
		mMapView.onPause();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		mMapView.onDestroy();
	}

	@Override
	public void onLowMemory() {
		super.onLowMemory();
		mMapView.onLowMemory();
	}
}