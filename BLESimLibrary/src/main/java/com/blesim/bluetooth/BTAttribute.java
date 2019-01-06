package com.blesim.bluetooth;

import java.util.UUID;

/**
 * Created by cerise on 11/10/18.
 */

public class BTAttribute {

    final private UUID uuid;

    public BTAttribute(){
        uuid = UUID.randomUUID();
    }

    public  BTAttribute( UUID id ){
        uuid = id;
    }

    public UUID getUuid() {
        return uuid;
    }
}
