package com.obelab.ui.ui.camera

import android.content.Context
import android.content.pm.PackageManager
import android.hardware.Camera
import android.hardware.camera2.CameraCharacteristics
import android.hardware.camera2.CameraManager
import android.hardware.camera2.CameraManager.AvailabilityCallback
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.obelab.ui.R
import com.obelab.ui.databinding.FragmentCameraBinding


class CameraFragment : Fragment() {
    val TAG = "[CameraFragment]"
    var fragmentCameraBinding = lazy { DataBindingUtil.inflate<FragmentCameraBinding>(layoutInflater, R.layout.fragment_camera, null, false) }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return fragmentCameraBinding.value.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val cameraManager: CameraManager = requireContext().getSystemService(Context.CAMERA_SERVICE) as CameraManager
        Log.i(TAG, "CAMERA INFO SIZE = ${cameraManager.cameraIdList.size}")
        Log.i(TAG, "FEATURE_CAMERA = ${requireContext().packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA)}")
        for (c in cameraManager.cameraIdList) {
            Log.i(TAG, "camera =  $c")

        }
        if (cameraManager.cameraIdList.size == 0) {
            Log.i(TAG, "getNumberOfCameras " + Camera.getNumberOfCameras())
            val c: Camera? = Camera.open()
            val GC = android.graphics.Camera()
            GC.restore()
            Log.i(TAG, "CAMERA INFO SIZE = ${cameraManager.cameraIdList.size}")


            cameraManager.registerAvailabilityCallback(object : AvailabilityCallback() {
                override fun onCameraAvailable(cameraId: String) {
                    super.onCameraAvailable(cameraId)

                    //Do your work
                    Log.i(TAG, "camera off")
                }

                override fun onCameraUnavailable(cameraId: String) {
                    super.onCameraUnavailable(cameraId)
                    //Do your work
                    Log.i(TAG, "camera on")
                    val cameraCharacteristics = cameraManager.getCameraCharacteristics("0")
                    val capabilities = cameraCharacteristics.get(CameraCharacteristics.REQUEST_AVAILABLE_CAPABILITIES)
                    Log.i(TAG, "capabilities = ${capabilities}")
                }
            }, Handler(Looper.myLooper()))


        }
    }

}