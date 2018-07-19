package com.example.mtap.locationapp.Locations;

import com.example.mtap.locationapp.model.Location;

import java.util.List;

public interface ILocationsListener {
    void onLocationsLoaded(List<Location> locationList);
}
