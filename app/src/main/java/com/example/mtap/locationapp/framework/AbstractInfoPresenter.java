package com.example.mtap.locationapp.framework;

import android.database.Cursor;
import android.util.Log;

public abstract class AbstractInfoPresenter<T> implements AbstractLoader.CallBack {
    private static final String TAG = AbstractInfoPresenter.class.getSimpleName();
    private InfoView<T> infoView;

    public AbstractInfoPresenter(InfoView<T> infoView) {
        this.infoView = infoView;
    }

    @Override public void onCursorAvailable(int requestId, Cursor cursor) {
        Log.d(TAG, "onCursorAvailable: ");
        infoView.onDataAvailable(getData(requestId, cursor));
    }

    protected abstract T getData(int requestId, Cursor cursor);

    /**
     * Called from the view that can build and initiate the data request via {@link AbstractLoader}
     */
    public abstract void loadData();

    public interface InfoView<M> {
        void onDataAvailable(M data);
    }
}
