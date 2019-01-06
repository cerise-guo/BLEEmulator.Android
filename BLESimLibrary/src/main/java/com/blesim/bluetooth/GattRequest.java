package com.blesim.bluetooth;

import java.lang.ref.WeakReference;
import java.util.UUID;

/**
 * Created by cerise on 11/9/18.
 */

public class GattRequest {

    public enum TYPE{
        DISCOVER_SERVICE,
        SET_NOTIFICATION,
        WRITE_DESCRIPTOR,
        READ_CHARACTERISTIC,
        WRITE_CHARACTERISTIC,
        CONNECT,
        DISCONNECT
    }

    final private WeakReference<BluetoothGatt> gattWeakReference;
    final private TYPE type;
    final private UUID uuid;
    final private boolean enable;

    //endable/disable write response, or enable/disable notification
    public GattRequest(TYPE requestType, BluetoothGatt gatt, UUID characterUUID, boolean enable ){
        this.type = requestType;
        this.gattWeakReference = new WeakReference<BluetoothGatt>(gatt);
        this.uuid = characterUUID;
        this.enable = enable;
    }

    public GattRequest(TYPE requestType, BluetoothGatt gatt, UUID descriptorUUID ){
        this.type = requestType;
        this.gattWeakReference = new WeakReference<BluetoothGatt>(gatt);
        this.uuid = descriptorUUID;
        this.enable = false;
    }

    public GattRequest(TYPE requestType, BluetoothGatt gatt){
        this.type = requestType;
        this.gattWeakReference = new WeakReference<BluetoothGatt>(gatt);
        this.uuid = null;
        this.enable = false;
    }

    public TYPE type(){
        return this.type;
    }

    public UUID getUUID(){ return this.uuid; }

    public BluetoothGatt gatt(){
        return gattWeakReference.get();
    }

    public boolean isEnable(){
        return this.enable;
    }
}
