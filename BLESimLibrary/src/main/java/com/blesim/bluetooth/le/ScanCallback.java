package com.blesim.bluetooth.le;

import java.util.List;

/**
 * Created by cerise on 5/26/18.
 */

public abstract class ScanCallback {
    public static final int SCAN_FAILED_ALREADY_STARTED = 1;
    public static final int SCAN_FAILED_APPLICATION_REGISTRATION_FAILED = 2;
    public static final int SCAN_FAILED_FEATURE_UNSUPPORTED = 4;
    public static final int SCAN_FAILED_INTERNAL_ERROR = 3;

    public ScanCallback() {
        //throw new RuntimeException("Stub!");
    }

    public void onScanResult(int callbackType, ScanResult result) {
        throw new RuntimeException("Stub!");
    }

    public void onBatchScanResults(List<ScanResult> results) {
        throw new RuntimeException("Stub!");
    }

    public void onScanFailed(int errorCode) {
        throw new RuntimeException("Stub!");
    }
}
