package com.example.mtap.locationapp.sync;

import android.content.ContentResolver;
import android.content.Context;
import android.content.SyncResult;
import android.database.Cursor;
import android.os.Bundle;

import com.example.mtap.locationapp.model.Location;
import com.example.mtap.locationapp.provider.LocalStoreContract;

public class StoreSyncHelper implements ISyncHelper {
    @Override
    public void performSync(Context context, ContentResolver contentResolver,
                            SyncResult syncResult, Bundle extras) {


        Cursor cursor=contentResolver.query(LocalStoreContract.LocationStore.CONTENT_URI,
                Location.PROJECTION,
                null,null,null);




    }
}
