package com.wyett.kafka.service;


import com.alibaba.fastjson.JSONObject;

/**
 * @author : wyettLei
 * @date : Created in 2019/11/25 18:22
 * @description: TODO
 */

public interface ZkService {

    /** Zookeeper get command. */
    public String get(String topicName, String cmd);

    /** Zookeeper ls command. */
    public String ls(String topicName, String cmd);

    /** Get zookeeper health status. */
    public String status(String host, String port);

    /** Get zookeeper cluster information. */
    public String zkCluster(String topicName);

    /** Judge whether the zkcli is active. */
    public JSONObject zkCliStatus(String topicName);
}
