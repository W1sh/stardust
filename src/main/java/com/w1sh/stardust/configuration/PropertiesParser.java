package com.w1sh.stardust.configuration;

import java.io.InputStream;
import java.util.Map;

public interface PropertiesParser {

    Map<String, String> parse(InputStream inputStream);
}
