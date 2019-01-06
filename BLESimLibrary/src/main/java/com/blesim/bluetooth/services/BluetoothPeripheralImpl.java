package com.blesim.bluetooth.services;

import com.blesim.bluetooth.BluetoothGatt;
import com.blesim.bluetooth.BluetoothGattCharacteristic;
import com.blesim.bluetooth.BluetoothGattDescriptor;
import com.blesim.bluetooth.BluetoothGattService;
import com.blesim.bluetooth.GLog;
import com.blesim.bluetooth.MutableBluetoothGatt;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import javax.microedition.khronos.opengles.GL;

import static com.blesim.bluetooth.BluetoothProfile.STATE_CONNECTED;
import static com.blesim.bluetooth.BluetoothProfile.STATE_DISCONNECTED;

/**
 * Created by cerise on 12/24/18.
 */

//This class has similar function as Android BluetoothGattServerCallback class
class BluetoothPeripheralImpl {

    //NOTE:
    // 1. This class shall avoid cache/keep gatt object
    // 2. The public APIs from this class are highly possible to be called by different threads,
    //    so be careful of the data sharing cross multiple threads, consider mutex.


    //ToDo:the callbacks to caller is better to use single serialized thread.

    //This flag shall be set according to the actual test scope whether to test background mode.
    //The background related code will be triggered when this flag is true
    private final Boolean backgroundMode = false;
    private final Object idleSignal = new Object();

    public void contiueInIdleMainteance(){

        synchronized ( idleSignal) {
            GLog.d("notify callback thread to continue");
            idleSignal.notify();
        }
    }

    private static class BillPughSingleton {
        private static final BluetoothPeripheralImpl INSTANCE = new BluetoothPeripheralImpl();
    }

    public static BluetoothPeripheralImpl sharedInstance(){

        return BillPughSingleton.INSTANCE;
    }

    void onDiscoverRequest(
            final MutableBluetoothGatt gatt){
        GLog.d();

        //When creating ble service here, only this function knows the relationship between
        //service and characteristic, instead of individual service's constructor.
        //So characteristic is created and linked here too

        final BluetoothGattService s1 = CreateHeartRateService();

        //final BluetoothGattService s1 = CreateBatteryService();

        List<BluetoothGattService> services = new ArrayList<BluetoothGattService>();
        services.add( s1 );
        gatt.setServices( services );

        //To protect the peripheral service loop, call callback in separate thread
        new Thread(new Runnable(){
            public void run() {

                if( backgroundMode ){
                    synchronized (idleSignal){

                        try {
                            GLog.d("Waiting for idle status to continue");
                            idleSignal.wait();

                            gatt.gattCallback.onServicesDiscovered(
                                    gatt,
                                    BluetoothGatt.GATT_SUCCESS);

                        }catch (InterruptedException ex){
                            GLog.d("thread A exception: " + ex );
                        }
                    }
                }
                else {
                    gatt.gattCallback.onServicesDiscovered(
                            gatt,
                            BluetoothGatt.GATT_SUCCESS);
                }
            }
        }).start();
    }

    void onConnectRequest(
            final MutableBluetoothGatt gatt){
        GLog.d();

        //To protect the peripheral service loop, call callback in separate thread
        new Thread(new Runnable(){
            public void run() {

                if( backgroundMode ){
                    synchronized (idleSignal){

                        try {
                            GLog.d("Waiting for idle status to continue");
                            idleSignal.wait();

                            gatt.setConnectionState( STATE_CONNECTED );
                            gatt.gattCallback.onConnectionStateChange(
                                    gatt,
                                    BluetoothGatt.GATT_SUCCESS,
                                    STATE_CONNECTED);

                        }catch (InterruptedException ex){
                            GLog.d("thread A exception: " + ex );
                        }
                    }
                }
                else {
                    try {
                        Thread.sleep(2000);
                    }
                    catch( InterruptedException ex){
                        GLog.e(ex.toString());
                    }

                    gatt.setConnectionState( STATE_CONNECTED );
                    gatt.gattCallback.onConnectionStateChange(
                            gatt,
                            BluetoothGatt.GATT_SUCCESS,
                            STATE_CONNECTED);
                }
            }
        }).start();
    }

    void onDisconnectRequest(
            final MutableBluetoothGatt gatt){
        GLog.d();

        //To protect the peripheral service loop, call callback in separate thread
        new Thread(new Runnable(){
            public void run() {

                if( backgroundMode ){
                    synchronized (idleSignal){

                        try {
                            GLog.d("Waiting for idle status to continue");
                            idleSignal.wait();

                            gatt.setConnectionState( STATE_DISCONNECTED );
                            gatt.gattCallback.onConnectionStateChange(
                                    gatt,
                                    BluetoothGatt.GATT_SUCCESS,
                                    STATE_DISCONNECTED);

                        }catch (InterruptedException ex){
                            GLog.d("thread A exception: " + ex );
                        }
                    }
                }else {

                    gatt.setConnectionState( STATE_DISCONNECTED );
                    gatt.gattCallback.onConnectionStateChange(
                            gatt,
                            BluetoothGatt.GATT_SUCCESS,
                            STATE_DISCONNECTED);
                }

            }
        }).start();
    }

