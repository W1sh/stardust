package com.w1sh.aperture.yaml;

import java.io.InputStream;

public interface YamlPropertiesRegistry {

    void register(InputStream inputStream);

    String getProperty(String key);
}
