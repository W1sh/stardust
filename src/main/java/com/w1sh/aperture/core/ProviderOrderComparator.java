package com.w1sh.aperture.core;

import java.util.Comparator;
import java.util.List;

public class ProviderOrderComparator implements Comparator<InitializationContext<?>> {

    public static final ProviderOrderComparator INSTANCE = new ProviderOrderComparator();

    @Override
    public int compare(InitializationContext<?> o1, InitializationContext<?> o2) {
        return o1.getOptions().order().compareTo(o2.getOptions().order());
    }

    public static void sort(List<InitializationContext<?>> list) {
        if (list.size() > 1) {
            list.sort(INSTANCE);
        }
    }
}
