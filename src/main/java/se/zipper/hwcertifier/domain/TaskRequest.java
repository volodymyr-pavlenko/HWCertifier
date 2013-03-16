package se.zipper.hwcertifier.domain;

import org.springframework.stereotype.Component;
import se.zipper.hwcertifier.exception.BadParametersException;

@Component("taskRequest")
public class TaskRequest {
    static public enum WinOs {
        WIN2K("5.0"),
        WINXP("5.1"),
        WINXP64("5.2"),
        WIN2K3("5.2"),
        WIN2K3R2("5.2"),
        WINVISTA("6.0"),
        WINSERVER2K8("6.0"),
        WINSERVER2K8R2("6.1"),
        WIN7("6.1"),
        WINSERVER2012("6.2"),
        WIN8("6.2");

        private String OsVersion;

        private WinOs(String osVersion) {
            this.OsVersion = osVersion;
        }

        public String getOsVersion() {
            return OsVersion;
        }
    }

    static public enum OsBit {
        X86("x86"), X64("amd64");
        private String OsBit;

        private OsBit(String osBit) {
            this.OsBit = osBit;
        }

        public String getOsBit() {
            return OsBit;
        }
    }

    private String vendor;
    private String model;
    private String task;
    private WinOs targetOs;
    private OsBit targetBit;

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

    public String getTask() {
        return task;
    }

    public void setTask(String task) {
        this.task = task;
    }

    public WinOs getTargetOs() {
        return targetOs;
    }

    public void setTargetOs(WinOs targetOs) {
        this.targetOs = targetOs;
    }

    public OsBit getTargetBit() {
        return targetBit;
    }

    public void setTargetBit(OsBit targetBit) {
        this.targetBit = targetBit;
    }

    public void processConfiguration(String [] args) {
        if (args.length<1) {
            throw new BadParametersException();
        }
        setTask(args[0].toLowerCase());
        String currentParameter = "";
        for (int i=1;i<args.length;i++) {
            if (args[i].startsWith("-")) {
                currentParameter = args[i];
            } else {
                switch(currentParameter) {
                    case "-vendor": setVendor(args[i]);break;
                    case "-model": setModel(args[i]);break;
                    case "-targetos": setTargetOs(WinOs.valueOf(args[i].toUpperCase()));break;
                    case "-targetbit": setTargetBit(OsBit.valueOf(args[i].toUpperCase()));break;
                }
            }
        }
    }

}
