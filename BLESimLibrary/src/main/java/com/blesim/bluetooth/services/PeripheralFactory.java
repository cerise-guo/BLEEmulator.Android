package com.blesim.bluetooth.services;

import com.blesim.bluetooth.BluetoothDevice;
import com.blesim.bluetooth.GLog;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by cerise on 11/10/18.
 */

class PeripheralFactory {

    public enum BluetoothDeviceType {
        HeartRateMonitor(0),
        CGM(1);

        private final int rawValue;

        BluetoothDeviceType(int value) {
            rawValue = value;
        }
    }

    private static PeripheralFactory instance = null;

    private PeripheralFactory(){
    }

    public static PeripheralFactory sharedInstance(){

        if(instance == null) {
            GLog.d("create PeripheralFactory");
            instance = new PeripheralFactory();
        }
        return instance;
    }

    public BluetoothDevice createBluetoothDevice( BluetoothDeviceType type ){
        GLog.d("type : " + type );
        switch (type){
            case HeartRateMonitor:
                return createHeartRateMonitor();
            default:
                assert false;
        }

        assert false;
        return null;
    }

    private BluetoothDevice createHeartRateMonitor(){

        return new BluetoothDevice("Galaxy Note8", "1F:2F:3F:4F:5F:6F");
    }
}
