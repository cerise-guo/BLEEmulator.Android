package com.blesim.demo;

/**
 * Created by cerise on 10/3/18.
 */

public class GATT_SERVICE_UUID {

    private static String UUID_POSTFIX = "-0000-1000-8000-00805f9b34fb";

    public static String GenericAccessUUID = "00001800" + UUID_POSTFIX;
    public static String GenericAttributeUUID = "00001801" + UUID_POSTFIX;
    public static String BatteryServiceUUID = "0000180F" + UUID_POSTFIX;
    public static String CurrentTimeServiceUUID = "00001805" + UUID_POSTFIX;
    public static String DeviceInformationServiceUUID = "0000180A" + UUID_POSTFIX;
    public static String HeartRateServiceUUID = "0000180D" + UUID_POSTFIX;

    //Apple services
    public static String ContinuityServiceUUID = "D0611E78-BBB4-4591-A5F8-487910AE4366";
    public static String AppleNotificationCenterServiceUUID = "7905f431-b5ce-4e99-a40f-4b1e122d00d0";
    public static String AppleMediaServiceUUID = "89d3502b-0f36-433a-8ef4-c502ad55f8dc";
}
