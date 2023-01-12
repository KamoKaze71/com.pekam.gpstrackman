
package com.pekam.gpstrackman.viewmodels;

import android.os.Bundle;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;
import org.jetbrains.annotations.NotNull;

public class ViewModelFactory extends ViewModelProvider implements ViewModelProvider.Factory {
   Bundle mBundle;

    public ViewModelFactory(@NotNull ViewModelStoreOwner owner, Bundle bundle) {
        super(owner);
        mBundle=bundle;
    }
    public ViewModelFactory(@NotNull ViewModelStoreOwner owner)  {
        super(owner);
        }

    @NotNull
    @Override
    public <T extends ViewModel> T create(@NotNull Class<T> aClass) {
        if (aClass.isAssignableFrom(UserViewModel.class)&&(mBundle!=null)) {
            return (T) new UserViewModel(mBundle.getParcelable("user"));
        }else
        {
            return (T) new UserViewModel();
        }
        //noinspection unchecked
       // throw new IllegalArgumentException("Unknown ViewModel class");
    }

    }


