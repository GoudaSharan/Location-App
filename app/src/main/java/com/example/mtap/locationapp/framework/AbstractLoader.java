package com.example.mtap.locationapp.framework;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;

import com.example.mtap.locationapp.model.Location;
import com.example.mtap.locationapp.provider.LocalStoreContract;

public abstract class AbstractLoader implements LoaderManager.LoaderCallbacks<Cursor> {
    private static final String TAG = AbstractLoader.class.getSimpleName();
    public static final int LOCATION_DATA = 0;
    private LoaderManager supportLoaderManager;
    private Cursor lastCursorRef;
    private CallBack callback;
    private Context context;

    public AbstractLoader(Context context, LoaderManager supportLoaderManager) {
        this.context = context;
        this.supportLoaderManager = supportLoaderManager;
    }

    public void setCallback(CallBack callback) {
        this.callback = callback;
    }

    protected void loadRequest(int requestId, CursorRequest cursorRequest) {
        Bundle args = cursorRequest.toArgs();
        this.supportLoaderManager.initLoader(requestId, args, this);
    }

    public RequestBuilder requestor() {
        return new RequestBuilder(this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        switch (id) {
            case LOCATION_DATA:
                return new CursorLoader(context,
                        LocalStoreContract.LocationStore.CONTENT_URI,
                        Location.PROJECTION, null, null, null);
            default:
                return null;
        }
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        lastCursorRef = data;
        if (callback != null)
            callback.onCursorAvailable(loader.getId(), data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        if (lastCursorRef != null && !lastCursorRef.isClosed())
            lastCursorRef.close();
        lastCursorRef = null;
    }

    protected CursorRequest argsToRequest(Bundle args) {
        String selection = args.getString(CursorRequest.SELECTION);
        String[] selectionArgs = args.getStringArray(CursorRequest.SELECTION_ARGS);
        String sortOrder = args.getString(CursorRequest.SORT_ORDER);
        return new CursorRequest(selection, selectionArgs, sortOrder);
    }

    public static class CursorRequest {
        static final String SELECTION = "selection";
        static final String SELECTION_ARGS = "selection_args";
        static final String SORT_ORDER = "sort_order";

        public String selection;
        public String[] selectionArgs;
        public String sortOrder;

        public CursorRequest(String selection, String[] selectionArgs, String sortOrder) {
            this.selection = selection;
            this.selectionArgs = selectionArgs;
            this.sortOrder = sortOrder;
        }

        public Bundle toArgs() {
            Bundle args = new Bundle();
            args.putString(SELECTION, selection);
            args.putStringArray(SELECTION_ARGS, selectionArgs);
            args.putString(SORT_ORDER, sortOrder);
            return args;
        }
    }

    public static class RequestBuilder {
        private String selection;
        private String[] selectionArgs;
        private String sortOrder;
        private AbstractLoader loader;

        public RequestBuilder(AbstractLoader loader) {
            this.loader = loader;
        }

        public RequestBuilder select(String selection, String[] selectionArgs) {
            this.selection = selection;
            this.selectionArgs = selectionArgs;
            return this;
        }

        public RequestBuilder sortOrder(String sortOrder) {
            this.sortOrder = sortOrder;
            return this;
        }

        public AbstractLoader loadRequest(int content) {
            CursorRequest cursorRequest = new CursorRequest(selection, selectionArgs, sortOrder);
            loader.loadRequest(content, cursorRequest);
            return loader;
        }
    }

    public interface CallBack {
        void onCursorAvailable(int requestID, Cursor cursor);
    }
}