package com.w1sh.stardust.interceptors;

import com.w1sh.stardust.configuration.PropertiesRegistry;
import com.w1sh.stardust.annotation.Property;
import com.w1sh.stardust.annotation.Required;
import com.w1sh.stardust.exception.PropertyValueSettingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;

public record PropertyValuePostConstructInterceptor(PropertiesRegistry registry) implements InvocationInterceptor {

    private static final Logger logger = LoggerFactory.getLogger(PropertyValuePostConstructInterceptor.class);

    @Override
    public void intercept(Object instance) {
        try {
            for (Field field : instance.getClass().getDeclaredFields()) {
                if (field.isAnnotationPresent(Property.class)) {
                    final var property = field.getAnnotation(Property.class).value();
                    final var propertyValue = registry.getProperty(property);
                    if (propertyValue == null && field.isAnnotationPresent(Required.class)) {
                        throw new PropertyValueSettingException("Failed to set value to property marked as required, property is not present.");
                    }
                    if (propertyValue != null) {
                        final var setter = instance.getClass().getMethod(formattedFieldName(field), propertyValue.getClass());
                        setter.invoke(instance, propertyValue);
                    } else {
                        logger.debug("Field \"{}\" marked with property but none was found.", field.getName());
                    }
                }
            }
        } catch (IllegalAccessException e) {
            throw new PropertyValueSettingException("Failed to set property value.", e);
        } catch (NoSuchMethodException e) {
            throw new PropertyValueSettingException("Failed to find corresponding setter method.", e);
        } catch (InvocationTargetException e) {
            throw new PropertyValueSettingException("Failed to invoke corresponding setter method.", e);
        }
    }

    @Override
    public InvocationType getInterceptorType() {
        return InvocationType.POST_CONSTRUCT;
    }

    private String formattedFieldName(Field field) {
        return "set" + field.getName().substring(0, 1).toUpperCase() + field.getName().substring(1);
    }
}
