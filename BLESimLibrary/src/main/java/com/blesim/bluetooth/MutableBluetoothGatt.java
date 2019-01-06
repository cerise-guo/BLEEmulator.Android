package com.blesim.bluetooth;

import java.lang.ref.WeakReference;
import java.util.List;
import java.util.UUID;

/**
 * Created by cerise on 11/18/18.
 */

public class MutableBluetoothGatt extends BluetoothGatt{

    public MutableBluetoothGatt(UUID deviceID, BluetoothGattCallback callback) {
        super(deviceID, callback);
    }

    public void setServices(List<BluetoothGattService> services ){
        this.mServices = services;
    }

    public boolean connect( BluetoothDevice device ) {

        this.wDevice = new WeakReference<>( device );

        return this.connect();
    }

    public void setConnectionState( final int state ){

        if( STATE_CONNECTED == state || STATE_CONNECTING == state
                || STATE_DISCONNECTED == state || STATE_DISCONNECTING == state ) {
            this.connectionState = state;
        }
        else{
            GLog.e("unknown BLE state: " + state );
        }
    }
}
