package com.w1sh.aperture.core.condition;

import com.w1sh.aperture.core.Environment;
import com.w1sh.aperture.core.DefaultProviderRegistry;
import com.w1sh.aperture.core.Metadata;
import com.w1sh.aperture.core.util.Types;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public class DefaultConditionProcessor implements ConditionEvaluator, ConditionRegistry {

    private static final Logger logger = LoggerFactory.getLogger(DefaultConditionProcessor.class);

    private final Set<MetadataConditionFactory<?>> metadataConditionFactories = HashSet.newHashSet(64);
    private final ConditionContext context;

    public DefaultConditionProcessor(DefaultProviderRegistry registry, Environment environment) {
        this.context = new ConditionContext(registry, environment);
    }

    public boolean shouldSkip(List<? extends Condition> conditions) {
        for (Condition condition : conditions) {
            if (!condition.matches(context)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean allMatch(List<? extends Condition> conditions, Condition.EvaluationPhase phase) {
        return conditions != null && conditions.stream().anyMatch(condition -> phase.equals(condition.getEvaluationPhase()));
    }

    @Override
    public List<? extends Condition> create(Metadata metadata) {
        final List<Condition> list = new ArrayList<>();
        for (MetadataConditionFactory<?> factory : metadataConditionFactories) {
            Condition condition = factory.create(metadata);
            if (condition != null) {
                list.add(condition);
            }
        }
        return list;
    }

    @Override
    public void addFactory(MetadataConditionFactory<?> factory) {
        logger.debug("Registering factory for condition {}", Types.getInterfaceActualTypeArgument(factory.getClass(), 0));
        metadataConditionFactories.add(factory);
    }

    @Override
    public void removeFactory(MetadataConditionFactory<?> factory) {
        logger.debug("Removing registered factory for condition {}", Types.getInterfaceActualTypeArgument(factory.getClass(), 0));
        metadataConditionFactories.remove(factory);
    }

    @Override
    public void removaAllFactories() {
        logger.debug("Removing all registered factories");
        metadataConditionFactories.clear();
    }
}
