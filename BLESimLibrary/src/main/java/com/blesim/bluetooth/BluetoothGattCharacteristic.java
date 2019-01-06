package com.blesim.bluetooth;

import com.blesim.bluetooth.services.GattCharacteristicContainer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

/**
 * Created by cerise on 6/28/18.
 */

public class BluetoothGattCharacteristic extends BTAttribute{

    public static final int PERMISSION_READ = 1;
    public static final int PERMISSION_READ_ENCRYPTED = 2;
    public static final int PERMISSION_READ_ENCRYPTED_MITM = 4;
    public static final int PERMISSION_WRITE = 16;
    public static final int PERMISSION_WRITE_ENCRYPTED = 32;
    public static final int PERMISSION_WRITE_ENCRYPTED_MITM = 64;
    public static final int PERMISSION_WRITE_SIGNED = 128;
    public static final int PERMISSION_WRITE_SIGNED_MITM = 256;
    public static final int PROPERTY_BROADCAST = 1;
    public static final int PROPERTY_EXTENDED_PROPS = 128;
    public static final int PROPERTY_INDICATE = 32;
    public static final int PROPERTY_NOTIFY = 16;
    public static final int PROPERTY_READ = 2;
    public static final int PROPERTY_SIGNED_WRITE = 64;
    public static final int PROPERTY_WRITE = 8;
    public static final int PROPERTY_WRITE_NO_RESPONSE = 4;
    public static final int WRITE_TYPE_DEFAULT = 2;
    public static final int WRITE_TYPE_NO_RESPONSE = 1;
    public static final int WRITE_TYPE_SIGNED = 4;

    protected BluetoothGattCharacteristic( UUID uuid ){
        super( uuid );
    }

    //data is initialized as empty.
    protected byte[] values = {};

    protected byte[] pendingWrtingValue = {};

    protected int writeType = WRITE_TYPE_DEFAULT;

    public byte[] getValue() {
        return values;
    }

    //set pending value for writing operation
    public boolean setValue(byte[] value) {
        pendingWrtingValue = Arrays.copyOf(value, value.length);

        return true;
    }

    public void setWriteType(int writeType) {
       this.writeType = writeType;
    }

    protected List<BluetoothGattDescriptor> descriptors = new ArrayList<BluetoothGattDescriptor>();

    Boolean isNotifying = false;

    public List<BluetoothGattDescriptor> getDescriptors() {
        return descriptors;
    }

    //shall not be called
    private BluetoothGattCharacteristic(){
    }
}
