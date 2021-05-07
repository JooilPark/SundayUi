package com.obelab.ui.ui.gallery;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.obelab.ui.R;
import com.obelab.ui.databinding.FragmentGalleryBinding;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class GalleryFragment extends Fragment {
    int value = 0;
    private FragmentGalleryBinding mFragmentGalleryBinding;
    private GalleryViewModel galleryViewModel;
    private final Handler mHandler = new Handler();
    Runnable ani = new Runnable() {
        @Override
        public void run() {
            if (value > 100) {
                value = 0;
            }
            value++;
            mFragmentGalleryBinding.meter.setValue(value);
            mFragmentGalleryBinding.textGallery.setText("value[" + value + "]");

            double[][] BrainActivity = new double[4][14];
            for (int a = 0; a < 4; a++) {
                for (int b = 0; b < 14; b++) {
                    BrainActivity[a][b] = new Random().nextInt(100);
                }
            }
            double[][] BrainConnectivity = new double[48][48];
            for (int a = 0; a < 48; a++) {
                for (int b = 0; b < 48; b++) {
                    BrainConnectivity[a][b] = ThreadLocalRandom.current().nextDouble(-1, 1);
                }
            }
            mFragmentGalleryBinding.bConnecty.setMap(BrainConnectivity);
            mFragmentGalleryBinding.brean.setMap(BrainActivity);
            mFragmentGalleryBinding.bar1.setValue(value);
            mFragmentGalleryBinding.centiped.setValue(value);
            if (value == 100) {
                mHandler.postDelayed(ani, 2000);
            } else {
                mHandler.postDelayed(ani, 100);
            }
        }
    };

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        galleryViewModel =
                ViewModelProviders.of(this).get(GalleryViewModel.class);

        mFragmentGalleryBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_gallery, container, false);
        galleryViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                mFragmentGalleryBinding.textGallery.setText(s);
            }
        });

        return mFragmentGalleryBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mFragmentGalleryBinding.meter.invalidate();
        mHandler.postDelayed(ani, 2000);
    }
}