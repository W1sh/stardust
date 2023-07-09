package com.w1sh.wave.web.http.mapper;

@FunctionalInterface
public interface Mapper<T> {

    T map(String value);
}
