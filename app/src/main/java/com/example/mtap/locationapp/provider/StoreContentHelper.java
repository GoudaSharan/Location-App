package com.example.mtap.locationapp.provider;

import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;

public abstract class StoreContentHelper {
    protected UriMatcher mUriMatcher;
    protected SQLiteOpenHelper databaseHelper;
    private Context context;
    public StoreContentHelper(UriMatcher uriMatcher, String authority) {
        mUriMatcher = uriMatcher;
    }

    public void openConnection(Context context, SQLiteOpenHelper databaseHelper) {
        this.context = context;
        this.databaseHelper = databaseHelper;
    }

    public void closeConnection() {
        this.context = null;
        // Keep the database connection open all time.
        /*if (databaseHelper != null) {
            databaseHelper.close();
        }*/
    }

    protected Context getContext() {
        return context;
    }

    public abstract Cursor query(Uri uri, String[] projection, String selection,
                                 String[] selectionArgs,
                                 String sortOrder);

    public abstract String getType(Uri uri);

    public abstract Uri insert(Uri uri, ContentValues contentValues);

    public abstract int delete(Uri uri, String selection, String[] selectionArgs);

    public abstract int update(Uri uri, ContentValues contentValues, String selection,
                               String[] selectionArgs);
}