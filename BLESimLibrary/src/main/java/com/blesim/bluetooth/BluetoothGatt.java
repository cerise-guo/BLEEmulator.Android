package com.blesim.bluetooth;

import android.bluetooth.*;

import com.blesim.bluetooth.le.BluetoothLeScanner;
import com.blesim.bluetooth.services.PeripheralService;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Queue;
import java.util.UUID;

import javax.microedition.khronos.opengles.GL;

/**
 * Created by cerise on 6/28/18.
 */

public class BluetoothGatt implements  BluetoothProfile {
    final String TAG = BluetoothGatt.class.getSimpleName();

    public static final int CONNECTION_PRIORITY_BALANCED = 0;
    public static final int CONNECTION_PRIORITY_HIGH = 1;
    public static final int CONNECTION_PRIORITY_LOW_POWER = 2;
    public static final int GATT_CONNECTION_CONGESTED = 143;
    public static final int GATT_FAILURE = 257;
    public static final int GATT_INSUFFICIENT_AUTHENTICATION = 5;
    public static final int GATT_INSUFFICIENT_ENCRYPTION = 15;
    public static final int GATT_INVALID_ATTRIBUTE_LENGTH = 13;
    public static final int GATT_INVALID_OFFSET = 7;
    public static final int GATT_READ_NOT_PERMITTED = 2;
    public static final int GATT_REQUEST_NOT_SUPPORTED = 6;
    public static final int GATT_SUCCESS = 0;
    public static final int GATT_WRITE_NOT_PERMITTED = 3;

    protected Queue<GattRequest> msgQueue;

    protected WeakReference<BluetoothDevice> wDevice = new WeakReference<BluetoothDevice>(null);

    final private UUID deviceUUID;

    protected int connectionState = STATE_DISCONNECTED;

    public final BluetoothGattCallback gattCallback;

    public BluetoothGatt( UUID deviceID, BluetoothGattCallback callback ){
        deviceUUID = deviceID;
        gattCallback = callback;

        PeripheralService.sharedInstance().register(this);
    }

    public void close() {
        GLog.d(TAG, "close gatt" );

        if( STATE_CONNECTING == connectionState || STATE_DISCONNECTED == connectionState) {

            GLog.d("Disconnect gatt during close");
            connectionState = STATE_DISCONNECTING;
            this.disconnect();
        }

        //invalid current gatt
        //ToDo: how to invalid gatt
    }

    public BluetoothDevice getDevice() {
        assert null != wDevice.get();

        return wDevice.get();
    }

    public boolean discoverServices() {
        final GattRequest request = new GattRequest( GattRequest.TYPE.DISCOVER_SERVICE, this );
        msgQueue.add(request);

        return true;
    }

    public void setRequestQueue(Queue<GattRequest> queue){
        msgQueue = queue;
    }

    List<BluetoothGattService> mServices;

    public List<BluetoothGattService> getServices() {
        return mServices;
    }

    public void disconnect() {

        //1. reset all in factory classes
        //2.

        if( STATE_CONNECTING == connectionState || STATE_CONNECTED == connectionState) {
            connectionState = STATE_DISCONNECTING;

            final GattRequest request = new GattRequest(GattRequest.TYPE.DISCONNECT, this);
            msgQueue.add(request);
        }
    }

    public boolean connect() {

        //1. stop ble advertisement if it is advertising.
        final BluetoothLeScanner scanner =
                com.blesim.bluetooth.BluetoothManager.sharedInstance().getAdapter().getBluetoothLeScanner();
        assert null != scanner;
        scanner.stopScan(null); //ToDo: it should only stop advertising, not stop the scan
                                        //check android native behavior

        //2. connect peripheral
        connectionState = STATE_CONNECTING;
        final GattRequest request = new GattRequest(GattRequest.TYPE.CONNECT, this);
        return msgQueue.add(request);
    }

    public boolean setCharacteristicNotification(BluetoothGattCharacteristic characteristic, boolean enable) {
        final GattRequest request = new GattRequest( GattRequest.TYPE.SET_NOTIFICATION, this, characteristic.getUuid(), enable);
        return msgQueue.add(request);
    }

    public boolean writeCharacteristic(BluetoothGattCharacteristic characteristic) {

        final GattRequest request = new GattRequest( GattRequest.TYPE.WRITE_CHARACTERISTIC, this, characteristic.getUuid());
        return msgQueue.add(request);
    }


    public boolean writeDescriptor(BluetoothGattDescriptor descriptor) {
        final GattRequest request = new GattRequest( GattRequest.TYPE.WRITE_DESCRIPTOR, this, descriptor.getUuid());
        return msgQueue.add(request);
    }

    public boolean readCharacteristic(BluetoothGattCharacteristic characteristic) {
        final GattRequest request = new GattRequest( GattRequest.TYPE.READ_CHARACTERISTIC, this, characteristic.getUuid());
        return msgQueue.add(request);
    }


    public List<BluetoothDevice> getConnectedDevices() {
        throw new UnsupportedOperationException(
                "Use BluetoothManager#getConnectedDevices instead.");
    }


    //shall not be used
    private BluetoothGatt(){
        deviceUUID = UUID.randomUUID();
        gattCallback = null;
    }
}
