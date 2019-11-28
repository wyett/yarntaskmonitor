package com.wyett.kafka.util;

import com.wyett.common.config.services.KafkaCfgService;
import com.wyett.common.io.impl.LoadKafkaProperties;
import org.apache.kafka.common.security.JaasUtils;
import org.apache.kafka.common.utils.Time;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * @author : wyettLei
 * @date : Created in 2019/11/25 15:04
 * @description: TODO
 */

public class KafkaZkClientUtil {

    private final static Logger LOG = LoggerFactory.getLogger(KafkaZkClientUtil.class);
    private static KafkaZkClientUtil instance = null;
    // Zookeeper client connection pool.
    private static Map<String, Vector<kafka.zk.KafkaZkClient>> zkCliPools = new HashMap<>();

    // read properties from kafka.properties
    private static KafkaCfgService kafkaCfgService = new LoadKafkaProperties().load();
    // Set pool max size
    private final static int zkCliPoolSize = kafkaCfgService.getKafkaZkLimitSize();
    // get consumer groups
    private static String[] kafkaConsumerGroups = kafkaCfgService.getKafkaConsumerGroup();
    private static String[] kafkaTopics = kafkaCfgService.getKafkaTopic();
    private static String[] kafkaZkArray = kafkaCfgService.getKafkaZk();

    private static Map<String, String> topicAndGroup = new HashMap<>();

    // get zk connection timeout
    public static final int ZK_CONNECTION_TIMEOUT_MS = 30000;
    public static final int ZK_SESSION_TIMEOUT_MS = 30000;

    // Init ZkClient pool numbers.
    static {
        // get topic and group
        for (int i = 0; i < kafkaCfgService.getKafkaConsumerGroup().length; i++) {
            if (kafkaTopics[i] != null) {
                topicAndGroup.put(kafkaTopics[i], kafkaConsumerGroups[i]);
            }
        }

        // init connect pool
        for(String zkCli: kafkaZkArray) {
            for (Map.Entry<String, String> tagEntry : topicAndGroup.entrySet()) {
                Vector<kafka.zk.KafkaZkClient> zkCliPool = new Vector<kafka.zk.KafkaZkClient>(zkCliPoolSize);
                kafka.zk.KafkaZkClient zkc = null;
                for (int i = 0; i < zkCliPoolSize; i++) {
                    try {
                        zkc = kafka.zk.KafkaZkClient.apply(zkCli, JaasUtils.isZkSecurityEnabled(), ZK_SESSION_TIMEOUT_MS,
                                ZK_CONNECTION_TIMEOUT_MS, Integer.MAX_VALUE, Time.SYSTEM, tagEntry.getValue(),
                                "SessionExpireListener");
                        zkCliPool.add(zkc);
                    } catch (Exception ex) {
                        LOG.error(ex.getMessage());
                    }
                }
                zkCliPools.put(tagEntry.getKey(), zkCliPool);
            }
        }
    }

    /**
     * single model
     * @return
     */
    public synchronized static KafkaZkClientUtil getInstance() {
        if (instance == null) {
            instance = new KafkaZkClientUtil();
        }
        return instance;
    }

    /**
     * get zkClient object
     * @param topicName
     * @return
     */
    public synchronized kafka.zk.KafkaZkClient getZkClient(String topicName) {
        Vector<kafka.zk.KafkaZkClient> zkCliPool = new Vector<kafka.zk.KafkaZkClient>(zkCliPoolSize);
        kafka.zk.KafkaZkClient zkc = null;
        try {
            if (zkCliPool.size() > 0) {
                zkc = zkCliPool.get(0);
                zkCliPool.remove(0);
                String osName = System.getProperties().getProperty("os.name");
                if (osName.contains("Linux")) {
                    LOG.debug("Get pool,and available size [" + zkCliPool.size() + "]");
                } else {
                    LOG.info("Get pool,and available size [" + zkCliPool.size() + "]");
                }
            } else {
                for (int i = 0; i < zkCliPoolSize; i++) {
                    try {
                        zkc = kafka.zk.KafkaZkClient.apply(kafkaZkArray[0], JaasUtils.isZkSecurityEnabled(),
                                ZK_SESSION_TIMEOUT_MS,
                            ZK_CONNECTION_TIMEOUT_MS, Integer.MAX_VALUE, Time.SYSTEM, topicAndGroup.get(topicName),
                            "SessionExpireListener");
                        zkCliPool.add(zkc);
                    } catch (Exception ex) {
                        LOG.error(ex.getMessage());
                    }
                }

                zkc = zkCliPool.get(0);
                zkCliPool.remove(0);
                String osName = System.getProperties().getProperty("os.name");
                if (osName.contains("Linux")) {
                    LOG.debug("Get pool,and available size [" + zkCliPool.size() + "]");
                } else {
                    LOG.warn("Get pool,and available size [" + zkCliPool.size() + "]");
                }
            }
        } catch (Exception e) {
            LOG.error("ZK init has error,msg is " + e.getMessage());
        }
        return zkc;
    }

    /**
     * Release zkClient object
     * @param topicName
     * @param zkc
     */
    public synchronized void release(String topicName, kafka.zk.KafkaZkClient zkc) {
        Vector<kafka.zk.KafkaZkClient> zkCliPool = zkCliPools.get(topicName);
        if (zkCliPool != null && zkCliPool.size() < zkCliPoolSize) {
            zkCliPool.add(zkc);
        }
        String osName = System.getProperties().getProperty("os.name");
        if (osName.contains("Linux")) {
            LOG.debug("Release pool,and available size [" + (zkCliPool == null ? 0 : zkCliPool.size()) + "]");
        } else {
            LOG.info("Release pool,and available size [" + (zkCliPool == null ? 0 : zkCliPool.size()) + "]");
        }
    }

    /** Construction method. */
    private KafkaZkClientUtil() {
    }

}
