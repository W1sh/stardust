package com.w1sh.aperture.core.condition;

import com.w1sh.aperture.core.Environment;
import com.w1sh.aperture.core.ProviderRegistry;

public record ConditionContext(ProviderRegistry registry, Environment environment) {

}
