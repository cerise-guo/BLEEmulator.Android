package com.blesim.bluetooth;

import android.util.Log;

/**
 * Created by cerise on 6/28/18.
 */

public abstract class BluetoothGattCallback {

    final static String TAG = BluetoothGattCallback.class.getSimpleName();

    public BluetoothGattCallback() {
        //throw new RuntimeException("Stub!");
    }

    public void onPhyUpdate(BluetoothGatt gatt, int txPhy, int rxPhy, int status) {
        throw new RuntimeException("Stub!");
    }

    public void onPhyRead(BluetoothGatt gatt, int txPhy, int rxPhy, int status) {
        throw new RuntimeException("Stub!");
    }

    public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
        Log.d(TAG, "onConnectionStateChange");
    }

    public void onServicesDiscovered(BluetoothGatt gatt, int status) {
        Log.d(TAG, "onServicesDiscovered");
    }


    public void onCharacteristicRead(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
        Log.d(TAG, "onCharacteristicRead");
    }

    public void onCharacteristicWrite(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
        Log.d(TAG, "onCharacteristicWrite");
    }

    public void onCharacteristicChanged(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic) {
        throw new RuntimeException("Stub!");
    }

    public void onDescriptorRead(BluetoothGatt gatt, BluetoothGattDescriptor descriptor, int status) {
        throw new RuntimeException("Stub!");
    }

    public void onDescriptorWrite(BluetoothGatt gatt, BluetoothGattDescriptor descriptor, int status) {
        throw new RuntimeException("Stub!");
    }

    public void onReliableWriteCompleted(BluetoothGatt gatt, int status) {
        throw new RuntimeException("Stub!");
    }

    public void onReadRemoteRssi(BluetoothGatt gatt, int rssi, int status) {
        throw new RuntimeException("Stub!");
    }

    public void onMtuChanged(BluetoothGatt gatt, int mtu, int status) {
        throw new RuntimeException("Stub!");
    }
}
