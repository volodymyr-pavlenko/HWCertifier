package se.zipper.hwcertifier.views;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import se.zipper.hwcertifier.domain.Computer;
import se.zipper.hwcertifier.domain.ComputerDevice;
import se.zipper.hwcertifier.domain.DriverMatch;
import se.zipper.hwcertifier.domain.DriverPackage;
import se.zipper.hwcertifier.service.DriverDatabaseService;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@Component
public class TextFileView implements HwCertifierView {

    @Autowired
    DriverDatabaseService driverDatabaseService;

    StringBuilder report = new StringBuilder();

    @Override
    public void renderComputer(Computer computer, String targetFolder) {
        for (String bus : computer.getDevices().keySet()) {
            File busFolder = new File(targetFolder + "\\" + bus);
            busFolder.mkdirs();
            List<ComputerDevice> computerBus = computer.getDevices().get(bus);
            report.append("\n\n\nParsing bus " + bus + "\n");
            for (ComputerDevice computerDevice : computerBus) {
                report.append("\n" + computerDevice.getDeviceName() + " (" + computerDevice.getMfg() + ")\n");
                File deviceFolder = new File(busFolder.getAbsolutePath() + "\\" + computerDevice.getDeviceName());
                deviceFolder.mkdirs();
                int i = 0;
                for (DriverMatch driverMatch : computerDevice.getMatchedDrivers()) {
                    String matchId = (++i) + "." + driverMatch.getMatchType().getValue() + "_" + driverMatch.getMatchTypeSource() + "." + driverMatch.getHardwareId().replaceAll("\\W", "_");
                    report.append("\tFound match, " + matchId + "\n");
                    String driverTargetFolder = deviceFolder.getAbsolutePath() + "\\" + matchId;
                    File driverMatchFolder = new File(driverTargetFolder);
                    driverMatchFolder.mkdirs();
                    int driverIndex = 0;
                    if (driverMatch.getMatchTypeSource() == DriverMatch.MatchTypeSource.DRV) {
                        for (DriverPackage packageToCopy : driverMatch.getDriverPackage()) {
                            String target = driverTargetFolder + "\\" + (++driverIndex);
                            report.append("\t\t\tDriver " + driverIndex + ". " + target + "\n");
                            report.append("\t\t\tCopying from " + packageToCopy.getPathToInf() + "\n");
                            driverDatabaseService.copyDriver(packageToCopy, target);
                            for (String error : packageToCopy.getCopyErrors()) {
                                report.append("!!!WARNING!!!\t\t\t\t" + error + "\n");
                            }

                        }
                    }
                }
            }
        }
        try (PrintWriter out = new PrintWriter("filename.txt");){
            out.print(report.toString());
        } catch (FileNotFoundException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

    private void renderBus() {

    }
}
