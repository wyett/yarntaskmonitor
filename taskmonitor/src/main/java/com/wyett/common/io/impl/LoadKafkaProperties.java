package com.wyett.common.io.impl;

import com.wyett.common.config.CfgFactory;
import com.wyett.common.config.services.KafkaCfgService;
import com.wyett.common.io.PropertiesLoader;
import com.wyett.common.io.Resources;

import java.io.InputStream;

/**
 * @author : wyettLei
 * @date : Created in 2019/11/19 15:20
 * @description: TODO
 */

public class LoadKafkaProperties implements PropertiesLoader {
    @Override
    public KafkaCfgService load() {
        InputStream in = Resources.getResourceAsStream("kafka.properties");
        KafkaCfgService kafkaCfgService = (KafkaCfgService) CfgFactory.readProperties(in);

        return kafkaCfgService;
    }
}
