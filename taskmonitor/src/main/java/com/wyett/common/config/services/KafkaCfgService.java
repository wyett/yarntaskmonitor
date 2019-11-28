package com.wyett.common.config.services;

import com.wyett.common.config.ReadConf;
import com.wyett.common.config.services.CfgService;

/**
 * @author : wyettLei
 * @date : Created in 2019/9/30 15:59
 * @description: TODO
 */

public interface KafkaCfgService extends CfgService {
    @ReadConf(value = "kafka.consumer.groups")
    String[] getKafkaConsumerGroup();

    @ReadConf(value = "kafka.topics")
    String[] getKafkaTopic();

    @ReadConf(value = "kafka.zk")
    String[] getKafkaZk();

    @ReadConf(value = "kafka.brokers")
    String[] getKafkaBrokers();

    @ReadConf(value = "kafka.zk.folder")
    String[] getKafkazkFolder();

    @ReadConf(value = "kafka.zk.limit.size")
    int getKafkaZkLimitSize();
}
