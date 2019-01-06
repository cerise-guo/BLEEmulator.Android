package com.blesim.bluetooth;

/**
 * Created by cerise on 6/28/18.
 */

public interface BluetoothProfile {

    int A2DP = 2;
    String EXTRA_PREVIOUS_STATE = "android.bluetooth.profile.extra.PREVIOUS_STATE";
    String EXTRA_STATE = "android.bluetooth.profile.extra.STATE";
    int GATT = 7;
    int GATT_SERVER = 8;
    int HEADSET = 1;
    int HEALTH = 3;
    int SAP = 10;
    int STATE_CONNECTED = 2;
    int STATE_CONNECTING = 1;
    int STATE_DISCONNECTED = 0;
    int STATE_DISCONNECTING = 3;


}
