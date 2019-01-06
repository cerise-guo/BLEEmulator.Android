package com.blesim.bluetooth.le;

/**
 * Created by cerise on 5/26/18.
 */

public final class ScanSettings {

    public static final int CALLBACK_TYPE_ALL_MATCHES = 1;
    //public static final Creator<ScanSettings> CREATOR = null;
    public static final int SCAN_MODE_BALANCED = 1;
    public static final int SCAN_MODE_LOW_LATENCY = 2;
    public static final int SCAN_MODE_LOW_POWER = 0;

    ScanSettings() {
        //throw new RuntimeException("Stub!");
    }

    public int getScanMode() {
        throw new RuntimeException("Stub!");
    }

    public int getCallbackType() {
        throw new RuntimeException("Stub!");
    }

    public int getScanResultType() {
        throw new RuntimeException("Stub!");
    }

    public long getReportDelayMillis() {
        throw new RuntimeException("Stub!");
    }

    //public void writeToParcel(Parcel dest, int flags) {
    //    throw new RuntimeException("Stub!");
    //}

    //public int describeContents() {
    //    throw new RuntimeException("Stub!");
    //}

    public static final class Builder {
        public Builder() {
            //throw new RuntimeException("Stub!");
        }

        public ScanSettings.Builder setScanMode(int scanMode) {
            //throw new RuntimeException("Stub!");
            return this;
        }

        public ScanSettings.Builder setReportDelay(long reportDelayMillis) {
            //throw new RuntimeException("Stub!");
            return this;
        }

        public ScanSettings build() {
            //throw new RuntimeException("Stub!");
            return new ScanSettings();
        }
    }
}
