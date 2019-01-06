package com.blesim.bluetooth;

import com.blesim.bluetooth.services.GattServiceContainer;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Created by cerise on 11/10/18.
 */

public class BluetoothGattService extends BTAttribute {

    final static public int SERVICE_TYPE_PRIMARY = TYPE.SERVICE_TYPE_PRIMARY.value();
    final static public int SERVICE_TYPE_SECONDARY = TYPE.SERVICE_TYPE_SECONDARY.value();

    public static enum TYPE{
        SERVICE_TYPE_PRIMARY(0),
        SERVICE_TYPE_SECONDARY(1);

        private final int rawValue;

        public int value(){
            return rawValue;
        }

        TYPE(int value) {
            rawValue = value;
        }
    }

    final private TYPE mType;

    public int getType() {
        return mType.value();
    }

    public BluetoothGattService( UUID uuid, TYPE type  ){
        super( uuid );
        mType = type;

        GattServiceContainer.sharedInstance().addService(uuid, this);
    }

    List<BluetoothGattCharacteristic> m_characteristic = null;

    public List<BluetoothGattCharacteristic> getCharacteristics() {
        return m_characteristic;
    }

    public void setCharacteristic( List<BluetoothGattCharacteristic> characters ){
        m_characteristic = characters;
    }

    private BluetoothGattService() {
        mType = TYPE.SERVICE_TYPE_SECONDARY;
    }

}
