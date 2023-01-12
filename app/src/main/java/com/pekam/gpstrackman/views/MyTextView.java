package com.pekam.gpstrackman.views;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.TypedValue;
import androidx.annotation.Nullable;
import androidx.core.view.ViewCompat;
import com.google.android.material.shape.CornerFamily;
import com.google.android.material.shape.MaterialShapeDrawable;
import com.google.android.material.shape.ShapeAppearanceModel;
import com.pekam.gpstrackman.R;

public class  MyTextView extends androidx.appcompat.widget.AppCompatTextView {

    public MyTextView(Context context) {
        super(context);
        setStyle(context);

    }

    public MyTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        attrs.getStyleAttribute();
        attrs.getAttributeCount();
        setStyle(context);
    }

    public MyTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
    private void setStyle(Context context){
this.setError("error");
        this.setPadding(10,10,10,10);
       // this.setBackgroundColor(StyleRes.);
       // this.setTextColor(R.style.);
        this.setMaxLines(1);
        this.setMinLines(1);
        this.setText("text");
       // this.setTextAppearance(context,R.style.MyTextViewStyle);
        this.setBackgroundColor(Color.GRAY);
        this.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
        float radius = 5; //getResources().getDimension("5");


        ShapeAppearanceModel shapeAppearanceModel = new ShapeAppearanceModel()
                .toBuilder()
                .setAllCornerSizes(15)
               .setAllCorners(CornerFamily.ROUNDED,radius)
                .build();

        MaterialShapeDrawable shapeDrawable = new MaterialShapeDrawable(shapeAppearanceModel);
        ViewCompat.setBackground(this,shapeDrawable);
        shapeDrawable.setStrokeWidth(1);
        shapeDrawable.setStrokeTint(Color.RED);


      //  shapeDrawable.setPadding(10,10,10,10);
        shapeDrawable.setFillColor(ColorStateList.valueOf(Color.GRAY));
        //shapeDrawable.setStrokeTint(Color.GRAY);
        this.setBackground(shapeDrawable);
    }
}
