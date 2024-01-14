package com.w1sh.stardust.configuration;

import java.util.Map;

public interface PropertiesRegistry {

    void set(Map<String, String> properties);

    String getProperty(String key);

    String getProperty(String key, String defaultValue);
}
