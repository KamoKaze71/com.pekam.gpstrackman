package com.pekam.gpstrackman.fragments;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.location.Location;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.RemoteViews;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.transition.Transition;
import androidx.transition.Visibility;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.transition.MaterialFadeThrough;
import com.pekam.gpstrackman.viewmodels.LocationViewModel;
import com.pekam.gpstrackman.viewmodels.UserViewModel;
import com.pekam.gpstrackman.Utils;
import com.pekam.gpstrackman.R;
import pekam.entities.TblGps;
import pekam.entities.TblTracks;

import java.text.DecimalFormat;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static android.content.Context.NOTIFICATION_SERVICE;

public class GpsStatusFragment extends Fragment {

    private TextView mTvSpeed;
    private TextView mTvAltitude;
    private TextView mTvSpeedDistance;

    private TextView mTvRecording;
    private TextView mTvTime;

    private Location mLocation;
    private UserViewModel mUserViewModel;
    private LocationViewModel mLocationViewModel;
    private UserViewModel mRecordingViewModel;
    private boolean mViewCreated;
    private FloatingActionButton fabAddmarker;
    private FloatingActionButton fapPause;

    private FloatingActionButton fapStop;

    private  FloatingActionButton fapRecord;

    private static final DecimalFormat df = new DecimalFormat("0.00");

    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        NotificationManager notificationManager= (NotificationManager) this.getActivity().getSystemService(NOTIFICATION_SERVICE);
        NotificationChannel notificationChannel = notificationManager.getNotificationChannel("0");

        mUserViewModel =new ViewModelProvider(this.getActivity()).get(UserViewModel.class);
        mLocationViewModel = new ViewModelProvider(this.getActivity()).get(LocationViewModel.class);
        mRecordingViewModel= new ViewModelProvider(this.getActivity()).get(UserViewModel.class);



      Transition exitTransition = new MaterialFadeThrough();
        Transition reenterTransition = new MaterialFadeThrough();


        Observer<Location> locationObserver= new Observer<Location>() {
            @Override
            public void onChanged(@Nullable final Location l) {

                    if (Boolean.TRUE.equals(mUserViewModel.IsRecording().getValue()) &&  mViewCreated) {
                        Double dis = Utils.distance(mUserViewModel.getUser().getValue().getTblTracks().get(0).getTblgps());
                        mTvSpeedDistance.setText(Utils.getNumberFormat().format(dis) + "km /"+ (Utils.getNumberFormat().format(l.getSpeed()))+"km\u00B1"+l.getSpeedAccuracyMetersPerSecond() +"m/s");
                        mTvAltitude.setText(Utils.getNumberFormat().format(l.getAltitude()) +"\u00B1"+ Utils.getNumberFormat().format(l.getVerticalAccuracyMeters()));
                        mTvSpeed.setText((Utils.getNumberFormat().format(l.getSpeed() )+"\u00B1"+ Utils.getNumberFormat().format(l.getAccuracy())));
                    }
            }
        };

        mLocationViewModel.getLastLocation().observe(this.getActivity(), locationObserver);

        Observer<Long> timeObserver= new Observer<Long>() {
            @Override
            public void onChanged(@Nullable final Long l) {

                if (Boolean.TRUE.equals(mUserViewModel.IsRecording().getValue())  && mViewCreated) {
                    long ms = Instant.now().toEpochMilli() - mUserViewModel.getTrackstartime().toEpochMilli() ;
                    int seconds = (int) (ms / 1000) % 60 ;
                    int minutes = (int) ((ms / (1000*60)) % 60);
                    int hours   = (int) ((ms / (1000*60*60)) % 24);
                    mTvTime.setText(String.format("%02d:%02d:%02d",hours,minutes,seconds));
                }
            }
        };
        mUserViewModel.msRecording().observe(this.getActivity(), timeObserver);

