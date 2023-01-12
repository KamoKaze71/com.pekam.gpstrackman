package com.pekam.gpstrackman.viewmodels;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

public class BundleAwareViewModelFactory  <T extends ParceableViewModel> implements ViewModelProvider.Factory {

        private final Bundle bundle;
        private final ViewModelProvider.Factory provider;

        public BundleAwareViewModelFactory(@Nullable Bundle bundle, ViewModelProvider.Factory provider) {
            this.bundle = bundle;
            this.provider = provider;
        }
        @SuppressWarnings("unchecked")
        @Override
        public T create(final Class UserViewModel) {
            T viewModel = (T) provider.create(UserViewModel);
            if (bundle != null) {
                viewModel.readFrom(bundle);
            }
            return viewModel;
        }
    }

