package rajiv.imena.demo.adapters;

import java.util.ArrayList;

import rajiv.imena.demo.R;
import rajiv.imena.demo.app.MyApp;
import rajiv.imena.demo.models.MyPhoto;
import rajiv.imena.demo.models.MyPlace;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;

public class PlaceImageAdapter extends BaseAdapter {

	private Context mContext;
	private MyPlace mPlaces;
	private ArrayList<MyPhoto> mPhotos;
	private ImageLoader mImageLoader;
	private LayoutInflater inflater;

	public PlaceImageAdapter(Context context, MyPlace places) {
		this.mContext = context;
		this.mPlaces = places;
		mPhotos = mPlaces.getPhotos();
        mImageLoader=MyApp.mImageLoader;
		inflater = LayoutInflater.from(mContext);
		
	}

	@Override
	public int getCount() {
		if (mPhotos != null) {
			return mPhotos.size();
		} else {
			return -1;

		}

	}

	public void swapPlaceList(ArrayList<MyPhoto> photos) {
		this.mPhotos = photos;
		notifyDataSetChanged();
	}

	@Override
	public Object getItem(int arg0) {
		if (mPhotos != null && mPhotos.size() > arg0) {
			return mPhotos.get(arg0);
		} else {
			return null;

		}
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		if (mPhotos != null && mPhotos.size() > arg0) {
			mPhotos.get(arg0).hashCode();
			return mPhotos.get(arg0).hashCode();
		} else {
			return -1;

		}
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		ViewHolder holder = null;
		if (convertView == null) {

			// inflate the layout
			convertView = inflater.inflate(R.layout.image_list_item, parent,
					false);

			holder = new ViewHolder(convertView);

			convertView.setTag(holder);

		} else {
			holder = (ViewHolder) convertView.getTag();
			// holder.ivImage.setImageResource(0);
		}

		// assign values if the object is not null
		MyPhoto mPhoto = mPhotos.get(position);
		String name = mPlaces.getmName();
		if (name != null) {
			holder.tvTitle.setText(name);

		}
//		holder.ivImage.setImageResource(R.drawable.ic_launcher);
		String url = null;
		if (mPhoto != null) {
			url = mPhoto.getmThumbNailUrl();
			if (url != null) {
				mImageLoader.displayImage(url, holder.ivImage);
			}
		}

		return convertView;
	}

	static class ViewHolder {
		TextView tvTitle;
		ImageView ivImage;

		ViewHolder(View v) {
			tvTitle = (TextView) v.findViewById(R.id.tvDetail);
			ivImage = (ImageView) v.findViewById(R.id.ivPlaceImage);

		}
	}
}