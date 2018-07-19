package com.example.mtap.locationapp.provider;

import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;


public class LocationContentHelper extends StoreContentHelper {
    public static final int LOCATION = 0;
    private UriMatcher uriMatcher;

    public LocationContentHelper(UriMatcher uriMatcher, String authority) {
        super(uriMatcher, authority);
        this.uriMatcher = uriMatcher;
        uriMatcher.addURI(authority, LocalStoreContract.PATH_LOCATIONS + "/*", LOCATION);
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs,
                        String sortOrder) {
        int match = uriMatcher.match(uri);
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        switch (match) {
            case LOCATION:
                Cursor cursor = db.query(LocalStoreContract.LocationStore.TABLE_NAME, projection, selection,
                        selectionArgs, null, null, sortOrder);
                cursor.setNotificationUri(getContext().getContentResolver(), uri);
                return cursor;
            default:
                throw new UnsupportedOperationException("Unsupported URI : " + uri);
        }
    }

    @Override public String getType(Uri uri) {
        final int match = uriMatcher.match(uri);
        switch (match) {
            case LOCATION:
                return LocalStoreContract.LocationStore.CONTENT_ITEM_TYPE;
            default:
                throw new UnsupportedOperationException("Unsupported URI: " + uri);
        }
    }

    @Override public Uri insert(Uri uri, ContentValues contentValues) {
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        int match = uriMatcher.match(uri);
        switch (match) {
            case LOCATION:
                db.insertOrThrow(LocalStoreContract.LocationStore.TABLE_NAME, null, contentValues);
                break;
            default:
                throw new UnsupportedOperationException("Unsupported URI: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null, false);
        return uri;
    }

    @Override public int delete(Uri uri, String selection, String[] selectionArgs) {
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        int match = uriMatcher.match(uri);
        int result;
        switch (match) {
            case LOCATION:
                result = db.delete(LocalStoreContract.LocationStore.TABLE_NAME, selection, selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unsupported URI: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null, false);
        return result;
    }

    @Override
    public int update(Uri uri, ContentValues contentValues, String selection, String[]
            selectionArgs) {
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        int match = uriMatcher.match(uri);
        int result;
        switch (match) {
            case LOCATION:
                result = db.update(LocalStoreContract.LocationStore.TABLE_NAME, contentValues, selection,
                        selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unsupported URI: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null, false);
        return result;
    }
}