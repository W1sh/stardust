package com.w1sh.aperture.core.condition;

import com.w1sh.aperture.core.Environment;
import com.w1sh.aperture.core.DefaultProviderRegistry;

public record ConditionContext(DefaultProviderRegistry registry, Environment environment) {

}
