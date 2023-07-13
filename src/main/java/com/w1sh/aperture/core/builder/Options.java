package com.w1sh.aperture.core.builder;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public record Options(String name, Integer order, Class<?>[] requiredClasses, Class<?>[] requiredMissingClasses, String[] profiles,
                      Map<String, String> requiredSystemProperties, boolean timed) {

    public static Builder builder() {
        return new Builder();
    }

    public static Options empty() { return Options.builder().build(); }

    public static final class Builder {

        private String name;
        private Integer order;
        private Map<String, String> requiredSystemProperties = HashMap.newHashMap(8);
        private Class<?>[] requiredClasses;
        private Class<?>[] requiredMissingClasses;
        private String[] profiles;
        private boolean timed;

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder order(Integer order) {
            this.order = order;
            return this;
        }

        public Builder conditionalOn(Class<?>... requiredClasses) {
            this.requiredClasses = requiredClasses;
            return this;
        }

        public Builder conditionalOnMissing(Class<?>... requiredMissingClasses) {
            this.requiredMissingClasses = requiredMissingClasses;
            return this;
        }

        public Builder profiles(String... profiles) {
            this.profiles = profiles;
            return this;
        }

        public Builder requiredSystemProperties(Map<String, String> requiredSystemProperties) {
            this.requiredSystemProperties = requiredSystemProperties;
            return this;
        }

        public Builder requiredSystemProperty(String key, String value) {
            this.requiredSystemProperties.put(key, value);
            return this;
        }

        public Builder timed() {
            this.timed = true;
            return this;
        }

        public Options build() {
            if (requiredClasses == null) this.requiredClasses = new Class[0];
            if (requiredMissingClasses == null) this.requiredMissingClasses = new Class[0];
            if (profiles == null) this.profiles = new String[0];
            return new Options(name, order, requiredClasses, requiredMissingClasses, profiles, requiredSystemProperties, timed);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Options options = (Options) o;
        return timed == options.timed && Objects.equals(name, options.name) && Arrays.equals(requiredClasses, options.requiredClasses) && Arrays.equals(requiredMissingClasses, options.requiredMissingClasses) && Arrays.equals(profiles, options.profiles) && Objects.equals(requiredSystemProperties, options.requiredSystemProperties);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(name, requiredSystemProperties, timed);
        result = 31 * result + Arrays.hashCode(requiredClasses);
        result = 31 * result + Arrays.hashCode(requiredMissingClasses);
        result = 31 * result + Arrays.hashCode(profiles);
        return result;
    }

    @Override
    public String toString() {
        return "Options{" +
                "name='" + name + '\'' +
                ", requiredClasses=" + Arrays.toString(requiredClasses) +
                ", requiredMissingClasses=" + Arrays.toString(requiredMissingClasses) +
                ", profiles=" + Arrays.toString(profiles) +
                ", requiredSystemProperties=" + requiredSystemProperties +
                ", timed=" + timed +
                '}';
    }
}
