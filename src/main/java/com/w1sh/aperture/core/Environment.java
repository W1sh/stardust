package com.w1sh.aperture.core;

import java.util.HashSet;
import java.util.Set;

public record Environment(Set<String> activeProfiles, Status status) {

    public static Environment empty() {
        return new Builder().build();
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {

        private Set<String> profiles;
        private Status status = Status.PREPARATION;

        public Builder profiles(String... profiles) {
            this.profiles = Set.of(profiles);
            return this;
        }

        public Builder status(Status status) {
            this.status = status;
            return this;
        }

        public Environment build() {
            if (profiles == null) this.profiles = new HashSet<>();
            return new Environment(profiles, status);
        }
    }

    private enum Status {
        PREPARATION, ONGOING, COMPLETE
    }
}
