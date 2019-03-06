package com.hea3ven.tools.commonutils.util;

import javax.annotation.Nonnull;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.function.Predicate;

public class ReflectionUtil {
    @FunctionalInterface
    public interface MethodHandler {
        void handle(Method mthd) throws IllegalAccessException, InvocationTargetException;
    }

    @FunctionalInterface
    public interface FieldHandler {
        void handle(Field field) throws IllegalAccessException;
    }

    public static void reflectMethod(Class<?> klass, String deobfName, String obfName,
            Class<?>[] paramsClasses, MethodHandler handler) {

        Method mthd = null;
        try {
            try {
                mthd = klass.getDeclaredMethod(obfName, paramsClasses);
            } catch (NoSuchMethodException e) {
                mthd = klass.getDeclaredMethod(deobfName, paramsClasses);
            }
            mthd.setAccessible(true);
            handler.handle(mthd);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException("Could not call onSwapCraft");
        } finally {
            if (mthd != null) {
                mthd.setAccessible(false);
            }
        }
    }

    public static void reflectField(Class<?> klass, String deobfName, String obfName,
            FieldHandler handler) {

        Field field = null;
        try {
            try {
                field = klass.getDeclaredField(obfName);
            } catch (NoSuchFieldException e) {
                field = klass.getDeclaredField(deobfName);
            }
            field.setAccessible(true);
            handler.handle(field);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException("Could not call onSwapCraft");
        } finally {
            if (field != null) {
                field.setAccessible(false);
            }
        }
    }

    @Nonnull
    public static Class<?> findNestedClass(Class<?> targetClass, Predicate<Class<?>> condition) {
        Class<?>[] classes = targetClass.getDeclaredClasses();
        for (Class<?> aClass : classes) {
            if (condition.test(aClass)) {
                return aClass;
            }
        }
        throw new RuntimeException("Could not find nested class");
    }

    @Nonnull
    public static <T> T newInstance(Class<T> targetClass, Class<?>[] argsClasses, Object[] args) {
        try {
            Constructor<T> constructor = targetClass.getDeclaredConstructor(argsClasses);
            constructor.setAccessible(true);
            return constructor.newInstance(args);
        } catch (NoSuchMethodException | IllegalAccessException | InstantiationException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }
}
