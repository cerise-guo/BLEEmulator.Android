package com.blesim.bluetooth.services;

import com.blesim.bluetooth.BluetoothGattCharacteristic;
import com.blesim.bluetooth.BluetoothGattDescriptor;
import com.blesim.bluetooth.GLog;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

/**
 * Created by cerise on 12/9/18.
 */

public class SimBluetoothGattCharacteristic extends BluetoothGattCharacteristic {
    private String TAG = SimBluetoothGattCharacteristic.class.getSimpleName();

    SimBluetoothGattCharacteristic(UUID uuid) {
        super(uuid);
        GLog.d(TAG, "create gatt characterisitc : " + uuid.toString());
        GattCharacteristicContainer.sharedInstance().addCharacteristic(this);
    }

    Boolean isLocalNotifying = false;
    Boolean isRemoteNotifying = false;

    public Boolean isNotificationActivated(){
        return isLocalNotifying && isRemoteNotifying;
    }

    //set value which will be returned to read request
    public void setReadValue( byte[] values) {
        this.values = Arrays.copyOf(values, values.length);
    }

    byte[] pendingValue(){
        return this.pendingWrtingValue;
    }

    public int writeType() {
        return this.writeType;
    }

    public void setReadableValue( byte[] newValue ){
        if( null == newValue ){
            GLog.d("null value");
            return;
        }

        this.values = Arrays.copyOf( newValue, newValue.length );
    }

    public void setDescriptors(List<BluetoothGattDescriptor> descriptors ){
        for( BluetoothGattDescriptor descriptor : descriptors){
            if( !this.descriptors.contains( descriptor)){
                GLog.d(TAG, "link descriptor to character");
                this.descriptors.add( descriptor );
            }else{
                GLog.d(TAG, "descriptor had been linked to charactertistic.");
            }
        }
    }
}
