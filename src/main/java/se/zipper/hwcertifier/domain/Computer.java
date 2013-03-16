package se.zipper.hwcertifier.domain;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: dreamer
 * Date: 12.03.13
 * Time: 17:23
 * To change this template use File | Settings | File Templates.
 */
public class Computer {
    private String vendor;
    private String model;
    //map BUS=>devices
    private Map<String, List<ComputerDevice>> devices = new HashMap<String, List<ComputerDevice>>();

    public String getVendor() {
        return vendor;
    }

    public void setVendor(String vendor) {
        this.vendor = vendor;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public Map<String, List<ComputerDevice>> getDevices() {
        return devices;
    }
}
