package com.blesim.bluetooth.services;

import com.blesim.bluetooth.BluetoothGattService;
import com.blesim.bluetooth.GLog;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Created by cerise on 11/18/18.
 */

public class GattServiceContainer {

    private Map<UUID, BluetoothGattService> deviceDict = new HashMap<UUID, BluetoothGattService>();

    private static GattServiceContainer instance = null;

    public static GattServiceContainer sharedInstance(){

        if(instance == null) {
            GLog.d("create GattServiceContainer");
            instance = new GattServiceContainer();
        }
        return instance;
    }

    public void addService(UUID uuid , BluetoothGattService service ){
        GLog.d();

        if( !deviceDict.containsKey( uuid )){
            deviceDict.put( uuid, service);
            GLog.d("added " + service.getUuid() + " to dict");
        }
        else{
            GLog.e( "service " + service.getUuid() + " already in dict");
        }
    }

    public BluetoothGattService getService( UUID uuid ){
        GLog.d();

        BluetoothGattService device = deviceDict.get( uuid );
        if( null == device ){
            GLog.d("can not find service : " + uuid );
        }
        return device;
    }

    private GattServiceContainer(){
    }
}
