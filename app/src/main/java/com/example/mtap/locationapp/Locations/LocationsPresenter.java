package com.example.mtap.locationapp.Locations;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.app.LoaderManager;

import com.example.mtap.locationapp.framework.AbstractInfoPresenter;
import com.example.mtap.locationapp.framework.AbstractLoader;
import com.example.mtap.locationapp.model.Location;

import java.util.ArrayList;
import java.util.List;

public class LocationsPresenter extends AbstractInfoPresenter<List<Location>> {

    private AbstractLoader abstractLoader;

    public LocationsPresenter(Context context, LoaderManager loaderManager,
                                 InfoView<List<Location>>
                                         infoView) {
        super(infoView);
        abstractLoader = new LocationLoader(context, loaderManager);
        abstractLoader.setCallback(this);
    }

    @Override
    protected List<Location> getData(int requestId, Cursor cursor) {
        List<Location> contacts = new ArrayList<>();
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            do {
                contacts.add(Location.fromCursor(cursor));
            } while (cursor.moveToNext());
        }
        return contacts;
    }

    @Override
    public void loadData() {
        abstractLoader.requestor().loadRequest(AbstractLoader.LOCATION_DATA);
    }
}
