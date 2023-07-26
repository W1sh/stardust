package com.w1sh.aperture;

import java.util.Set;

public interface ResolvableExecutable<S> extends Resolvable<S> {

    Integer getPriority();

    Set<String> getActiveProfiles();

    Boolean isPrimary();

    Scope getScope();

    String getName();

    Set<ResolvableParameter<?>> getParameters();

    Object resolve(Object[] args);

}
