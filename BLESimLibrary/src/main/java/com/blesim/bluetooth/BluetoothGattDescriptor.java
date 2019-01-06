package com.blesim.bluetooth;

import android.bluetooth.*;

import com.blesim.bluetooth.services.SimBluetoothGattCharacteristic;

import java.util.UUID;

/**
 * Created by cerise on 6/28/18.
 */

public class BluetoothGattDescriptor extends BTAttribute {

    public static final byte[] DISABLE_NOTIFICATION_VALUE = {0,0};
    public static final byte[] ENABLE_INDICATION_VALUE = {2,0};
    public static final byte[] ENABLE_NOTIFICATION_VALUE = {1,0};

    public BluetoothGattDescriptor( UUID uuid ){
        super( uuid );
    }
    protected SimBluetoothGattCharacteristic characteristic;

    byte[] values;

    public boolean setValue(byte[] value) {
        values = value;

        return true;
    }

    public byte[] getValue(){
        return values;
    }

    public SimBluetoothGattCharacteristic getCharacteristic() {
        return characteristic;
    }
}
