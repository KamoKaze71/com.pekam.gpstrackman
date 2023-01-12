package com.pekam.gpstrackman.fragments;

import android.app.Activity;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import android.widget.RelativeLayout;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.gms.maps.*;
import com.google.android.gms.maps.model.*;

import com.google.maps.android.collections.GroundOverlayManager;
import com.google.maps.android.collections.PolygonManager;
import com.google.maps.android.collections.PolylineManager;
import com.pekam.gpstrackman.LocationServiceHttpRequests;
import com.pekam.gpstrackman.ScaleBar;
import com.pekam.gpstrackman.viewmodels.LocationViewModel;
import com.pekam.gpstrackman.viewmodels.UserViewModel;
import com.pekam.gpstrackman.Utils;
import com.pekam.gpstrackman.R;
import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import pekam.entities.TblGps;
import pekam.entities.TblGpsAll;

import java.time.Instant;
import java.util.*;



public class MapFragment extends com.google.android.gms.maps.SupportMapFragment implements OnMapReadyCallback {

    private GoogleMap mGoogleMap;
    private GoogleMapOptions mGoogleMapOptions;
    private final PolylineOptions mPlo = new PolylineOptions();
    private  Polyline mPl;

    private  Integer mZoomLevel=20;
    private LocationViewModel mLocationViewModel;
    private UserViewModel mUserViewModel;
    private ScaleBar mScaleBar;
    private FragmentManager mFm;
    private LocationServiceHttpRequests req;
    private GroundOverlayManager groundOverlayManager;
    public MapFragment() {        super();




    }

