package se.zipper.hwcertifier.domain;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: dreamer
 * Date: 12.03.13
 * Time: 20:04
 * To change this template use File | Settings | File Templates.
 */
public class DriverMatch {

    static public enum MatchTypeSource {
        DRV(),STD();
    }
    static public enum MatchType {
        HWID("HW"),COID("CO");
        private String value;
        private MatchType(String str) {
            this.value = str;
        }

        public String getValue() {
            return value;
        }
    }

    private String hardwareId;
    private List<DriverPackage> driverPackage = new ArrayList<DriverPackage>();
    private MatchTypeSource matchTypeSource;
    private MatchType matchType;

    public MatchTypeSource getMatchTypeSource() {
        return matchTypeSource;
    }

    public void setMatchTypeSource(MatchTypeSource matchTypeSource) {
        this.matchTypeSource = matchTypeSource;
    }

    public MatchType getMatchType() {
        return matchType;
    }

    public void setMatchType(MatchType matchType) {
        this.matchType = matchType;
    }

    public String getHardwareId() {
        return hardwareId;
    }

    public void setHardwareId(String hardwareId) {
        this.hardwareId = hardwareId;
    }

    public List<DriverPackage> getDriverPackage() {
        return driverPackage;
    }

}
