package com.sunday.ui.ui.healthmaxisb

import com.hoho.android.usbserial.driver.CdcAcmSerialDriver
import com.hoho.android.usbserial.driver.ProbeTable
import com.hoho.android.usbserial.driver.UsbSerialProber

class CustomProber {
    companion object{
        fun getCustomProber(): UsbSerialProber? {
            val customTable = ProbeTable()
            customTable.addProduct(0x16d0, 0x087e, CdcAcmSerialDriver::class.java) // e.g. Digispark CDC
            return UsbSerialProber(customTable)
        }
    }

}