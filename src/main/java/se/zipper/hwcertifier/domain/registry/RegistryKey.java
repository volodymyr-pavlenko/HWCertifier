package se.zipper.hwcertifier.domain.registry;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * User: dreamer
 * Date: 12.03.13
 * Time: 18:30
 * To change this template use File | Settings | File Templates.
 */
public class RegistryKey {
    String path = "";
    String name = "";
    Map<String, RegistryValue> values = new HashMap<String, RegistryValue>();
    Map<String, RegistryKey> keys = new HashMap<String, RegistryKey>();

    public RegistryKey() {
    }

    public RegistryKey(String name, String path) {
        this.name = name;
        this.path = path;
    }

    public RegistryKey addRegistryKey(String name) {
        String[] parts = name.split("\\\\", 2);
        RegistryKey curKey;
        if (!keys.containsKey(parts[0])) {
            curKey = new RegistryKey(parts[0], path + "\\" + parts[0]);
            keys.put(parts[0], curKey);
        } else {
            curKey = keys.get(parts[0]);
        }
        if (parts.length == 2) {
            curKey = curKey.addRegistryKey(parts[1]);
        }
        return curKey;
    }

    public RegistryValue parseRegistryValue(String rawData) {
        RegistryValue newRV = RegistryValues.valueOf(rawData);
        if (newRV != null) {
            values.put(newRV.getName(), newRV);
        }
        return newRV;
    }

    public RegistryKey getRegistryKey(String path) {
        String[] parts = path.split("\\\\", 2);
        RegistryKey curKey;
        if (keys.containsKey(parts[0])) {
            curKey = keys.get(parts[0]);
        } else {
            return null;
        }
        if (parts.length == 2) {
            curKey = curKey.getRegistryKey(parts[1]);
        }
        return curKey;
    }

    public Set<String> enumKeys() {
        return keys.keySet();
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Map<String, RegistryValue> getValues() {
        return values;
    }

    public Map<String, RegistryKey> getKeys() {
        return keys;
    }

    public RegistryKey findSubKeyWithValue(String name) {
        RegistryKey result = null;
        if (values.containsKey(name)) {
            result = this;
        }
        for (String el : keys.keySet()) {
            if (result!= null) {
                return result;
            }
            RegistryKey subkey = keys.get(el);
            if (subkey != null) {
                result = subkey.findSubKeyWithValue(name);
            }
        }
        return result;
    }

    public String getStringValue(String name) {
        RegistryValue value = values.get(name);
        if (value == null) {
            return null;
        }
        return value.getData();
    }

}
