package com.wyett.druid;

import com.wyett.common.config.CfgFactory;
import com.wyett.common.io.Resources;
import com.wyett.common.config.services.DruidCfgService;

import java.io.InputStream;

/**
 * @author : wyettLei
 * @date : Created in 2019/9/30 18:14
 * @description: TODO
 */

public class DruidCfgDemo {
    public static void main(String[] args) {
        InputStream in = Resources.getResourceAsStream("druid.properties");
        DruidCfgService druidCfgService = CfgFactory.readProperties(in);
        if (druidCfgService == null) {
            return;
        }
        String host = druidCfgService.getHost();
        Integer port = druidCfgService.getPort();
        String protocol = druidCfgService.getProtocol();
        String[] collectionList  = druidCfgService.getCollectionList();
        System.out.println(host + "\n" + port + "\n"
                        + protocol + "\n"
                        + collectionList + "\n");
    }
}
