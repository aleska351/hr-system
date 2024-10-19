package com.codingdrama.hrsystem.util;

import java.lang.reflect.Field;

public class FieldUtil {

    public static <T> void updateFields(T source, T target) {
        if (source == null || target == null) {
            throw new IllegalArgumentException("Source or target object cannot be null.");
        }

        Class<?> clazz = source.getClass();

        for (Field field : clazz.getDeclaredFields()) {
            field.setAccessible(true);
            try {
                Object newValue = field.get(source);  // Get value from source 
                if (newValue != null) {
                    Field targetField = target.getClass().getDeclaredField(field.getName());
                    targetField.setAccessible(true);
                    Object existingValue = targetField.get(target);  // Get value from target

                    // Update the field in target if the new value is different from the existing value
                    if (!newValue.equals(existingValue)) {
                        targetField.set(target, newValue);
                    }
                }
            } catch (IllegalAccessException | NoSuchFieldException e) {
                throw new RuntimeException("Error updating fields", e);
            }
        }
    }
}
