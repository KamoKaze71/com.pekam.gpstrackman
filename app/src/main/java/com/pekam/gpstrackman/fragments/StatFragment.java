package com.pekam.gpstrackman.fragments;

import android.content.Context;
import android.location.Location;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.Observer;
import com.pekam.gpstrackman.R;
import com.pekam.gpstrackman.viewmodels.UserViewModel;
import com.pekam.gpstrackman.views.MyTextView;
import org.jetbrains.annotations.NotNull;

import java.time.Instant;

public class StatFragment extends Fragment {
    private MyTextView mTvStatusLatLong;
    private MyTextView mTvStatusAccuracy;
    private MyTextView TextView;
    private MyTextView mTvStatusAlt;
    private MyTextView mTvStatusSpeed;



    private MyTextView mTvStatusSat;
    private MyTextView mTvStatusRecordTime;
    private Location mLocation;
    private UserViewModel mUserViewModel;
public static StatFragment newInstance() {
        return new StatFragment();
}

    /**
     * Called when a fragment is being created as part of a view layout
     * inflation, typically from setting the content view of an activity.  This
     * may be called immediately after the fragment is created from a
     * {@link FragmentContainerView} in a layout file.  Note this is <em>before</em>
     * the fragment's {@link #onAttach(Context)} has been called; all you should
     * do here is parse the attributes and save them away.
     *
     * <p>This is called <em>the first time</em> the fragment is inflated. If it is
     * being inflated into a new instance with saved state, this method will not be
     * called a second time for the restored state fragment.</p>
     *
     * <p>Here is a typical implementation of a fragment that can take parameters
     * both through attributes supplied here as well from {@link #getArguments()}:</p>
     * <p>
     * {@sample frameworks/support/samples/Support4Demos/src/main/java/com/example/android/supportv4/app/FragmentArgumentsSupport.java
     * fragment}
     *
     * <p>Note that parsing the XML attributes uses a "styleable" resource.  The
     * declaration for the styleable used here is:</p>
     * <p>
     * {@sample frameworks/support/samples/Support4Demos/src/main/res/values/attrs.xml fragment_arguments}
     *
     * <p>The fragment can then be declared within its activity's content layout
     * through a tag like this:</p>
     * <p>
     * {@sample frameworks/support/samples/Support4Demos/src/main/res/layout/fragment_arguments_support.xml from_attributes}
     *
     * <p>This fragment can also be created dynamically from arguments given
     * at runtime in the arguments Bundle; here is an example of doing so at
     * creation of the containing activity:</p>
     * <p>
     * {@sample frameworks/support/samples/Support4Demos/src/main/java/com/example/android/supportv4/app/FragmentArgumentsSupport.java
     * create}
     *
     * @param context            The Activity that is inflating this fragment.
     * @param attrs              The attributes at the tag where the fragment is
     *                           being created.
     * @param savedInstanceState If the fragment is being re-created from
     */

    @Override
    public void onInflate(@NonNull @NotNull Context context, @NonNull @NotNull AttributeSet attrs, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onInflate(context, attrs, savedInstanceState);
    }

    /**
     * Called when a fragment is first attached to its context.
     * {@link #onCreate(Bundle)} will be called after this.
     *
     * @param context
     */
    @Override
    public void onAttach(@NonNull @NotNull Context context) {
        super.onAttach(context);
    }

