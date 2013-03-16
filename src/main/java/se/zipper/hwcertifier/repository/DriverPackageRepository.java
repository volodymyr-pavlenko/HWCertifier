package se.zipper.hwcertifier.repository;

import se.zipper.hwcertifier.domain.DriverPackage;

import java.io.FileNotFoundException;

/**
 * Created with IntelliJ IDEA.
 * User: dreamer
 * Date: 12.03.13
 * Time: 13:39
 * To change this template use File | Settings | File Templates.
 */
public interface DriverPackageRepository {
    public DriverPackage getDriverPackageByPath(String driverPackagePath);
}
