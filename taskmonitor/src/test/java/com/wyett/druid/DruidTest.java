package com.wyett.druid;

import com.wyett.common.io.impl.LoadDruidProperties;

/**
 * @author : wyettLei
 * @date : Created in 2019/11/22 19:09
 * @description: TODO
 */

public class DruidTest {
    public static void main(String[] args) {

        DruidUtil druidUtil =
                new DruidUtil(new UrlBuilder().build(new LoadDruidProperties().load()));
        System.out.println(druidUtil.execQuery("select max(__time) from mysqlslowlog"));
    }
}
