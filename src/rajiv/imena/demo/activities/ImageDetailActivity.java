package rajiv.imena.demo.activities;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;

import org.apache.http.Header;

import rajiv.imena.demo.R;
import rajiv.imena.demo.app.MyApp;
import rajiv.imena.demo.models.MyPlace;
import android.app.Activity;
import android.app.DownloadManager;
import android.app.DownloadManager.Query;
import android.app.DownloadManager.Request;
import android.app.ProgressDialog;
import android.app.WallpaperManager;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.MediaStore.Images;
import android.provider.MediaStore.Images.Media;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.BinaryHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.nostra13.universalimageloader.core.ImageLoader;

public class ImageDetailActivity extends Activity {

	private String mPlaceid;
	private String url;
	private MyPlace mPlace;

	private ImageView ivDisplayImage;
	private TextView tvDetailText;
	private ImageLoader mImageLoader;
	private long enqueue;
	private DownloadManager dm;
	private String[] mAllowedContentTypes = new String[] {
			RequestParams.APPLICATION_OCTET_STREAM, "image/jpeg", "image/png",
			"image/gif" };

	private ProgressDialog mProgressDialog;
	private BroadcastReceiver downLoadReceiver;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mPlaceid = getIntent().getStringExtra("placeid");
		url = getIntent().getStringExtra("url");
		mPlace = (MyPlace) getIntent().getSerializableExtra("place");//MyApp.mPlaceIdnPlaceMap.get(mPlaceid);
		mImageLoader = MyApp.mImageLoader;
		setContentView(R.layout.image_detail_activity);
		initView();
		if (url != null)
			;
		mImageLoader.displayImage(url, ivDisplayImage);
		if (mPlace != null)
			tvDetailText.setText(mPlace.getmName() + "\n"
					+ mPlace.getmAddress());
		downLoadReceiver = new BroadcastReceiver() {
			@Override
			public void onReceive(Context context, Intent intent) {
				String action = intent.getAction();
				if (DownloadManager.ACTION_DOWNLOAD_COMPLETE.equals(action)) {
					long downloadId = intent.getLongExtra(
							DownloadManager.EXTRA_DOWNLOAD_ID, 0);
					Query query = new Query();
					query.setFilterById(enqueue);
					Cursor c = dm.query(query);
					if (c.moveToFirst()) {
						int columnIndex = c
								.getColumnIndex(DownloadManager.COLUMN_STATUS);
						if (DownloadManager.STATUS_SUCCESSFUL == c
								.getInt(columnIndex)) {

							// ImageView view = (ImageView)
							// findViewById(R.id.imageView1);
							String uriString = c
									.getString(c
											.getColumnIndex(DownloadManager.COLUMN_LOCAL_FILENAME));
							Toast.makeText(ImageDetailActivity.this,
									"" + uriString, Toast.LENGTH_LONG).show();
							// view.setImageURI(Uri.parse(uriString));
						}
					}
				}
			}
		};

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.image_menu, menu);
		return true;
	}

	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {

		case R.id.download:
			downloadImage(url);
			return true;
		case R.id.share:

			shareImage(url);
			return true;
		case R.id.wallpaper:

			new WallPaperTask().execute();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	@Override
	protected void onResume() {

		super.onResume();

		registerReceiver(downLoadReceiver, new IntentFilter(
				DownloadManager.ACTION_DOWNLOAD_COMPLETE));
	}

	@Override
	protected void onPause() {

		super.onPause();

		unregisterReceiver(downLoadReceiver);
	}

	@Override
	protected void onStop() {

		super.onStop();

	}

	private void initView() {
		ivDisplayImage = (ImageView) findViewById(R.id.ivPlaceImage);
		tvDetailText = (TextView) findViewById(R.id.tvDetail);
		setListener();
	}

	private void setListener() {
		ivDisplayImage.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

			}
		});
	}

	private void downloadImage(String url) {
		dm = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
		Request request = new Request(Uri.parse(url));
		request.setTitle("My Image Download");
		request.setDestinationInExternalPublicDir(
				Environment.DIRECTORY_DOWNLOADS, mPlaceid + "_images.jpg");
		request.setNotificationVisibility(Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
		request.setVisibleInDownloadsUi(true);
		enqueue = dm.enqueue(request);

	}

	private void setImageAsBackground(String url) throws MalformedURLException,
			IOException {
		WallpaperManager wpm = WallpaperManager.getInstance(this);
		InputStream ins = new URL(url).openStream();
		wpm.setStream(ins);

	}

	private String sharefilPath;

	private void shareImage(String url) {

		// share.putExtra(Intent.EXTRA_STREAM,
		// Uri.parse(url));
		// startActivity(Intent.createChooser(share, "Share Image"));
		//
		AsyncHttpClient client = new AsyncHttpClient();

		client.get(url, new BinaryHttpResponseHandler(mAllowedContentTypes) {

			@Override
			public void onStart() {

				mProgressDialog = ProgressDialog.show(ImageDetailActivity.this,
						"Wait", "Downloading...");
				mProgressDialog.setCancelable(false);
				super.onStart();
			}

			@Override
			public void onFinish() {
				super.onFinish();
				if (null != mProgressDialog) {
					mProgressDialog.dismiss();
				}

				if (sharefilPath != null) {

					File file = new File(sharefilPath);
					Uri phototUri = getImageContentUri(
							ImageDetailActivity.this, file);

					Log.d("sharefile", "file path: " + file.getPath());

					if (file.exists()) {
						// file create success

					} else {
						// file create fail
					}
					ContentValues values = new ContentValues();
					values.put(Images.Media.TITLE, "title");
					values.put(Images.Media.MIME_TYPE, "image/jpeg");
					Uri uri = getContentResolver().insert(
							Media.EXTERNAL_CONTENT_URI, values);

					Intent shareIntent = new Intent(Intent.ACTION_SEND);
					shareIntent.setType("image/jpeg");

					shareIntent.setData(phototUri);
					// shareIntent.setType("image/png");
					shareIntent.putExtra(Intent.EXTRA_STREAM, phototUri);
					// startActivityForResult(Intent.createChooser(shareIntent,
					// "Share Via"), 2022);
					startActivity(Intent.createChooser(shareIntent,
							"Share Image"));
				}
			}

			@Override
			public void onFailure(int arg0, Header[] arg1, byte[] arg2,
					Throwable arg3) {

			}

			@Override
			public void onSuccess(int arg0, Header[] arg1, byte[] bytes) {

				File file = new File(Environment.getExternalStorageDirectory()
						+ File.separator + "temporary_file.jpg");

				OutputStream f;
				try {
					f = new FileOutputStream(file);

					f.write(bytes); // your bytes
					f.close();

					sharefilPath = file.getAbsolutePath();

				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
	}

	public static Uri getImageContentUri(Context context, File imageFile) {
		String filePath = imageFile.getAbsolutePath();
		Cursor cursor = context.getContentResolver().query(
				MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
				new String[] { MediaStore.Images.Media._ID },
				MediaStore.Images.Media.DATA + "=? ",
				new String[] { filePath }, null);
		if (cursor != null && cursor.moveToFirst()) {
			int id = cursor.getInt(cursor
					.getColumnIndex(MediaStore.MediaColumns._ID));
			return Uri.withAppendedPath(
					MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "" + id);
		} else {
			if (imageFile.exists()) {
				ContentValues values = new ContentValues();
				values.put(MediaStore.Images.Media.DATA, filePath);
				return context.getContentResolver().insert(
						MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
			} else {
				return null;
			}
		}
	}

	public void showDownload(View view) {
		Intent i = new Intent();
		i.setAction(DownloadManager.ACTION_VIEW_DOWNLOADS);
		startActivity(i);
	}

	private class WallPaperTask extends AsyncTask<Void, Void, Void> {

		boolean isAdded = false;

		@Override
		protected void onPreExecute() {

			mProgressDialog = ProgressDialog.show(ImageDetailActivity.this,
					"Wait", "Setting as Wallpaper...");
			mProgressDialog.setCancelable(false);

			super.onPreExecute();
		}

		@Override
		protected Void doInBackground(Void... params) {
			try {
				setImageAsBackground(url);
				// Toast.makeText(ImageDetailActivity.this,
				// "Wallpaper Set Successfully!!", Toast.LENGTH_SHORT).show();
				isAdded = true;
			} catch (Exception e) {
				// Toast.makeText(ImageDetailActivity.this,
				// "Setting WallPaper Failed!!", Toast.LENGTH_SHORT).show();
				isAdded = false;
				e.printStackTrace();
			}

			return null;
		}

		@Override
		protected void onPostExecute(Void result) {

			super.onPostExecute(result);
			if (null != mProgressDialog) {
				mProgressDialog.dismiss();
			}
			if (isAdded) {
				Toast.makeText(ImageDetailActivity.this,
						"Wallpaper Set Successfully!!", Toast.LENGTH_SHORT)
						.show();

			} else {
				Toast.makeText(ImageDetailActivity.this,
						"Setting WallPaper Failed!!", Toast.LENGTH_SHORT)
						.show();

			}
		}
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		// deal with this with whatever constant you use. i have a navigator
		// object to handle my navigation so it also holds all mys constants for
		// intents
		if (requestCode == 2022) {
			// delete temp file
			if (null != sharefilPath) {
				File file = new File(sharefilPath);
				file.delete();
			}
			Toast.makeText(this, "Successfully shared", Toast.LENGTH_LONG)
					.show();
			;
		}
	}
}
