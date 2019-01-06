package com.blesim.bluetooth.services;

import com.blesim.bluetooth.BluetoothGattCharacteristic;
import com.blesim.bluetooth.GLog;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Created by cerise on 11/18/18.
 */

public class GattCharacteristicContainer {

    private Map<UUID, SimBluetoothGattCharacteristic> deviceDict = new HashMap<UUID, SimBluetoothGattCharacteristic>();

    private static GattCharacteristicContainer instance = null;

    public static GattCharacteristicContainer sharedInstance(){

        if(instance == null) {
            GLog.d("create GattCharacteristicContainer");
            instance = new GattCharacteristicContainer();
        }
        return instance;
    }

    public void addCharacteristic(SimBluetoothGattCharacteristic character ){
        GLog.d();

        if( !deviceDict.containsKey( character.getUuid() )){
            deviceDict.put( character.getUuid(), character);
            GLog.d("added " + character.getUuid() + " to dict");
        }
        else{
            GLog.e( "Characteristic " + character.getUuid() + " already in dict");
        }
    }

    public SimBluetoothGattCharacteristic getCharacteristic( UUID uuid ){
        GLog.d();

        SimBluetoothGattCharacteristic character = deviceDict.get( uuid );
        if( null == character ){
            GLog.d("can not find Characteristic : " + uuid );
        }
        return character;
    }


    private GattCharacteristicContainer(){
    }
}
