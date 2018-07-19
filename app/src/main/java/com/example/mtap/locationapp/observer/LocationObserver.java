package com.example.mtap.locationapp.observer;

import android.content.ContentResolver;
import android.database.ContentObserver;
import android.net.Uri;

import java.util.logging.Handler;

public class LocationObserver extends ContentObserver {
    // The account name
    public static final String ACCOUNT = "dummyaccount";
    // The authority for the sync adapter's content provider
    public static final String AUTHORITY = "com.example.mtap.locationapp.localstore";
    public LocationObserver(android.os.Handler handler){
      super(handler);
    }

    /*
     * Define a method that's called when data in the
     * observed content provider changes.
     * This method signature is provided for compatibility with
     * older platforms.
     */
    @Override
    public void onChange(boolean selfChange) {
        /*
         * Invoke the method signature available as of
         * Android platform version 4.1, with a null URI.
         */
        onChange(selfChange, null);
    }
    /*
     * Define a method that's called when data in the
     * observed content provider changes.
     */
    @Override
    public void onChange(boolean selfChange, Uri changeUri) {
        /*
         * Ask the framework to run your sync adapter.
         * To maintain backward compatibility, assume that
         * changeUri is null.
         */
      //  ContentResolver.requestSync(ACCOUNT, AUTHORITY, null);
    }

}
