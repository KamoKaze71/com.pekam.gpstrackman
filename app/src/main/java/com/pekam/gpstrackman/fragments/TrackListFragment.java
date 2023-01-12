package com.pekam.gpstrackman.fragments;

import android.animation.AnimatorInflater;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.*;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import com.pekam.gpstrackman.viewmodels.UserViewModel;
import com.pekam.gpstrackman.adapter.TrackListViewAdapter;
import com.pekam.gpstrackman.R;
import pekam.entities.TblTracks;
import pekam.entities.TblUser;

import java.util.List;
import java.util.Objects;

public class TrackListFragment extends Fragment {

    private TblUser mUser;
    private ListView myListView;
    private UserViewModel mUserViewModel;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_tracks, container, false);
      //  return super.onCreateView(inflater, R.layout.fragment_tracks, savedInstanceState);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        mUserViewModel = new ViewModelProvider(Objects.requireNonNull(this.getActivity())).get(UserViewModel.class);

        Observer<TblUser> mNameObserver = new Observer<TblUser>() {
            @Override
            public void onChanged(@Nullable TblUser data) {

                refreshTrackListview();
            }
        };
        mUserViewModel.getUser().observe(this.getActivity(), mNameObserver);

    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // Setup any handles to view objects here


        myListView = view.findViewById(R.id.tracklistview);
        myListView.setChoiceMode(AbsListView.CHOICE_MODE_MULTIPLE);
        myListView.setSelection(0);
        ColorDrawable c =new ColorDrawable();
        c.setColor(Color.GRAY);
        myListView.setDivider(c);
        myListView.setDividerHeight(15);
       /* myListView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });*/
        myListView.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mUserViewModel.addTrackToRender(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        myListView.setSelection(0);
        myListView.setClickable(true);
        myListView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            public void onItemClick(AdapterView<?> parent, View view, int position, long i)
            {
                 view.setStateListAnimator(AnimatorInflater.loadStateListAnimator(getContext(),R.animator.list_selector));

                 view.setSelected(true);
                view.setActivated(true);
                view.setAnimation(new AlphaAnimation(0,1));
                view.animate();
                 view.findViewById(R.id.tblRow3).setVisibility(View.VISIBLE);

            }
        });

        refreshTrackListview();
    }

    private void refreshTrackListview() {
         if (Objects.requireNonNull(mUserViewModel.getUser().getValue()).getTblTracks() != null) {
            List<TblTracks> your_array_list = mUserViewModel.getUser().getValue().getTblTracks();

            // This is the array adapter, it takes the context of the activity as a
            // first parameter, the type of list view as a second parameter and your
            // array as a third parameter.
            TrackListViewAdapter trackListarray= new TrackListViewAdapter(Objects.requireNonNull(getContext()), R.layout.fragment_tracklistitemdetail, your_array_list);

            myListView.post(new Runnable() {
                public void run() {
                    myListView.setAdapter(trackListarray);;
                }
            });

        }
    }


}

