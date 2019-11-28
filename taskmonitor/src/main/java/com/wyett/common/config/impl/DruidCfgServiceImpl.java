package com.wyett.common.config.impl;

import com.wyett.common.config.CfgFactory;
import com.wyett.common.io.Resources;
import com.wyett.common.config.services.DruidCfgService;

import java.io.InputStream;

/**
 * @author : wyettLei
 * @date : Created in 2019/11/22 19:56
 * @description: TODO
 */

public class DruidCfgServiceImpl implements DruidCfgService {
    private static InputStream in = Resources.getResourceAsStream("conf/druid.properties");
    private static DruidCfgService dcs;
    public DruidCfgServiceImpl() {
        dcs = CfgFactory.readProperties(in);
    }

    @Override
    public String getHost() {
        return dcs.getHost();
    }

    @Override
    public int getPort() {
        return dcs.getPort();
    }

    @Override
    public String getProtocol() {
        return dcs.getProtocol();
    }

    @Override
    public String[] getCollectionList() {
        return dcs.getCollectionList();
    }

    @Override
    public long getMaxTimeLag() {
        return dcs.getMaxTimeLag();
    }
}
