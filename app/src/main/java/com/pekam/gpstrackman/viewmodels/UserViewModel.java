package com.pekam.gpstrackman.viewmodels;

import android.annotation.SuppressLint;
import android.location.Location;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import androidx.annotation.NonNull;
import androidx.lifecycle.*;
import org.jetbrains.annotations.NotNull;
import pekam.entities.TblGps;
import pekam.entities.TblTracks;
import pekam.entities.TblUser;

import java.time.Instant;
import java.util.*;

@SuppressLint("ParcelCreator")
public class UserViewModel extends ParceableViewModel {

    private TblUser mUser;

    private final MutableLiveData<TblUser> userMutableLiveData = new MutableLiveData<>();
    private final MutableLiveData<Boolean> isRecordingMutableLiveData = new MutableLiveData<>();
    private final MutableLiveData<Boolean> isNetworkAvailable = new MutableLiveData<>();
    private final MutableLiveData<Long> msRecording = new MutableLiveData<>();
    private  MutableLiveData<List<Integer>> mTracksToRender = new MutableLiveData<>();

    private Instant trackStarTime;
    public Location mLocation;

    public UserViewModel() {
        super();
        isNetworkAvailable.setValue(false);
        Long i = Long.valueOf("0");
        isRecordingMutableLiveData.setValue(false);
        msRecording.setValue(i);
        mTracksToRender.setValue(new ArrayList<Integer>());
        }

    public UserViewModel(Parcelable user) {
        super();
    }

    public void addTrackToRender(Integer i) {
/**  adds a new Index Reference to trackstoRender   */
        mTracksToRender.getValue().add(i);
/** triggers map track rendering   */
        mTracksToRender.setValue(mTracksToRender.getValue());
    }
    public void addNewTrack(TblTracks track){
    track.setTblUser(userMutableLiveData.getValue());

    userMutableLiveData.getValue().getTblTracks().add(0,track);
}
    public void addNewGps(TblGps gps)
{
      userMutableLiveData.getValue().getTblTracks().get(0).getTblgps().add(0,gps);
}
    public MutableLiveData<List<Integer>> getTrackstoRender()
    {
        return mTracksToRender;
    }
    public MutableLiveData<TblUser> getUser() {
        if (userMutableLiveData.getValue() == null) {
            TblUser newUser = new TblUser();
            List<TblTracks> trackList = new ArrayList<TblTracks>();
            newUser.setTbltracks(trackList);
           /* TblTracks track = new TblTracks();
            track.setDescr("MyTrackDescription");
            track.setName("MyTrackeName");
            track.setTblUser(newUser);*/

           /* List<TblGps> gpsList = new ArrayList<TblGps>();
            track.setTblgps(gpsList);
            trackList.add(0, track);*/
            newUser.setTbltracks(trackList);
            userMutableLiveData.setValue(newUser);
        }
        return userMutableLiveData;
    }
    public MutableLiveData<Boolean> isNetworkAvailable() {
        return isNetworkAvailable;
    }
    public MutableLiveData<Boolean> IsRecording() {
        return isRecordingMutableLiveData;
    }
    public MutableLiveData<Long> msRecording() {
        return msRecording;
    }

    public Instant getTrackstartime() {
        return userMutableLiveData.getValue().getTblTracks().get(0).getTblgps().get(0).getDate();

    }

    public void setTrackstartime(Instant trackStarTime) {
        this.trackStarTime = trackStarTime;

    }

    @Override
    public void writeTo(@NonNull @NotNull Bundle bundle) {
        bundle.putParcelable("user", this);
    }

    @Override
    public void readFrom(@NonNull @NotNull Bundle bundle) {
        UserViewModel u = (UserViewModel) bundle.getParcelable("user");
        this.msRecording.setValue(u.msRecording.getValue());
        this.userMutableLiveData.setValue(u.userMutableLiveData.getValue());
        this.isRecordingMutableLiveData.setValue(u.isRecordingMutableLiveData.getValue());
        this.mUser = u.mUser;
        this.trackStarTime = u.trackStarTime;
        this.mTracksToRender.setValue(u.mTracksToRender.getValue());
    }

    /**
     * Describe the kinds of special objects contained in this Parcelable
     * instance's marshaled representation. For example, if the object will
     * include a file descriptor in the output of {@link #writeToParcel(Parcel, int)},
     * the return value of this method must include the
     * {@link #CONTENTS_FILE_DESCRIPTOR} bit.
     *
     * @return a bitmask indicating the set of special object types marshaled
     * by this Parcelable object instance.
     */
    @Override
    public int describeContents() {
        return 0;
    }

    /**
     * Flatten this object in to a Parcel.
     *
     * @param dest  The Parcel in which the object should be written.
     * @param flags Additional flags about how the object should be written.
     *              May be 0 or {@link #PARCELABLE_WRITE_RETURN_VALUE}.
     */
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        try {
           // dest.writeParcelable(this.getUser().getValue(), 0);
            dest.writeSerializable(this.msRecording().toString());
            dest.writeSerializable(this.IsRecording().toString());
            dest.writeSerializable(this.getUser().getValue());
        } catch (Exception e) {
        }
    }
}
