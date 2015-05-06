package rajiv.imena.demo.db;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class FavPlacesTable {

	public static final String FAVPLACE_TABLE = "favplacetable";
	public static final String COLUMN__ID = "_id";
	public static final String COLUMN_PID = "pid";
	public static final String COLUMN_PLACE_ID = "placeid";
	public static final String COLUMN_PLACE_NAME = "placename";
	public static final String COLUMN_PLACE_ADDRESS = "placeaddress";
	public static final String COLUMN_PLACE_ICON = "placeicon";
	public static final String COLUMN_PLACE_LAT = "lat";
	public static final String COLUMN_PLACE_LNG = "lng";

	private static final String CREATE_TABLE = "create table IF NOT EXISTS "
			+ FAVPLACE_TABLE + "(" + COLUMN__ID
			+ " integer primary key autoincrement," + COLUMN_PID
			+ " text not null," + COLUMN_PLACE_ID + " text not null, "
			+ COLUMN_PLACE_NAME + " text not null, " + COLUMN_PLACE_ADDRESS
			+ " text not null, " + COLUMN_PLACE_ICON + " text not null, "
			+ COLUMN_PLACE_LAT + " text not null, " + COLUMN_PLACE_LNG
			+ " text not null, " + "UNIQUE (" + COLUMN_PLACE_ID
			+ ") ON CONFLICT IGNORE );";

	public static void onCreate(SQLiteDatabase database) {
		database.execSQL(CREATE_TABLE);
	}

	public static void onUpgrade(SQLiteDatabase database, int oldVersion,
			int newVersion) {
		Log.w(FavPlacesTable.class.getName(),
				"Upgrading database from version " + oldVersion + " to "
						+ newVersion + ", which will destroy all old data");
		database.execSQL("DROP TABLE IF EXISTS " + FAVPLACE_TABLE);
		onCreate(database);
	}

}
