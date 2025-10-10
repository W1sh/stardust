package com.w1sh.stardust;

import java.util.List;
import java.util.Set;

public sealed interface ResolvableExecutable<S> extends Resolvable<S> permits ResolvableConstructorImpl, ResolvableMethodImpl {

    Integer getPriority();

    Set<String> getActiveProfiles();

    Boolean isPrimary();

    Scope getScope();

    String getName();

    List<ResolvableParameter<?>> getParameters();

    Object resolve(Object[] args);

}
