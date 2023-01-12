package com.pekam.gpstrackman.viewmodels;

import android.location.Location;
import androidx.lifecycle.*;
import com.google.android.gms.maps.model.LatLng;

import java.util.Objects;

public class LocationViewModel extends ViewModel {

    private Location mLocation = null;
    private final MutableLiveData<Location> locationMutableLiveData = new MutableLiveData<>();



    public LocationViewModel() {
        super();

    }

    public MutableLiveData<Location> getLastLocation() {

        return locationMutableLiveData;
    }

    public LatLng getLastLocationLatLng() {

        LatLng lt = new LatLng(Objects.requireNonNull(locationMutableLiveData.getValue()).getLatitude(), locationMutableLiveData.getValue().getLongitude());
        return lt;

    }
}



