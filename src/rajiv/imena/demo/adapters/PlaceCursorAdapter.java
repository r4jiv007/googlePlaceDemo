package rajiv.imena.demo.adapters;

import rajiv.imena.demo.R;
import rajiv.imena.demo.db.FavPlacesTable;
import rajiv.imena.demo.utils.Utils;
import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.AbsoluteLayout.LayoutParams;

import com.nostra13.universalimageloader.core.ImageLoader;

public class PlaceCursorAdapter extends BaseAdapter {
	private Activity mContext;
	private Cursor mCursor;
	private int normalVal;

	public PlaceCursorAdapter(Activity context, Cursor cursor) {
		this.mContext = context;
		this.mCursor = cursor;
	}

	public int getCount() {
		if (mCursor != null && mCursor.getCount() > 0)
			return mCursor.getCount();
		else
			return -1;
	}

	public void swapCursor(Cursor cursor) {
		mCursor = cursor;
		notifyDataSetChanged();
	}

	public Object getItem(int position) {
		if (mCursor != null && mCursor.getCount() >= position + 1) {
			mCursor.moveToPosition(position);
			return mCursor;
		} else {
			return null;
		}

	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(int position, View view, ViewGroup parent) {

		if (mCursor != null && mCursor.getCount() > 0) {
			ViewHolder holder = null;
			LayoutInflater layoutInflater = (LayoutInflater) mContext
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			int itemPos = (position % mCursor.getCount());
			if (view == null) {

				// view = layoutInflater.inflate(R.layout.tvguide_rowunit_smart,
				// null);
				view = layoutInflater.inflate(R.layout.place_list_item, null);
				holder = new ViewHolder(view);
				view.setTag(holder);

			} else {
				holder = (ViewHolder) view.getTag();
			}

			int height = (int) (.07f * Utils.getDimentions(mContext)[1]);
			LinearLayout.LayoutParams mParams = (LinearLayout.LayoutParams) holder.tvPlaceName
					.getLayoutParams();
			mParams.width = LayoutParams.MATCH_PARENT;
			mParams.height = height;
			mParams.gravity = Gravity.CENTER_VERTICAL;
			holder.tvPlaceName.setLayoutParams(mParams);

			mCursor.moveToPosition(itemPos);
			String placeName = mCursor.getString(mCursor
					.getColumnIndex(FavPlacesTable.COLUMN_PLACE_NAME));
			if (placeName != null) {
				holder.tvPlaceName.setText(placeName);
			}

			return view;
		} else
			return null;
	}

	static class ViewHolder {
		TextView tvPlaceName;

		ViewHolder(View v) {
			tvPlaceName = (TextView) v.findViewById(R.id.tvName);
		}
	}
}
