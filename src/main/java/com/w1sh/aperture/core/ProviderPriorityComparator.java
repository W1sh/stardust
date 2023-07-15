package com.w1sh.aperture.core;

import java.util.Comparator;
import java.util.List;

public class ProviderPriorityComparator implements Comparator<Definition<?>> {

    public static final ProviderPriorityComparator INSTANCE = new ProviderPriorityComparator();

    @Override
    public int compare(Definition<?> o1, Definition<?> o2) {
        return o1.getMetadata().priority().compareTo(o2.getMetadata().priority());
    }

    public static void sort(List<Definition<?>> list) {
        if (list.size() > 1) {
            list.sort(INSTANCE);
        }
    }
}
