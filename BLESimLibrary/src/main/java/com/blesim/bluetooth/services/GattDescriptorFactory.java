package com.blesim.bluetooth.services;

import com.blesim.bluetooth.BluetoothGattDescriptor;
import com.blesim.bluetooth.GLog;

import java.util.UUID;

/**
 * Created by cerise on 12/9/18.
 */

class GattDescriptorFactory {
    private static GattDescriptorFactory instance = null;

    public enum DESCRIPTOR_UUID{
        CHARACTERISTIC_CONFIGURATION( UUID.fromString("00002902-0000-1000-8000-00805f9b34fb") );

        private final UUID rawValue;

        public UUID uuid(){
            return rawValue;
        }

        DESCRIPTOR_UUID(UUID value) {
            rawValue = value;
        }
    }

    private GattDescriptorFactory(){
    }

    public static GattDescriptorFactory sharedInstance(){

        if(instance == null) {
            GLog.d("create GattDescriptorFactory");
            instance = new GattDescriptorFactory();
        }
        return instance;
    }

    public SimBluetoothGattDescriptor createCharacteristic(UUID uuid ){
        GLog.d("" + uuid.toString());

        if( null == uuid ){
            GLog.e("null uuid for Bluetooth Descriptor");
        }

        return new SimBluetoothGattDescriptor( uuid );
    }
}
