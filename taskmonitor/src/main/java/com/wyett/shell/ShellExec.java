package com.wyett.shell;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * @author : wyettLei
 * @date : Created in 2019/11/21 15:25
 * @description: TODO
 */

public class ShellExec {
    private static final Logger LOGGER = LoggerFactory.getLogger(ShellExec.class);

    public static void exec(String cmd) throws IOException, InterruptedException {
        if (cmd == null) {
            LOGGER.error(cmd);
        }

        Runtime runtime = Runtime.getRuntime();

        Process pro = runtime.exec(cmd);
        int rs = pro.waitFor();
        if (rs != 0) {
            LOGGER.info("execute command " + cmd + " failed");
        }
    }
}
