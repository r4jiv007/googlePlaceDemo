package rajiv.imena.demo.contentprovider;

import rajiv.imena.demo.db.FavPlacesTable;
import rajiv.imena.demo.db.helper.DBHelper;
import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;

public class FavPlaceContentProvider extends ContentProvider {

	private DBHelper tableHelper;

	private static final int FAVPLACE_TABLE_ = 10;
	private static final int FAVPLACE_TABLE_ROW = 20;

	private static final String AUTHORITY = "rajiv.imena.demo.content";

	private static final String FAVPLACE_TABLE_BASE_PATH = "FavPlacesTable";

	public static final Uri FAVPLACE_TABLE_CONTENT_URI = Uri.parse("content://"
			+ AUTHORITY + "/" + FAVPLACE_TABLE_BASE_PATH);

	private static final UriMatcher sURIMatcher = new UriMatcher(
			UriMatcher.NO_MATCH);
	static {
		sURIMatcher
				.addURI(AUTHORITY, FAVPLACE_TABLE_BASE_PATH, FAVPLACE_TABLE_);
		sURIMatcher.addURI(AUTHORITY, FAVPLACE_TABLE_BASE_PATH + "/#",
				FAVPLACE_TABLE_ROW);
	}

	@Override
	public boolean onCreate() {

		tableHelper = new DBHelper(getContext());

		return true;
	}

	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		int uriType = sURIMatcher.match(uri);
		SQLiteDatabase sqlDB = tableHelper.getWritableDatabase();
		int rowsDeleted;
		String id;
		switch (uriType) {
		case FAVPLACE_TABLE_:
			rowsDeleted = sqlDB.delete(FavPlacesTable.FAVPLACE_TABLE,
					selection, selectionArgs);
			break;
		case FAVPLACE_TABLE_ROW:
			id = uri.getLastPathSegment();
			if (TextUtils.isEmpty(selection)) {
				rowsDeleted = sqlDB.delete(FavPlacesTable.FAVPLACE_TABLE,
						FavPlacesTable.FAVPLACE_TABLE + "=" + id, null);
			} else {
				rowsDeleted = sqlDB.delete(FavPlacesTable.FAVPLACE_TABLE,
						FavPlacesTable.FAVPLACE_TABLE + "=" + id + " and "
								+ selection, selectionArgs);
			}
			break;
		default:
			throw new IllegalArgumentException("Unknown URI: " + uri);
		}
		getContext().getContentResolver().notifyChange(uri, null);
		return rowsDeleted;

	}

	@Override
	public String getType(Uri uri) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Uri insert(Uri uri, ContentValues values) {
		// TODO Auto-generated method stub

		int uriType = sURIMatcher.match(uri);
		SQLiteDatabase sqlDB = tableHelper.getWritableDatabase();
		String basePath;
		// int rowsDeleted = 0;
		long id;
		switch (uriType) {
		case FAVPLACE_TABLE_:
			id = sqlDB.insert(FavPlacesTable.FAVPLACE_TABLE, null, values);
			basePath = FAVPLACE_TABLE_BASE_PATH;
			break;

		default:
			throw new IllegalArgumentException("Unknown URI: " + uri);
		}
		getContext().getContentResolver().notifyChange(uri, null);
		return Uri.parse(basePath + "/" + id);

	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {

		SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
		String tableName = FavPlacesTable.FAVPLACE_TABLE;
		queryBuilder.setTables(tableName);
		SQLiteDatabase database = tableHelper.getWritableDatabase();

		int uriType = sURIMatcher.match(uri);
		switch (uriType) {
		case FAVPLACE_TABLE_:
			break;
		case FAVPLACE_TABLE_ROW:
			queryBuilder.appendWhere(FavPlacesTable.COLUMN__ID + "="
					+ uri.getLastPathSegment());
			break;
		default:
			throw new IllegalArgumentException("Unknown URI: " + uri);
		}

		Cursor cursor = queryBuilder.query(database, projection, selection,
				selectionArgs, null, null, sortOrder);
		cursor.setNotificationUri(getContext().getContentResolver(), uri);

		return cursor;

	}

	@Override
	public int update(Uri uri, ContentValues values, String selection,
			String[] selectionArgs) {
		// TODO Auto-generated method stub
		int uriType = sURIMatcher.match(uri);
		SQLiteDatabase sqlDB = tableHelper.getWritableDatabase();
		int rowsUpdated;
		String id;
		switch (uriType) {
		case FAVPLACE_TABLE_:
			rowsUpdated = sqlDB.update(FavPlacesTable.FAVPLACE_TABLE, values,
					selection, selectionArgs);
			break;
		case FAVPLACE_TABLE_ROW:
			id = uri.getLastPathSegment();
			if (TextUtils.isEmpty(selection)) {
				rowsUpdated = sqlDB.update(FavPlacesTable.FAVPLACE_TABLE,
						values, FavPlacesTable.COLUMN__ID + "=" + id, null);
			} else {
				rowsUpdated = sqlDB.update(FavPlacesTable.FAVPLACE_TABLE,
						values, FavPlacesTable.COLUMN__ID + "=" + id + " and "
								+ selection, selectionArgs);
			}
			break;
		default:
			throw new IllegalArgumentException("Unknown URI: " + uri);
		}
		getContext().getContentResolver().notifyChange(uri, null);
		return rowsUpdated;
	}

}
