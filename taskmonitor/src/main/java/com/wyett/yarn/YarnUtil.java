package com.wyett.yarn;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.yarn.client.api.YarnClient;

/**
 * @author : wyettLei
 * @date : Created in 2019/11/23 15:59
 * @description: TODO
 */

public class YarnUtil {
    public Configuration YarnUtil() {
        YarnClient yarnClient = YarnClient.createYarnClient();
        return null;
    }
    public static String getApplicationId() {
        return null;
    }
}
