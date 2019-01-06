package com.blesim.bluetooth.services;

import com.blesim.bluetooth.BluetoothGattCharacteristic;
import com.blesim.bluetooth.BluetoothGattDescriptor;
import com.blesim.bluetooth.GLog;

import java.util.UUID;

/**
 * Created by cerise on 12/9/18.
 */

class SimBluetoothGattDescriptor extends BluetoothGattDescriptor {
    private String TAG = SimBluetoothGattDescriptor.class.getSimpleName();

    SimBluetoothGattDescriptor(UUID uuid) {
        super(uuid);
        GLog.d(TAG, "create gatt descriptor: " + uuid.toString());
        GattDescriptorContainer.sharedInstance().addDescriptor(this);
    }

    void setCharacteristic(SimBluetoothGattCharacteristic characteristic){
        this.characteristic = characteristic;
    }
}
