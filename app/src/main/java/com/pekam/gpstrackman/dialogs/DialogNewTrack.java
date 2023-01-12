package com.pekam.gpstrackman.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import com.pekam.gpstrackman.R;

public class DialogNewTrack extends Dialog {


    public Dialog d;
    public Button confirm, cancel;

    public DialogNewTrack(Context context) {
        super(context);

        // TODO Auto-generated constructor stub


    }

    /**
     * Set the title text for this dialog's window. The text is retrieved
     * from the resources with the supplied identifier.
     *
     * @param titleId the title's text resource identifier
     */
    @Override
    public void setTitle(int titleId) {
        super.setTitle(titleId);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
       ViewGroup.LayoutParams lp= new ViewGroup.LayoutParams(400,400);
         setContentView(R.layout.dialog_newtrack);

        confirm = (Button) findViewById(R.id.btnConfirm);
        cancel = (Button) findViewById(R.id.btnDismiss);
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }
  }
