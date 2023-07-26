package com.w1sh.aperture;

import java.util.Set;

public record Environment(ProviderContainer container, Set<String> activeProfiles) {

}
