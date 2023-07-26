package com.w1sh.aperture;

import java.util.Comparator;
import java.util.List;

public class ProviderPriorityComparator implements Comparator<Class<?>> {

    public static final ProviderPriorityComparator INSTANCE = new ProviderPriorityComparator();

    @Override
    public int compare(Class<?> o1, Class<?> o2) {
        return 0;
    }

    public static void sort(List<Class<?>> list) {
        if (list.size() > 1) {
            list.sort(INSTANCE);
        }
    }
}
