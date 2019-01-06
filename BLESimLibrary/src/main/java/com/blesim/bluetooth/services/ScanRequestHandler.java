package com.blesim.bluetooth.services;

import com.blesim.bluetooth.BluetoothDevice;
import com.blesim.bluetooth.GLog;

import java.util.UUID;
import com.blesim.bluetooth.le.ScanCallback;
import com.blesim.bluetooth.le.ScanRecord;
import com.blesim.bluetooth.le.ScanResult;
import android.os.Handler;

import static com.blesim.bluetooth.le.ScanSettings.CALLBACK_TYPE_ALL_MATCHES;

/**
 * Created by cerise on 6/8/18.
 */

class ScanRequestHandler {
    final String TAG = ScanRequestHandler.class.getSimpleName();
    final UUID id = UUID.randomUUID();

    private Boolean reScanWithNewOption = false;

    private Boolean terminate = false;

    private ScanCallback scanCallback = null;

    //static var shall use unique name to avoid unpredictable duplication.
    private Thread scanRequestThread;

    private Boolean stopScan = true;

    private MessageConsumer consumer;

    public ScanRequestHandler(){
        consumer = new MessageConsumer();
        scanRequestThread = new Thread( consumer );
        scanRequestThread.start();
    }

    public void scan( ScanCallback callback ){
        GLog.d(TAG, "scan : " + id);
        this.scanCallback = callback;

        reScanWithNewOption = true;

        stopScan = false;

        try {

            synchronized (consumer) {
                //By default, the MessageConsumer/runnable is created and paused
                //Here resume the MessageConsumer/runnable to send scan result back to user
                consumer.notify();
            }
            //I got this :
            //    resume thread failed: java.lang.IllegalMonitorStateException: object not locked by thread before notify()

        }catch ( Exception e ){
            GLog.e(TAG, "resume thread failed: " + e.toString());
        }
    }

    public void terminate(){
        stopScan( null );
        terminate = true;
        scanCallback = null;
    }

    public void stopScan( ScanCallback callback ){
        GLog.d(TAG, "stop scan : " + id);

        if( null != callback ){
            //ToDo
            //check whether callback is same as scanning start time
        }

        stopScan = true;
    }

    class MessageConsumer implements Runnable
    {
        public void run()
        {
            GLog.d(TAG, "MessageConsumer - run");

            int rssiValue = 0;

            final BluetoothDevice device =
                    PeripheralFactory.sharedInstance().createBluetoothDevice(
                            PeripheralFactory.BluetoothDeviceType.HeartRateMonitor);

            final ScanRecord recorder = new ScanRecord( device.getName());

            final ScanResult result = new ScanResult( device, recorder, rssiValue, 123);

            final Handler callbackHandler = new Handler(android.os.Looper.getMainLooper());

            while( !terminate ){

                try {
                    synchronized (this) {
                        while (stopScan) {
                            GLog.d(TAG, "scan request thread suspended");
                            this.wait();
                        }
                    }

                    if (reScanWithNewOption) {
                        //GLog.d(TAG, "reset the scan parameters, namely callback result");

                        //ToDo
                    }
                    assert null != scanCallback;

                    result.setRssi(++rssiValue);
                    //GLog.d(TAG, "return scan result");

                    callbackHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            scanCallback.onScanResult(CALLBACK_TYPE_ALL_MATCHES, result);
                        }
                    });
                    //scanCallback.onScanResult(CALLBACK_TYPE_ALL_MATCHES, result);

                    Thread.sleep(2000);
                } catch (Exception e) {

                    //The exception from caller's callback implementation will also be captured here
                    //It should ignore the exception here and let scan handler continue reporting
                    //scan result back to caller.
                    GLog.e(TAG, "scan handler exception: " + e.toString());
                }
            }
        }
    }
}
