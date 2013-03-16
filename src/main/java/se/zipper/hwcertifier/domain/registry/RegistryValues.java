package se.zipper.hwcertifier.domain.registry;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created with IntelliJ IDEA.
 * User: dreamer
 * Date: 12.03.13
 * Time: 18:53
 * To change this template use File | Settings | File Templates.
 */
public class RegistryValues {
    private static Pattern valuePattern = Pattern.compile("\"(.*)\"=((.*):){0,1}(.*)");

    enum Type {
        REG_NONE("hex(0)"),
        REG_SZ(""),
        REG_EXPAND_SZ("hex(2)"),
        REG_BINARY("hex"),
        REG_DWORD("dword"),
        REG_DWORD_LO_ENDIAN("hex(4)"),
        REG_DWORD_BIG_ENDIAN("hex(5)"),
        REG_LINK("hex(6)"),
        REG_MULTI_SZ("hex(7)"),
        REG_RESOURCE_LIST("hex(8)"),
        REG_FULL_RESOURCE_DESCRIPTOR("hex(9)"),
        REG_RESOURCE_REQUIREMENTS_LIST("hex(a)"),
        REG_QWORD("hex(b)");
        private static final Map<String, Type> MAPPING;

        static {
            MAPPING = new HashMap<String, Type>();
            for (Type t : values()) {
                MAPPING.put(t.toString(), t);
            }
        }

        private final String _prefix;

        private Type(String prefix) {
            _prefix = prefix;
        }
    }

    static RegistryValue valueOf(String raw) {
        Matcher valueMatcher;
        String name = "";
        String data = "";
        String type = "";
        if ((valueMatcher = valuePattern.matcher(raw)) != null && valueMatcher.find()) {
            String valueType = valueMatcher.group(3);
            if (valueType == null) {
                valueType = "";
            }
            name = valueMatcher.group(1);
            switch (valueType) {
                case "hex(7)":
                    data = parseHex7(valueMatcher.group(4));
                    type = "hex(7)";
                    break;
                case "":
                    type = "";
                    data =String.valueOf(valueMatcher.group(4));
            }
            RegistryValue result = new RegistryValue();
            result.setName(name);
            result.setType(type);
            result.setData(data);
            return result;
        }

        return null;
    }

    private static String parseHex7(String raw) {
        String[] parts = raw.split(",");
        byte[] bytes = new byte[parts.length];
        for (int i = 0; i < parts.length; i++) {
            bytes[i] = (byte) Integer.parseInt(parts[i], 16);
        }
        String result = "";
        try {
            result = new String(bytes, "UTF-16LE");

        } catch (UnsupportedEncodingException e) {
        }
        return result;
    }


}