    @NotNull
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        this.getMapAsync(this);
        //return inflater.inflate(R.layout.fragment_maps_layout,container,false);
        return super.onCreateView(inflater, container, savedInstanceState);

    }

    @Override
    public void getMapAsync(@NonNull OnMapReadyCallback callback) {
        super.getMapAsync(callback);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    @Override
    public void onAttach(@NonNull Activity activity) {
        super.onAttach(activity);

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        req = new LocationServiceHttpRequests(getActivity(),getContext());
        mLocationViewModel = new ViewModelProvider(this.getActivity()).get(LocationViewModel.class);
        mUserViewModel = new ViewModelProvider(this.getActivity()).get(UserViewModel.class);

        Observer<List<Integer>> tracksToRenderObserver = new Observer<List<Integer>>() {
            /**
             * Called when the data is changed.
             *
             * @param longs The new data
             */
            @Override
            public void onChanged(List<Integer> longs) { renderSelectedTracks(); } };
                mUserViewModel.getTrackstoRender().observe(this.getActivity(), tracksToRenderObserver);

        Observer<Location> locationObserver = new Observer<Location>() {
            @Override
            public void onChanged(final Location location) {
                // Update the UI, in this case, a TextView.

                TblGpsAll gpsall = new TblGpsAll();

                gpsall.setLat(location.getLatitude());
                gpsall.setLng(location.getLongitude());
                gpsall.setDeviceID(Objects.requireNonNull(mUserViewModel.getUser().getValue()).getAppInstall());
                gpsall.setDeviceName(Utils.getDeviceName());
                gpsall.setAltitude(location.getAltitude());
                gpsall.setSpeed(location.getSpeed());
                gpsall.setDate(Instant.now());
                gpsall.setAccuracy(location.getAccuracy());
                try {
                    req.updateGpsAll(gpsall);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                LatLng lt = new LatLng(location.getLatitude(), location.getLongitude());


                if (Boolean.TRUE.equals(mUserViewModel.IsRecording().getValue()) && mGoogleMap != null) {
                    TblGps gps = Utils.getGps(location);
                    gps.setTbltrack(mUserViewModel.getUser().getValue().getTblTracks().get(0));
                    mUserViewModel.addNewGps(gps);
                    mPl.setPoints(Utils.getLatLng(mUserViewModel.getUser().getValue().getTblTracks().get(0).getTblgps()));

                }
                if (mGoogleMap != null) {

                    CircleOptions circleOptions = new CircleOptions();

                    // Specifying the center of the circle
                    circleOptions.center(lt);

                    // Radius of the circle
                    circleOptions.radius(location.getAccuracy());

                    // Border color of the circle
                    circleOptions.strokeColor(Color.BLACK);

                    // Fill color of the circle
                    circleOptions.fillColor(0x30ff0000);

                    // Border width of the circle
                    circleOptions.strokeWidth(2);

                    // Adding the circle to the GoogleMap
                    mGoogleMap.addCircle(circleOptions);

                    float zoom = mGoogleMap.getCameraPosition().zoom;
                    mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(lt, mZoomLevel));
                }
            }
        };
                mLocationViewModel.getLastLocation().observe(this.getActivity(), locationObserver);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);
        if (savedInstanceState==null) {
            mFm = this.getParentFragment().getChildFragmentManager();
            mFm.beginTransaction().add(R.id.navHost, new GpsStatusFragment(), "").commit();
        }

      }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onInflate(@NonNull Activity activity, @NonNull AttributeSet attrs, @Nullable Bundle savedInstanceState) {
        super.onInflate(activity, attrs, savedInstanceState);
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
     }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onStart() {
        super.onStart();

    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void setArguments(@Nullable Bundle args) {
        super.setArguments(args);
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mGoogleMap=googleMap;
      /*  GroundOverlayOptions grdOptions = new GroundOverlayOptions();

        GroundOverlayManager groundOverlayManager = new GroundOverlayManager(googleMap);
        groundOverlayManager.newCollection().addGroundOverlay(grdOptions);


        PolygonManager polygonManager = new PolygonManager(googleMap);
        PolylineManager polylineManager = new PolylineManager(googleMap);*/

        ScaleBar mScaleBar = new ScaleBar(getContext(), googleMap);
        mScaleBar.layout(70, 50, 50, 50);

        mScaleBar.setVisibility(View.VISIBLE);
        mScaleBar.bringToFront();
       /* groundOverlayManager = new GroundOverlayManager(googleMap);
        GroundOverlayManager.Collection c = groundOverlayManager.newCollection();
        c.showAll();*/
        UiSettings mUiSettings = googleMap.getUiSettings();

        // Keep the UI Settings state in sync with the checkboxes.

        mUiSettings.setZoomControlsEnabled(true);
        mUiSettings.setCompassEnabled(true);
        mUiSettings.setMyLocationButtonEnabled(true);
        mUiSettings.setScrollGesturesEnabled(true);
        mUiSettings.setZoomGesturesEnabled(true);
        mUiSettings.setTiltGesturesEnabled(true);
        mUiSettings.setRotateGesturesEnabled(true);
        mUiSettings.setMapToolbarEnabled(true);
        googleMap.getMaxZoomLevel();
        googleMap.getMinZoomLevel();

        googleMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
        googleMap.setMaxZoomPreference(26);
        googleMap.setMinZoomPreference(3);
        googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                                               @Override
                                               public boolean onMarkerClick(@NonNull @NotNull Marker marker) {
                                                   return false;
                                               }
                                           }
                                                );
        moveZoomControls();

        try {
            float zoom = mGoogleMap.getCameraPosition().zoom;
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mLocationViewModel.getLastLocationLatLng(), mZoomLevel));
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    /** set google map zoom controls */
    private void moveZoomControls() {
       final int ZoomControl_id = 0x1;
       final int MyLocation_button_id = 0x2;

        // Find ZoomControl view

        View zoomControls = this.getView().findViewById(ZoomControl_id);
        if (zoomControls != null && zoomControls.getLayoutParams() instanceof RelativeLayout.LayoutParams) {
            // ZoomControl is inside of RelativeLayout
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) zoomControls.getLayoutParams();

            // Align it to - parent top|left
            params.addRule(RelativeLayout.ALIGN_PARENT_TOP);
            params.addRule(RelativeLayout.ALIGN_PARENT_LEFT);

            // Update margins, set to 10dp
            final int margin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10, getResources().getDisplayMetrics());
            params.setMargins(margin, margin, margin, margin);
        }
    }

    /**
     * Styles the polyline, based on type.
     * @param polyline The polyline object that needs styling.
     */
    private void stylePolyline(Polyline polyline) {
        String type = "";
        // Get the data object stored with the polyline.
        if (polyline.getTag() != null) {
            type = polyline.getTag().toString();
        }
        polyline.setVisible(true);
        polyline.setJointType(JointType.ROUND);
        polyline.setGeodesic(true);
        polyline.setColor(Color.RED);
        polyline.setWidth(15);

        switch (type) {
            // If no type is given, allow the API to use the default.
            case "A":
                // Use a custom bitmap as the cap at the start of the line.
                polyline.setStartCap(new CustomCap(BitmapDescriptorFactory.fromResource(R.drawable.ic_map_marker_radius_grey600_24dp), 18));
                polyline.setEndCap(new CustomCap(BitmapDescriptorFactory.fromResource(R.drawable.ic_map_marker_radius_grey600_24dp), 18));
                break;
            case "B":
                // Use a round cap at the start of the line.
                polyline.setStartCap(new RoundCap());
                polyline.setEndCap(new RoundCap());
                break;
        }



    }

    private void renderSelectedTracks() {
        if (mGoogleMap != null) {
        /*    new Runnable() {
                public void run() {*/

            for (Integer l : mUserViewModel.getTrackstoRender().getValue()) {
                mPl = mGoogleMap.addPolyline(mPlo);
                mPl.setTag("A");
                stylePolyline(mPl);
                List<LatLng> mlTrack = new ArrayList<LatLng>(Utils.getLatLng(Objects.requireNonNull(mUserViewModel.getUser().getValue()).getTblTracks().get(l).getTblgps()));
                mPl.setPoints(mlTrack);
                stylePolyline(mPl);
            }


            for (Integer l : mUserViewModel.getTrackstoRender().getValue()) {
                mPl = mGoogleMap.addPolyline(mPlo);
                mPl.setTag("A");
                stylePolyline(mPl);
                ArrayList<TblGps> mlTrack = (ArrayList<TblGps>) mUserViewModel.getUser().getValue().getTblTracks().get(l).getTblgps();

                for (TblGps g : mlTrack) {
                    if (Objects.equals(g.getType(), "MARKER")) {
                        MarkerOptions mo = new MarkerOptions();
                        mo.position(Utils.getLatLng(g));
                        mo.draggable(false);
                        mo.visible(true);
                        mo.title(Utils.formatDate(g.getDate(), getContext()));
                        mo.snippet(g.getDescr());
                        mo.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));
                        Marker m = mGoogleMap.addMarker(mo);

                    }
                }
                stylePolyline(mPl);
            }

        }
    }
            }
  /*      }
    }
}*/
