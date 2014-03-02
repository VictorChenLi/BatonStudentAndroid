package ca.utoronto.ece1778.baton.util;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * not in use yet
 *  
 * @author Yi Zhao
 *
 */
public class DatabaseService extends SQLiteOpenHelper {

	private final String TABLE_GCM_USER_PROFILE = "CREATE TABLE GCM_USER_PROFILE (id integer primary key, gcm_register_id varchar(255))";

	private String mDatabaseName;

	public DatabaseService(Context context, String name, CursorFactory factory,
			int version) {
		super(context, name, factory, version);
		mDatabaseName = name;
	}

	@Override
	public void onCreate(SQLiteDatabase db) {

		if (mDatabaseName.equals(Constants.SQLLITE_STUDENT_DATABASE_NAME))
			db.execSQL(TABLE_GCM_USER_PROFILE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

	}

}