    void onNotificationRequest(
            final MutableBluetoothGatt gatt,
            UUID characteristicUUID,
            boolean enable){

        GLog.d("UUID: " + characteristicUUID.toString() + " , " + enable);

        if( null == characteristicUUID ){
            GLog.e("miss characteristic UUID");
            return;
        }

        final SimBluetoothGattCharacteristic character =
                GattCharacteristicContainer.sharedInstance().getCharacteristic( characteristicUUID );
        if( null == character ){
            GLog.e("can't find characteristic: " + characteristicUUID.toString());
            return;
        }

        if( backgroundMode ){
            synchronized (idleSignal){

                try {
                    GLog.d("Waiting for idle status to continue");
                    idleSignal.wait();

                    character.isLocalNotifying = enable;

                }catch (InterruptedException ex){
                    GLog.d("thread A exception: " + ex );
                }
            }
        }else {
            character.isLocalNotifying = enable;
        }
    }

    void onCharacteristicWriteRequest(
            final MutableBluetoothGatt gatt,
            UUID characteristicUUID){

        GLog.d("UUID: " + characteristicUUID.toString());

        if( null == characteristicUUID ){
            GLog.e("miss characteristic UUID");
            return;
        }

        final SimBluetoothGattCharacteristic character =
                GattCharacteristicContainer.sharedInstance().getCharacteristic( characteristicUUID );
        if( null == character ){
            GLog.e("can't find characteristic: " + characteristicUUID.toString());
            return;
        }

        if( null != character.pendingValue() && 0< character.pendingValue().length) {
            GLog.d("write characteristic value: " + character.pendingValue()[0]);
        }else{
            GLog.e("write characteristic value: null");
        }


        if (character.WRITE_TYPE_SIGNED == character.writeType()) {
            //To protect the peripheral service loop, call callback in separate thread
            new Thread(new Runnable() {
                public void run() {

                    if (backgroundMode) {
                        synchronized (idleSignal) {

                            try {
                                GLog.d("Waiting for idle status to continue");
                                idleSignal.wait();

                                gatt.gattCallback.onCharacteristicWrite(
                                        gatt,
                                        character,
                                        BluetoothGatt.GATT_SUCCESS);

                            } catch (InterruptedException ex) {
                                GLog.d("thread A exception: " + ex);
                            }
                        }
                    } else {
                        gatt.gattCallback.onCharacteristicWrite(
                                gatt,
                                character,
                                BluetoothGatt.GATT_SUCCESS);
                    }

                }
            }).start();
        }
    }

    void onCharacteristicReadRequest(
            final MutableBluetoothGatt gatt,
            UUID characteristicUUID){

        GLog.d("UUID: " + characteristicUUID.toString());

        if( null == characteristicUUID ){
            GLog.e("miss characteristic UUID");
            return;
        }

        final SimBluetoothGattCharacteristic simCharacter =
                GattCharacteristicContainer.sharedInstance().getCharacteristic( characteristicUUID );
        if( null == simCharacter ){
            GLog.e("can't find characteristic");
            return;
        }

        final byte newValue[]={16,15,14,13};
        simCharacter.setReadValue( newValue );

        //gatt.gattCallback.onCharacteristicRead( gatt, character,  BluetoothGatt.GATT_SUCCESS );
        new Thread(new Runnable(){
            public void run() {

                if (backgroundMode) {
                    synchronized (idleSignal) {

                        try {
                            GLog.d("Waiting for idle status to continue");
                            idleSignal.wait();

                            gatt.gattCallback.onCharacteristicRead(
                                    gatt,
                                    simCharacter,
                                    BluetoothGatt.GATT_SUCCESS );

                        } catch (InterruptedException ex) {
                            GLog.d("thread A exception: " + ex);
                        }
                    }
                } else {
                    gatt.gattCallback.onCharacteristicRead(
                            gatt,
                            simCharacter,
                            BluetoothGatt.GATT_SUCCESS );
                }


            }
        }).start();
    }

    public void onDescriptorWriteRequest (final MutableBluetoothGatt gatt,
                                          UUID descriptorUUID){
        if( null == descriptorUUID ){
            GLog.e("miss descriptor UUID");
            return;
        }

        final SimBluetoothGattDescriptor descriptor =
                GattDescriptorContainer.sharedInstance().getDescriptor(descriptorUUID);
        if( null == descriptor ){
            GLog.e("can't find descriptor");
            return;
        }

        final byte[] values = descriptor.getValue();

        GLog.d("write descriptor value : ");
        for( byte value : values){
            GLog.d("value : " + value);
        }

        final SimBluetoothGattCharacteristic simCharacter = descriptor.getCharacteristic();

        if( null == simCharacter){
            GLog.e("can't find character");
            return;
        }

        if(Arrays.equals( values, BluetoothGattDescriptor.ENABLE_INDICATION_VALUE) ||
                Arrays.equals(values, BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE))
        {
            GLog.d("enabled remote notification");
            simCharacter.isRemoteNotifying = true;
        }
        else if( Arrays.equals( values, BluetoothGattDescriptor.DISABLE_NOTIFICATION_VALUE))
        {
            GLog.d("disabled remote notification");
            simCharacter.isRemoteNotifying = false;
        }
        else{
            GLog.d("Unknown descriptor value");
        }

        //ToDo: sent character UUID to an notificationDistributor.
        //If both remote / local notification are set, the distributor can start
        //the notification
    }

