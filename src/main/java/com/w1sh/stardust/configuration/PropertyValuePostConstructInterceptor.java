package com.w1sh.stardust.configuration;

import com.w1sh.stardust.InvocationInterceptor;
import com.w1sh.stardust.annotation.Inject;
import com.w1sh.stardust.annotation.Property;
import com.w1sh.stardust.annotation.Provide;
import com.w1sh.stardust.annotation.Required;
import com.w1sh.stardust.exception.PropertyValueSettingException;

import java.lang.reflect.Field;

@Provide
public class PropertyValuePostConstructInterceptor implements InvocationInterceptor {

    private final PropertiesRegistry registry;

    @Inject
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
                    if (propertyValue == null && field.isAnnotationPresent(Required.class)) {
                        throw new PropertyValueSettingException("Failed to set value to property marked as required, property is not present.");
                    }
                    field.setAccessible(true);
                    field.set(instance, propertyValue);
                }
            }
        } catch (IllegalAccessException e) {
            throw new PropertyValueSettingException("Failed to set property value.", e);
        }
    }

    @Override
    public InvocationType getInterceptorType() {
        return InvocationType.POST_CONSTRUCT;
    }
}
