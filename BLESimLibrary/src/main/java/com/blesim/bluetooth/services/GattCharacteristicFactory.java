package com.blesim.bluetooth.services;

import com.blesim.bluetooth.BluetoothGattCharacteristic;
import com.blesim.bluetooth.GLog;

import java.util.UUID;

/**
 * Created by cerise on 11/18/18.
 */

class GattCharacteristicFactory {

    private static GattCharacteristicFactory instance = null;

    private GattCharacteristicFactory(){
    }

    public enum CHARACTERISTIC_UUID{
        HeartRateMeasurement( UUID.fromString("00002a37-0000-1000-8000-00805f9b34fb") ),
        BodySensorLocation( UUID.fromString("00002a38-0000-1000-8000-00805f9b34fb") ),
        HeartRateControlPoint( UUID.fromString("00002a39-0000-1000-8000-00805f9b34fb") ),

        BatteryLevel( UUID.fromString("00002a19-0000-1000-8000-00805f9b34fb") );

        private final UUID rawValue;

        public UUID uuid(){
            return rawValue;
        }

        CHARACTERISTIC_UUID(UUID value) {
            rawValue = value;
        }
    }

    public static GattCharacteristicFactory sharedInstance(){

        if(instance == null) {
            GLog.d("create GattCharacteristicFactory");
            instance = new GattCharacteristicFactory();
        }
        return instance;
    }

    public SimBluetoothGattCharacteristic createCharacteristic(UUID uuid ){

        if( uuid.equals( CHARACTERISTIC_UUID.HeartRateMeasurement.uuid())){

            return new SimBluetoothGattCharacteristic( uuid );
        }
        else if( uuid.equals( CHARACTERISTIC_UUID.BodySensorLocation.uuid())){

            return new SimBluetoothGattCharacteristic( uuid );
        }
        else if( uuid.equals( CHARACTERISTIC_UUID.HeartRateControlPoint.uuid())){

            return new SimBluetoothGattCharacteristic( uuid );
        }
        else if( uuid.equals( CHARACTERISTIC_UUID.BatteryLevel.uuid())){

            return new SimBluetoothGattCharacteristic( uuid );
        }
        else
        {
            GLog.e("unexpected characteristic : " + uuid );
        }

        return null;
    }
}
