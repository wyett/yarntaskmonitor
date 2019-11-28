package com.wyett;

import com.wyett.druid.DruidQuery;
import org.apache.log4j.PropertyConfigurator;
import java.util.List;

/**
 * @author : wyettLei
 * @date : Created in 2019/11/21 16:02
 * @description: TODO
 */

public class MainMonitor {
    public static void main(String[] args) {
        PropertyConfigurator.configure("conf/log4j.properties");

        DruidQuery query = new DruidQuery();
        List<String> ls = query.getFailureTaskColl();

        System.out.println("MainMonitor result:" + ls);
    }
}
