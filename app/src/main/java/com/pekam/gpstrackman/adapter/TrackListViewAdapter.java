package com.pekam.gpstrackman.adapter;

import android.content.Context;
import android.content.Intent;
import android.database.DataSetObserver;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;
import com.pekam.gpstrackman.R;
import com.pekam.gpstrackman.viewmodels.UserViewModel;

import com.pekam.gpstrackman.Utils;
import org.jetbrains.annotations.NotNull;
import pekam.entities.TblTracks;


import java.time.Instant;
import java.util.List;

public class TrackListViewAdapter extends ArrayAdapter<TblTracks>implements ListAdapter {

    LayoutInflater mInflater = LayoutInflater.from(getContext());
    List<TblTracks> myList;
    UserViewModel mUserViewModel=null;
    /**
     * Constructor
     *
     * @param context  The current context.
     * @param resource The resource ID for a layout file containing a TextView to use when
     */
    public TrackListViewAdapter(@NonNull Context context, int resource) {
        super(context, resource);

    }

    /**
     * Constructor
     *
     * @param context  The current context.
     * @param resource The resource ID for a layout file containing a TextView to use when
     *                 instantiating views.
     * @param objects  The objects to represent in the ListView.
     */
    public TrackListViewAdapter(@NonNull @NotNull Context context, int resource, @NonNull @NotNull List<TblTracks> objects) {

        super(context, resource, objects);

        mUserViewModel = new ViewModelProvider((ViewModelStoreOwner)(getContext())).get(UserViewModel.class);
        myList=objects;

    }

    /**
     * Indicates whether all the items in this adapter are  enabled. If the
     * value returned by this method changes over time, there is no guarantee
     * it will take effect.  If true, it means all items are selectable and
     * clickable (there is no separator.)
     *
     * @return True if all items are enabled, false otherwise.
     * @see #isEnabled(int)
     */
    @Override
    public boolean areAllItemsEnabled() {
        return false;
    }

    /**
     * Returns true if the item at the specified position is not a separator.
     * (A separator is a non-selectable, non-clickable item).
     * <p>
     * The result is unspecified if position is invalid. An {@link ArrayIndexOutOfBoundsException}
     * should be thrown in that case for fast failure.
     *
     * @param position Index of the item
     * @return True if the item is not a separator
     * @see #areAllItemsEnabled()
     */
    @Override
    public boolean isEnabled(int position) {
        return true;
    }

    /**
     * Register an observer that is called when changes happen to the data used by this adapter.
     *
     * @param observer the object that gets notified when the data set changes.
     */
    @Override
    public void registerDataSetObserver(DataSetObserver observer) {

    }

    /**
     * Unregister an observer that has previously been registered with this
     * adapter via {@link #registerDataSetObserver}.
     *
     * @param observer the object to unregister.
     */
    @Override
    public void unregisterDataSetObserver(DataSetObserver observer) {

    }

    /**
     * How many items are in the data set represented by this Adapter.
     *
     * @return Count of items.
     */
    @Override
    public int getCount() {
        return myList.size();
    }

    /**
     * Get the data item associated with the specified position in the data set.
     *
     * @param position Position of the item whose data we want within the adapter's
     *                 data set.
     * @return The data at the specified position.
     */


    /**
     * Get the row id associated with the specified position in the list.
     *
     * @param position The position of the item within the adapter's data set whose row id we want.
     * @return The id of the item at the specified position.
     */
    @Override
    public long getItemId(int position) {
        if (myList.get(position).getId() == null) {
            return 0;
        } else {
            return myList.get(position).getId();
        }

    }


    /**
     * Indicates whether the item ids are stable across changes to the
     * underlying data.
     *
     * @return True if the same id always refers to the same object.
     */
    @Override
    public boolean hasStableIds() {
        return true;
    }

