package com.w1sh.aperture.core.condition;

import com.w1sh.aperture.core.builder.Options;
import com.w1sh.aperture.core.util.Types;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public class ProviderConditionFactory {

    private static final Logger logger = LoggerFactory.getLogger(ProviderConditionFactory.class);

    private final Set<MetadataConditionFactory<?>> metadataConditionFactories = HashSet.newHashSet(64);

    public List<? extends Condition> create(Options options) {
        return metadataConditionFactories.stream()
                .map(factory -> factory.create(options))
                .filter(Objects::nonNull)
                .toList();
    }

    public void register(MetadataConditionFactory<?> factory) {
        logger.debug("Registering factory for condition {}", Types.getActualTypeArgument(factory.getClass(), 0));
        metadataConditionFactories.add(factory);
    }
}
