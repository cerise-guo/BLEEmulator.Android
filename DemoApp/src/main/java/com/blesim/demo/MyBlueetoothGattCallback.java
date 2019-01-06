package com.blesim.demo;

import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothProfile;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import android.bluetooth.BluetoothGattCallback;

import java.util.List;
import java.util.UUID;

/**
 * Created by cerise on 12/8/18.
 */

class MyBlueetoothGattCallback extends BluetoothGattCallback {
    final String TAG = MyBlueetoothGattCallback.class.getSimpleName();

    BluetoothGattService heartRateService = null;

    final UUID heartRateServiceUUID = createUUID("180D");
    final UUID heartRateMeasureUUID = createUUID("2A37");

    final UUID heartRateControlPointUUID = createUUID( "2A39");


    final UUID batteryLevelServiceUUID = createUUID("180F");
    final UUID batteryCharacterUUID = createUUID("2A19");

    BluetoothGattCharacteristic heartRateMeasurement = null;
    BluetoothGattCharacteristic batteryCharacter = null;
    BluetoothGattCharacteristic heartRateControlPoint = null;

    private Handler uiHandler = null;
    private BluetoothGatt mGatt = null;

    void setGatt(BluetoothGatt gatt ){
        mGatt = gatt;
    }

    void setUIHandler(Handler mainUIHandler) {
        uiHandler = mainUIHandler;
    }

    private void sendUIMessage( String msg ){
        if ( null != uiHandler ) {
            Message message = uiHandler.obtainMessage();
            message.obj = msg;
            uiHandler.sendMessage(message);
        }
    }

    private UUID createUUID(String shortUUID) {

        final String UUIDMask = "0000xxxx-0000-1000-8000-00805f9b34fb";//"0000ABCD-1212-EFDE-1523-785FEF13D123";
        String UUIDString = UUIDMask.replace("xxxx", shortUUID);

        //final String UUIDMask2 = " 0000xxxx-0000-1000-8000-00805f9b34fb";
        return UUID.fromString(UUIDString);
    }

    public void writeCharacteristic(){
        Log.d(TAG, "will write characteristic");

        if( null != heartRateControlPoint ){

            final byte values[]={16,17,18};
            heartRateControlPoint.setValue( values );
            heartRateControlPoint.setWriteType( BluetoothGattCharacteristic.WRITE_TYPE_SIGNED );
            mGatt.writeCharacteristic( heartRateControlPoint);
        }else{
            sendUIMessage( "error: no heart rate cp");
        }
    }

    public void readCharacteristic(){
        Log.d(TAG, "will read characteristic");

        if( null != batteryCharacter ){
            mGatt.readCharacteristic( batteryCharacter );
        }
        else{
            sendUIMessage( "error: no battery service");
        }
    }

    public void setNotification() {
        Log.d(TAG, "will set notification");

        if( null != heartRateMeasurement ){
            mGatt.setCharacteristicNotification( heartRateMeasurement, true );
            Log.d(TAG, "have set notification");

            List<BluetoothGattDescriptor> descriptors = heartRateMeasurement.getDescriptors();
            assert 1 == descriptors.size();
            BluetoothGattDescriptor d1 = descriptors.get(0);

            Log.d(TAG, "descriptor id: " + d1.getUuid().toString());

            d1.setValue( BluetoothGattDescriptor.ENABLE_INDICATION_VALUE);
            mGatt.writeDescriptor( d1 );
        }
        else{
            sendUIMessage( "error: no heart rate measure");
        }
    }

    @Override
    public void onPhyUpdate(BluetoothGatt gatt, int txPhy, int rxPhy, int status) {
        super.onPhyUpdate(gatt, txPhy, rxPhy, status);
    }

    @Override
    public void onPhyRead(BluetoothGatt gatt, int txPhy, int rxPhy, int status) {
        super.onPhyRead(gatt, txPhy, rxPhy, status);
    }

    @Override
    public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
        super.onConnectionStateChange(gatt, status, newState);