    /**
     * Get a View that displays the data at the specified position in the data set. You can either
     * create a View manually or inflate it from an XML layout file. When the View is inflated, the
     * parent View (GridView, ListView...) will apply default layout parameters unless you use
     * {@link LayoutInflater#inflate(int, ViewGroup, boolean)}
     * to specify a root view and to prevent attachment to the root.
     *
     * @param position    The position of the item within the adapter's data set of the item whose view
     *                    we want.
     * @param convertView The old view to reuse, if possible. Note: You should check that this view
     *                    is non-null and of an appropriate type before using. If it is not possible to convert
     *                    this view to display the correct data, this method can create a new view.
     *                    Heterogeneous lists can specify their number of view types, so that this View is
     *                    always of the right type (see {@link #getViewTypeCount()} and
     *                    {@link #getItemViewType(int)}).
     * @param parent      The parent that this view will eventually be attached to
     * @return A View corresponding to the data at the specified position.
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        if (v == null){
        v= mInflater.inflate(R.layout.fragment_tracklistitemdetail,parent,false);
        }

        ImageButton btnShare = v.findViewById(R.id.btnShare);
        btnShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, "This is my text to send.");
                sendIntent.setType("text/plain");

                Intent shareIntent = Intent.createChooser(sendIntent, null);
                getContext().startActivity(shareIntent);
            }
        });

        ImageButton btnDelete = v.findViewById(R.id.btnDelete);
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mUserViewModel.getUser().getValue().getTblTracks().remove(position);
                mUserViewModel.getUser().setValue(mUserViewModel.getUser().getValue());
            }
        });

        ImageButton btnRecord = v.findViewById(R.id.btnrecord);
        btnRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mUserViewModel.getUser().getValue().getTblTracks().add(0,mUserViewModel.getUser().getValue().getTblTracks().get(position));
                mUserViewModel.IsRecording().setValue(true);
                mUserViewModel.getUser().setValue(mUserViewModel.getUser().getValue());


            }
        });


        TextView tvName = v.findViewById(R.id.txtTrackName);
        TextView tvDescr = v.findViewById(R.id.txtTrackDescr);
        
        TextView tvTrackDistance = v.findViewById(R.id.txtTrackStatDistance);
        TextView tvTrackTimeElaspsed = v.findViewById(R.id.txtTrackStatTimeElapsed);
        TextView tvTrackNumberofPoints = v.findViewById(R.id.txtTrackStatPoints);
        TextView tvTrackTimeStarted = v.findViewById(R.id.txtTrackStatTimeStarted);

        TblTracks t = getItem(position);

        Integer i= t.getTblgps().size();

        Instant end = t.getTblgps().get(i-1).getDate();
        Instant start = t.getTblgps().get(0).getDate();
        Long seconds = start.getEpochSecond() - end.getEpochSecond();
        tvTrackTimeElaspsed.setText(Utils.timeelapsedFormat(seconds));

        Double d = Utils.distance(t.getTblgps());
        tvTrackDistance.setText(Utils.getNumberFormat().format(d));

        tvTrackNumberofPoints.setText(Utils.getNumberFormat().format(i));
        tvTrackTimeStarted.setText(Utils.formatDate(start,getContext()));
        tvName.setText(t.getName());
        tvDescr.setText(t.getDescr());

        return v;
    }

    /**
     * Get the type of View that will be created by {@link #getView} for the specified item.
     *
     * @param position The position of the item within the adapter's data set whose view type we
     *                 want.
     * @return An integer representing the type of View. Two views should share the same type if one
     * can be converted to the other in {@link #getView}. Note: Integers must be in the
     * range 0 to {@link #getViewTypeCount} - 1. {@link #IGNORE_ITEM_VIEW_TYPE} can
     * also be returned.
     * @see #IGNORE_ITEM_VIEW_TYPE
     */
    @Override
    public int getItemViewType(int position) {
        return IGNORE_ITEM_VIEW_TYPE;

    }


    @Override
    public int getViewTypeCount() {

        return 3;
    }

    /**
     * @return true if this adapter doesn't contain any data.  This is used to determine
     * whether the empty view should be displayed.  A typical implementation will return
     * getCount() == 0 but since getCount() includes the headers and footers, specialized
     * adapters might want a different behavior.
     */
    @Override
    public boolean isEmpty() {
        return false;
    }


}