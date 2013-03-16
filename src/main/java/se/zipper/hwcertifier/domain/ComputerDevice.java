package se.zipper.hwcertifier.domain;

import java.util.ArrayList;
import java.util.List;

public class ComputerDevice {
    private String detectedName;
    private String detectedVendor;
    private String deviceName;
    private List<String> hardwareId = new ArrayList<String>();
    private List<String> compatibleId = new ArrayList<String>();
    private List<DriverMatch> matchedDrivers = new ArrayList<DriverMatch>();
    private String Mfg;

    public ComputerDevice() {

    }

    public String getMfg() {
        return Mfg;
    }

    public void setMfg(String mfg) {
        Mfg = mfg;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public List<DriverMatch> getMatchedDrivers() {
        return matchedDrivers;
    }

    public String getDetectedName() {
        return detectedName;
    }

    public void setDetectedName(String detectedName) {
        this.detectedName = detectedName;
    }

    public String getDetectedVendor() {
        return detectedVendor;
    }

    public void setDetectedVendor(String detectedVendor) {
        this.detectedVendor = detectedVendor;
    }

    public List<String> getHardwareId() {
        return hardwareId;
    }

    public List<String> getCompatibleId() {
        return compatibleId;
    }
}
