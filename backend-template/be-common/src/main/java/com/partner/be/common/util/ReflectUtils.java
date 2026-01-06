package com.partner.be.common.util;

import com.partner.be.common.db.HasCreator;
import com.partner.be.common.db.HasUpdater;
import com.partner.be.common.filter.UserThreadHolder;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Reflection utility class
 *
 * Provides utility methods for reflection operations including field access,
 * value setting, and object-to-map conversions using Java reflection API.
 */
@Slf4j
public class ReflectUtils {
    /**
     * Get field value from object using reflection
     *
     * @param obj target object
     * @param fieldName target field name
     * @return field value
     */
    public static Object getFieldValue(Object obj, String fieldName) {
        Object result = null;
        Field field = getField(obj, fieldName);
        if (field != null) {
            field.setAccessible(true);
            try {
                result = field.get(obj);
            } catch (Exception e) {
                log.error("Failed to get field value: field={}, object={}", fieldName, obj.getClass().getName(), e);
            }
        }
        return result;
    }

    /**
     * Get field from object using reflection
     *
     * @param obj target object
     * @param fieldName target field name
     * @return Field object
     */
    private static Field getField(Object obj, String fieldName) {
        Field field = null;
        for (Class<?> clazz = obj.getClass(); clazz != Object.class; clazz = clazz.getSuperclass()) {
            try {
                field = clazz.getDeclaredField(fieldName);
                break;
            } catch (NoSuchFieldException e) {
                // Field not found in this class, continue checking parent class
                log.debug("Field '{}' not found in class {}, checking parent class", fieldName, clazz.getName());
            }
        }
        return field;
    }

    /**
     * Set field value on object using reflection
     *
     * @param obj target object
     * @param fieldName target field name
     * @param fieldValue value to set
     */
    public static void setFieldValue(Object obj, String fieldName, Object fieldValue) {
        Field field = getField(obj, fieldName);
        if (field != null) {
            try {
                field.setAccessible(true);
                if (field.getType().getSimpleName().equalsIgnoreCase("Integer")) {
                    field.set(obj, Integer.parseInt((String) fieldValue));
                } else if (field.getType().getSimpleName().equalsIgnoreCase("Long")) {
                    field.set(obj, Long.parseLong((String) fieldValue));
                } else if (field.getType().getSimpleName().equalsIgnoreCase("Float")) {
                    field.set(obj, Float.parseFloat((String) fieldValue));
                } else if (field.getType().getSimpleName().equalsIgnoreCase("Double")) {
                    field.set(obj, Double.parseDouble((String) fieldValue));
//                } else if (field.getType().getSimpleName().equalsIgnoreCase("Date")) {
//                    if (fieldName.endsWith("Date")) {
//                        field.set(obj, DateUtil.strToDate((String) fieldValue, "yyyy-MM-dd"));
//                    } else if (fieldName.endsWith("Time")) {
//                        field.set(obj, DateUtil.strToDate((String) fieldValue, "yyyy-MM-dd hh:mm:ss"));
//                    }
                } else {
                    field.set(obj, fieldValue);
                }
            } catch (Exception e) {
                throw new IllegalArgumentException("Field error, fieldName:" + fieldName, e);
            }
        }
    }

    /**
     * Convert JavaBean to Map
     *
     * @param javaBean source object
     * @return Map containing field names and values
     */
    public static <K, V> Map<K, V> bean2Map(Object javaBean) {
        Map<K, V> ret = new HashMap<K, V>();
        try {
            for (Class<?> clazz = javaBean.getClass(); clazz != Object.class; clazz = clazz.getSuperclass()) {
                Method[] methods = clazz.getDeclaredMethods();
                for (Method method : methods) {
                    if (method.getName().startsWith("get")) {
                        String field = method.getName();
                        field = field.substring(field.indexOf("get") + 3);
                        field = field.toLowerCase().charAt(0) + field.substring(1);
                        Object value = method.invoke(javaBean, (Object[]) null);
                        ret.put((K) field, (V) (null == value ? "" : value));
                    }
                }
            }
        } catch (Exception e) {
            log.error("Failed to convert JavaBean to Map: {}", javaBean.getClass().getName(), e);
        }
        return ret;
    }

    /**
     * Convert Map to JavaBean
     *
     * @param map source map containing field values
     * @param clazz target class type
     * @return populated JavaBean instance
     */
    public static <R> R map2Bean(Map<String, String> map, Class<R> clazz) {
        R result = null;
        try {
            result = clazz.newInstance();
        } catch (Exception e) {
            log.error("Failed to instantiate class: {}", clazz.getName(), e);
            return null;
        }
        for (Map.Entry<String, String> entry : map.entrySet()) {
            setFieldValue(result, entry.getKey(), entry.getValue());
        }

        Date createTime = new Date();
        if (result instanceof HasCreator) {
            ((HasCreator) result).setCreatedBy((String) UserThreadHolder.getLoginUser().getUserId());
            ((HasCreator) result).setCreatedAt(createTime);
        }
        if (result instanceof HasUpdater) {
            ((HasUpdater) result).setUpdatedBy((String) UserThreadHolder.getLoginUser().getUserId());
            ((HasUpdater) result).setUpdatedAt(createTime);
        }
        return result;
    }
}
