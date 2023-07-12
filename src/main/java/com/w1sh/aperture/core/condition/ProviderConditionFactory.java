package com.w1sh.aperture.core.condition;

import com.w1sh.aperture.core.builder.Options;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ProviderConditionFactory {

    public List<Condition> create(Options options) {
        final List<Condition> conditions = new ArrayList<>();
        if (options.requiredClasses() != null && options.requiredClasses().length > 0) {
            conditions.add(new RequiresClassCondition(List.of(options.requiredClasses())));
        } else if (options.requiredMissingClasses() != null && options.requiredMissingClasses().length > 0) {
            conditions.add(new RequiresMissingClassCondition(Arrays.asList(options.requiredMissingClasses())));
        } else if (options.profiles() != null && options.profiles().length > 0) {
            conditions.add(new ActiveProfileCondition(Arrays.asList(options.profiles())));
        }
        return conditions;
    }
}
