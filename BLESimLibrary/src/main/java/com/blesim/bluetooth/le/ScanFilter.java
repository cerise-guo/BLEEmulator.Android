package com.blesim.bluetooth.le;

import android.os.ParcelUuid;

/**
 * Created by cerise on 5/26/18.
 */

public final class ScanFilter {

    ScanFilter(){

    }

    public String getDeviceName() {
        throw new RuntimeException("Stub!");
    }

    public ParcelUuid getServiceUuid() {
        throw new RuntimeException("Stub!");
    }

    public String getDeviceAddress() {
        throw new RuntimeException("Stub!");
    }

    public byte[] getServiceData() {
        throw new RuntimeException("Stub!");
    }

    public ParcelUuid getServiceDataUuid() {
        throw new RuntimeException("Stub!");
    }

    public int getManufacturerId() {
        throw new RuntimeException("Stub!");
    }

    public byte[] getManufacturerData() {
        throw new RuntimeException("Stub!");
    }

    public static final class Builder {
        public Builder() {
            throw new RuntimeException("Stub!");
        }

        public ScanFilter.Builder setDeviceName(String deviceName) {
            throw new RuntimeException("Stub!");
        }

        public ScanFilter.Builder setDeviceAddress(String deviceAddress) {
            throw new RuntimeException("Stub!");
        }

        public ScanFilter.Builder setServiceUuid(ParcelUuid serviceUuid) {
            throw new RuntimeException("Stub!");
        }

        public ScanFilter.Builder setServiceUuid(ParcelUuid serviceUuid, ParcelUuid uuidMask) {
            throw new RuntimeException("Stub!");
        }

        public ScanFilter.Builder setServiceData(ParcelUuid serviceDataUuid, byte[] serviceData) {
            throw new RuntimeException("Stub!");
        }

        public ScanFilter.Builder setServiceData(ParcelUuid serviceDataUuid, byte[] serviceData, byte[] serviceDataMask) {
            throw new RuntimeException("Stub!");
        }

        public ScanFilter.Builder setManufacturerData(int manufacturerId, byte[] manufacturerData) {
            throw new RuntimeException("Stub!");
        }

        public ScanFilter.Builder setManufacturerData(int manufacturerId, byte[] manufacturerData, byte[] manufacturerDataMask) {
            throw new RuntimeException("Stub!");
        }

        public ScanFilter build() {
            throw new RuntimeException("Stub!");
        }
    }

}
