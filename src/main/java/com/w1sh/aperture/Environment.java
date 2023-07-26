package com.w1sh.aperture;

import java.util.HashSet;
import java.util.Set;

public record Environment(ProviderContainer container, Set<String> activeProfiles) {

    public static Environment empty() {
        return new Builder().build();
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {

        private ProviderContainer container;
        private Set<String> profiles;

        public Builder container(ProviderContainer container) {
            this.container = container;
            return this;
        }

        public Builder profiles(String... profiles) {
            this.profiles = Set.of(profiles);
            return this;
        }

        public Environment build() {
            if (profiles == null) this.profiles = new HashSet<>();
            return new Environment(container, profiles);
        }
    }
}
