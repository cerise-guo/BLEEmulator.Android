package com.blesim.bluetooth.le;

import com.blesim.bluetooth.BluetoothDevice;

import java.lang.ref.WeakReference;

/**
 * Created by cerise on 5/26/18.
 */

public final class ScanResult {
    //public static final Creator<ScanResult> CREATOR = null;

    int rssi;

    WeakReference<BluetoothDevice> device;

    final ScanRecord record;

    public ScanResult(BluetoothDevice btdevice, ScanRecord scanRecord, int rssi, long timestampNanos) {

        device = new WeakReference<BluetoothDevice>(btdevice);
        record = scanRecord;

        this.rssi = rssi;
    }

    //public void writeToParcel(Parcel dest, int flags) {
    //    throw new RuntimeException("Stub!");
    //}

    //public int describeContents() {
    //    throw new RuntimeException("Stub!");
    //}

    public BluetoothDevice getDevice() {
        return device.get();
    }

    public ScanRecord getScanRecord() {
        return record;
    }

    public int getRssi() {
        return this.rssi;
    }
    public void setRssi( int value ){
        this.rssi = value;
    }

    public long getTimestampNanos() {
        throw new RuntimeException("getTimestampNanos Stub!");
    }

    public int hashCode() {
        throw new RuntimeException("hashCode Stub!");
    }

    public boolean equals(Object obj) {
        throw new RuntimeException("equals Stub!");
    }

    public String toString() {
        throw new RuntimeException("toString Stub!");
    }
}
