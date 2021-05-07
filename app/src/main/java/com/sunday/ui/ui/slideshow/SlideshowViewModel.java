package com.sunday.ui.ui.slideshow;

import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbManager;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

public class SlideshowViewModel extends AndroidViewModel {

    private MutableLiveData<String> mText = new MutableLiveData<String>();

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (UsbManager.ACTION_USB_DEVICE_DETACHED.equals(action)) {
                UsbDevice device = (UsbDevice)intent.getParcelableExtra(UsbManager.EXTRA_DEVICE);
                if (device != null) {
                    // call your method that cleans up and closes communication with the device
                    mText.postValue("장비 해제됨[" + action + "]");
                }
            }else if(UsbManager.ACTION_USB_DEVICE_ATTACHED.equals(action)){
                UsbDevice device = (UsbDevice)intent.getParcelableExtra(UsbManager.EXTRA_DEVICE);
                if(device == null){
                    mText.postValue("무슨 장비지 ?[" + action + "]");
                }else{
                    mText.postValue(device.getDeviceName() + "][" + device.getProductName() + action + "]");
                }
            }
        }
    };

    public SlideshowViewModel(@NonNull Application application) {
        super(application);
        application.registerReceiver(broadcastReceiver,new IntentFilter(UsbManager.ACTION_USB_DEVICE_DETACHED));
        application.registerReceiver(broadcastReceiver,new IntentFilter(UsbManager.ACTION_USB_DEVICE_ATTACHED));
    }




    /**
     *  Android Support 라이브러리 버전이 27.1.0 이하일 경우 onCleared가 Fragment 에서 호출이 되지 않는 다는 이슈가 있다.
     * 버전을 27.1.1 이상으로 올려봐라. (28.0.0-rc1에서 정상 수정이 되었다는 내용도 있음)
     *
     * 출처: https://gogorchg.tistory.com/entry/AndroidDI-ViewModel-onCleared-함수가-호출-되지-않을-때 [항상 초심으로]
     */
    @Override
    protected void onCleared() {
        super.onCleared();

        getApplication().unregisterReceiver(broadcastReceiver);
    }

    public LiveData<String> getText() {
        return mText;
    }
}