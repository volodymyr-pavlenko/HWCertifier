package se.zipper.hwcertifier.parsers;

import se.zipper.hwcertifier.domain.registry.RegistryKey;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created with IntelliJ IDEA.
 * User: dreamer
 * Date: 12.03.13
 * Time: 18:28
 * To change this template use File | Settings | File Templates.
 */
public class RegistryParser {
    private RegistryKey readNextKey(BufferedReader br, RegistryKey root) throws IOException {
        String s;
        String keyName;
        Pattern p = Pattern.compile("\\[(.*)\\]");
        while (true) {
            while ((s = br.readLine()) != null && !s.startsWith("[")) {
            }
            if (s == null) {
                return null;
            }
            Matcher m = p.matcher(s);
            if (m.find()) {
                keyName = m.group(1);
                break;
            }
        }
        // System.out.println(keyName);
        RegistryKey Key = root.addRegistryKey(keyName);
        // START READING VALUES
        s = "";

        while (true) {
            StringBuilder strBuilder = new StringBuilder();
            while (((s = br.readLine()) != null) && !"".equals(s)) {
                s = s.trim();
                if (s.endsWith("\\")) {
                    strBuilder.append(s, 0, s.length() - 1);
                } else {
                    strBuilder.append(s);
                    break;
                }
            }
            if (s == null || "".equals(s)) {
                break;
            }
            Key.parseRegistryValue(strBuilder.toString());
        }

        return Key;
    }

    //    public RegistryElement readFile(String filename) throws FileNotFoundException {
//        FileReader fr;
//        RegistryElement key;
//        try {
//            br = new BufferedReader(new InputStreamReader(new FileInputStream(filename), "UTF-16LE"));
//            while ((key = readNextKey()) != null) {
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        return root;
//    }
    public RegistryKey loadFile(String fname, Charset charset) throws IOException {
        RegistryKey root = new RegistryKey();
        BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(fname), "UTF-16LE"));
        while ((readNextKey(br, root)) != null) {
        }
        return root;
    }
}
