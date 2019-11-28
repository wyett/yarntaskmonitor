package com.wyett.druid;

import java.sql.*;

/**
 * @author : wyettLei
 * @date : Created in 2019/11/19 10:49
 * @description: TODO
 */

public class DruidUtil {

    private String url;

    public DruidUtil(String url) {
        this.url = url;
    }

    public ResultSet execQuery(String sql) {
        Connection conn = null;
        Statement st = null;
        ResultSet rs = null;

        try {
            conn = DriverManager.getConnection(url);
            st = conn.createStatement();
            rs = st.executeQuery(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return rs;
    }
}
