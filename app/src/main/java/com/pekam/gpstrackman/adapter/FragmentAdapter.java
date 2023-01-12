package com.pekam.gpstrackman.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import  com.pekam.gpstrackman.fragments.LogFragment;
import  com.pekam.gpstrackman.fragments.MapFragment;
import  com.pekam.gpstrackman.fragments.StatFragment;
import org.jetbrains.annotations.NotNull;

public class FragmentAdapter extends FragmentStateAdapter {

    public FragmentAdapter(@NonNull @NotNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);

    }

    @NonNull
    @NotNull
    @Override
    public Fragment createFragment(int position) {
        if (position == 0) {
            return new MapFragment();

        } else if (position == 1) {

            return StatFragment.newInstance();

        } else if (position == 2) {

            return LogFragment.newInstance();

        } else return LogFragment.newInstance();
    }

    /**
     * Returns the total number of items in the data set held by the adapter.
     *
     * @return The total number of items in this adapter.
     */
    @Override
    public int getItemCount() {
        return 3;
    }
}