        Log.d(TAG, "onConnectionStateChange : old : " + status + " -> new : " + newState);
        if (BluetoothGatt.GATT_SUCCESS == status) {
            Log.d(TAG, "success status");
        }
        if (BluetoothProfile.STATE_CONNECTED == newState) {
            Log.d(TAG, "BLE connected");

            Log.d(TAG, "BLE start discovering service");
            gatt.discoverServices();

        } else if (BluetoothProfile.STATE_DISCONNECTED == newState) {
            Log.d(TAG, "BLE disconnected");

            gatt.close();
            Log.d(TAG, "BLE Gatt closed");

            this.heartRateMeasurement = null;
            this.batteryCharacter = null;
            this.heartRateControlPoint = null;
        }
    }

    @Override
    public void onServicesDiscovered(BluetoothGatt gatt, int status) {
        super.onServicesDiscovered(gatt, status);

        if (status == BluetoothGatt.GATT_SUCCESS) {
            List<BluetoothGattService> services = gatt.getServices();

            for (BluetoothGattService service : services) {

                final String serviceType =
                        (service.getType() == BluetoothGattService.SERVICE_TYPE_PRIMARY) ? "Primary" : "Secondary";

                Log.d(TAG, "discovered service: " + serviceType + " , " + service.getUuid());

                if (service.getUuid().equals(heartRateServiceUUID) ||
                    service.getUuid().equals(batteryLevelServiceUUID )) {
                    Log.d(TAG, "found target service : " + service.getUuid());

                    sendUIMessage( "found service: " + service.getUuid());

                    List<BluetoothGattCharacteristic> gattCharacteristics =
                            service.getCharacteristics();
                    for (BluetoothGattCharacteristic character : gattCharacteristics) {

                        Log.d(TAG, "characteristic : " + character.getUuid());

                        if (character.getUuid().equals(heartRateMeasureUUID)) {

                            Log.d(TAG, "found heartRateMeasure charactertistic");
                            heartRateMeasurement = character;

                            sendUIMessage( "found heart rate measure");
                        }
                        else if( character.getUuid().equals(batteryCharacterUUID)){

                            Log.d(TAG, "found batteryService characteristic");

                            batteryCharacter = character;

                            sendUIMessage( "found battery characteristic");
                        }
                        else if( character.getUuid().equals(heartRateControlPointUUID)){

                            Log.d(TAG, "found heart rate control point characteristic");

                            heartRateControlPoint = character;

                            sendUIMessage( "found heart rate control point");
                        }
                    }
                }
            }

        } else {
            Log.d(TAG, "discover service error: " + status);
        }
    }

    @Override
    public void onCharacteristicRead(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
        super.onCharacteristicRead(gatt, characteristic, status);

        Log.d(TAG, "onCharacteristicRead: " + characteristic.getValue()[0]);
    }

    @Override
    public void onCharacteristicWrite(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
        super.onCharacteristicWrite(gatt, characteristic, status);

        Log.d(TAG, "onCharacteristicWrite : " + characteristic.getUuid() + " , " + status);
    }

    @Override
    public void onCharacteristicChanged(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic) {
        super.onCharacteristicChanged(gatt, characteristic);

        Log.d(TAG, "onCharacteristicChanged : " + (int)characteristic.getValue()[1]);
    }

    @Override
    public void onDescriptorRead(BluetoothGatt gatt, BluetoothGattDescriptor descriptor, int status) {
        super.onDescriptorRead(gatt, descriptor, status);

        Log.d(TAG, "onDescriptorRead");
    }

    @Override
    public void onDescriptorWrite(BluetoothGatt gatt, BluetoothGattDescriptor descriptor, int status) {
        super.onDescriptorWrite(gatt, descriptor, status);

        Log.d(TAG, "onDescriptorWrite");
    }

    @Override
    public void onReliableWriteCompleted(BluetoothGatt gatt, int status) {
        super.onReliableWriteCompleted(gatt, status);

        Log.d(TAG, "onReliableWriteCompleted");
    }

    @Override
    public void onReadRemoteRssi(BluetoothGatt gatt, int rssi, int status) {
        super.onReadRemoteRssi(gatt, rssi, status);

        Log.d(TAG, "onReadRemoteRssi");
    }

    @Override
    public void onMtuChanged(BluetoothGatt gatt, int mtu, int status) {
        super.onMtuChanged(gatt, mtu, status);

        Log.d(TAG, "onMtuChanged");
    }
}