        mRecordingViewModel.IsRecording().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if (mViewCreated) {
                    if (aBoolean) {
                        mTvRecording.setText(R.string.recording);
                        fapRecord.setVisibility(View.INVISIBLE);
                        fapPause.setVisibility(View.VISIBLE);
                    } else  {
                        mTvRecording.setText(R.string.stopped);
                        fapRecord.setVisibility(View.VISIBLE);
                        fapPause.setVisibility(View.INVISIBLE);
                    }
                }
            }
        });


    }
    @Override
    public void onInflate(@NonNull Context context, @NonNull AttributeSet attrs, @Nullable Bundle savedInstanceState) {
        super.onInflate(context, attrs, savedInstanceState);
          }

    @Override
    public void onAttach(@NonNull Context context) {

        super.onAttach(context);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_gps_status, container, false);

        //return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mViewCreated=true;
        mTvSpeedDistance = view.findViewById(R.id.txtSpeedDistance);
        mTvTime=view.findViewById(R.id.txtTime);
     
        mTvAltitude= view.findViewById(R.id.txtAltitude);
        mTvRecording= view.findViewById(R.id.txtRecording);
        mTvRecording.setText(R.string.stopped);

        mTvAltitude.setText("-----±--m");

        mTvTime.setText("0");
       // mTvGpsStatus_distance.setText("D:" + Utils.getNumberFormat().format(0) +" Km");
        mTvSpeedDistance.setText(" / ----" +"/-----" );

         fabAddmarker = (FloatingActionButton) view.findViewById(R.id.fab_AddMarker);

         fapPause = (FloatingActionButton) view.findViewById(R.id.fab_pause);
       fapPause.setTooltipText("pause track");

         fapStop = (FloatingActionButton) view.findViewById(R.id.fab_stop);
        fapStop.setTooltipText("stop track");

         fapRecord = (FloatingActionButton) view.findViewById(R.id.fab_record);
        fapRecord.setTooltipText("start recording new track");

        fabAddmarker.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                if (Objects.equals(mUserViewModel.IsRecording().getValue(), true)) {
                                                       mUserViewModel.getUser().getValue().getTblTracks().get(0).getTblgps().get(0).setType("MARKER");
                                                       mUserViewModel.getTrackstoRender().setValue(mUserViewModel.getTrackstoRender().getValue());
                                                }
                                            }
                                        }
        );

        fapPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mUserViewModel.IsRecording().setValue(false);
            }
        });

        fapStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mUserViewModel.IsRecording().setValue(false);

                fapRecord.setVisibility(View.VISIBLE);
                fapPause.setVisibility(View.INVISIBLE);
                fapStop.setVisibility(View.INVISIBLE);
            }
        });
        // Create & Start  recording a new Track!!!!!!!!!!
        fapRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mUserViewModel.setTrackstartime(Instant.now());

                   // nav (R.id.nav_dialog_newTrack);
              /*      DialogNewTrack nt= new DialogNewTrack();
                        nt.show(getActivity().getSupportFragmentManager());*/

             //   List<TblTracks> trackList = mUserViewModel.getUser().getValue().getTblTracks();
                String newstring = Utils.formatDate(Instant.now(),getContext());

                TblTracks track = new TblTracks();
                track.setDescr(Utils.formatDate(Instant.now(),getContext()));
                track.setName(Utils.formatDate(Instant.now(),getContext()));
                track.setTblUser(mUserViewModel.getUser().getValue());

                List<TblGps> gpsList = new ArrayList<TblGps>();
               if (mLocationViewModel.getLastLocation().getValue()!=null) {
                   TblGps gps = Utils.getGps(mLocationViewModel.getLastLocation().getValue());
                   gps.setType("S");
                   gps.setTbltrack(track);
                   gpsList.add(gps);
               }
                track.setTblgps(gpsList);
              /*  track.setTblgps(gpsList);
                for (TblGps gps: gpsList
                     ) {
                    gps.setTbltrack(track);
                }
                trackList.add(track);*/
                mUserViewModel.addNewTrack(track);
                mUserViewModel.IsRecording().setValue(true);
                mUserViewModel.getUser().setValue(mUserViewModel.getUser().getValue());
                mUserViewModel.addTrackToRender(0);
                fapPause.setVisibility(View.VISIBLE);
                fapStop.setVisibility(View.VISIBLE);
                fapRecord.setVisibility(View.INVISIBLE);


            }
        });
        if (Boolean.TRUE.equals(mUserViewModel.IsRecording().getValue()))      {
            fapPause.setVisibility(View.VISIBLE);
            fapStop.setVisibility(View.VISIBLE);
            fapRecord.setVisibility(View.INVISIBLE);
        } else if (Boolean.FALSE.equals(mUserViewModel.IsRecording().getValue()))    {
            fapPause.setVisibility(View.INVISIBLE);
            fapStop.setVisibility(View.INVISIBLE);
            fapRecord.setVisibility(View.VISIBLE);
        }

        mViewCreated=true;
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
    public void onDetach() {
        super.onDetach();
    }
    /*@Subscribe
    public void onEvent(LocationResultEvent locationResult) {

        mTvStatusLatLong.setText(locationResult.getLocationResult().getLastLocation().getLongitude() +" / " + locationResult.getLocationResult().getLastLocation().getLatitude());
        mTvStatusAccuracy.setText("+-" +df.format(locationResult.getLocationResult().getLastLocation().getAccuracy()) +"");
        mTvStatusAlt.setText(df.format(locationResult.getLocationResult().getLastLocation().getAltitude())+"");
        mTvStatusSpeed.setText(df.format(locationResult.getLocationResult().getLastLocation().getSpeed())+"");
        mTvStatusSat.setText(locationResult.getLocationResult().getLastLocation().getProvider()+"");
        mTvStatusRecordTime.setText(locationResult.getLocationResult().getLastLocation().getTime()+"");

        }*/
    }

