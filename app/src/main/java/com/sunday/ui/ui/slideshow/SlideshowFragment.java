package com.sunday.ui.ui.slideshow;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.os.EnvironmentCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.sunday.ui.R;

import java.io.File;


/**
 * https://codechacha.com/ko/android-q-scoped-storage/
 */
public class SlideshowFragment extends Fragment {

    private SlideshowViewModel slideshowViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        slideshowViewModel = new ViewModelProvider(this).get(SlideshowViewModel.class);

        View root = inflater.inflate(R.layout.fragment_slideshow, container, false);
        final TextView textView = root.findViewById(R.id.text_slideshow);
        slideshowViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
                if(isPermission()){
                    getDevice();
                }
            }
        });
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDevice();
            }
        });
        return root;
    }
    private boolean isPermission(){

        if(ContextCompat.checkSelfPermission(requireContext() , Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(requireActivity() , new String[]{Manifest.permission.READ_EXTERNAL_STORAGE , Manifest.permission.WRITE_EXTERNAL_STORAGE} ,10000 );
            return false;
        }else{
            return true;
        }
    }
    private void getDevice(){
        File[] f1 = ContextCompat.getExternalFilesDirs(requireActivity() , EnvironmentCompat.MEDIA_UNKNOWN);
        for (int i = 0; i < f1.length; i++) {
            try {

                String path = f1[i].getParent().replace("/Android/data/", "").replace(getContext().getPackageName(), "");
                Log.d("DIRS", path); //sdcard and internal and usb
                for(File e : f1[i].listFiles()){
                    Log.d("DIRS", "[" + e.getName() + "][" + e.isFile() + "][" + e.getPath());
                }
                File OTG = new File("/storage/");
                Log.d("DIRS","OTG = " +  OTG.exists());
                for(File e : OTG.listFiles()){
                    Log.d("DIRS", "[" + e.getName() + "][" + e.isDirectory() + "][" + e.getPath());
                    if(e.isDirectory()){
                        if(e.listFiles() != null){
                            for(File e1 : e.listFiles()){
                                Log.d("DIRS", "1[" + e1.getName() + "][" + e1.isFile() + "][" + e1.getPath());
                            }
                        }else if(e.list() != null){
                            for(String e1 : e.list()){
                                Log.d("DIRS", "2[" + e1 + "]");
                            }
                        }else{
                            Log.d("DIRS", "2[" + e.getName() + "]" + e.getPath());
                        }

                    }
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }


        File[] f = ContextCompat.getExternalFilesDirs(getActivity(), null);
        for (int i = 0; i < f.length; i++) {
            try {

                String path = f[i].getParent().replace("/Android/data/", "").replace(getContext().getPackageName(), "");
                Log.d("DIRS", path); //sdcard and internal and usb
                for(File e : f[i].listFiles()){
                    Log.d("DIRS", "[" + e.getName() + "][" + e.isFile() + "][" + e.getPath());
                }
                File OTG = new File("/storage/");
                Log.d("DIRS","OTG = " +  OTG.exists());
                for(File e : OTG.listFiles()){
                    Log.d("DIRS", "[" + e.getName() + "][" + e.isDirectory() + "][" + e.getPath());
                    if(e.isDirectory()){
                       if(e.listFiles() != null){
                           for(File e1 : e.listFiles()){
                               Log.d("DIRS", "1[" + e1.getName() + "][" + e1.isFile() + "][" + e1.getPath());
                           }
                       }else if(e.list() != null){
                           for(String e1 : e.list()){
                               Log.d("DIRS", "2[" + e1 + "]");
                           }
                       }else{
                           Log.d("DIRS", "2[" + e.getName() + "]" + e.getPath());
                       }

                    }
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }
    private void getApipath(){

    }
    private void getSAF(){

    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}