package com.sunday.ui.ui.healthmaxisb

import android.hardware.usb.UsbDevice
import com.hoho.android.usbserial.driver.UsbSerialDriver

data class ListItem(var device: UsbDevice, var port: Int, var driver: UsbSerialDriver?){
    var name : String? = device.deviceName + "[" + device.manufacturerName
    var vendername : String? = "Vender = "+device.vendorId
    var productname : String = "Product = "+device.productId
}
