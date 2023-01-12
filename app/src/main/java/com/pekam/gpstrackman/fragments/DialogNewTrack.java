package com.pekam.gpstrackman.fragments;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;
import com.pekam.gpstrackman.R;
import org.jetbrains.annotations.NotNull;

public class DialogNewTrack extends DialogFragment {


    public static DialogNewTrack newInstance(String title) {
        DialogNewTrack frag = new DialogNewTrack();
        Bundle args = new Bundle();
        args.putString("title", title);
        frag.setArguments(args);
        return frag;
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
     *                {@link FragmentTransaction#add(Fragment, String) FragmentTransaction.add}.
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
    @Override
    public void setupDialog(@NonNull @NotNull Dialog dialog, int style) {
        super
                .setupDialog(dialog, style);
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

            super.onResume();
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

    public void show(FragmentManager parentFragmentManager) {
    }
}


