package com.blesim.demo;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothManager;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanResult;
import android.bluetooth.le.ScanSettings;
import com.blesim.bluetooth.services.BackgroundDelegate;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends Activity {

    final String TAG = "BLESimApp";

    //final String TARGET_PERIPHERAL_NAME = "Heart Rate"; //my iPhone SE
    //final String TARGET_PERIPHERAL_NAME = "iPod Touch";
    //final String TARGET_PERIPHERAL_NAME = "G5";
    final String TARGET_PERIPHERAL_NAME = "Galaxy Note8";

    private static int REQUEST_ENABLE_BT = 1000;

    private static Handler mainUIHandler;

    ScanCallback scanCallback = null;

    MyBlueetoothGattCallback gattCallback = new MyBlueetoothGattCallback();

    BluetoothGatt bluetoothGatt = null;

    private Map<String, String> deviceList = new HashMap<>();

    // Used to load the 'native-lib' library on application startup.
    static {
        System.loadLibrary("native-lib");
    }

    @SuppressLint("HandlerLeak")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.d("MainActivity", "onCreate");

        if( BuildConfig.bleSim ) {
            //This line is to enable background mode in ble emulator.
            BackgroundDelegate.startMonitorPowerStatus(this);
        }

        //Disable the BLE connect button until the peripheral is discovered.
        Button connectButton = (Button) findViewById(R.id.button_c);
        connectButton.setEnabled(false);

        int permissionCheck = this.checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION);
        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            if (this.shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_COARSE_LOCATION)) {
                Toast.makeText(this, "The permission to get BLE location data is required", Toast.LENGTH_SHORT).show();
            } else {
                requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            }
        } else {
            Toast.makeText(this, "Location permissions already granted", Toast.LENGTH_SHORT).show();
        }

        BluetoothManager btManager = (BluetoothManager) getSystemService(BLUETOOTH_SERVICE);
        BluetoothAdapter btAdapter = btManager.getAdapter();

        if (btAdapter != null && !btAdapter.isEnabled()) {
            Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
        }

        scanCallback = new ScanCallback() {
            @Override
            public void onScanResult(int callbackType,
                                     ScanResult result) {

                Boolean found = false;
                if (deviceList.containsKey(result.getDevice().getName())) {
                    found = true;
                }

                if (!found) {
                    deviceList.put(result.getDevice().getName(), result.getDevice().getAddress());
                    Log.d(TAG, "new scan result : " + result.getDevice().getName() + " , " +
                            result.getDevice().getAddress() + " , " + result.getScanRecord().getDeviceName() + ", " + result.getRssi());

                    if (TARGET_PERIPHERAL_NAME.equals(result.getDevice().getName())) {

                        //enable connect button
                        Log.d(TAG, "discovered target peripheral");
                        setUIText("discovered " + TARGET_PERIPHERAL_NAME);  //ToDo in UI thread
                        Button connectButton = (Button) findViewById(R.id.button_c);
                        connectButton.setEnabled(true);
                    }
                } else {
                    Log.d(TAG, "scan result : " + result.getDevice().getName());
                }
            }

            @Override
            public void onScanFailed(int errorCode) {
                Log.d(TAG, "scan failed: " + errorCode);
            }

            @Override
            public void onBatchScanResults(List<ScanResult> results) {
                Log.d(TAG, "batch scan result : " + results.size());
            }
        };


        mainUIHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                Log.d(TAG, "recevied UI message: " + msg.obj.toString());

                setUIText( msg.obj.toString() );

                // TODO Auto-generated method stub
                //Toast.makeText(getApplicationContext(),
                //        "Random no " + (int) msg.obj + " from BGThread",
                //        Toast.LENGTH_SHORT).show();
            }
        };
        gattCallback.setUIHandler( mainUIHandler );
    }

    private void setUIText( final String text ){

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                TextView uiMsg = (TextView) findViewById(R.id.status_text);
                uiMsg.setText(text);
            }
        });
    }

    @Override
    public Object getSystemService(String name) {

        if( BuildConfig.bleSim ) {
            if (name.equals(BLUETOOTH_SERVICE)) {
                return com.blesim.bluetooth.BluetoothManager.sharedInstance();
            }
        }
        return super.getSystemService(name);
    }

    /**
     * A native method that is implemented by the 'native-lib' native library,
     * which is packaged with this application.
     */
    public native String stringFromJNI();

    public void onClickA( View view)
    {
        Log.d(TAG, "start scan");

        BluetoothManager btManager = (BluetoothManager)getSystemService(BLUETOOTH_SERVICE);
        BluetoothAdapter btAdapter = btManager.getAdapter();

        ScanSettings settings = new ScanSettings.Builder()
                .setScanMode(ScanSettings.CALLBACK_TYPE_ALL_MATCHES)
                .build();
        btAdapter.getBluetoothLeScanner().startScan(null, settings, scanCallback);
    }

    public void onClickB( View view)
    {
        Log.d(TAG, "stop scan");

        BluetoothManager btManager = (BluetoothManager)getSystemService(BLUETOOTH_SERVICE);
        BluetoothAdapter btAdapter = btManager.getAdapter();
        btAdapter.getBluetoothLeScanner().stopScan(scanCallback);
    }


    public void onClickC( View view)
    {
        Log.d(TAG, "connect and discover service");

        BluetoothManager btManager = (BluetoothManager)getSystemService(BLUETOOTH_SERVICE);
        BluetoothAdapter btAdapter = btManager.getAdapter();

        final String address = deviceList.get( TARGET_PERIPHERAL_NAME);
        final BluetoothDevice device = btAdapter.getRemoteDevice(address);
        bluetoothGatt = device.connectGatt(this,false,gattCallback);
        gattCallback.setGatt( bluetoothGatt );
    }

    public void onClickD( View view)
    {
        Log.d(TAG, "Set notification");

        gattCallback.setNotification();
    }

    public void onClickE( View view)
    {
        Log.d(TAG, "will disconnect");

        if( null != bluetoothGatt ){
            bluetoothGatt.disconnect();
        }
    }

    public void onClickF( View view)
    {
        Log.d(TAG, "will read");
        gattCallback.readCharacteristic();

    }

    public void onClickG( View view)
    {
        Log.d(TAG, "will write");
        gattCallback.writeCharacteristic();

    }
}
