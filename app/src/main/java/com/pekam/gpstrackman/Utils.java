package com.pekam.gpstrackman;

import android.content.Context;
import android.location.Location;
import android.os.Build;

import com.google.android.gms.location.LocationResult;
import com.google.android.gms.maps.model.LatLng;

import pekam.entities.TblGps;

import java.text.NumberFormat;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class Utils {
public static Locale locale = Locale.GERMANY;
    private static final String PATTERN_FORMAT = "yyyy-MM-dd HH:mm:ss";
public static NumberFormat getNumberFormat() {

    NumberFormat nf = NumberFormat.getInstance(Locale.GERMANY);
        nf.setMaximumIntegerDigits(4 );
        nf.setMinimumIntegerDigits(2);
        nf.setMaximumFractionDigits(2);
    nf.setMinimumFractionDigits(0);
    return nf;
}


public static String timeelapsedFormat (Long seconds)
{/*
    String s;
    Duration duration = Duration.ofMillis(seconds);
    long HH = duration.toHours();
    long MM = 0;
*/
     //   MM = duration.toMinutesPart();

  //  long SS = duration.toSecondsPart();
   // String timeInHHMMSS = String.format("%02d:%02d:%02d", HH, MM, SS);
    return "100"; //timeInHHMMSS;
}
    public static String formatDate(Instant i, Context context) {
String r ;

            DateTimeFormatter formatter = DateTimeFormatter.ISO_INSTANT;
        formatter.withZone(ZoneId.systemDefault());
        r= formatter.format(i);


        return r;
    }

    public static TblGps getGps(LocationResult location) {
        TblGps gps = new TblGps();
        gps.setLat(location.getLastLocation().getLatitude());
        gps.setLng(location.getLastLocation().getLongitude());
        gps.setDate(Instant.now());
        gps.setAltitude(location.getLastLocation().getAltitude());
        gps.setAccuracy(location.getLastLocation().getAccuracy());
        gps.setProvider(location.getLastLocation().getProvider());
        return gps;
    }
    public static TblGps getGps(Location location) {
        TblGps gps = new TblGps();
        gps.setLat(location.getLatitude());
        gps.setLng(location.getLongitude());
        gps.setDate(Instant.now());
        gps.setAltitude(location.getAltitude());
        gps.setAccuracy(location.getAccuracy());
        gps.setProvider(location.getProvider());
        return gps;
    }
    public static ArrayList<TblGps> getGps(ArrayList<LocationResult> locations) {
    ArrayList<TblGps> list = new ArrayList<>();
        for (LocationResult r: locations)
        {
        TblGps gps = new TblGps();

        gps.setLat(r.getLastLocation().getLatitude());
        gps.setLng(r.getLastLocation().getLongitude());
        gps.setDate(Instant.ofEpochMilli(r.getLastLocation().getTime()));
        gps.setAltitude(r.getLastLocation().getAltitude());
        gps.setAccuracy(r.getLastLocation().getAccuracy());
        gps.setProvider(r.getLastLocation().getProvider());

        list.add(gps);
        }
        return list;
    }
    public static List<LatLng> getLatLng(List<TblGps> gl) {
        List<LatLng> ltln = new ArrayList<>();
        for (TblGps g : gl) {
            LatLng lg = new LatLng(g.getLat(), g.getLng());
            ltln.add(lg);
        }
            return ltln;
    }
    public static LatLng getLatLng(LocationResult lr) {

            LatLng lg = new LatLng(lr.getLastLocation().getLatitude(),lr.getLastLocation().getLongitude());


        return lg;
    }
    public static LatLng getLatLng(TblGps g) {

        LatLng lg = new LatLng(g.getLat(),g.getLng());


        return lg;
    }
    public static LatLng getLatLng(Location l) {

        LatLng lg = new LatLng(l.getLatitude(),l.getLongitude());


        return lg;
    }
    /**
     * Calculate distance between two points in latitude and longitude taking
     * into account height difference. If you are not interested in height
     * difference pass 0.0. Uses Haversine method as its base.
     * <p>
     * lat1, lon1 Start point lat2, lon2 End point el1 Start altitude in meters
     * el2 End altitude in meters
     *
     * @returns Distance in Meters
     */
    public static double distance(List<TblGps> list) {
        double mDistance=0;
        TblGps lastG = null;
        int i = 0;
        for (TblGps g : list) {
            if (i == 0) {
                lastG = g;

            } else {
                double lat1 = lastG.getLat();
                double lat2 = g.getLat();
                double lon1 = lastG.getLng();
                double lon2 = g.getLng();
                double el1 = lastG.getAltitude();
                double el2 = g.getAltitude();
                lastG = g;


                final int R = 6371; // Radius of the earth

                double latDistance = Math.toRadians(lat2 - lat1);
                double lonDistance = Math.toRadians(lon2 - lon1);
                double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                        + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                        * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
                double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
                double distance = R * c * 1000; // convert to meters

                double height = el1 - el2;

                distance = Math.pow(distance, 2) + Math.pow(height, 2);
                mDistance=mDistance+distance;

            }
            i = i + 1;

        }
        return Math.sqrt(mDistance);
    }
    public static String getDeviceName() {
        String manufacturer = Build.MANUFACTURER;
        String model = Build.MODEL;
        if (model.toLowerCase().startsWith(manufacturer.toLowerCase())) {
            return capitalize(model);
        } else {
            return capitalize(manufacturer) + " " + model;
        }
    }


    private static String capitalize(String s) {
        if (s == null || s.length() == 0) {
            return "";
        }
        char first = s.charAt(0);
        if (Character.isUpperCase(first)) {
            return s;
        } else {
            return Character.toUpperCase(first) + s.substring(1);
        }
    }
}
