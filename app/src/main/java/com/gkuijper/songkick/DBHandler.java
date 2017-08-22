package com.gkuijper.songkick;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Created by Sander on 8/21/2017.
 */

public class DBHandler extends SQLiteOpenHelper {
    private final String TAG = getClass().getSimpleName();

    private static final int DB_VERSION = 5;
    private static final String DB_NAME = "songkick.db";

    // Table names
    private static final String TABLE_EVENT = "event";
    private static final String TABLE_PERFORMANCE = "performance";
    private static final String TABLE_CITY = "city";



    // Columns event
    private static final String COLUMN_EVENT_ID = "event_id";
    private static final String COLUMN_EVENT_NAME = "event_name";
    private static final String COLUMN_EVENT_TYPE = "event_type";
    private static final String COLUMN_EVENT_VENUE = "event_venue";
    private static final String COLUMN_EVENT_STARTDATE = "event_startdate";
    private static final String COLUMN_EVENT_TIME = "event_time";
    private static final String COLUMN_EVENT_ENDDATE = "event_enddate";


    // Columns performance
    private static final String COLUMN_PERFORMANCE_ID = "performance_id";
    private static final String COLUMN_PERFORMANCE_ARTIST = "performance_artist";
    private static final String COLUMN_PERFORMANCE_BILLING = "performance_billing";
    private static final String COLUMN_PERFORMANCE_EVENTID = "performance_eventid";

    // Columns city
    private static final String COLUMN_CITY_ID = "city_id";
    private static final String COLUMN_CITY_NAME ="city_name";
    private static final String COLUMN_CITY_COUNTRY ="city_country";
    private static final String COLUMN_CITY_EVENTID = "city_eventid";

