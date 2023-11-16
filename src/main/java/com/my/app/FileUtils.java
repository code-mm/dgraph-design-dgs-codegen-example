
/**
 * Copyright © 2022 WXFGGZS. All Rights Reserved.
 */

package com.my.app;

import java.io.*;

public class FileUtils {

    public static String getExtensionName(String name) {
        return (name.substring(name.lastIndexOf(".")));
    }

    public static void copyFile(InputStream src, OutputStream tag) {
        try {
            byte[] buf = new byte[1024 * 1024 * 8];
            int len = 0;
            while ((len = src.read(buf)) != -1) {
                tag.write(buf, 0, len);
                tag.flush();
            }
            src.close();
            tag.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static final String encoding = java.nio.charset.StandardCharsets.UTF_8.toString();

    public static String readToString(String fileName) {

        File file = new File(fileName);
        Long filelength = file.length();
        byte[] filecontent = new byte[filelength.intValue()];
        try {
            FileInputStream in = new FileInputStream(file);
            in.read(filecontent);
            in.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            return new String(filecontent, encoding);
        } catch (UnsupportedEncodingException e) {
            System.err.println("The OS does not support " + encoding);
            e.printStackTrace();
            return null;
        }
    }
}
