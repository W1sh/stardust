package com.w1sh.aperture.condition;

import com.w1sh.aperture.ProviderContainerImpl;
import com.w1sh.aperture.Environment;

public record ConditionContext(ProviderContainerImpl registry, Environment environment) {

}
