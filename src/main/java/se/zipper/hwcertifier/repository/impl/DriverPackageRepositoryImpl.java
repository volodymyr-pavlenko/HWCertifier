package se.zipper.hwcertifier.repository.impl;

import com.glaforge.i18n.io.CharsetToolkit;
import org.ini4j.Ini;
import org.ini4j.Wini;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import se.zipper.hwcertifier.domain.DriverPackage;
import se.zipper.hwcertifier.domain.TaskRequest;
import se.zipper.hwcertifier.domain.registry.RegistryValue;
import se.zipper.hwcertifier.parsers.CustomIniParser;
import se.zipper.hwcertifier.repository.DriverPackageRepository;

import java.io.*;
import java.nio.charset.Charset;
import java.util.*;
import java.util.regex.Pattern;

/**
 * Created with IntelliJ IDEA.
 * User: dreamer
 * Date: 12.03.13
 * Time: 14:04
 * To change this template use File | Settings | File Templates.
 */
@Component("driverPackageRepository")
public class DriverPackageRepositoryImpl implements DriverPackageRepository {
    @Autowired
    TaskRequest taskRequest;

    private Pattern manufacturerPattern = Pattern.compile("(NT)(x86|amd64|ia64){0,1}(\\.(\\d)){0,1}(\\.(\\d)){0,1}");

    @Override
    public DriverPackage getDriverPackageByPath(String driverPackagePath) {
        CustomIniParser ini;
        File file = new File(driverPackagePath);
        try {
            Charset guessedCharset = CharsetToolkit.guessEncoding(file, 4096);
            ini = new CustomIniParser(driverPackagePath, guessedCharset);
            //ini.load(new InputStreamReader(new FileInputStream(file),guessedCharset));
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        DriverPackage driverPackage = new DriverPackage();
        driverPackage.setPathToInf(file.getAbsolutePath());
        driverPackage.setBaseDir(file.getParent());
        Set<String> manufacturers = ini.enumValues("Manufacturer");
        String manufacturerValue;
        String devicesSection;
        for (String manufacturer : manufacturers) {
            manufacturerValue = ini.getProperty("Manufacturer", manufacturer).replace(" ", "");
            List<String> parts = Arrays.asList(manufacturerValue.split(","));
            if (parts.contains("NT" + taskRequest.getTargetBit().getOsBit() + taskRequest.getTargetOs().getOsVersion())) {
                devicesSection = parts.get(0) + ".NT" + taskRequest.getTargetBit().getOsBit() + taskRequest.getTargetOs().getOsVersion();
            } else if (parts.contains("NT" + taskRequest.getTargetBit().getOsBit())) {
                devicesSection = parts.get(0) + ".NT" + taskRequest.getTargetBit().getOsBit();
            } else if (parts.contains("NT")) {
                devicesSection = parts.get(0) + ".NT";
            } else {
                devicesSection = parts.get(0);
            }
            parseDevices(ini, driverPackage, devicesSection);
            parseStrings(ini, driverPackage);
            parseDisks(ini, driverPackage);
            parseFiles(ini, driverPackage);
            parseSupportFiles(ini, driverPackage);
        }
        return driverPackage;
    }

    private void parseDevices(CustomIniParser ini, DriverPackage driverPackage, String deviceSection) {
        List<String> hardwareStrings = driverPackage.getHardwareStrings();
        Set<String> deviceGroups = ini.enumValues(deviceSection);
        for (String deviceGroup : deviceGroups) {
            for (String device : ini.getProperties(deviceSection, deviceGroup)) {
                String[] parts = device.split(",", 3);
                if (parts.length >= 2) {
                    hardwareStrings.add(parts[1].trim());
                }
            }
        }
    }

    private void parseStrings(CustomIniParser ini, DriverPackage driverPackage) {
        Map<String, String> strings = driverPackage.getStrings();
        for (String str : ini.enumValues("Strings")) {
            strings.put(str, ini.getProperty("Strings", str));
        }

    }

    private void parseDisks(CustomIniParser ini, DriverPackage driverPackage) {
        Map<String, Map<String, String>> sourceDisksNames = driverPackage.getSourceDisksNames();
        for (String diskBit : DriverPackage.BITS) {
            String section = "SourceDisksNames";
            if (!"".equals(diskBit)) {
                section += "." + diskBit;
            }
            Set<String> disks = ini.enumValues(section);
            if (disks.size() > 0) {
                Map<String, String> sourceDisks = new HashMap<String, String>();
                sourceDisksNames.put(diskBit, sourceDisks);
                for (String diskId : disks) {
                    String diskValue = ini.getProperty(section, diskId);
                    String[] parts = diskValue.split(",", 3);
                    String subFolder = "";
                    if (parts.length > 1) {
                        subFolder = parts[1].trim();
                        if (subFolder.startsWith("\\")) {
                            subFolder = subFolder.substring(1);
                        }
                    }
                    sourceDisks.put(diskId, subFolder);
                }
            }
        }
    }

    private void parseFiles(CustomIniParser ini, DriverPackage driverPackage) {
        Map<String, Map<String, String>> sourceDisksFiles = driverPackage.getSourceDisksFiles();
        Map<String, Map<String, String>> sourceDisksNames = driverPackage.getSourceDisksNames();
        Map<String, String> disksNames;
        for (String diskBit : DriverPackage.BITS) {
            String section = "SourceDisksFiles";
            if (!"".equals(diskBit)) {
                section += "." + diskBit;
            }
            disksNames = sourceDisksNames.get(diskBit);
            if (disksNames == null) {
                disksNames = sourceDisksNames.get("");
            }
            if (disksNames == null) {
                disksNames = new HashMap<String, String>();
            }
            Set<String> files = ini.enumValues(section);
            if (files.size() > 0) {
                Map<String, String> sourceFiles = new HashMap<String, String>();
                sourceDisksFiles.put(diskBit, sourceFiles);
                for (String fileName : files) {
                    String fileValue = ini.getProperty(section, fileName);
                    String[] parts = fileValue.split(",", 3);
                    String subFolder = "";
                    if (parts.length > 1) {
                        subFolder = parts[1].trim();
                        if (subFolder.startsWith("\\")) {
                            subFolder = subFolder.substring(1);
                        }
                    }
                    String currentDisk = disksNames.get(parts[0]);
                    if (currentDisk != null && !"".equals(currentDisk)) {
                        subFolder = currentDisk + "\\" + subFolder;
                    }
                    sourceFiles.put(fileName, subFolder);

                }
            }
        }
    }

    private void parseSupportFiles(CustomIniParser ini, DriverPackage driverPackage) {
        Map<String, String> supportFiles = new HashMap<String, String>();
        driverPackage.getSourceDisksFiles().put(DriverPackage.BITS[0], supportFiles);
        supportFiles.put(ini.getFileName(), "");
        for (String modifier : new String[]{"", ".nt", ".ntx86", ".ntadm64", ".ntia64"}) {
            String catalogFile = ini.getProperty("Version", "CatalogFile" + modifier);
            if (catalogFile != null) {
                supportFiles.put(catalogFile, "");
            }
        }
    }
}
