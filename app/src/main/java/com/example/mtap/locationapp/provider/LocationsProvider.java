package com.example.mtap.locationapp.provider;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.example.mtap.locationapp.BuildConfig;
import com.example.mtap.locationapp.database.DatabaseHelper;
import com.example.mtap.locationapp.model.Location;

public class LocationsProvider extends ContentProvider {
   // private static final String AUTHORITY = "com.locationapp.mylocations";
    private static final String BASE_PATH = "locations";


    private static final int CONTACTS = 1;
    private static final int CONTACT_ID = 2;

   /* private static final UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
    static {
        uriMatcher.addURI(AUTHORITY,BASE_PATH, CONTACTS);
        uriMatcher.addURI(AUTHORITY,BASE_PATH + "/#",CONTACT_ID);
    }*/

 //   public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + BASE_PATH );
    public static final String AUTHORITY = BuildConfig.APPLICATION_ID + ".db";

    private static final int
            TABLE_ITEMS = 0;
    private static final UriMatcher sUriMatcher;
    static {
        sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        sUriMatcher.addURI(AUTHORITY, Location.TABLE_NAME + "/offset/" + "#", TABLE_ITEMS);
    }

    public static Uri urlForItems(int limit) {
        return Uri.parse("content://" + AUTHORITY + "/" + Location.TABLE_NAME  + "/offset/" + limit);
    }

    private SQLiteDatabase database;


    @Override
    public boolean onCreate() {
        DatabaseHelper helper = new DatabaseHelper(getContext());
        database = helper.getWritableDatabase();
        return true;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] strings, String s, String[] strings1, String s1) {
        Cursor cursor;
        switch (sUriMatcher.match(uri)) {
            case TABLE_ITEMS:


                // Select All Query
                String selectQuery = "SELECT  * FROM " + Location.TABLE_NAME;


                 cursor = database.rawQuery(selectQuery, null);

                //cursor =  database.query(Location.TABLE_NAME,Location.ALL_COLUMNS, "select *",null,null,null,Location.COLUMN_ID +" DESC");
                break;
            default:
                throw new IllegalArgumentException("This is an Unknown URI " + uri);
        }
        cursor.setNotificationUri(getContext().getContentResolver(),uri);

        return cursor;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {

      /*  switch (uriMatcher.match(uri)) {
            case TABLE_ITEMS:
                return "vnd.android.cursor.dir/contacts";
            default:
                throw new IllegalArgumentException("This is an Unknown URI " + uri);
        }*/
      return  null;
    }


    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {
        long id = database.insert(Location.TABLE_NAME,null,contentValues);

     /*   if (id > 0) {
            Uri _uri = ContentUris.withAppendedId(Uri.parse("content://" + AUTHORITY + "/" + Location.TABLE_NAME  + "/offset/" + "0", id);
            getContext().getContentResolver().notifyChange(_uri, null);
            return _uri;
        }*/
        throw new SQLException("Insertion Failed for URI :" + uri);

    }

    @Override
    public int delete(Uri uri, String s, String[] strings) {
        int delCount = 0;
        switch (sUriMatcher.match(uri)) {
            case CONTACTS:
                delCount =  database.delete(Location.TABLE_NAME,s,strings);
                break;
            default:
                throw new IllegalArgumentException("This is an Unknown URI " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return delCount;
    }

    @Override
    public int update(Uri uri, ContentValues contentValues, String s, String[] strings) {
        int updCount = 0;
        switch (sUriMatcher.match(uri)) {
            case CONTACTS:
                updCount =  database.update(Location.TABLE_NAME,contentValues,s,strings);
                break;
            default:
                throw new IllegalArgumentException("This is an Unknown URI " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return updCount;
    }
}
