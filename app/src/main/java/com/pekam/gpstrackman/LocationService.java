package com.pekam.gpstrackman;

import android.Manifest;
import android.app.*;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;

import android.graphics.drawable.Icon;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.widget.RemoteViews;


import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;


import com.google.android.gms.location.*;
import com.pekam.gpstrackman.events.LocationResultEvent;
import com.pekam.gpstrackman.events.MessageEvent;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;

public class LocationService extends Service {

    private NotificationManager mManager;
    private LocationResult lastKnownLocation;
    private final IBinder binder = new LocalBinder();
    public ArrayList<LocationResult> locations = new ArrayList();
    private boolean isBound = false;
    private PendingIntent pauseIntent,recordIntent, stopIntent;
    public LocationService() {   }


    public class LocalBinder extends Binder {
        LocationService getService() {
            // Return this instance of LocalService so clients can call public methods
            return LocationService.this;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {

        isBound = true;
        handleIntent(intent);
        return binder;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        EventBus.getDefault().post(new MessageEvent("Service OnCreate"));

        NotificationChannel notfifationChannel = new NotificationChannel("1", "LocationService", NotificationManager.IMPORTANCE_DEFAULT);
        notfifationChannel.enableLights(true);
        // Sets whether notification posted to this channel should vibrate.
        notfifationChannel.enableVibration(true);
        // Sets the notification light color for notifications posted to this channel
        notfifationChannel.setLightColor(Color.GREEN);
        // Sets whether notifications posted to this channel appear on the lockscreen or not
        notfifationChannel.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);

        RemoteViews notificationLayout = new RemoteViews(getPackageName(), R.layout.layout_notification_small);
        RemoteViews notificationLayoutExpanded = new RemoteViews(getPackageName(), R.layout.layout_notification_large);
        notificationLayout.setTextViewText(R.id.notificationLargeTextView,"this is");
        // PendingIntent pi = PendingIntent.getService(getApplicationContext(), Notification., objIntent, PendingIntent.FLAG_CANCEL_CURRENT);
        //  notificationLayoutExpanded.setViewOutlinePreferredRadius(7);

        Intent intent = new Intent(getApplicationContext(), MapsActivity.class);

        PendingIntent StopPendingIntent = PendingIntent.getService(getApplicationContext(), PendingIntent.FLAG_ONE_SHOT, intent, 0);
        notificationLayoutExpanded.setOnClickPendingIntent(R.id.btnStop, StopPendingIntent);

        PendingIntent PlayPendingIntent = PendingIntent.getService(getApplicationContext(), PendingIntent.FLAG_UPDATE_CURRENT, intent, 0);
        notificationLayoutExpanded.setOnClickPendingIntent(R.id.btnPlay, PlayPendingIntent);

        PendingIntent PausePendingIntent = PendingIntent.getService(getApplicationContext(), PendingIntent.FLAG_UPDATE_CURRENT, intent, 0);
        notificationLayoutExpanded.setOnClickPendingIntent(R.id.btnPause, PausePendingIntent);

        PendingIntent RecordPendingIntent = PendingIntent.getService(getApplicationContext(), PendingIntent.FLAG_UPDATE_CURRENT, intent, 0);

        notificationLayoutExpanded.setOnClickPendingIntent(R.id.btnRecord, RecordPendingIntent);


// Apply the layouts to the notification
        Notification customNotification = new NotificationCompat.Builder(this.getApplicationContext(), String.valueOf(1))
                .setTicker("TrackMan Ticker")
                .setContentTitle("TrackManService Title")
                .setContentText("TrackManService Text")
                .setSmallIcon(R.drawable.ic_compass_rose_black_18dp)
              //  .setLargeIcon(Icon.createWithResource(R.id.))

                .setStyle(new NotificationCompat.DecoratedCustomViewStyle())

                .setCustomContentView(notificationLayout)
                .setCustomBigContentView(notificationLayoutExpanded)

                .build();
        getManager().createNotificationChannel(notfifationChannel);
        getManager().notify(1, customNotification);


    }

    private void handleIntent(Intent intent) {
        if (intent != null && intent.getAction() != null) {
            Log.e("CustomNotificationService", intent.getAction().toString());
        }
    }

    /**
     * Called when all clients have disconnected from a particular interface
     * published by the service.  The default implementation does nothing and
     * returns false.
     *
     * @param intent The Intent that was used to bind to this service,
     *               as given to {@link Context#bindService
     *               Context.bindService}.  Note that any extras that were included with
     *               the Intent at that point will <em>not</em> be seen here.
     * @return Return true if you would like to have the service's
     * {@link #onRebind} method later called when new clients bind to it.
     */
    @Override
    public boolean onUnbind(Intent intent) {
        isBound = false;
        return super.onUnbind(intent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        EventBus.getDefault().post(new MessageEvent("Service on start"));

        startLocationRequest();

        return super.onStartCommand(intent, flags, startId);
    }

    private void startLocationRequest() {

        LocationRequest mLocationRequest = LocationRequest.create()
               // .setNumUpdates(0)
                .setInterval(6000)
                .setFastestInterval(6000)
                .setWaitForAccurateLocation(true)
                .setSmallestDisplacement(10)
                ;

        FusedLocationProviderClient mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getApplicationContext());

        LocationCallback mLocationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(@NonNull LocationResult locationResult) {
                super.onLocationResult(locationResult);
                if (isBound) {
                    EventBus.getDefault().post(new LocationResultEvent(locationResult));
                } else {
                    locations.add(locationResult);
                }
            }

            @Override
            public void onLocationAvailability(@NonNull LocationAvailability locationAvailability) {
                super.onLocationAvailability(locationAvailability);

            }
        };

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mFusedLocationProviderClient.requestLocationUpdates(mLocationRequest, mLocationCallback, this.getMainLooper());


}


    private NotificationManager getManager() {

        if (mManager == null) {
            mManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        }
        return mManager;
    }

    private Notification.Builder getAndroidChannelNotification(String title, String body) {
        return new Notification.Builder(getApplicationContext(), "1")
                .setContentTitle(title)
                .setContentText(body)
                .setSmallIcon(android.R.drawable.star_on)
                .setAutoCancel(false);
    }




}
