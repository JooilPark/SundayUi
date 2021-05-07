package com.sunday.ui.ui.healthmaxisb

import android.content.Context
import android.hardware.usb.UsbDevice
import android.hardware.usb.UsbManager
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.hoho.android.usbserial.driver.UsbSerialDriver
import com.hoho.android.usbserial.driver.UsbSerialPort
import com.hoho.android.usbserial.driver.UsbSerialProber
import com.hoho.android.usbserial.util.SerialInputOutputManager
import java.io.IOException
import java.util.concurrent.Executors


/**
 * 1. 장비 조회
 * 2. 보내기/받기
 * 3. 실시간 수신 감지
 */
class HealthMaxControllers(context: Context) {
    private val TAG = "[HealthMaxControllers]"
    private val usbManager: UsbManager = context.getSystemService(Context.USB_SERVICE) as UsbManager
    val usbDevices: MutableLiveData<ArrayList<ListItem>> = MutableLiveData()
    val connectText: MutableLiveData<String> = MutableLiveData()
    var stringLog = StringBuffer()
    var usbSerialPort: UsbSerialPort? = null
    private val WRITE_WAIT_MILLIS = 2000
    private val READ_WAIT_MILLIS = 2000
    lateinit var usbIoManager: SerialInputOutputManager
    var connected = false
    private var usbPermission = UsbPermission.Unknown

    init {

    }

    fun refrashdevice() {
        val usbDefaultProber = UsbSerialProber.getDefaultProber()
        val usbCustomProber: UsbSerialProber? = CustomProber.getCustomProber()
        val usbDeviceItems: ArrayList<ListItem> = ArrayList<ListItem>()
        for (device in usbManager.deviceList.values) {
            usbCustomProber?.let {
                var driver: UsbSerialDriver? = usbDefaultProber.probeDevice(device)
                if (driver == null) {
                    driver = usbCustomProber.probeDevice(device)
                }
                driver?.let {
                    for (port in 0 until driver.ports.size) {
                        val ite = ListItem(device, port, driver)
                        usbDeviceItems.add(ite)
                        Log.i("HealthMaxControllers", ite.device.deviceName)
                        connectText.value = ite.device.deviceName + "\n"
                    }
                } ?: run {
                    Log.i("HealthMaxControllers", "null")
                    connectText.value = ("null\n")
                    usbDeviceItems.add(ListItem(device, 0, null))
                }

            } ?: run {
                usbDeviceItems.add(ListItem(device, 0, null))
            }


        }
        Log.i("HealthMaxControllers", "[" + usbDeviceItems.size + "]")
        connectText.value = ("[" + usbDeviceItems.size + "]\n")
        usbDevices.postValue(usbDeviceItems)
    }

    var statusMessage: String = ""
    fun connect(deviceId: Int, portNum: Int, baudRate: Int, withIoManager: Boolean, listItem: ListItem): Boolean {
        statusMessage = ""
        stringLog = StringBuffer()
        var device: UsbDevice? = null
        for (v in usbManager.deviceList.values) if (v.deviceId == deviceId) device = v
        if (device == null) {
            statusMessage = "connection failed: device not found"
            connectText.value = ("connection failed: device not found\n")
            return false
        }
        var driver = UsbSerialProber.getDefaultProber().probeDevice(device)
        if (driver == null) {
            driver = CustomProber.getCustomProber()!!.probeDevice(device)
        }
        if (driver == null) {
            statusMessage = "connection failed: no driver for device"
            connectText.value = ("connection failed: no driver for device\n")
            return false
        }
        if (driver.ports.size < portNum) {
            statusMessage = "connection failed: not enough ports at device"
            connectText.value = ("connection failed: not enough ports at device\n")
            return false
        }
        usbSerialPort = driver.ports[portNum]
        val usbConnection = usbManager.openDevice(driver.device)

        usbSerialPort?.let {
            if (usbConnection == null) {
                if (!usbManager.hasPermission(driver.device)) {
                    Log.i(TAG, "connection failed: permission denied")
                    connectText.value = ("connection failed: permission denied\n")
                } else {
                    Log.i(TAG, "connection failed: open failed")
                    connectText.value = ("connection failed: open failed\n")
                }
                return false
            }
            try {
                it.open(usbConnection)
                it.setParameters(baudRate, UsbSerialPort.DATABITS_8, UsbSerialPort.STOPBITS_1, UsbSerialPort.PARITY_NONE)

                if (withIoManager) {
                    usbIoManager = SerialInputOutputManager(usbSerialPort, listiner)
                    Executors.newSingleThreadExecutor().submit(usbIoManager)
                }
                Log.i(TAG, "connected")
                connectText.value = ("connected\n")
                connected = true
            } catch (e: java.lang.Exception) {
                Log.i(TAG, "connection failed: " + e.message)
                connectText.value = ("connection failed\n")
                disconnect()
            }

            return true
        } ?: run {
            Log.i(TAG, "connected fail")
            connectText.value = ("connected fail\n")
            return false
        }


    }

