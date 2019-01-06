package com.blesim.bluetooth.le;

import java.util.UUID;

/**
 * Created by cerise on 6/8/18.
 */

public class BLERequest {
    final UUID bleScannerID;
    final TYPE type;
    final com.blesim.bluetooth.le.ScanCallback scanCallback;

    public BLERequest( UUID uuid, TYPE requestType, ScanCallback callback ){
        this.bleScannerID = uuid;
        this.type = requestType;
        this.scanCallback = callback;
    }

    public TYPE type(){
        return this.type;
    }
    public UUID bleScannerID(){
        return this.bleScannerID;
    }
    public ScanCallback callback() { return this.scanCallback; }

    public enum TYPE
    {
        SCAN,
        STOP_SCAN
    }

    //shall not be called
    private BLERequest(){
        assert false;
        bleScannerID = UUID.randomUUID();
        type = null;
        scanCallback = null;
    }
}
