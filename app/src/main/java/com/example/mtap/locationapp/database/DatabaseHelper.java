package com.example.mtap.locationapp.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import com.example.mtap.locationapp.interfaces.IRefreshLocationListListener;
import com.example.mtap.locationapp.model.Location;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {
    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "location_db";
private Context mContext;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        mContext=context;
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {

        // create notes table
        db.execSQL(Location.CREATE_TABLE);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + Location.TABLE_NAME);

        // Create tables again
        onCreate(db);
    }

    public long insertLocation(Location location) {
        // get writable database as we want to write data
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        // `id` and `timestamp` will be inserted automatically.
        // no need to add them
        values.put(Location.COLUMN_LAT, location.getLat());
        values.put(Location.COLUMN_LNG, location.getLng());
        // insert row
        long id = db.insert(Location.TABLE_NAME, null, values);
if(id>=1.0){

    try {
        IRefreshLocationListListener locationListListener=
                (IRefreshLocationListListener)mContext;
        if(locationListListener!=null){
            locationListListener.onInsertionSuccess(location);

        }
    } catch (ClassCastException e) {
        Toast.makeText(mContext, "Exception occured", Toast.LENGTH_SHORT).show();
    }

}
        // close db connection
        if(db!=null){
            db.close();
        }

        // return newly inserted row id
        return id;
    }

    public List<Location> getAllLocations() {
        List<Location> locations = new ArrayList<>();

        // Select All Query
        String selectQuery = "SELECT  * FROM " + Location.TABLE_NAME;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

       if(cursor!=null){
           // looping through all rows and adding to list
           if (cursor.moveToFirst()) {
               do {
                   Location location = new Location();
                   // location.setId(cursor.getInt(cursor.getColumnIndex(Note.COLUMN_ID)));
                   location.setLat(cursor.getString(cursor.getColumnIndex(Location.COLUMN_LAT)));
                   location.setLng(cursor.getString(cursor.getColumnIndex(Location.COLUMN_LNG)));;

                   locations.add(location);
               } while (cursor.moveToNext());
           }
       }

        // close db connection
       if(db!=null){
           db.close();
       }

        // return locations list
        return locations;
    }

}
