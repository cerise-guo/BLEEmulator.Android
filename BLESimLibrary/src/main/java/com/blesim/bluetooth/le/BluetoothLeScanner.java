package com.blesim.bluetooth.le;


import com.blesim.bluetooth.GLog;
import com.blesim.bluetooth.services.AdvertiseService;

import java.util.List;
import java.util.Queue;
import java.util.UUID;

/**
 * Created by cerise on 5/26/18.
 */

public class BluetoothLeScanner {
    //final String TAG = BluetoothLeScanner.class.getSimpleName();

    protected Queue<BLERequest> msgQueue;

    final private UUID id = UUID.randomUUID();

    BluetoothLeScanner(){
        GLog.d(id.toString());
        AdvertiseService.sharedInstance().register(this);
    }

    public void setRequestQueue(Queue<BLERequest> queue){
        msgQueue = queue;
    }

    public void startScan(List<ScanFilter> filters, com.blesim.bluetooth.le.ScanSettings settings, ScanCallback callback) {
        //throw new RuntimeException("Stub!");

        BLERequest request = new BLERequest( this.id, BLERequest.TYPE.SCAN, callback);
        msgQueue.add(request);
    }

    public void stopScan(ScanCallback callback) {
        BLERequest request = new BLERequest( this.id, BLERequest.TYPE.STOP_SCAN, callback);
        msgQueue.add(request);
    }

    public void flushPendingScanResults(ScanCallback callback) {
        throw new RuntimeException("Stub!");
    }
}
