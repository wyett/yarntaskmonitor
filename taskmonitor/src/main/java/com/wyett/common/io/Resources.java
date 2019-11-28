package com.wyett.common.io;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

/**
 * @author : wyettLei
 * @date : Created in 2019/11/22 16:23
 * @description: TODO
 */

public class Resources {
    public static InputStream getResourceAsStream(String filePath) {
//        return Resources.class.getClassLoader().getResourceAsStream("conf/" + filePath);
//        return Thread.currentThread().getContextClassLoader().getResourceAsStream("conf/" + filePath);
        InputStream in = null;
        try {
            in = new FileInputStream("conf/" + filePath);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return in;
    }
}
