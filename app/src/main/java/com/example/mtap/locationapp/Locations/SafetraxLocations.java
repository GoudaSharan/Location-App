package com.example.mtap.locationapp.Locations;

import android.support.v7.app.AppCompatActivity;

import com.example.mtap.locationapp.framework.AbstractInfoPresenter;
import com.example.mtap.locationapp.model.Location;

import java.util.List;

public class SafetraxLocations implements AbstractInfoPresenter.InfoView<List<Location>> {

    private AppCompatActivity activity;
    private ILocationsListener listener;

    public SafetraxLocations(AppCompatActivity activity, ILocationsListener listener) {
        this.activity = activity;
        this.listener = listener;
    }

    public void loadLocations() {
        LocationsPresenter locationsPresenter = new LocationsPresenter(activity,
                activity.getSupportLoaderManager(), this);
        locationsPresenter.loadData();
    }

    @Override
    public void onDataAvailable(List<Location> data) {
        if (listener != null) {
            listener.onLocationsLoaded(data);
        }
    }
}