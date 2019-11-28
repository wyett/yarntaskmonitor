package com.wyett.druid;

import com.wyett.common.config.services.DruidCfgService;

public class UrlBuilder {
    private static final String URL_PRE = "jdbc:avatica:remote:url";

    public String build(DruidCfgService dcs) {
        return URL_PRE + "="
                + dcs.getProtocol()+ "://"
                + dcs.getHost() + ":"
                + dcs.getPort() + "/druid/v2/sql/avatica/";

    }

//    public String build(String host, String port, String protocal) {
//        return URL_PRE + "="
//                + protocal + "://"
//                + host + ":"
//                + port + "/druid/v2/sql/avatica/";
//    }
}
