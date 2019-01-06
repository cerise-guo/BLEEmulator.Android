package com.blesim.bluetooth.le;

import android.os.ParcelUuid;
import android.util.SparseArray;

import java.util.List;
import java.util.Map;

/**
 * Created by cerise on 5/26/18.
 */

public final class ScanRecord {

    final String deviceName;

    public ScanRecord( String discoveredDeviceName ) {
        deviceName = discoveredDeviceName;
    }

    private ScanRecord(){
        deviceName = "unsupported ctor";
    }

    public int getAdvertiseFlags() {
        throw new RuntimeException("Stub!");
    }

    public List<ParcelUuid> getServiceUuids() {
        throw new RuntimeException("Stub!");
    }

    public SparseArray<byte[]> getManufacturerSpecificData() {
        throw new RuntimeException("Stub!");
    }

    public byte[] getManufacturerSpecificData(int manufacturerId) {
        throw new RuntimeException("Stub!");
    }

    public Map<ParcelUuid, byte[]> getServiceData() {
        throw new RuntimeException("Stub!");
    }

    public byte[] getServiceData(ParcelUuid serviceDataUuid) {
        throw new RuntimeException("Stub!");
    }

    public int getTxPowerLevel() {
        throw new RuntimeException("Stub!");
    }

    public String getDeviceName() {
        return deviceName;
    }

    public byte[] getBytes() {
        throw new RuntimeException("Stub!");
    }

    public String toString() {
        throw new RuntimeException("Stub!");
    }
}