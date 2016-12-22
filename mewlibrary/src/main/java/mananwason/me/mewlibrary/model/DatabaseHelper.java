package mananwason.me.mewlibrary.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by apurv on 10/1/2015.
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String LOG = "DatabaseHelper";

    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "MewDb";

    // Table Names
    private static final String TABLE_QUERY = "query";

    // Query Table - column names
    private static final String KEY_ID = "_id";
    private static final String KEY_QUERY_NUMBER = "query_no";
    private static final String KEY_START_TIME = "start_time";
    private static final String KEY_END_TIME = "end_time";
    private static final String KEY_SENSOR_NAME = "sensor_name";
    private static final String KEY_USER_ACTIVITY = "user_activity";
    private static final String KEY_DEVICE_CHARGING = "device_state";
    private static final String KEY_ROUTING_KEY = "routing_key";
    private static final String KEY_PROCESSED = "processed";


    // Table Create Statements
    // Query table create statement
    private static final String CREATE_TABLE_QUERY = "CREATE TABLE "
            + TABLE_QUERY + "(" + KEY_ID + " INTEGER PRIMARY KEY," + KEY_QUERY_NUMBER
            + " TEXT," + KEY_START_TIME + " TEXT," + KEY_END_TIME + " TEXT," + KEY_SENSOR_NAME + " TEXT,"
            + KEY_DEVICE_CHARGING + " INTEGER DEFAULT 0," + KEY_USER_ACTIVITY + " INTEGER,"
            + KEY_ROUTING_KEY + " TEXT," + KEY_PROCESSED + " TEXT" + ")";


    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // creating required tables
        db.execSQL(CREATE_TABLE_QUERY);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        // on upgrade drop older tables
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_QUERY);
        onCreate(db);
    }

    /*
     * Adding a new query
     */
    public long addQuery(QueryModel query) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        Log.d("ADD Q", query.getQueryNo() + "/" + query.getSensors());
        values.put(KEY_QUERY_NUMBER, query.getQueryNo());
        values.put(KEY_START_TIME, String.valueOf(query.getStartTime()));
        values.put(KEY_END_TIME, String.valueOf(query.getEndTime()));
        values.put(KEY_SENSOR_NAME, query.getSensors());
        values.put(KEY_ROUTING_KEY, query.getRoutingKey());
        values.put(KEY_PROCESSED, query.getProcessed());
        values.put(KEY_USER_ACTIVITY, query.getUserActivity());
        values.put(KEY_DEVICE_CHARGING, query.getDeviceState());

        // insert row
        long query_id = db.insert(TABLE_QUERY, null, values);

        return query_id;
    }

    public QueryModel getQueryByNumber(String queryNo) {
        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT  * FROM " + TABLE_QUERY + " WHERE "
                + KEY_QUERY_NUMBER + " = " + "'" + queryNo + "'";
//        Log.d(LOG, selectQuery);

        Cursor c = db.rawQuery(selectQuery, null);
        QueryModel query = new QueryModel();
        if (c != null && c.moveToFirst() && (c.getCount() > 0)) {

            query.setId(c.getInt(c.getColumnIndex(KEY_ID)));
            query.setQueryNo(c.getString(c.getColumnIndex(KEY_QUERY_NUMBER)));
            query.setStartTime(Long.parseLong(c.getString(c.getColumnIndex(KEY_START_TIME))));
            query.setEndTime(Long.parseLong(c.getString(c.getColumnIndex(KEY_END_TIME))));
            query.setSensors(c.getString(c.getColumnIndex(KEY_SENSOR_NAME)));
            query.setRoutingKey(c.getString(c.getColumnIndex(KEY_ROUTING_KEY)));
            query.setProcessed(c.getInt(c.getColumnIndex(KEY_PROCESSED)));
            //return -1 if the activity/deviceCharging field is empty
            int userActivity;
            if (c.isNull(c.getColumnIndex(KEY_USER_ACTIVITY)))
                userActivity = -1;
            else
                userActivity = c.getInt(c.getColumnIndex(KEY_USER_ACTIVITY));
            query.setUserActivity(userActivity);
            //return -1 if the activity/deviceCharging field is empty
            int deviceState;
            if (c.isNull(c.getColumnIndex(KEY_DEVICE_CHARGING)))
                deviceState = -1;
            else
                deviceState = c.getInt(c.getColumnIndex(KEY_DEVICE_CHARGING));
            query.setDeviceState(deviceState);
            c.close();
            return query;
        }
        Log.e(LOG, "Returning null QueryObject Query#" + queryNo + " and " + selectQuery);
        return null;
    }

    public QueryModel getQueryByID(int queryId) {
        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT  * FROM " + TABLE_QUERY + " WHERE "
                + KEY_QUERY_NUMBER + " = " + queryId;
//        Log.d(LOG, selectQuery);

        Cursor c = db.rawQuery(selectQuery, null);

        if (c != null)
            c.moveToFirst();

        QueryModel query = new QueryModel();
        query.setId(c.getInt(c.getColumnIndex(KEY_ID)));
        query.setQueryNo(c.getString(c.getColumnIndex(KEY_QUERY_NUMBER)));
        query.setStartTime(c.getInt(c.getColumnIndex(KEY_START_TIME)));
        query.setEndTime(c.getInt(c.getColumnIndex(KEY_END_TIME)));
        query.setSensors(c.getString(c.getColumnIndex(KEY_SENSOR_NAME)));
        query.setRoutingKey(c.getString(c.getColumnIndex(KEY_ROUTING_KEY)));
        query.setProcessed(c.getInt(c.getColumnIndex(KEY_PROCESSED)));
        //return -1 if the activity/deviceCharging field is empty
        int userActivity;
        if (c.isNull(c.getColumnIndex(KEY_USER_ACTIVITY)))
            userActivity = -1;
        else
            userActivity = c.getInt(c.getColumnIndex(KEY_USER_ACTIVITY));
        query.setUserActivity(userActivity);
        //return -1 if the activity/deviceCharging field is empty
        int deviceState;
        if (c.isNull(c.getColumnIndex(KEY_DEVICE_CHARGING)))
            deviceState = -1;
        else
            deviceState = c.getInt(c.getColumnIndex(KEY_DEVICE_CHARGING));
        query.setDeviceState(deviceState);
        return query;
    }

    public void deleteQuery(int queryId) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_QUERY, KEY_ID + " = ?",
                new String[]{String.valueOf(queryId)});
        db.close();
    }


    // closing database
    public void closeDb() {
        SQLiteDatabase db = this.getReadableDatabase();
        if (db != null && db.isOpen())
            db.close();
    }
}
