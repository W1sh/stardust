package com.w1sh.aperture.yaml;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.yaml.snakeyaml.Yaml;

import java.io.InputStream;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class YamlPropertiesRegistryImpl implements YamlPropertiesRegistry {

    private static final Logger logger = LoggerFactory.getLogger(YamlPropertiesRegistryImpl.class);
    private final Map<String, Object> properties = new ConcurrentHashMap<>(256);
    private final Yaml yaml;

    public YamlPropertiesRegistryImpl() {this.yaml = new Yaml();}

    @Override
    public void register(InputStream inputStream) {
        Map<String, Object> data = yaml.load(inputStream);
        logger.debug("Found {} yaml properties to include in the registry", data.size());
        properties.putAll(data);
    }

    @Override
    public String getProperty(String key) {
        if (key.isBlank()) return null;
        return getProperty(key, properties);
    }

    @SuppressWarnings("unchecked")
    private String getProperty(String key, Map<String, Object> treeMap) {
        if (key.contains(".")) {
            String[] keyYamlPath = key.split("\\.");
            if (treeMap.containsKey(keyYamlPath[0])) {
                return getProperty(key.substring(key.indexOf(".")+1), (Map<String, Object>) properties.get(keyYamlPath[0]));
            } else {
                return null;
            }
        } else {
            return (String) treeMap.get(key);
        }
    }
}
