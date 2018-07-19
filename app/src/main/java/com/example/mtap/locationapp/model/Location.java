package com.example.mtap.locationapp.model;

import android.database.Cursor;

public class Location {
    String lat;
    String lng;

   /* public Location(String lat, String lng) {
        this.lat = lat;
        this.lng = lng;
    }*/

    public static final String TABLE_NAME = "location";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_LAT = "lat";
    public static final String COLUMN_LNG = "lng";

    public static final String[] ALL_COLUMNS =
            {COLUMN_ID,COLUMN_LAT,COLUMN_LNG};

    // Create table SQL query
    public static final String CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME + "("
                    + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + COLUMN_LAT + " TEXT,"
                    + COLUMN_LNG + " TEXT"
                    + ")";


    public static final String[] PROJECTION = new String[]{
            COLUMN_LAT, COLUMN_LNG
    };

    public static Location fromCursor(Cursor cursor) {
        Location location = new Location();
        location.lat = cursor.getString(cursor.getColumnIndex(COLUMN_LAT));
        location.lng = cursor.getString(cursor.getColumnIndex(COLUMN_LNG));
        return location;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLng() {
        return lng;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }
}
