package com.sjimtv.remoteclient.showStructure;

public class ConnectedDevice {

    private String name;
    private String ipAddress;

    public ConnectedDevice(){

    }

    public ConnectedDevice(String name, String ipAddress){
        this.name = name;
        this.ipAddress = ipAddress;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public String getName() {
        return name;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public void setName(String name) {
        this.name = name;
    }
}
