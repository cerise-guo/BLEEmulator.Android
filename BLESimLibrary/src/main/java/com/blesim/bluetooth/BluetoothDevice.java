package com.blesim.bluetooth;

import android.content.Context;

import com.blesim.bluetooth.services.PeripheralContainer;

import java.lang.ref.WeakReference;
import java.util.UUID;

import javax.microedition.khronos.opengles.GL;

/**
 * Created by cerise on 5/26/18.
 */

public final class BluetoothDevice {

    final private String m_name;
    final private String m_address;

    final private UUID id = UUID.randomUUID();

    private WeakReference<MutableBluetoothGatt> m_gatt = new WeakReference<MutableBluetoothGatt>(null);

    public String getName() {
        return m_name;
    }

    //Gatt is bonded during this call
    public BluetoothGatt connectGatt(Context context, boolean autoConnect, BluetoothGattCallback callback) {
        GLog.d();

        if( null == m_gatt.get() ){
            GLog.d("Create Gatt for : " + this.id );
            m_gatt = new WeakReference<MutableBluetoothGatt>( new MutableBluetoothGatt( this.id, callback ));
        }

        if( m_gatt.get().connect( this )){
            return m_gatt.get();
        }

        GLog.e("failed to create gatt");
        return null;
    }

    public String getAddress() {
        return m_address;
    }

    public BluetoothDevice( String name, String address ){
        m_address = address;
        m_name = name;

        PeripheralContainer.sharedInstance().addPeripheral(address, this);
    }

    private BluetoothDevice(){
        m_name = "unsupported ctor";
        m_address = "unsupported ctor";
    }
}
