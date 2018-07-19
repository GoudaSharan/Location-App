package com.example.mtap.locationapp.sync;

import android.content.ContentResolver;
import android.content.Context;
import android.content.SyncResult;
import android.os.Bundle;

public interface ISyncHelper {
    void performSync(Context context, ContentResolver contentResolver, SyncResult syncResult,
                     Bundle extras);
}
