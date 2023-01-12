package com.pekam.gpstrackman.fragments;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.google.android.gms.location.LocationResult;
import com.pekam.gpstrackman.R;
import com.pekam.gpstrackman.events.MessageEvent;
import org.greenrobot.eventbus.*;


public class LogFragment extends Fragment {
    private LinearLayout mView;


    public static LogFragment newInstance()
    { return new LogFragment();}
    public LogFragment() {


    }

    @Override
    public void onInflate(@NonNull Context context, @NonNull AttributeSet attrs, @Nullable Bundle savedInstanceState) {
        super.onInflate(context, attrs, savedInstanceState);

    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        EventBus.getDefault().register(this);

    }

    /**
     * Called when the fragment is no longer attached to its activity.  This
     * is called after {@link #onDestroy()}.
     */
    @Override
    public void onDetach() {
        super.onDetach();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onStart() {
        super.onStart();

    }

    @NonNull
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {

       super.onViewCreated(view,savedInstanceState);
        // Setup any handles to view objects here
        mView = (LinearLayout) view.findViewById(R.id.lllog);
        mView.setBackgroundColor(Color.TRANSPARENT);
    }

    public void log(String logmessage)
    {/*
        TextView tvd = new TextView(mContext);
        tvd.setTextColor(Color.WHITE);
        tvd.setText(java.time.Instant.now().toString());
        mView.addView(tvd);

        TextView tv = new TextView(mContext);
        tv.setTextColor(Color.WHITE);
        tv.setText(logmessage);
        mView.addView(tv);*/

    }
    @Subscribe
    public void onEvent(MessageEvent event) {
      if (mView!=null) {
          TextView tv = new TextView(getContext());
          tv.setTextColor(Color.WHITE);
         tv.setBackgroundColor(Color.GRAY);
          tv.setText(event.getMessage());
          mView.addView(tv);
      }
    }
    @Subscribe
    public void onEvent(LocationResult event) {
        if (mView!=null) {
            TextView tv = new TextView(getContext());
            tv.setTextColor(Color.RED);
            tv.setText(event.getLastLocation().toString());
            mView.addView(tv);
        }
    }

}
