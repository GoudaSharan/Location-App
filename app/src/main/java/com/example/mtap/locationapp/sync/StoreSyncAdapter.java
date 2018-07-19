package com.example.mtap.locationapp.sync;

import android.accounts.Account;
import android.annotation.TargetApi;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.ContentResolver;
import android.content.Context;
import android.content.SyncResult;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import java.io.IOException;
import java.util.Date;

public class StoreSyncAdapter extends AbstractThreadedSyncAdapter {
    private final ContentResolver mContentResolver;
    private static final String TAG = StoreSyncAdapter.class.getSimpleName();
    private StoreSyncHelper storeSyncHelper = new StoreSyncHelper();

    /**
     * Constructor. Obtains handle to content resolver for later use.
     */
    public StoreSyncAdapter(Context context, boolean autoInitialize) {
        super(context, autoInitialize);
        mContentResolver = context.getContentResolver();
    }

    /**
     * Constructor. Obtains handle to content resolver for later use.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public StoreSyncAdapter(Context context, boolean autoInitialize, boolean allowParallelSyncs) {
        super(context, autoInitialize, allowParallelSyncs);
        mContentResolver = context.getContentResolver();
    }

    @Override
    public void onPerformSync(Account account, Bundle extras, String authority,
                              ContentProviderClient provider, SyncResult syncResult) {
        storeSyncHelper.performSync(getContext(), mContentResolver, syncResult, extras);
    }

}
