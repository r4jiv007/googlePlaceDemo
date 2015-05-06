package rajiv.imena.demo.adapters;

import java.util.ArrayList;

import rajiv.imena.demo.R;
import rajiv.imena.demo.models.MyPlace;
import rajiv.imena.demo.utils.Utils;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.AbsoluteLayout.LayoutParams;

public class PlaceAdapter extends BaseAdapter {

	private Context mContext;
	private ArrayList<MyPlace> mPlaces;

	public PlaceAdapter(Context context, ArrayList<MyPlace> places) {
		this.mContext = context;
		this.mPlaces = places;
	}

	@Override
	public int getCount() {
		if (mPlaces != null) {
			return mPlaces.size();
		} else {
			return -1;

		}

	}

	public void swapPlaceList(ArrayList<MyPlace> places) {
		this.mPlaces = places;
		notifyDataSetChanged();
	}

	@Override
	public Object getItem(int arg0) {
		if (mPlaces != null && mPlaces.size() > arg0) {
			return mPlaces.get(arg0);
		} else {
			return null;

		}
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		if (mPlaces != null && mPlaces.size() > arg0) {
			mPlaces.get(arg0).hashCode();
			return mPlaces.get(arg0).hashCode();
		} else {
			return -1;

		}
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		ViewHolder holder = null;
		if (convertView == null) {

			// inflate the layout
			LayoutInflater inflater = LayoutInflater.from(mContext);
			convertView = inflater.inflate(R.layout.place_list_item, parent,
					false);

			holder = new ViewHolder(convertView);

			convertView.setTag(holder);

		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		// assign values if the object is not null
		int height = (int)(.07f * Utils.getDimentions(mContext)[1]);
		LinearLayout.LayoutParams mParams= (LinearLayout.LayoutParams) holder.tvTitle.getLayoutParams();
		mParams.width=LayoutParams.MATCH_PARENT;
		mParams.height=height;
		mParams.gravity=Gravity.CENTER_VERTICAL;
		holder.tvTitle.setLayoutParams(mParams);
		MyPlace mPlace = mPlaces.get(position);
		String name = mPlace.getmName();
		if (name != null) {
			holder.tvTitle.setText(name);
		}

		return convertView;
	}


	static class ViewHolder {
		TextView tvTitle;

		ViewHolder(View v) {
			tvTitle = (TextView) v.findViewById(R.id.tvName);

		}
	}
}