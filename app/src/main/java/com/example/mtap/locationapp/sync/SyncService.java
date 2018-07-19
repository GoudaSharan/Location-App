package com.example.mtap.locationapp.sync;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class SyncService extends Service {

    private static final String TAG = "SyncService";

    private static final Object S_SYNC_ADAPTER_LOCK = new Object();
    private static StoreSyncAdapter sSyncAdapter = null;

    /**
     * Thread-safe constructor, creates static {@link StoreSyncAdapter} instance.
     */
    @Override
    public void onCreate() {
        super.onCreate();
        synchronized (S_SYNC_ADAPTER_LOCK) {
            if (sSyncAdapter == null) {
                sSyncAdapter = new StoreSyncAdapter(getApplicationContext(), true);
            }
        }
    }

    @Override
    /**
     * Logging-only destructor.
     */
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return sSyncAdapter.getSyncAdapterBinder();
    }
}
