package se.zipper.hwcertifier.parsers;

import org.apache.log4j.Logger;

import java.io.*;
import java.nio.charset.Charset;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CustomIniParser {
    private Map<String, Map<String, List<String>>> properties = new HashMap<String, Map<String, List<String>>>();

    private final Pattern valuePattern = Pattern.compile("(\\S*)\\s*=\\s*([\\S ,]*)\\s*;*.*");
    private final Pattern sectionPattern = Pattern.compile("^[\\s]*\\[(\\S*)\\]");
    private String fileName;
    private Logger log = Logger.getLogger(this.getClass().getName());

    public CustomIniParser(String fname, Charset charset) throws IOException {
        fileName = new File(fname).getName();
        loadFile(fname, charset);
    }

    public String getFileName() {
        return fileName;
    }

    private void loadFile(String fname, Charset charset) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(fname), charset));
        try {
            String section = "";
            String line;
            while ((line = br.readLine()) != null) {
                int commentIndex = line.indexOf(";");
                if ( commentIndex >= 0){
                    line = line.substring(0, commentIndex);
                }
                Matcher m;
                if ((m = sectionPattern.matcher(line)) != null && m.find()) {
                    section = m.group(1);
                } else if ((!"".equalsIgnoreCase(section)) && ((m = valuePattern.matcher(line)) != null) && m.find()) {
                    addProperty(section, m.group(1), m.group(2));
                }
            }
        } finally {
            br.close();
        }
    }

    private void addProperty(String section, String name, String value) {
//        System.out.println(section + "." + name + "=" + value);
        Map<String, List<String>> sectionMap = properties.get(section);
        if (sectionMap == null) {
            sectionMap = new HashMap<String, List<String>>();
            properties.put(section, sectionMap);
        }
        List<String> valuesList = sectionMap.get(name);
        if (valuesList == null) {
            valuesList = new ArrayList<String>();
            sectionMap.put(name, valuesList);
        }
        valuesList.add(value);
    }

    public List<String> getProperties(String section, String var) {
        Map<String, List<String>> sectionMap = properties.get(section);
        if (sectionMap == null) {
            return null;
        }
        return sectionMap.get(var);
    }

    public String getProperty(String section, String var, String def) {
        List<String> values = getProperties(section, var);
        if (values == null) {
            return def;
        }
        String value = values.get(0);
        if (value == null) {
            return def;
        }
        return value;
    }

    public String getProperty(String section, String var) {

        return getProperty(section, var, null);
    }

    public Set<String> enumValues(String section) {
        Map<String, List<String>> sectionMap = properties.get(section);
        if (sectionMap == null) {
            return new HashSet<String>();
        }
        return sectionMap.keySet();
    }
}
