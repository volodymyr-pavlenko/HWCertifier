package se.zipper.hwcertifier.repository;

import se.zipper.hwcertifier.domain.Computer;
import se.zipper.hwcertifier.domain.DriverPackage;

/**
 * Created with IntelliJ IDEA.
 * User: dreamer
 * Date: 12.03.13
 * Time: 13:38
 * To change this template use File | Settings | File Templates.
 */
public interface ComputerRepository {
    public Computer getComputerFromRegistryDump(String pathToRegFile);
}
