package com.wyett.common.config;

import com.wyett.common.config.services.DruidCfgService;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Proxy;
import java.util.Properties;

/**
 * @author : wyettLei
 * @date : Created in 2019/9/30 12:00
 * @description: TODO
 */

public class CfgFactory {

    public CfgFactory() {}

    public static DruidCfgService readProperties(final InputStream inputStream) {
        final Properties properties = new Properties();
        try {
            properties.load(inputStream);
        } catch (IOException e) {
            return null;
        }
        return (DruidCfgService) Proxy.newProxyInstance(
                DruidCfgService.class.getClassLoader(),
                new Class[] { DruidCfgService.class },
                new PropertyInvocationHandler(properties));
    }
}
