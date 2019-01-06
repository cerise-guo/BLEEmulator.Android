package com.blesim.bluetooth;

import com.blesim.bluetooth.le.*;
import com.blesim.bluetooth.services.PeripheralContainer;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Created by cerise on 4/22/18.
 */

public class BluetoothAdapter {

    //final private String TAG = BluetoothAdapter.class.getSimpleName();

    static private BluetoothLeScanner scanner = null;

    final private int id = ThreadLocalRandom.current().nextInt(1, 1000 + 1);

    public static final String ACTION_REQUEST_ENABLE = "android.bluetooth.adapter.action.REQUEST_ENABLE";

    public BluetoothAdapter(){

        GLog.d( "ctor " + Integer.toString(id));

        GLog.detectStackDepth(); //initiate the logger

        GLog.d(Integer.toString(id));
    }

    public BluetoothDevice getRemoteDevice(String address) {

        final BluetoothDevice device = PeripheralContainer.sharedInstance().getDevice(address);
        if( null == device ){
            //ToDo: if device is not cached already, scan and discover it ?
        }

        return device;
    }

    public BluetoothLeScanner getBluetoothLeScanner() {

        if( null == scanner ) {

            //To keep the non-public ctor, use reflect to access ctor
            Constructor<BluetoothLeScanner> constructor = (Constructor<BluetoothLeScanner>) BluetoothLeScanner.class.getDeclaredConstructors()[0];
            constructor.setAccessible(true);
            try {
                scanner = constructor.newInstance();
            } catch (InstantiationException e) {
                GLog.d(e.toString());
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                GLog.d(e.toString());
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                GLog.d(e.toString());
                e.printStackTrace();
            }
        }

        return this.scanner;
    }

    public String getAddress(){
        return "xx:xx:xx:xx:xx:xx";
    }

    public boolean isEnabled() {
        return true;
    }

    @Override
    protected void finalize(){
        GLog.d(Integer.toString(id));
    }
}
