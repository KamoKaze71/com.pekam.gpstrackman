package com.pekam.gpstrackman.viewmodels;

import android.os.Bundle;
import android.os.Parcelable;
import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;

public abstract class ParceableViewModel extends ViewModel implements  Parcelable {

    public abstract void writeTo(@NonNull Bundle bundle);
    public abstract void readFrom(@NonNull Bundle bundle);
}