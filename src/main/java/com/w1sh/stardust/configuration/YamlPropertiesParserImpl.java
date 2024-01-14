package com.w1sh.stardust.configuration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.yaml.snakeyaml.Yaml;

import java.io.InputStream;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

import static java.util.Collections.singletonMap;

public class YamlPropertiesParserImpl implements PropertiesParser {

    private static final Logger logger = LoggerFactory.getLogger(YamlPropertiesParserImpl.class);
    private final Yaml yaml;

    public YamlPropertiesParserImpl() {this.yaml = new Yaml();}

    @Override
    public Map<String, String> parse(InputStream inputStream) {
        Map<String, Object> data = yaml.load(inputStream);
        logger.debug("Found {} yaml properties to include in the registry", data.size());
        Map<String, String> result = new LinkedHashMap<>();
        flattenedMap(result, data, null);
        return result;
    }

    @SuppressWarnings("unchecked")
    private static void flattenedMap(Map<String, String> result, Map<String, Object> source, String path) {
        source.forEach((key, value) -> {
            if (path != null && !path.isBlank())
                key = path + (key.startsWith("[") ? key : '.' + key);
            if (value instanceof String v) {
                result.put(key, v);
            } else if (value instanceof Map) {
                flattenedMap(result, (Map<String, Object>) value, key);
            } else if (value instanceof Collection<?> v) {
                int count = 0;
                for (Object object : v)
                    flattenedMap(result, singletonMap("[" + (count++) + "]", object), key);
            } else {
                result.put(key, value != null ? "" + value : "");
            }
        });
    }
}
