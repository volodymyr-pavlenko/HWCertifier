package se.zipper.hwcertifier.controller;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import se.zipper.hwcertifier.domain.Computer;
import se.zipper.hwcertifier.domain.DriverPackage;
import se.zipper.hwcertifier.domain.TaskRequest;
import se.zipper.hwcertifier.exception.BadParametersException;
import se.zipper.hwcertifier.repository.ComputerRepository;
import se.zipper.hwcertifier.repository.DriverPackageRepository;
import se.zipper.hwcertifier.service.DriverDatabaseService;
import se.zipper.hwcertifier.views.HwCertifierView;
import se.zipper.hwcertifier.views.TextFileView;

import java.util.List;

@Controller
public class MatchController implements ActionController{

    @Autowired
    TaskRequest taskRequest;

    @Autowired
    HwCertifierView view;


    @Autowired
    ComputerRepository computerRepository;

    @Autowired
    DriverPackageRepository driverPackageRepository;

    @Autowired
    DriverDatabaseService driverDatabaseService;

    private Logger log = Logger.getLogger(this.getClass().getName());

    @Override
    public void process() {
        if (taskRequest.getTargetOs() == null || taskRequest.getTargetBit() == null) {
            throw new BadParametersException("-targetOS or -TargetBit is incorrect");
        }
        driverDatabaseService.scanFolder("drivers");
        driverDatabaseService.scanStandardFolder();
//        List<DriverPackage> list = driverDatabaseService.matchHardwareString(DriverDatabaseService.DRIVER_DATABASE,"HDAUDIO\\FUNC_01&VEN_8384&DEV_7626&SUBSYS_103C30D4" );
//        driverDatabaseService.registerDriverPackage(driverPackageRepository.getDriverPackageByPath("oem.inf"));
        Computer computer = computerRepository.getComputerFromRegistryDump("1.reg");
        driverDatabaseService.matchComputer(computer);
        view.renderComputer(computer, "result");
        throw new UnsupportedOperationException();
    }
}
