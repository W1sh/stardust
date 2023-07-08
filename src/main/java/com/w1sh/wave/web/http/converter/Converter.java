package com.w1sh.wave.web.http.converter;

@FunctionalInterface
public interface Converter<T> {

    T convert(String value);
}
