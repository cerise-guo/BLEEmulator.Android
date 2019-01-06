package com.blesim.bluetooth.services;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.PowerManager;
import android.util.Log;

import com.blesim.bluetooth.GLog;

/**
 * Created by cerise on 1/1/19.
 */

public class BackgroundDelegate {

    private BackgroundDelegate(){
        GLog.d();
    }

    public static void startMonitorPowerStatus(Context context){

        if(instance == null) {
            GLog.d("create new AdvertiseService");
            instance = new BackgroundDelegate();

            instance.registerPowerStatusEvent( context );
        }
    }


    public void stopMonitorPowerStatus( Context context ){
        if( instance != null ){
            context.unregisterReceiver(broadcastReceiver);

        }
    }

    private static BackgroundDelegate instance = null;

    private final BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            PowerManager pm = (PowerManager)context.getSystemService( Context.POWER_SERVICE);
            GLog.d("isDeviceIdleMode() : " + pm.isDeviceIdleMode());
            GLog.d("isInteractive() : " + pm.isInteractive());
            GLog.d("isPowerSaveMode() : " + pm.isPowerSaveMode());

            if( pm.isDeviceIdleMode()){
                GLog.d("trigger signal");

                BluetoothPeripheralImpl.sharedInstance().contiueInIdleMainteance();
            }
        }
    };

    private void registerPowerStatusEvent( Context ctx ){
        GLog.d();

        IntentFilter filter = new IntentFilter();
        filter.addAction(PowerManager.ACTION_DEVICE_IDLE_MODE_CHANGED);
        ctx.registerReceiver(broadcastReceiver, filter);
    }
}
