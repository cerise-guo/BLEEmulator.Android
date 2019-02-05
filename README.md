# BLE Simulator

This BLE simulator is a library which simulates mobile OS Bluetooth Low Energy behavior by mocking mobile OS BLE interface.

# Goal of Project

The goal of this project is to fully simulate mobile OS BLE interaction in application layer, and allow running mobile BLE application without real BLE peripheral.

# Challenge During Mobile BLE Development
 * Hard to run automatic test with synchronized BLE peripheral hardware
 * Hard to trigger end cases and timing condition
 * Mocked code presents unfaithful logic
 * Hard to test background/idle mode
 * Can not Step by Step debugging
 * software development has to wait till firmware & hardware is developed

# Documents :

| Plugin | README |
| ------ | ------ |
| Architecture and Design | [Design.md](https://github.com/cerise-guo/BLESimulator.Design/blob/master/Design.md) |
| Status and Todo | [Status.md](https://github.com/cerise-guo/BLESimulator.Design/blob/master/Status.md) |
| How to use in Android | [AndroidHelp.md](https://github.com/cerise-guo/BLESimulator.Design/blob/master/AndroidHelp.md) |
| How to use in iOS | [iOSHelp.md](https://github.com/cerise-guo/BLESimulator.Design/blob/master/iOSHelp.md) |

 