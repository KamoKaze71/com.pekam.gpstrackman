package com.pekam.gpstrackman;

import java.time.Instant;

import java.util.*;

import android.Manifest;

import android.app.Dialog;
import android.content.*;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.os.IBinder;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import android.os.Bundle;
import androidx.drawerlayout.widget.DrawerLayout;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import androidx.navigation.*;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import com.google.android.gms.location.LocationResult;


import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;

import com.pekam.gpstrackman.databinding.ActivityMapsBinding;
import com.pekam.gpstrackman.viewmodels.LocationViewModel;
import com.pekam.gpstrackman.viewmodels.UserViewModel;
import com.pekam.gpstrackman.events.LocationResultEvent;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.jetbrains.annotations.NotNull;
import com.pekam.gpstrackman.R;

import pekam.entities.TblUser;

import android.provider.Settings.Secure;

public class MapsActivity extends AppCompatActivity  {

    private LocationService mService=null;
    private boolean mBound = false;
    private ServiceConnection mConnection;
    public static UserViewModel mUserViewModel;
    private static LocationViewModel mLocationViewModel;
    private static AppBarConfiguration mAppBarConfiguration;
    private  DrawerLayout mDrawerLayout;
    private  NavController mNavController =null;
    private Toolbar mToolbar;
    private ActionBar mActionBar;
    private final Timer  t= new Timer();
    private  NavigationView mNavigationView=null;
    private String android_id;
    private String device_name;
    private Dialog mNewTrack;
    private Logger mLogger;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /*Register Eventbis for LocationEvent from the Service */
        EventBus.getDefault().register(this);

       /*Provides Viewmodel Data */
        mLocationViewModel = new ViewModelProvider(this).get(LocationViewModel.class);
        mUserViewModel = new ViewModelProvider(this).get(UserViewModel.class);

        /** Defines callbacks for service binding, passed to bindService() */
        mConnection = new ServiceConnection() {

            @Override
            public void onServiceConnected(ComponentName className, IBinder service) {
                // We've bound to LocalService, cast the IBinder and get LocalService instance
                LocationService.LocalBinder binder = (LocationService.LocalBinder) service;
                mService = binder.getService();

                if (mService.locations.size()>0 || mUserViewModel.IsRecording().getValue()) {
                    mUserViewModel.getUser().getValue().getTblTracks().get(0).getTblgps().addAll(Utils.getGps(mService.locations));
                    mUserViewModel.getUser().setValue(mUserViewModel.getUser().getValue());
                }
                mBound = true;
            }

            @Override
            public void onServiceDisconnected(ComponentName arg0) {
                mBound = false;
            }
        };

        /** Handles the Permission Request */
        checkRequestPermissions();

       ActivityMapsBinding binding =ActivityMapsBinding.inflate(getLayoutInflater());

        if (savedInstanceState==null) {
            mLogger = new Logger(this.getFilesDir().getAbsolutePath() + "/logfile.txt");
            mLogger.appendLog(Utils.formatDate(Instant.now(), getApplicationContext()) + "Starting Activity");
        }

        android_id = Secure.getString(this.getContentResolver(), Secure.ANDROID_ID);
        Objects.requireNonNull(mUserViewModel.getUser().getValue()).setAppInstall(android_id);





            mUserViewModel.getUser().observe(this, new Observer<TblUser>() {
                @Override
                public void onChanged(@Nullable TblUser data) {

                    Snackbar snackbar = Snackbar
                            .make(mDrawerLayout, "Data has been saved!", Snackbar.LENGTH_LONG);
                    snackbar.show();
                }
            });

            t.schedule(new TimerTask() {
                @Override
                public void run() {
                    mUserViewModel.msRecording().postValue(mUserViewModel.msRecording().getValue() + 1000);
                }
            }, 1000, 1000);

        LocationServiceHttpRequests mReq = new LocationServiceHttpRequests(this, this.getBaseContext());
        mReq.getUser(Secure.getString(this.getContentResolver(), Secure.ANDROID_ID));

        setContentView(binding.getRoot());

        mToolbar = findViewById(R.id.toolbar);
             setSupportActionBar(mToolbar);

            mDrawerLayout = binding.drawerLayout;
            mNavigationView = binding.navView;
            mNavigationView.setBackgroundColor(Color.DKGRAY);
            View headerLayout = mNavigationView.getHeaderView(0);

            // Passing each menu ID as a set of Ids because each
            // menu should be considered as top level destinations.
            mAppBarConfiguration = new AppBarConfiguration.Builder(
                    )
                    .setOpenableLayout(mDrawerLayout)
                    .build();

