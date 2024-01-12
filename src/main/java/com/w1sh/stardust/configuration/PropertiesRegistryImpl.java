package com.w1sh.stardust.configuration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertiesRegistryImpl implements PropertiesRegistry {

    private static final Logger logger = LoggerFactory.getLogger(PropertiesRegistryImpl.class);
    private final Properties properties;

    public PropertiesRegistryImpl() {this.properties = new Properties();}

    @Override
    public void register(InputStream inputStream) {
        try {
            properties.load(inputStream);
        } catch (IOException e) {
            logger.error("Failed to load and register properties file", e);
        }
    }

    @Override
    public String getProperty(String key) {
        return properties.getProperty(key);
    }
}
