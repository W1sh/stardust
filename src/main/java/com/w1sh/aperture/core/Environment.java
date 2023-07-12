package com.w1sh.aperture.core;

import java.util.HashSet;
import java.util.Set;

public record Environment(Set<String> activeProfiles) {

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {

        private Set<String> profiles;

        public Builder profiles(String... profiles) {
            this.profiles = Set.of(profiles);
            return this;
        }

        public Environment build() {
            if (profiles == null) this.profiles = new HashSet<>();
            return new Environment(profiles);
        }
    }
}
