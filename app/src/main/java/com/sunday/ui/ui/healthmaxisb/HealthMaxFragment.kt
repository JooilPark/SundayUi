package com.sunday.ui.ui.healthmaxisb

import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.sunday.ui.R
import com.sunday.ui.databinding.FragmentHealthmaxBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class HealthMaxFragment : Fragment() {
    val TAG: String = "[" + this.javaClass.simpleName + "]"
    val VM: HealthMaxViewModel by viewModel()
    lateinit var binding: FragmentHealthmaxBinding
    lateinit var adapter: AdapterDeviceList
    private val baudRate = 9600
    private val withIoManager = true
    private lateinit var healthMaxControllers: HealthMaxControllers
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(layoutInflater, R.layout.fragment_healthmax, null, false)
        healthMaxControllers = HealthMaxControllers(requireActivity())
        adapter = AdapterDeviceList(onclick = {
            Log.i(TAG, it.name)
            if (healthMaxControllers.connected) {
                healthMaxControllers.disconnect()
            } else {
                healthMaxControllers.connect(it.device.deviceId, it.port, baudRate, withIoManager, it)
            }
        })
        binding.connectTExt.movementMethod = ScrollingMovementMethod.getInstance()

        binding.usbList.adapter = adapter
        binding.buttonSendDevicelist.setOnClickListener {
            healthMaxControllers.refrashdevice()
        }
        healthMaxControllers.usbDevices.observe(viewLifecycleOwner, Observer {
            Log.i("TAG", "observe[" + it.size + "]")
            Log.i("TAG", it.toString())
            adapter.deviceList = it
            adapter.notifyDataSetChanged()
        })
        binding.buttonSend7.setOnClickListener {
            healthMaxControllers.send(byteArrayOf(0x07))
        }
        binding.buttonSend8.setOnClickListener {
            healthMaxControllers.send(byteArrayOf(0x08))
        }
        binding.buttonSendM.setOnClickListener {
            healthMaxControllers.send(byteArrayOf(*"ME".toByteArray()))
        }
        healthMaxControllers.connectText.observe(viewLifecycleOwner, Observer {
            binding.connectTExt.append(it.toString())
        })
        binding.connectTExt.setOnClickListener {
            binding.connectTExt.text = ""
        }
        return binding.root
    }


}