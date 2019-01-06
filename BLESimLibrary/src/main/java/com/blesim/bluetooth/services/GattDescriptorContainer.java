package com.blesim.bluetooth.services;

import com.blesim.bluetooth.BluetoothGattDescriptor;
import com.blesim.bluetooth.GLog;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Created by cerise on 12/9/18.
 */

class GattDescriptorContainer {

    private Map<UUID, SimBluetoothGattDescriptor> deviceDict = new HashMap<UUID, SimBluetoothGattDescriptor>();

    private static GattDescriptorContainer instance = null;

    public static GattDescriptorContainer sharedInstance(){

        if(instance == null) {
            GLog.d("create GattDescriptorContainer");
            instance = new GattDescriptorContainer();
        }
        return instance;
    }

    private GattDescriptorContainer(){
    }

    public void addDescriptor(SimBluetoothGattDescriptor descriptor ){
        GLog.d();

        if( !deviceDict.containsKey( descriptor.getUuid() )){
            deviceDict.put( descriptor.getUuid(), descriptor);
            GLog.d("added " + descriptor.getUuid() + " to dict");
        }
        else{
            GLog.e( "Descriptor " + descriptor.getUuid() + " already in dict");
        }
    }

    public SimBluetoothGattDescriptor getDescriptor( UUID uuid ){
        GLog.d();

        SimBluetoothGattDescriptor descriptor = deviceDict.get( uuid );
        if( null == descriptor ){
            GLog.d("can not find descriptor : " + uuid );
        }
        return descriptor;
    }
}
