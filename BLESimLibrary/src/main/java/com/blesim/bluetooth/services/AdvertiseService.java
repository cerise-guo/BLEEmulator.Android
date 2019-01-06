package com.blesim.bluetooth.services;

import android.util.Log;

import com.blesim.bluetooth.GLog;
import com.blesim.bluetooth.le.BLERequest;
import com.blesim.bluetooth.le.BluetoothLeScanner;

import java.util.HashMap;
import java.util.Map;
import java.util.Queue;
import java.util.UUID;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Created by cerise on 6/7/18.
 */

public class AdvertiseService implements Runnable {
    //final static private String TAG = AdvertiseService.class.getSimpleName();

    final private int id = ThreadLocalRandom.current().nextInt(1, 1000 + 1);

    private LinkedBlockingQueue<BLERequest> m_MsgQueue;
    private Thread m_dispathThread;

    //public Queue<BLERequest> getMsgQueue(){
    //    return m_MsgQueue;
    //}

    private AdvertiseService(){
        GLog.d(Integer.toString(id));
        m_MsgQueue = new LinkedBlockingQueue(50);
    }

    public static AdvertiseService sharedInstance(){

        if(instance == null) {
            GLog.d("create new AdvertiseService");
            instance = new AdvertiseService();
            instance.m_dispathThread = new Thread(instance);
            instance.m_dispathThread.start();
        }
        return instance;
    }

    private static AdvertiseService instance = null;

    public void register(BluetoothLeScanner scanner){
        GLog.d();

        scanner.setRequestQueue( this.m_MsgQueue );
    }

    @Override
    public void run(){
        GLog.d();

        Map<UUID, ScanRequestHandler> scannerMap = new HashMap<UUID, ScanRequestHandler>();

        try{
            BLERequest  request = null;

            while( null != (request = m_MsgQueue.take())){

                GLog.d("received msg: " + request.type() + " , " + request.bleScannerID() );

                switch( request.type() ){
                    case SCAN: {
                        GLog.d("get scan request");

                        ScanRequestHandler handler = scannerMap.get(request.bleScannerID());

                        //If there is existing scan
                        if (null == handler) {
                            handler = new ScanRequestHandler();
                            scannerMap.put(request.bleScannerID(), handler);
                        } else {
                            //stop the existing scan operation first.
                            //ToDo: should stop running scanning or throw exception, what is OS behavior ?
                            handler.stopScan( request.callback());
                        }
                        handler.scan( request.callback() );

                        break;
                    }
                    case STOP_SCAN: {
                        GLog.d("get stop-scan request");

                        ScanRequestHandler handler = scannerMap.get(request.bleScannerID());
                        if (null == handler) {
                            GLog.e("can not find scan handler for : " + request.bleScannerID());

                            GLog.d("Not a real error, the user may call stop scan directly. The " +
                                    "assert is just for monitoring and needs to be removed in " +
                                    "formal project.");
                            //ToDo: remove the assert in formal project
                            assert false;
                        } else {
                            //stop the existing scan operation first.
                            handler.stopScan( request.callback() );
                        }
                        break;
                    }
                    default:
                        GLog.d("unknown ble request");
                        break;
                }
            }
        }catch(InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void finalize(){
        GLog.d(Integer.toString(id));
    }
}