            mNavController = Navigation.findNavController(this, R.id.navHost);
            NavigationUI.setupActionBarWithNavController(this, mNavController, mAppBarConfiguration);
            NavigationUI.setupWithNavController(mNavigationView, mNavController);
        mToolbar.setLogo(R.drawable.ic_compass_rose_white_24dp);
            ActionBarDrawerToggle actionbarDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, mToolbar, R.string.openNavDraw, R.string.closeNavDraw);
              mDrawerLayout.addDrawerListener(actionbarDrawerToggle);
            actionbarDrawerToggle.syncState();

        Menu menuNav = mNavigationView.getMenu();

            mNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                    switch (item.getItemId()) {
                        case (R.id.nav_map):

                            try {
                                mNavController.navigate(R.id.nav_map);
                                mDrawerLayout.closeDrawer(mNavigationView,true);

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            break;

                        case (R.id.nav_DialogSave):
                            try {

                                mNavController.navigate(R.id.nav_DialogSave);
                                mDrawerLayout.closeDrawer(mNavigationView,true);

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            break;

                        case (R.id.nav_dialog_newTrack):


                            try {

                                mNavController.navigate(R.id.nav_dialog_newTrack);
                                mDrawerLayout.closeDrawer(mNavigationView,true);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            break;

                        default:
                            System.out.println("default");
                            break;
                    }
                    return false;
                }
            });
        }

    @Override
    protected void onStop() {
    //    EventBus.getDefault().unregister(this);
        super.onStop();
    //    unbindService(mConnection);
      //  mBound = false;

    }

    @Override
    protected void onStart() {
       // EventBus.getDefault().register(this);
        super.onStart();

    }

    @Override
    protected void onResume() {
        super.onResume();
       // EventBus.getDefault().register(this);
    }

    @Override
    protected void onPause() {
         super.onPause();
       // EventBus.getDefault().unregister(this);
    }

    @Override
    protected void onSaveInstanceState(@NotNull Bundle outState) {
        outState.putBoolean("REQUESTING_LOCATION_UPDATES_KEY", true);
        super.onSaveInstanceState(outState);
        new Runnable() {
            public void run() {
                mUserViewModel.writeTo(outState);
            }

        };
    }

    /**
     * This method is called after {@link #onStart} when the activity is
     * being re-initialized from a previously saved state, given here in
     * <var>savedInstanceState</var>.  Most implementations will simply use {@link #onCreate}
     * to restore their state, but it is sometimes convenient to do it here
     * after all of the initialization has been done or to allow subclasses to
     * decide whether to use your default implementation.  The default
     * implementation of this method performs a restore of any view state that
     * had previously been frozen by {@link #onSaveInstanceState}.
     *
     * <p>This method is called between {@link #onStart} and
     * {@link #onPostCreate}. This method is called only when recreating
     * an activity; the method isn't invoked if {@link #onStart} is called for
     * any other reason.</p>
     *
     * @param savedInstanceState the data most recently supplied in {@link #onSaveInstanceState}.
     * @see #onCreate
     * @see #onPostCreate
     * @see #onResume
     * @see #onSaveInstanceState
     */
    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        new Runnable() {
            public void run() {
                mUserViewModel.readFrom(savedInstanceState);
            }
        };
    }

    private void checkRequestPermissions() {
        boolean shouldexplainPermissions = true;
        if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

            Intent intent = new Intent(getApplicationContext(), LocationService.class);

            if (getSystemService(LocationService.class)==null) {
                  this.startService(intent);
                 bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
             }else if (getSystemService(LocationService.class)!=null)
             {
                 bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
             }
            //   Service mlocationServie = getApplicationContext().getSystemService(LocationService.class);

        } else if (shouldexplainPermissions) {
            // In an educational UI, explain to the user why your app requires this
            // permission for a specific feature to behave as expected. In this UI,
            // include a "cancel" or "no thanks" button that allows the user to
            // continue using your app without granting the permission.
            // showInContextUI(...);

            requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION);
        } else {
            // You can directly ask for the permission.
            // The registered ActivityResultCallback gets the result of this request.
            requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION);

        }

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        super.onSupportNavigateUp();
        NavController navController = Navigation.findNavController(this, R.id.navHost);

        return NavigationUI.navigateUp(navController, mAppBarConfiguration);
    }
  /*  @Subscribe
    public void onEvent(MessageEvent mE) {
        Snackbar snackbar = Snackbar
                .make(mDrawerLayout, mE.getMessage().toString(), Snackbar.LENGTH_LONG);
        snackbar.show();
    }*/
   @Subscribe
    public void onEvent(LocationResultEvent locationResultEvent) {
                   mLocationViewModel.getLastLocation().setValue(locationResultEvent.getLocationResult().getLastLocation());
    }
    @Subscribe
    public void onEvent(ArrayList<LocationResult> locations) {
        LocationResult r = locations.get(locations.size()-1);

        if (mUserViewModel.IsRecording().getValue()==true) {
            Objects.requireNonNull(mUserViewModel.getUser().getValue()).getTblTracks().get(0).getTblgps().addAll(Utils.getGps(locations));
            mLocationViewModel.getLastLocation().setValue(r.getLastLocation());
        }else {
            mLocationViewModel.getLastLocation().setValue(r.getLastLocation());
        }
    }

    private final ActivityResultLauncher<String> requestPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                if (isGranted) {

                   getApplicationContext().startService(new Intent(getApplicationContext(),LocationService.class));

                } else {
                    // Explain to the user that the feature is unavailable because the
                    // features requires a permission that the user has denied. At the
                    // same time, respect the user's decision. Don't link to system
                    // settings in an effort to convince the user to change their
                    // decision.
                }
            });
    private final ConnectivityManager.NetworkCallback networkCallback = new ConnectivityManager.NetworkCallback() {
        @Override
        public void onAvailable(@NonNull Network network) {
            super.onAvailable(network);
            mUserViewModel.isNetworkAvailable().setValue(true);
        }

        @Override
        public void onLost(@NonNull Network network) {
            super.onLost(network);
            mUserViewModel.isNetworkAvailable().setValue(false);
        }

        @Override
        public void onCapabilitiesChanged(@NonNull Network network, @NonNull NetworkCapabilities networkCapabilities) {
            super.onCapabilitiesChanged(network, networkCapabilities);
            final boolean unmetered = networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_NOT_METERED);
        }
    };
    }

