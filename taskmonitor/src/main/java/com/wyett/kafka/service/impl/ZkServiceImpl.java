package com.wyett.kafka.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.wyett.kafka.service.ZkService;
import com.wyett.kafka.util.KafkaZkClientUtil;
import kafka.zk.KafkaZkClient;
import org.apache.zookeeper.data.Stat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import scala.Option;
import scala.Tuple2;
import scala.collection.JavaConverters;
import scala.collection.Seq;

/**
 * @author : wyettLei
 * @date : Created in 2019/11/25 18:23
 * @description: TODO
 */

public class ZkServiceImpl implements ZkService {
    private static final Logger LOGGER = LoggerFactory.getLogger(ZkServiceImpl.class);

    /**
     * get instance
     */
    KafkaZkClientUtil kafkaZkClientUtil = KafkaZkClientUtil.getInstance();

    /**
     *
     * @param topicName
     * @param cmd
     * @return
     */
    @Override
    public String get(String topicName, String cmd) {
        String ret = "";
        KafkaZkClient zkc = kafkaZkClientUtil.getZkClient(topicName);
        if (zkc.pathExists(cmd)) {
            Tuple2<Option<byte[]>, Stat> tuple2 = zkc.getDataAndStat(cmd);
            ret += new String(tuple2._1.get()) + "\n";
            ret += "cZxid = " + tuple2._2.getCzxid() + "\n";
            ret += "ctime = " + tuple2._2.getCtime() + "\n";
            ret += "mZxid = " + tuple2._2.getMzxid() + "\n";
            ret += "mtime = " + tuple2._2.getMtime() + "\n";
            ret += "pZxid = " + tuple2._2.getPzxid() + "\n";
            ret += "cversion = " + tuple2._2.getCversion() + "\n";
            ret += "dataVersion = " + tuple2._2.getVersion() + "\n";
            ret += "aclVersion = " + tuple2._2.getAversion() + "\n";
            ret += "ephemeralOwner = " + tuple2._2.getEphemeralOwner() + "\n";
            ret += "dataLength = " + tuple2._2.getDataLength() + "\n";
            ret += "numChildren = " + tuple2._2.getNumChildren() + "\n";
        }
        if (zkc != null) {
            kafkaZkClientUtil.release(topicName, zkc);
            zkc = null;
        }
        return ret;
    }

    @Override
    public String ls(String topicName, String cmd) {
        String target = "";
        KafkaZkClient zkc = kafkaZkClientUtil.getZkClient(topicName);
        boolean status = zkc.pathExists(cmd);
        if (status) {
            Seq<String> seq = zkc.getChildren(cmd);
//            target = JavaConversions.seqAsJavaList(seq).toString();
            target = JavaConverters.seqAsJavaList(seq).toString();
        }
        if (zkc != null) {
            kafkaZkClientUtil.release(topicName, zkc);
            zkc = null;
        }
        return target;
    }

    @Override
    public String status(String host, String port) {
        return null;
    }

    @Override
    public String zkCluster(String clusterAlias) {
        return null;
    }

    @Override
    public JSONObject zkCliStatus(String clusterAlias) {
        return null;
    }
}
