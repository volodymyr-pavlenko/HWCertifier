package se.zipper.hwcertifier.service;

import se.zipper.hwcertifier.domain.Computer;
import se.zipper.hwcertifier.domain.DriverPackage;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: dreamer
 * Date: 12.03.13
 * Time: 13:52
 * To change this template use File | Settings | File Templates.
 */
public interface DriverDatabaseService {
    static public final int DRIVER_DATABASE = 1;
    static public final int STANDARD_DRIVER_DATABASE = 2;

    public void registerDriverPackage(DriverPackage driverPackage);
    public void scanFolder(String path);
    public void scanStandardFolder();
    public List<DriverPackage> matchHardwareString(int driverDatabaseType, String hardwareString);
    public void matchComputer(Computer computer);
    public void copyDriver(DriverPackage driverPackage, String targetDir);
}
