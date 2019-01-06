package com.blesim.bluetooth.services;

import com.blesim.bluetooth.BluetoothDevice;
import com.blesim.bluetooth.BluetoothGatt;
import com.blesim.bluetooth.GLog;

import java.util.HashMap;
import java.util.Map;

import javax.microedition.khronos.opengles.GL;

/**
 * Created by cerise on 11/14/18.
 */

public class PeripheralContainer {

    //map.put("dog", "type of animal");
    private Map<String, BluetoothDevice> deviceDict = new HashMap<String, BluetoothDevice>();

    private static PeripheralContainer instance = null;

    public static PeripheralContainer sharedInstance(){

        if(instance == null) {
            GLog.d("create PeripheralContainer");
            instance = new PeripheralContainer();
        }
        return instance;
    }

    public void addPeripheral(String macAddress , BluetoothDevice device ){
        GLog.d();

        if( !deviceDict.containsKey( macAddress )){
            deviceDict.put( macAddress, device);
            GLog.d("added " + device.getName() + " to dict");
        }
        else{
            GLog.d( device.getName() + " already in dict");
        }
    }

    public BluetoothDevice getDevice( String macAddress ){
        GLog.d();

        BluetoothDevice device = deviceDict.get( macAddress );
        if( null == device ){
            GLog.d("can not find device : " + macAddress );
        }
        return device;
    }

    private PeripheralContainer(){
    }
}
