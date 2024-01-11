package com.w1sh.aperture.configuration;

import com.w1sh.aperture.InvocationInterceptor;
import com.w1sh.aperture.annotation.Property;
import com.w1sh.aperture.annotation.Provide;
import com.w1sh.aperture.exception.PropertyValueSettingException;

import java.lang.reflect.Field;

@Provide
public class PropertyValuePostConstructInterceptor implements InvocationInterceptor {

    private final PropertiesRegistry registry;

    public PropertyValuePostConstructInterceptor(PropertiesRegistry registry) {
        this.registry = registry;
    }

    @Override
    public void intercept(Object instance) {
        try {
            for (Field field : instance.getClass().getDeclaredFields()) {
                if (field.isAnnotationPresent(Property.class)) {
                    String property = field.getAnnotation(Property.class).value();
                    String propertyValue = registry.getProperty(property);
                    field.setAccessible(true);
                    field.set(instance, propertyValue);
                }
            }
        } catch (IllegalAccessException e) {
            throw new PropertyValueSettingException("Failed to set property value", e);
        }
    }

    @Override
    public InvocationType getInterceptorType() {
        return InvocationType.POST_CONSTRUCT;
    }
}
