package com.blesim.bluetooth.services;

import com.blesim.bluetooth.BluetoothGatt;
import com.blesim.bluetooth.BluetoothGattCharacteristic;
import com.blesim.bluetooth.BluetoothGattDescriptor;
import com.blesim.bluetooth.BluetoothGattService;
import com.blesim.bluetooth.GLog;
import com.blesim.bluetooth.GattRequest;
import com.blesim.bluetooth.MutableBluetoothGatt;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadLocalRandom;

import static com.blesim.bluetooth.BluetoothProfile.STATE_CONNECTED;
import static com.blesim.bluetooth.BluetoothProfile.STATE_DISCONNECTED;

/**
 * Created by cerise on 11/10/18.
 */

public class PeripheralService implements Runnable {
    private String TAG = PeripheralService.class.getSimpleName();

    final private int id = ThreadLocalRandom.current().nextInt(1, 1000 + 1);

    private LinkedBlockingQueue<GattRequest> m_MsgQueue;

    private Thread m_dispathThread;


    private PeripheralService(){
        GLog.d(Integer.toString(id));
        m_MsgQueue = new LinkedBlockingQueue(50);
    }

    private static PeripheralService instance = null;


    //At the first release, the design supports only one peripheral / gatt object,
    //so this singleton is acceptable.
    public static PeripheralService sharedInstance(){

        if(instance == null) {
            GLog.d("create new PeripheralService");
            instance = new PeripheralService();
            instance.m_dispathThread = new Thread(instance);
            instance.m_dispathThread.start();
        }
        return instance;
    }

    public void register( BluetoothGatt gatt ){
        GLog.d();

        gatt.setRequestQueue( this.m_MsgQueue );
    }

    @Override
    public void run() {
        GLog.d();

        try {
            GattRequest request = null;

            while( null != (request = m_MsgQueue.take())){

                GLog.d("received gatt request : " + request.type() );

                if(!( request.gatt() instanceof  MutableBluetoothGatt )){
                    GLog.e("error: got immutable gatt from gatt request");
                    continue;
                }

                final MutableBluetoothGatt mutableGatt = (MutableBluetoothGatt)request.gatt();

                switch( request.type()){
                    case DISCONNECT:
                    {
                        GLog.d("received disconnect request");

                        new Thread(new Runnable(){
                            public void run() {
                                final BluetoothPeripheralImpl callback = BluetoothPeripheralImpl.sharedInstance();

                                try {
                                    callback.onDisconnectRequest(mutableGatt);
                                }catch(Exception e){
                                    GLog.d("exception: " + e.toString());
                                }

                            }
                        }).start();

                        break;
                    }
                    case CONNECT:
                    {
                        GLog.d("received connect request");

                        new Thread(new Runnable(){
                            public void run() {
                                final BluetoothPeripheralImpl callback = BluetoothPeripheralImpl.sharedInstance();

                                try{
                                callback.onConnectRequest(mutableGatt);
                                }catch(Exception e){
                                    GLog.d("exception: " + e.toString());
                                }
                            }
                        }).start();

                        break;
                    }
                    case DISCOVER_SERVICE:
                    {
                        GLog.d("received discover service request");

                        new Thread(new Runnable(){
                            public void run() {
                                final BluetoothPeripheralImpl callback = BluetoothPeripheralImpl.sharedInstance();

                                try{
                                callback.onDiscoverRequest(mutableGatt);
                                }catch(Exception e){
                                    GLog.d("exception: " + e.toString());
                                }
                            }
                        }).start();

                        break;
                    }
                    case SET_NOTIFICATION:
                    {
                        GLog.d("received set notification request");

                        final UUID characteristicUUID = request.getUUID();
                        final boolean enableNotification = request.isEnable();

                        new Thread(new Runnable(){
                            public void run() {
                                final BluetoothPeripheralImpl callback = BluetoothPeripheralImpl.sharedInstance();

                                try{
                                callback.onNotificationRequest(
                                        mutableGatt,
                                        characteristicUUID,
                                        enableNotification);
                                }catch(Exception e){
                                    GLog.d("exception: " + e.toString());
                                }
                            }
                        }).start();

                        break;
                    }
                    case WRITE_DESCRIPTOR:
                    {
                        GLog.d("received write descriptor request");

                        final UUID descriptorUUID = request.getUUID();

                        new Thread(new Runnable(){
                            public void run() {
                                final BluetoothPeripheralImpl callback = BluetoothPeripheralImpl.sharedInstance();

                                try{
                                callback.onDescriptorWriteRequest (
                                        mutableGatt,
                                        descriptorUUID);
                                }catch(Exception e){
                                    GLog.d("exception: " + e.toString());
                                }
                            }
                        }).start();

                        break;
                    }
                    case WRITE_CHARACTERISTIC:
                    {
                        GLog.d("received write characteristic request");

                        final UUID characteristicUUID = request.getUUID();

                        new Thread(new Runnable(){
                            public void run() {
                                final BluetoothPeripheralImpl callback = BluetoothPeripheralImpl.sharedInstance();

                                try{
                                callback.onCharacteristicWriteRequest(
                                        mutableGatt,
                                        characteristicUUID);
                                }catch(Exception e){
                                    GLog.d("exception: " + e.toString());
                                }
                            }
                        }).start();

                        break;
                    }
                    case READ_CHARACTERISTIC:
                    {
                        GLog.d(TAG, "received read characteristic request");

                        final UUID characteristicUUID = request.getUUID();

                        new Thread(new Runnable(){
                            public void run() {
                                final BluetoothPeripheralImpl callback = BluetoothPeripheralImpl.sharedInstance();
                                try{
                                callback.onCharacteristicReadRequest(
                                        mutableGatt,
                                        characteristicUUID);
                                }catch(Exception e){
                                    GLog.d("exception: " + e.toString());
                                }
                            }
                        }).start();

                        break;
                    }
                }
            }
        }catch(InterruptedException e) {
            e.printStackTrace();
        }
    }
}
