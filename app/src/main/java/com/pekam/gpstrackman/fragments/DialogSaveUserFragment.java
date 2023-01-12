package com.pekam.gpstrackman.fragments;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.BlendMode;
import android.os.Bundle;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import com.pekam.gpstrackman.LocationServiceHttpRequests;
import com.pekam.gpstrackman.viewmodels.UserViewModel;
import com.pekam.gpstrackman.R;
import org.jetbrains.annotations.NotNull;
import org.json.JSONException;

public class DialogSaveUserFragment extends DialogFragment {

    private LocationServiceHttpRequests mReq=null;
    private UserViewModel mUserViewModel;

    public static DialogSaveUserFragment newInstance(String title) {
        DialogSaveUserFragment frag = new DialogSaveUserFragment();
        Bundle args = new Bundle();
        args.putString("title", title);
        frag.setArguments(args);
        return frag;
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
        NavController nav = Navigation.findNavController(getActivity(),R.id.navHost);
       Button btnConfirm = view.findViewById(R.id.btnConfirm);
        Button btnDismiss =view.findViewById(R.id.btnDismiss);
        view.setElevation(10);
        view.setBackgroundTintBlendMode(BlendMode.OVERLAY);


        if (savedInstanceState==null) {
            mUserViewModel = new ViewModelProvider(this.getActivity()).get(UserViewModel.class);
            mReq = new LocationServiceHttpRequests(getActivity(), getContext());
          //  FragmentManager mFm = getActivity().getSupportFragmentManager();
        }

        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    mUserViewModel.getUser().getValue().setAppInstall(Settings.Secure.getString(getActivity().getContentResolver(), Settings.Secure.ANDROID_ID));
                    mReq.updateUser(mUserViewModel.getUser().getValue());
                   nav.popBackStack();

                }catch (JSONException e)
                {
                    e.printStackTrace();
                }

            }
        });

        btnDismiss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nav.popBackStack();
            }
        });
    }

    /**
     * Called to have the fragment instantiate its user interface view.
     * This is optional, and non-graphical fragments can return null. This will be called between
     * {@link #onCreate(Bundle)} and {@link #onViewCreated(View, Bundle)}.
     * <p>A default View can be returned by calling  in your
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

        return inflater.inflate(R.layout.dialog_fragment,container,false);

      //  return super.onCreateView(inflater, container, savedInstanceState);
    }

    /**
     * Display the dialog, adding the fragment to the given FragmentManager.  This
     * is a convenience for explicitly creating a transaction, adding the
     * fragment to it with the given tag, and {@link FragmentTransaction#commit() committing} it.
     * This does <em>not</em> add the transaction to the fragment back stack.  When the fragment
     * is dismissed, a new transaction will be executed to remove it from
     * the activity.
     *
     * @param manager The FragmentManager this fragment will be added to.
     * @param tag     The tag for this fragment, as per
     *                .
     */
    @Override
    public void show(@NonNull @NotNull FragmentManager manager, @Nullable @org.jetbrains.annotations.Nullable String tag) {
        super.show(manager, tag);
    }

    /**
     * Dismiss the fragment and its dialog.  If the fragment was added to the
     * back stack, all back stack state up to and including this entry will
     * be popped.  Otherwise, a new transaction will be committed to remove
     * the fragment.
     */
    @Override
    public void dismiss() {
        super.dismiss();
    }

    /**
     * Version of {@link #dismiss()} that uses
     * {@link FragmentTransaction#commitAllowingStateLoss()
     * FragmentTransaction.commitAllowingStateLoss()}. See linked
     * documentation for further details.
     */
    @Override
    public void dismissAllowingStateLoss() {
        super.dismissAllowingStateLoss();
    }

    /**
     * Control whether the shown Dialog is cancelable.  Use this instead of
     * directly calling {@link Dialog#setCancelable(boolean)
     * Dialog.setCancelable(boolean)}, because DialogFragment needs to change
     * its behavior based on this.
     *
     * @param cancelable If true, the dialog is cancelable.  The default
     *                   is true.
     */
    @Override
    public void setCancelable(boolean cancelable) {
        super.setCancelable(cancelable);
    }

    @Override
    public void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    /**
     * @param dialog
     * @param style
     * @hide
     */
    @SuppressLint("RestrictedApi")
    @Override
    public void setupDialog(@NonNull @NotNull Dialog dialog, int style) {

        super.setupDialog(dialog, style);

    }

    /**
     * Override to build your own custom Dialog container.  This is typically
     * used to show an AlertDialog instead of a generic Dialog; when doing so,
     * {@link #onCreateView(LayoutInflater, ViewGroup, Bundle)} does not need
     * to be implemented since the AlertDialog takes care of its own content.
     *
     * <p>This method will be called after {@link #onCreate(Bundle)} and
     * immediately before {@link #onCreateView(LayoutInflater, ViewGroup, Bundle)}.  The
     * default implementation simply instantiates and returns a {@link Dialog}
     * class.
     *
     * <p><em>Note: DialogFragment own the {@link Dialog#setOnCancelListener
     * Dialog.setOnCancelListener} and {@link Dialog#setOnDismissListener
     * Dialog.setOnDismissListener} callbacks.  You must not set them yourself.</em>
     * To find out about these events, override {@link #onCancel(DialogInterface)}
     * and {@link #onDismiss(DialogInterface)}.</p>
     *
     * @param savedInstanceState The last saved instance state of the Fragment,
     *                           or null if this is a freshly created Fragment.
     * @return Return a new Dialog instance to be displayed by the Fragment.
     */
    @NonNull
    @NotNull
    @Override
    public Dialog onCreateDialog(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {

        return super.onCreateDialog(savedInstanceState);
    }

    @Override
    public void onCancel(@NonNull @NotNull DialogInterface dialog) {
        super.onCancel(dialog);
    }

    @Override
    public void onDismiss(@NonNull @NotNull DialogInterface dialog) {
        super.onDismiss(dialog);
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onSaveInstanceState(@NonNull @NotNull Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    /**
     * Remove dialog.
     */
    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}


