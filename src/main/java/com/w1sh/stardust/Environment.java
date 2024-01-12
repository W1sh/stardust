package com.w1sh.stardust;

import java.util.Set;

public record Environment(ProviderContainer container, Set<String> activeProfiles) {

}
