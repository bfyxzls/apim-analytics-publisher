package org.wso2.am.analytics.publisher.util;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.Set;

/**
 * 配置文件辅助对象.
 */
public class ConfigFactory {
    private static final Logger logger = LogManager.getLogger(ConfigFactory.class);
    private static final String KAFKA_HOST = "KAFKA_HOST";
    private static ConfigFactory instance = null;
    private Properties properties = new Properties();

    private ConfigFactory() {
        try {
            //读取resources/application.properties文件
            InputStream fis = ConfigFactory.class.getClassLoader().getResourceAsStream("application.properties");
            properties.load(fis);

            if (System.getenv().containsKey(KAFKA_HOST)) {
                properties.setProperty("kafka.host", System.getenv().get(KAFKA_HOST));
            }
            this.printInfo();
        } catch (IOException e) {
            logger.info("Manage properties field: " + e.getMessage());
        }
    }

    public static ConfigFactory getInstance() {
        if (instance == null) {
            instance = new ConfigFactory();
        }
        return instance;
    }

    public static void main(String... argv) {
        System.out.printf(ConfigFactory.getInstance().getStrPropertyValue("afka.acks"));
    }

    private void printInfo() {
        Set<Object> propKeySet = properties.keySet();
        for (Object propertyKey : propKeySet) {
            String propValue = properties.getProperty(propertyKey.toString());
            if (propValue != null) {
                System.out.println("--------------------====" + propertyKey.toString() + ":" + propValue);
            }
        }
    }

    public String getStrPropertyValue(String name) {
        String var = System.getenv(name);
        if (var != null) {
            logger.info("System variable, key: " + name + ", value: " + var);
            return var;
        }
        var = properties.getProperty(name);
        logger.info("User variable, key: " + name + ", value: " + var);
        return var;
    }

    public int getIntPropertyValue(String key, int defaultValue) {
        String data = getStrPropertyValue(key);
        try {
            int valor = Integer.parseInt(data);
            return valor;
        } catch (Exception e) {
            logger.info("Get Int Property Value field: " + e.getMessage());
            return defaultValue;
        }
    }
}
