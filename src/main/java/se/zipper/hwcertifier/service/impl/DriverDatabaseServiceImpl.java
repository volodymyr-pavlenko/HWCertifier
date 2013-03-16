package se.zipper.hwcertifier.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import se.zipper.hwcertifier.domain.*;
import se.zipper.hwcertifier.repository.DriverPackageRepository;
import se.zipper.hwcertifier.service.DriverDatabaseService;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: dreamer
 * Date: 12.03.13
 * Time: 13:52
 * To change this template use File | Settings | File Templates.
 */
@Service("driverDatabaseService")
public class DriverDatabaseServiceImpl implements DriverDatabaseService {

    Map<String, List<DriverPackage>> driverDatabase = new HashMap<String, List<DriverPackage>>();
    Map<String, List<DriverPackage>> standardDatabase = new HashMap<String, List<DriverPackage>>();

    @Autowired
    DriverPackageRepository driverPackageRepository;

    @Autowired
    TaskRequest taskRequest;

    @Override
    public void registerDriverPackage(DriverPackage driverPackage) {
        registerDriverPackage(driverPackage, driverDatabase);
    }

    private void registerDriverPackage(DriverPackage driverPackage, Map<String, List<DriverPackage>> targetDatabase) {
        if (driverPackage == null) {
            return;
        }
        List<DriverPackage> driverPackageList;
        for (String hwString : driverPackage.getHardwareStrings()) {
            driverPackageList = targetDatabase.get(hwString);
            if (driverPackageList == null) {
                driverPackageList = new ArrayList<DriverPackage>();
                targetDatabase.put(hwString.toUpperCase(), driverPackageList);
            }
            driverPackageList.add(driverPackage);
        }
    }

    @Override
    public void scanFolder(String path) {
        File directory = new File(path);
        scanFolder(directory, 50, driverDatabase);
    }

    @Override
    public void scanStandardFolder() {
        scanFolder(new File("StandardDrivers\\NT" + taskRequest.getTargetBit().getOsBit() + "." + taskRequest.getTargetOs().getOsVersion()), 50, standardDatabase);
    }

    @Override
    public List<DriverPackage> matchHardwareString(int driverDatabaseType, String hardwareString) {
        Map<String, List<DriverPackage>> currentDatabase = null;
        switch (driverDatabaseType) {
            case DRIVER_DATABASE:
                currentDatabase = driverDatabase;
                break;
            case STANDARD_DRIVER_DATABASE:
            default:
                currentDatabase = standardDatabase;
        }
        return currentDatabase.get(hardwareString);
    }


    private void scanFolder(File path, int depth, Map<String, List<DriverPackage>> targetDatabase) {
        if (depth == 0) {
            return;
        }
        File[] files = path.listFiles(new FileFilter() {
            public boolean accept(File file) {
                return file.isDirectory() || file.getName().toLowerCase().endsWith(".inf");
            }
        });
        for (File file : files) {
            if (file.isDirectory()) {
                scanFolder(file, depth - 1, targetDatabase);
            } else {
                System.out.println(file.getAbsolutePath());
                registerDriverPackage(driverPackageRepository.getDriverPackageByPath(file.getAbsolutePath()), targetDatabase);
            }
        }
    }

    public void matchComputer(Computer computer) {
        String matchType = "";


        for (String bus : computer.getDevices().keySet()) {
            List<ComputerDevice> computerBus = computer.getDevices().get(bus);
            for (ComputerDevice computerDevice : computerBus) {
                matchDevice(computerDevice);
            }
        }
    }

    private void matchDevice(ComputerDevice computerDevice) {
        DriverMatch.MatchTypeSource matchTypeSource;
        for (String hardwareId : computerDevice.getHardwareId()) {
            List<DriverPackage> matchedPackage = driverDatabase.get(hardwareId);
            matchTypeSource = DriverMatch.MatchTypeSource.DRV;
            if (matchedPackage == null) {
                matchedPackage = standardDatabase.get(hardwareId);
                matchTypeSource = DriverMatch.MatchTypeSource.STD;
            }
            if (matchedPackage != null) {
                DriverMatch driverMatch = new DriverMatch();
                driverMatch.setHardwareId(hardwareId);
                driverMatch.getDriverPackage().addAll(matchedPackage);
                driverMatch.setMatchTypeSource(matchTypeSource);
                driverMatch.setMatchType(DriverMatch.MatchType.HWID);
                computerDevice.getMatchedDrivers().add(driverMatch);
                System.out.println("found a match " + hardwareId + " package = " + matchedPackage.get(0).getPathToInf() + driverMatch.getMatchType());
            }
        }
        for (String hardwareId : computerDevice.getCompatibleId()) {
            List<DriverPackage> matchedPackage = driverDatabase.get(hardwareId);
            matchTypeSource = DriverMatch.MatchTypeSource.DRV;
            if (matchedPackage == null) {
                matchedPackage = standardDatabase.get(hardwareId);
                matchTypeSource = DriverMatch.MatchTypeSource.STD;
            }
            if (matchedPackage != null) {
                DriverMatch driverMatch = new DriverMatch();
                driverMatch.setHardwareId(hardwareId);
                driverMatch.getDriverPackage().addAll(matchedPackage);
                driverMatch.setMatchTypeSource(matchTypeSource);
                driverMatch.setMatchType(DriverMatch.MatchType.COID);
                computerDevice.getMatchedDrivers().add(driverMatch);
                System.out.println("found a match " + hardwareId + " package = " + matchedPackage.get(0).getPathToInf() + ". Match = " + driverMatch.getMatchType());
            }
//                    System.out.println("no match for " + hardwareId);
        }

    }

    public void copyDriver(DriverPackage driverPackage, String targetDir) {
        CopyOption[] copyOptions = new CopyOption[] {
                StandardCopyOption.REPLACE_EXISTING,
                StandardCopyOption.COPY_ATTRIBUTES
        };
        for (String bit : DriverPackage.BITS) {
            Map<String, String> files = driverPackage.getSourceDisksFiles().get(bit);
            if (files != null) {
                for (String file : files.keySet()) {
                    String copyTo = targetDir + "\\";
                    String copyFrom = driverPackage.getBaseDir() + "\\";
                    if (!"".equals(files.get(file))) {
                        copyTo += files.get(file) + "\\";
                        copyFrom += files.get(file) + "\\";
                    }
                    try {
                        new File(copyTo).mkdirs();
                        Files.copy(Paths.get(copyFrom + file), Paths.get(copyTo + file), copyOptions);
                    } catch (IOException e) {
                        driverPackage.getCopyErrors().add("Unable to copy \"" + copyFrom + "\\" + file + "\"");
                        e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                    }
                }
            }
        }
    }

}
