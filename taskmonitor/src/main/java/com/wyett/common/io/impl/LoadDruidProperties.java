package com.wyett.common.io.impl;

import com.wyett.common.config.CfgFactory;
import com.wyett.common.config.services.DruidCfgService;
import com.wyett.common.io.PropertiesLoader;
import com.wyett.common.io.Resources;

import java.io.InputStream;

/**
 * @author : wyettLei
 * @date : Created in 2019/11/19 15:20
 * @description: TODO
 */

public class LoadDruidProperties implements PropertiesLoader {
    @Override
    public DruidCfgService load() {
        InputStream in = Resources.getResourceAsStream("druid.properties");
        DruidCfgService druidCfgService = CfgFactory.readProperties(in);

        return druidCfgService;
    }
}
