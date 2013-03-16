package se.zipper.hwcertifier.domain;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DriverPackage {
    public static final String[] BITS = {"SupportFiles", "", "x86", "amd64"};

    //disks {bit=> {diskId=>subdir}
    Map<String, Map<String, String>> sourceDisksNames = new HashMap<String, Map<String, String>>();

    //files{bit=> {fileName=>subdir}
    Map<String, Map<String, String>> sourceDisksFiles = new HashMap<String, Map<String, String>>();

    List<String> copyErrors = new ArrayList<String>();

    List<String> hardwareStrings  = new ArrayList<String>();
    String pathToInf;
    String baseDir;
    Map<String, String> strings = new HashMap<String, String>();

    public DriverPackage() {
    }

    public List<String> getCopyErrors() {
        return copyErrors;
    }

    public Map<String, Map<String, String>> getSourceDisksFiles() {
        return sourceDisksFiles;
    }

    public Map<String, Map<String, String>> getSourceDisksNames() {
        return sourceDisksNames;
    }

    public Map<String, String> getStrings() {
        return strings;
    }

    public List<String> getHardwareStrings() {
        return hardwareStrings;
    }

    public String getPathToInf() {
        return pathToInf;
    }

    public void setPathToInf(String pathToInf) {
        this.pathToInf = pathToInf;
    }

    public String getBaseDir() {
        return baseDir;
    }

    public void setBaseDir(String baseDir) {
        this.baseDir = baseDir;
    }
}
