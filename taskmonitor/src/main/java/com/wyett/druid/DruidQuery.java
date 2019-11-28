package com.wyett.druid;

import com.wyett.common.io.impl.LoadDruidProperties;
import com.wyett.common.config.services.DruidCfgService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.*;

/**
 * @author : wyettLei
 * @date : Created in 2019/11/22 19:23
 * @description: TODO
 */

public class DruidQuery {
    private static final Logger LOGGER = LoggerFactory.getLogger(DruidQuery.class);
    private DruidUtil druidUtil;
    private DruidCfgService druidCfgService;

    public DruidQuery() {
        druidCfgService = new LoadDruidProperties().load();
        druidUtil = new DruidUtil(new UrlBuilder().build(druidCfgService));
    }

    /**
     * get max time of every collection, save it into map
     * @return
     * @throws SQLException
     */
    public List<String> getFailureTaskColl() {
        List<String> cList = Arrays.asList(druidCfgService.getCollectionList());
        List<String> failreTaskCollList = new ArrayList<>();

        for (String cs : cList) {
            ResultSet rs = druidUtil.execQuery("select max(__time) from " + cs);

            try {
                while (rs.next()) {
                    Timestamp collLastestTs = (Timestamp)rs.getTimestamp(1);
                    long tmpMills = minus(collLastestTs);
                    LOGGER.info(cs + ":" + tmpMills);
                    if (tmpMills > druidCfgService.getMaxTimeLag()) {
                        failreTaskCollList.add(cs);
                    }
                }
            } catch (SQLException e) {
                LOGGER.error(e.getMessage());
            }
        }
        return failreTaskCollList;
    }

    /**
     * minus timestamp
     * @param t
     * @return
     */
    public long minus(Timestamp t) {
        long nowTime = System.currentTimeMillis();
        return nowTime - t.getTime() - 8 * 60 * 60 * 1000;
    }
}
