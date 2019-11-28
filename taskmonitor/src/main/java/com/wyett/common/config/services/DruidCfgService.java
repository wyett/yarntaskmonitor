package com.wyett.common.config.services;

import com.wyett.common.config.ReadConf;
import com.wyett.common.config.services.CfgService;

/**
 * @author : wyettLei
 * @date : Created in 2019/9/30 15:59
 * @description: TODO
 */

public interface DruidCfgService extends CfgService {
    @ReadConf(value = "host")
    String getHost();

    @ReadConf(value = "port")
    int getPort();

    @ReadConf(value = "protocol")
    String getProtocol();

    @ReadConf(value = "collectionList")
    String[] getCollectionList();

    @ReadConf(value = "maxTimeLag")
    long getMaxTimeLag();
}
