package com.blesim.bluetooth;

import java.util.concurrent.ThreadLocalRandom;


/**
 * Created by cerise on 4/21/18.
 */

public class BluetoothManager {

    //final private String TAG = BluetoothManager.class.getSimpleName();

    final private int id = ThreadLocalRandom.current().nextInt(1, 1000 + 1);

    private static BluetoothAdapter mAdapter;

    private static class BillPughSingleton {
        private static final BluetoothManager INSTANCE = new BluetoothManager();
    }

    public static BluetoothManager sharedInstance(){

        return BillPughSingleton.INSTANCE;
    }

    private BluetoothManager(){

        GLog.d( "ctor " + Integer.toString(id));

        if( null == mAdapter ) {
            mAdapter = new BluetoothAdapter();
        }
    }

    public BluetoothAdapter getAdapter(){
        GLog.d();

        return mAdapter;
    }

    @Override
    protected void finalize(){
        GLog.d(Integer.toString(id));
    }
}