    /**
     * Called to do initial creation of a fragment.  This is called after
     * {@link #onAttach(Activity)} and before
     * {@link #onCreateView(LayoutInflater, ViewGroup, Bundle)}.
     *
     * <p>Note that this can be called while the fragment's activity is
     * still in the process of being created.  As such, you can not rely
     * on things like the activity's content view hierarchy being initialized
     * at this point.  If you want to do work once the activity itself is
     * created, add a {@link LifecycleObserver} on the
     * activity's Lifecycle, removing it when it receives the
     * {@link Lifecycle.State#CREATED} callback.
     *
     * <p>Any restored child fragments will be created before the base
     * <code>Fragment.onCreate</code> method returns.</p>
     *
     * @param savedInstanceState If the fragment is being re-created from
     *                           a previous saved state, this is the state.
     */
    @Override
    public void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

}

    /**
     * Called to have the fragment instantiate its user interface view.
     * This is optional, and non-graphical fragments can return null. This will be called between
     * {@link #onCreate(Bundle)} and {@link #onViewCreated(View, Bundle)}.
     * <p>A default View can be returned by calling {@link #Fragment(int)} in your
     * constructor. Otherwise, this method returns null.
     *
     * <p>It is recommended to <strong>only</strong> inflate the layout in this method and move
     * logic that operates on the returned View to {@link #onViewCreated(View, Bundle)}.
     *
     * <p>If you return a View from here, you will later be called in
     * {@link #onDestroyView} when the view is being released.
     *
     * @param inflater           The LayoutInflater object that can be used to inflate
     *                           any views in the fragment,
     * @param container          If non-null, this is the parent view that the fragment's
     *                           UI should be attached to.  The fragment should not add the view itself,
     *                           but this can be used to generate the LayoutParams of the view.
     * @param savedInstanceState If non-null, this fragment is being re-constructed
     *                           from a previous saved state as given here.
     * @return Return the View for the fragment's UI, or null.
     */
    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public View onCreateView(@NonNull @NotNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    /**
     * Called immediately after {@link #onCreateView(LayoutInflater, ViewGroup, Bundle)}
     * has returned, but before any saved state has been restored in to the view.
     * This gives subclasses a chance to initialize themselves once
     * they know their view hierarchy has been completely created.  The fragment's
     * view hierarchy is not however attached to its parent at this point.
     *
     * @param view               The View returned by {@link #onCreateView(LayoutInflater, ViewGroup, Bundle)}.
     * @param savedInstanceState If non-null, this fragment is being re-constructed
     */
    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mTvStatusLatLong= view.findViewById(R.id.tvStatusLatLong);
        mTvStatusAccuracy=view.findViewById(R.id.tvStatusAccuracy);
        mTvStatusAlt=view.findViewById(R.id.tvStatusAlt);
        mTvStatusSpeed=view.findViewById(R.id.tvStatusSpeed);
        mTvStatusSat=view.findViewById(R.id.tvStatusSat);
        mTvStatusRecordTime=view.findViewById(R.id.tvStatusRecordTime);

        Observer<Location> nameObserver= new Observer<Location>() {
            @Override
            public void onChanged(@Nullable final Location location) {

                mTvStatusLatLong.setText(location.getLongitude() + "/" + location.getLatitude());
                mTvStatusAccuracy.setText(Float.toString(location.getAccuracy()));
                mTvStatusAlt.setText(Double.toString(location.getAltitude()));
                mTvStatusSpeed.setText(Float.toString(location.getSpeed()));
                mTvStatusSat.setText(Long.toString(location.getTime()/1000));

                if (mUserViewModel.IsRecording().getValue()==true) {
                    Long ms = mUserViewModel.getTrackstartime().toEpochMilli() - Instant.now().toEpochMilli();
                    mTvStatusRecordTime.setText((ms.toString()));
                }
            }
        };


        Observer<Long> timeObserver= new Observer<Long>() {
            @Override
            public void onChanged(@Nullable final Long l) {

                if (mUserViewModel.IsRecording().getValue()==true) {
                    Long ms = mUserViewModel.getTrackstartime().toEpochMilli() - Instant.now().toEpochMilli();
                    mTvStatusRecordTime.setText((ms.toString()));
                }
            }
        };
        mUserViewModel.msRecording().observe(this.getActivity(), timeObserver);
        super.onViewCreated(view, savedInstanceState);
    }


    /**
     * Called when the Fragment is visible to the user.  This is generally
     * tied to {@link Activity#onStart() Activity.onStart} of the containing
     * Activity's lifecycle.
     */
    @Override
    public void onStart() {
        super.onStart();
    }

    /**
     * Called when the fragment is visible to the user and actively running.
     * This is generally
     * tied to {@link Activity#onResume() Activity.onResume} of the containing
     * Activity's lifecycle.
     */
    @Override
    public void onResume() {
        super.onResume();
    }

    /**
     * Called to ask the fragment to save its current dynamic state, so it
     * can later be reconstructed in a new instance if its process is
     * restarted.  If a new instance of the fragment later needs to be
     * created, the data you place in the Bundle here will be available
     * in the Bundle given to {@link #onCreate(Bundle)},
     * {@link #onCreateView(LayoutInflater, ViewGroup, Bundle)}, and
     * {@link #onViewCreated(View, Bundle)}.
     *
     * <p>This corresponds to {@link Activity#onSaveInstanceState(Bundle)
     * Activity.onSaveInstanceState(Bundle)} and most of the discussion there
     * applies here as well.  Note however: <em>this method may be called
     * at any time before {@link #onDestroy()}</em>.  There are many situations
     * where a fragment may be mostly torn down (such as when placed on the
     * back stack with no UI showing), but its state will not be saved until
     * its owning activity actually needs to save its state.
     *
     * @param outState Bundle in which to place your saved state.
     */
    @Override
    public void onSaveInstanceState(@NonNull @NotNull Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    /**
     * Called when the Fragment is no longer started.  This is generally
     * tied to {@link Activity#onStop() Activity.onStop} of the containing
     * Activity's lifecycle.
     */
    @Override
    public void onStop() {
        super.onStop();
    }

    /**
     * Called when the view previously created by {@link #onCreateView} has
     * been detached from the fragment.  The next time the fragment needs
     * to be displayed, a new view will be created.  This is called
     * after {@link #onStop()} and before {@link #onDestroy()}.  It is called
     * <em>regardless</em> of whether {@link #onCreateView} returned a
     * non-null view.  Internally it is called after the view's state has
     * been saved but before it has been removed from its parent.
     */
    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    /**
     * Called when the fragment is no longer in use.  This is called
     * after {@link #onStop()} and before {@link #onDetach()}.
     */
    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    /**
     * Called when the fragment is no longer attached to its activity.  This
     * is called after {@link #onDestroy()}.
     */
    @Override
    public void onDetach() {
        super.onDetach();
    }
}
