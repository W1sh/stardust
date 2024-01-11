package com.w1sh.aperture.configuration;

import java.io.InputStream;

public interface PropertiesRegistry {

    void register(InputStream inputStream);

    String getProperty(String key);
}