    private BluetoothGattService CreateHeartRateService(){

        final GattServiceContainer svrContainer = GattServiceContainer.sharedInstance();
        final GattServiceFactory svrFactory = GattServiceFactory.sharedInstance();
        final GattCharacteristicContainer charContainer = GattCharacteristicContainer.sharedInstance();
        final GattCharacteristicFactory charFactory = GattCharacteristicFactory.sharedInstance();

        BluetoothGattService s1 = svrContainer.getService( GattServiceFactory.SERVICE_UUID.HeartRateService.uuid());
        if( null == s1 ){
            s1 = svrFactory.createService( GattServiceFactory.SERVICE_UUID.HeartRateService.uuid() );

            SimBluetoothGattCharacteristic char1 = charContainer.getCharacteristic(GattCharacteristicFactory.CHARACTERISTIC_UUID.HeartRateMeasurement.uuid());
            if( null == char1 ){
                char1 = charFactory.createCharacteristic(GattCharacteristicFactory.CHARACTERISTIC_UUID.HeartRateMeasurement.uuid());
            }
            {
                //create descriptor and link to characteristic
                final GattDescriptorContainer descContainer = GattDescriptorContainer.sharedInstance();

                SimBluetoothGattDescriptor descriptor = descContainer.getDescriptor(
                        GattDescriptorFactory.DESCRIPTOR_UUID.CHARACTERISTIC_CONFIGURATION.uuid());
                if (null == descriptor) {
                    descriptor = GattDescriptorFactory.sharedInstance().createCharacteristic(
                            GattDescriptorFactory.DESCRIPTOR_UUID.CHARACTERISTIC_CONFIGURATION.uuid()
                    );
                    descriptor.setCharacteristic(char1);
                }
                assert (descriptor.getCharacteristic().hashCode() == char1.hashCode());

                List<BluetoothGattDescriptor> descriptors = new ArrayList<BluetoothGattDescriptor>();
                descriptors.add(descriptor);
                char1.setDescriptors(descriptors);
            }
            List<BluetoothGattCharacteristic> characters = new ArrayList<BluetoothGattCharacteristic>();
            characters.add( char1 );

            char1 = charContainer.getCharacteristic(GattCharacteristicFactory.CHARACTERISTIC_UUID.BodySensorLocation.uuid());
            if( null == char1 ){
                char1 = charFactory.createCharacteristic(GattCharacteristicFactory.CHARACTERISTIC_UUID.BodySensorLocation.uuid());
            }
            characters.add( char1 );

            char1 = charContainer.getCharacteristic(GattCharacteristicFactory.CHARACTERISTIC_UUID.HeartRateControlPoint.uuid());
            if( null == char1 ){
                char1 = charFactory.createCharacteristic(GattCharacteristicFactory.CHARACTERISTIC_UUID.HeartRateControlPoint.uuid());
            }
            characters.add( char1 );

            s1.setCharacteristic(characters);
        }else{
            GLog.d("already created service, reuse existing service");
        }

        return s1;
    }


    private BluetoothGattService CreateBatteryService(){

        final GattServiceContainer svrContainer = GattServiceContainer.sharedInstance();
        final GattServiceFactory svrFactory = GattServiceFactory.sharedInstance();
        final GattCharacteristicContainer charContainer = GattCharacteristicContainer.sharedInstance();
        final GattCharacteristicFactory charFactory = GattCharacteristicFactory.sharedInstance();

        BluetoothGattService s1 = svrContainer.getService( GattServiceFactory.SERVICE_UUID.BatteryService.uuid());
        if( null == s1 ){
            s1 = svrFactory.createService( GattServiceFactory.SERVICE_UUID.BatteryService.uuid() );

            SimBluetoothGattCharacteristic char1 = charContainer.getCharacteristic(GattCharacteristicFactory.CHARACTERISTIC_UUID.BatteryLevel.uuid());
            if( null == char1 ){
                char1 = charFactory.createCharacteristic(GattCharacteristicFactory.CHARACTERISTIC_UUID.BatteryLevel.uuid());
            }

            List<BluetoothGattCharacteristic> characters = new ArrayList<BluetoothGattCharacteristic>();
            characters.add( char1 );

            s1.setCharacteristic(characters);
        }

        return s1;
    }
}
