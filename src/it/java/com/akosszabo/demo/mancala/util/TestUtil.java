package com.akosszabo.demo.mancala.util;

import org.apache.commons.io.IOUtils;

import java.io.FileInputStream;
import java.io.IOException;

public class TestUtil {

    public static String readJSONFile(final String name) throws IOException {
        FileInputStream fis = new FileInputStream("src/it/resources/"+name+".json");
        return  IOUtils.toString(fis, "UTF-8");
    }
}
