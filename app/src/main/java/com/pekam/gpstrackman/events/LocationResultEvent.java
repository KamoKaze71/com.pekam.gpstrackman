package com.pekam.gpstrackman.events;

import com.google.android.gms.location.LocationResult;

public class LocationResultEvent {

public LocationResult mLocationResult;

    public LocationResultEvent(LocationResult locationResult) { mLocationResult=locationResult; }

    public LocationResult getLocationResult() { return mLocationResult;  }
}

