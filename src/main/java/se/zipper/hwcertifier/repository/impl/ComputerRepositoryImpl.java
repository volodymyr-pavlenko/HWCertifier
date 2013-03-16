package se.zipper.hwcertifier.repository.impl;

import com.glaforge.i18n.io.CharsetToolkit;
import org.ini4j.Reg;
import org.springframework.stereotype.Repository;
import se.zipper.hwcertifier.domain.Computer;
import se.zipper.hwcertifier.domain.ComputerDevice;
import se.zipper.hwcertifier.domain.registry.RegistryKey;
import se.zipper.hwcertifier.domain.registry.RegistryValue;
import se.zipper.hwcertifier.parsers.CustomIniParser;
import se.zipper.hwcertifier.parsers.RegistryParser;
import se.zipper.hwcertifier.repository.ComputerRepository;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.*;

@Repository
public class ComputerRepositoryImpl implements ComputerRepository {
    private final String PATH_TO_ENUM = "HKEY_LOCAL_MACHINE\\SYSTEM\\CurrentControlSet\\Enum\\";
    private final String[] BUSES_TO_SCAN = {"ACPI", "HDAUDIO", "DISPLAY", "PCI", "USB"};

    @Override
    public Computer getComputerFromRegistryDump(String pathToRegFile) {
        RegistryParser reg = new RegistryParser();
        File file = new File(pathToRegFile);
        RegistryKey root;
        try {
            Charset guessedCharset = CharsetToolkit.guessEncoding(file, 4096);
            //reg.load(new InputStreamReader(new FileInputStream(file),guessedCharset));
            root = reg.loadFile(pathToRegFile, guessedCharset);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        Computer computer = new Computer();
        for (String bus : BUSES_TO_SCAN) {
            RegistryKey busRegistryKey = root.getRegistryKey(PATH_TO_ENUM + bus);
            List<ComputerDevice> computerBus = new ArrayList<ComputerDevice>();
            computer.getDevices().put(bus, computerBus);
            if (busRegistryKey == null) {
                continue;
            }
            Map<String, RegistryKey> devicesRegistryKeys = busRegistryKey.getKeys();
            List<String> keySet =new ArrayList<String>(devicesRegistryKeys.keySet());
            Collections.sort(keySet);
            for (String deviceName : keySet) {
                RegistryKey deviceRegistryKey = devicesRegistryKeys.get(deviceName).findSubKeyWithValue("HardwareID");
                if (deviceRegistryKey == null) {
                    continue;
                }
                ComputerDevice computerDevice = new ComputerDevice();
                computerDevice.setDeviceName(deviceName);
                computerDevice.setMfg(deviceRegistryKey.getStringValue("Mfg"));
                String compatibleIdsValue = deviceRegistryKey.getStringValue("CompatibleIDs");
                String hardwareIdValue = deviceRegistryKey.getStringValue("HardwareID");
                if (compatibleIdsValue != null) {
                    String[] compatibleIds = compatibleIdsValue.split("\\u0000");
                    computerDevice.getCompatibleId().addAll(Arrays.asList(compatibleIds));
                }
                if (hardwareIdValue != null) {
                    String[] hardwareId= hardwareIdValue.split("\\u0000");
                    computerDevice.getHardwareId().addAll(Arrays.asList(hardwareId));
                }
                computerBus.add(computerDevice);
            }
        }

        return computer;
    }
}