    fun disconnect() {
        connected = false

        usbIoManager.stop()

        try {
            usbSerialPort!!.close()
        } catch (ignored: IOException) {
        }
        usbSerialPort = null
    }

    private enum class UsbPermission {
        Unknown, Requested, Granted, Denied
    }

    var reciceBytes = byteArrayOf()
    private val listiner: SerialInputOutputManager.Listener = object : SerialInputOutputManager.Listener {
        override fun onNewData(data: ByteArray?) {
            data?.let {
                try {
                    if (data.isNotEmpty()) {
                        stringLog.append(HexDump.bytesToHex(data))
                        connectText.postValue(("onNewData]" + HexDump.bytesToHex(data) + "\n"))
                        reciceBytes = byteArrayOf(*reciceBytes, *data)

                        Log.i(TAG, "${reciceBytes.asUByteArray().joinToString("") { it.toString(16).padStart(2, '0') }}       =     ${reciceBytes.toString(Charsets.UTF_8)}         ")


                        for (index in reciceBytes.indices) {
                            if (reciceBytes[index] == 0x02.toByte()) {

                                if (reciceBytes.size > index + 14) {
                                    Log.i(TAG, " ${index} 번쨰 ?  ${String.format("%02X", reciceBytes[11])}  === ${reciceBytes[index + 11] == 0x03.toByte()}")
                                }
                                if (reciceBytes.size > index + 14 && reciceBytes[index + 11] == 0x03.toByte()) {
                                    Log.i(TAG, "유저 아이디  ${toHexString(byteArrayOf(*reciceBytes.copyOfRange(index+1, index + 11)))}  =    ${byteArrayOf(*reciceBytes.copyOfRange(index+1, index + 10)).toString(Charsets.UTF_8)}")
                                    reciceBytes = byteArrayOf(*reciceBytes.copyOfRange(index + 14, reciceBytes.size))
                                    Log.i(TAG, "${toHexString(reciceBytes)}       =     ${reciceBytes.toString(Charsets.UTF_8)}         ")
                                    break
                                } else
                                    if (reciceBytes.size > index + 5 && reciceBytes[index + 5] == 0x03.toByte()) {
                                        Log.i(TAG, "상태문의 ${toHexString(byteArrayOf(*reciceBytes.copyOfRange(index+1, index + 5)))}  =     ${byteArrayOf(*reciceBytes.copyOfRange(index, index + 6)).toString(Charsets.UTF_8)}")
                                        reciceBytes = byteArrayOf(*reciceBytes.copyOfRange(index + 6, reciceBytes.size))
                                        break
                                    }
                            }
                        }

                    } else {
                        Log.i(TAG, "onNewData] EMPTY")
                        connectText.postValue("onNewData] EMPTY \n")
                    }
                } catch (e: java.lang.Exception) {
                    e.printStackTrace()
                }


            }/*?.run {
                Log.i(TAG, "onNewData] NULL")
                connectText.postValue("onNewData] NULL \n")
            }*/

        }

        override fun onRunError(e: Exception?) {
            Log.i(TAG, "onRunError = ${e!!.message}")
            connectText.postValue("onRunError = ${e.message}\n")
        }
    }

    fun send(str: ByteArray) {
        if (!connected) {
            Log.i(TAG, "not connected")
            connectText.value = ("not connected\n")
            return
        }
        try {

            /*   var data = byteArrayOf(0x02, *str, 0x03)
               data = byteArrayOf(*data, data.sum().toByte())
               Log.i(TAG, "SEND ${HexDump.bytesToHex(data)}")
               connectText.value = ("SEND ${HexDump.dumpHexString(data)}\n")
               usbSerialPort!!.write(data, WRITE_WAIT_MILLIS)*/
            connectText.value = ("SEND ${toHexString(str)}\n")
            usbSerialPort!!.write(str, WRITE_WAIT_MILLIS)
        } catch (e: java.lang.Exception) {
            listiner.onRunError(e)
        }
    }


    fun toHexString(b: ByteArray): String = run { b.toUByteArray().joinToString("") { it.toString(16).padStart(2, '0') } }

    fun send(str: String) {
        send(str.toByteArray())
    }

}