    public DBHandler(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.i(TAG, "onCreate called.");

        String CREATE_TABLE;

        //Create table event
        CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_EVENT +
                "(" +
                COLUMN_EVENT_ID + " INTEGER PRIMARY KEY," +
                COLUMN_EVENT_NAME + " TEXT," +
                COLUMN_EVENT_TYPE + " TEXT," +
                COLUMN_EVENT_VENUE + " TEXT," +
                COLUMN_EVENT_STARTDATE + " TEXT," +
                COLUMN_EVENT_TIME + " TEXT," +
                COLUMN_EVENT_ENDDATE + " TEXT" +
                ")";

        db.execSQL(CREATE_TABLE);


        CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_PERFORMANCE +
                "(" +
                COLUMN_PERFORMANCE_ID + " INTEGER PRIMARY KEY," +
                COLUMN_PERFORMANCE_ARTIST + " TEXT," +
                COLUMN_PERFORMANCE_BILLING + " TEXT," +
                COLUMN_PERFORMANCE_EVENTID + " INTEGER" +
                ")";

        db.execSQL(CREATE_TABLE);

        CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_CITY +
                "(" +
                COLUMN_CITY_ID + " INTEGER PRIMARY KEY," +
                COLUMN_CITY_NAME + " TEXT," +
                COLUMN_CITY_COUNTRY + " TEXT," +
                COLUMN_CITY_EVENTID + " TEXT" +
                ")";

        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_EVENT);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PERFORMANCE);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CITY);

        onCreate(db);

    }

    public ArrayList<Event> getAllEvents() {
        Log.i(TAG, "getAllEvents");

        String query = "SELECT * FROM " + TABLE_EVENT;

        Log.i(TAG, "Query: " + query);

        ArrayList<Event> result = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        Event event;
        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {

            event = new Event();
            event.setEventID(cursor.getString(cursor.getColumnIndex(COLUMN_EVENT_ID)));
            event.setName(cursor.getString(cursor.getColumnIndex(COLUMN_EVENT_NAME)));
            event.setVenue(cursor.getString(cursor.getColumnIndex(COLUMN_EVENT_VENUE)));
            event.setType(cursor.getString(cursor.getColumnIndex(COLUMN_EVENT_TYPE)));
            event.setStartdate(cursor.getString(cursor.getColumnIndex(COLUMN_EVENT_STARTDATE)));
            event.setTime(cursor.getString(cursor.getColumnIndex(COLUMN_EVENT_TIME)));
            event.setEnddate(cursor.getString(cursor.getColumnIndex(COLUMN_EVENT_ENDDATE)));

            event.setPerformances(getPerformances(Integer.parseInt(event.getEventID())));
            event.setCity(getCity(Integer.parseInt(event.getEventID())));

            Log.i(TAG, "Found " + event + ", adding to list");
            event.setSaved(true);
            result.add(event);
        }

        db.close();
        Log.i(TAG, "Returning " + result.size() + " items");
        return result;
    }

    public ArrayList<Performance> getPerformances(int id) {

        String query = "SELECT * FROM " + TABLE_PERFORMANCE + " WHERE " + COLUMN_PERFORMANCE_EVENTID + " = " + id;

        Log.i(TAG, "Query: " + query);
        ArrayList<Performance> performances = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        Performance performance;
        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {

            performance = new Performance();
            performance.setName(cursor.getString(cursor.getColumnIndex(COLUMN_PERFORMANCE_ARTIST)));
            performance.setBilling(cursor.getString(cursor.getColumnIndex(COLUMN_PERFORMANCE_BILLING)));

            Log.i(TAG, "Found " + performance + ", adding to list");
            performances.add(performance);
        }

        db.close();
        Log.i(TAG, "Returning " + performances.size() + " items");
        return performances;


    }
    public Long addEvent(Event event) {
        Log.i(TAG, "addEvent " + event);

        ContentValues values = new ContentValues();
        values.put(COLUMN_EVENT_NAME, event.getName());
        values.put(COLUMN_EVENT_ID, event.getEventID());
        values.put(COLUMN_EVENT_TYPE, event.getType());
        values.put(COLUMN_EVENT_VENUE, event.getVenue());
        values.put(COLUMN_EVENT_STARTDATE, event.getStartdate());
        values.put(COLUMN_EVENT_ENDDATE, event.getEnddate());
        values.put(COLUMN_EVENT_TIME, event.getTime());

        addPerformance(event);
        addCity(event);

        Log.i(TAG, "Event with id " + event.getEventID() + " created");

        SQLiteDatabase db = this.getWritableDatabase();
        return db.insert(TABLE_EVENT, null, values);
    }

    public String addPerformance(Event event) {
        ArrayList<Performance> performances = event.getPerformances();
        Log.i(TAG, "addPerformances " + performances.toString());

        for(int i = 0; i < performances.size(); i++) {
            ContentValues values = new ContentValues();
            values.put(COLUMN_PERFORMANCE_ARTIST, performances.get(i).getName());
            values.put(COLUMN_PERFORMANCE_BILLING, performances.get(i).getBilling());
            values.put(COLUMN_PERFORMANCE_EVENTID, event.getEventID());

            Log.i(TAG, "Performancs with " + event.getEventID() + " added");
            SQLiteDatabase db = this.getWritableDatabase();
            db.insert(TABLE_PERFORMANCE, null, values);
        }
        return performances.toString();
    }

    public String addCity(Event event) {
        City city = event.getCity();
        Log.i(TAG, "addCity " + city.toString());

            ContentValues values = new ContentValues();
            values.put(COLUMN_CITY_NAME, city.getName());
            values.put(COLUMN_CITY_COUNTRY, city.getCountry());
            values.put(COLUMN_CITY_EVENTID, event.getEventID());

            Log.i(TAG, "City with " + event.getEventID() + " added");
            SQLiteDatabase db = this.getWritableDatabase();
            db.insert(TABLE_CITY, null, values);

        return city.toString();
    }

    public City getCity(int id) {

        String query = "SELECT * FROM " + TABLE_CITY + " WHERE " + COLUMN_CITY_EVENTID + " = " + id;

        Log.i(TAG, "Query: " + query);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        City city = new City();
        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
            city.setName(cursor.getString(cursor.getColumnIndex(COLUMN_CITY_NAME)));
            city.setCountry(cursor.getString(cursor.getColumnIndex(COLUMN_CITY_COUNTRY)));

            Log.i(TAG, "Found " + city.toString());
        }

        db.close();
        return city;
    }



}
