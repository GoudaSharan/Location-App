package com.example.mtap.locationapp.provider;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

import com.example.mtap.locationapp.model.Location;

public class LocalStoreContract {

    public static final String CONTENT_AUTHORITY = "com.example.mtap.locationapp.localstore";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    /**
     * Constants used for the building content URI path.
     */
    static final String PATH_LOCATIONS = "location";

    private static final String TYPE_TEXT = " TEXT";
    private static final String TYPE_INTEGER = " INTEGER";
    private static final String CONSTRAINT_NOT_NULL = " NOT NULL";
    private static final String CONSTRAINT_UNIQUE = " UNIQUE";
    private static final String COMMA_DELIMITER = ",";

    static final String[] CREATE_TABLE_QUERIES = new String[]{
            LocationStore.CREATE_QUERY
    };
    static final String[] DROP_TABLE_QUERIES = new String[]{
            LocationStore.DROP_QUERY
    };

    public interface LocationStore extends BaseColumns {

        String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE
                + "/vnd.employee.io.locations";
        Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon()
                .appendPath(PATH_LOCATIONS).appendPath("all").build();

        String TABLE_NAME = "location";

        String CREATE_QUERY = "CREATE TABLE " + TABLE_NAME
                + " (" + _ID + TYPE_INTEGER + " PRIMARY KEY" + COMMA_DELIMITER
                + Location.COLUMN_LAT + TYPE_TEXT
                + COMMA_DELIMITER
                + Location.COLUMN_LNG + TYPE_TEXT + ")";
        String DROP_QUERY = "DROP TABLE IF EXISTS " + TABLE_NAME;
    }
}