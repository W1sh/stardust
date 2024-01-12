package com.w1sh.stardust.configuration;

import java.io.InputStream;

public interface PropertiesRegistry {

    void register(InputStream inputStream);

    String getProperty(String key);
}
