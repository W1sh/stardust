package com.w1sh.aperture.web.http.mapper;

@FunctionalInterface
public interface Mapper<T> {

    T map(String value);
}
