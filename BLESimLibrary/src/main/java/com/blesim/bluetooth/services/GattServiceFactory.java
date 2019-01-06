package com.blesim.bluetooth.services;

import com.blesim.bluetooth.BluetoothGattService;
import com.blesim.bluetooth.GLog;

import java.util.UUID;

/**
 * Created by cerise on 11/18/18.
 */

class GattServiceFactory {

    private static GattServiceFactory instance = null;

    private GattServiceFactory(){
    }

    public enum SERVICE_UUID{
        HeartRateService( UUID.fromString("0000180d-0000-1000-8000-00805f9b34fb") ),
        BatteryService( UUID.fromString("0000180F-0000-1000-8000-00805f9b34fb"));

        private final UUID rawValue;

        public UUID uuid(){
            return rawValue;
        }

        SERVICE_UUID(UUID value) {
            rawValue = value;
        }
    }

    public static GattServiceFactory sharedInstance(){

        if(instance == null) {
            GLog.d("create PeripheralFactory");
            instance = new GattServiceFactory();
        }
        return instance;
    }

    public BluetoothGattService createService(UUID uuid ){

        if( uuid.equals( SERVICE_UUID.HeartRateService.uuid())){

            return new BluetoothGattService( uuid, BluetoothGattService.TYPE.SERVICE_TYPE_PRIMARY);
        }
        else if( uuid.equals( SERVICE_UUID.BatteryService.uuid())){

            return new BluetoothGattService( uuid, BluetoothGattService.TYPE.SERVICE_TYPE_PRIMARY);
        }


        return null;
    }
}
