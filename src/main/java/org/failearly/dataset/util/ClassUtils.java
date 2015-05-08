/*
 * dataSet - Test Support For Datastores.
 *
 * Copyright (C) 2014-2014 Marko Umek (http://fail-early.com/contact)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>
 */
package org.failearly.dataset.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * ClassUtils provides utility methods for reflecting classes.
 */
public final class ClassUtils {
    private static final Logger LOGGER = LoggerFactory.getLogger(ClassUtils.class);

    private ClassUtils() {
    }

    /**
     * Collect the (super) classes including the declaring class (without interfaces and {@code Object.class}) from given {@code method}.
     *
     * @param method a method instances
     * @return the method's declared class hierarchy.
     *
     * @see java.lang.reflect.Method#getDeclaringClass()
     * @see Class#getSuperclass()
     */
    public static List<Class<?>> collectClasses(Method method) {
        return collectClasses(method.getDeclaringClass());
    }

    /**
     * Same like {@link #collectClasses(java.lang.reflect.Method)}, but with reverted order.
     *
     * @param method a method instances
     * @return the method's declared class hierarchy.
     *
     * @see #collectClasses(java.lang.reflect.Method)
     * @see java.lang.reflect.Method#getDeclaringClass()
     * @see Class#getSuperclass()
     */
    public static List<Class<?>> collectClassesReverted(Method method) {
        return collectClassesReverted(method.getDeclaringClass());
    }

    /**
     * Same like {@link #collectClasses(Class)}, but with reverse order.
     * @param clazz a class instance.
     * @return the clazz and all super classes (exclude {@code Object}).
     */
    public static List<Class<?>> collectClassesReverted(Class<?> clazz) {
        final List<Class<?>> classes=collectClasses(clazz);
        Collections.reverse(classes);
        return classes;
    }

    /**
     * Collect all super classes including {@code clazz}. The order is from sub class to super classes excluding {@link java.lang.Object}.
     *
     * @param clazz the class to collect from.
     * @return the clazz and all super classes (exclude {@code Object}).
     *
     * @see Class#getSuperclass()
     */
    public static List<Class<?>> collectClasses(Class<?> clazz) {
        final List<Class<?>> classes = new LinkedList<>();
        climbClassHierarchyAndCollectClasses(clazz, classes);
        return classes;
    }

    private static void climbClassHierarchyAndCollectClasses(Class<?> clazz, List<Class<?>> classes) {
        while (clazz != null && clazz != Object.class) {
            classes.add(clazz);
            clazz = clazz.getSuperclass();
        }
    }

    /**
     * Create an instance of {@code fqcn} which will be casted to {@code targetClass}.
     *
     * @param targetClass  the target class.
     * @param fqcn the full qualified class name.
     * @param <T> the target type.
     * @return  an instance of T
     *
     * @see Class#forName(String, boolean, ClassLoader)
     * @see Class#newInstance()
     * @see Class#cast(Object)
     */
    public static <T> T createInstance(Class<T> targetClass, String fqcn) {
        Class<? extends T> clazz = loadClass(targetClass, fqcn);
        return createInstance(clazz);
    }

    /**
     * Creates an instance of clazz by using {@link Class#newInstance()}.
     * @param clazz the clazz object.
     * @param <T> the target type.
     * @return  an instance of T
     */
    public static <T> T createInstance(Class<? extends T> clazz) {
        try {
            return clazz.newInstance();
        } catch (Exception ex) {
            throw new RuntimeException("Can't newInstance instance of class " + clazz.getName(), ex);
        }
    }

    /**
     * Creates an instance of clazz by using {@link java.lang.reflect.Constructor}.
     * @param clazz the clazz object.
     * @param <T> the target type.
     * @return  an instance of T
     */
    public static <T> T createInstanceByConstructor(Class<? extends T> clazz) {
        try {
            final Constructor<? extends T> constructor=clazz.getDeclaredConstructor();
            constructor.setAccessible(true);
            return constructor.newInstance();
        } catch (Exception ex) {
            throw new RuntimeException("Can't newInstance instance of class " + clazz.getName(), ex);
        }
    }

    /**
     * Load class of specified {@code targetClass}.
     * @param targetClass the expected target class.
     * @param fqcn the full qualified class name.
     * @param <T> the target class type
     * @return the class object
     */
    public static <T> Class<? extends T> loadClass(Class<T> targetClass, String fqcn) {
        try {
            Class<?> clazz = loadClass(fqcn, false, getContextClassLoader());
            if (clazz == null) {
                clazz = loadClass(fqcn, true, getClassLoader(targetClass));
            }
            return clazz.asSubclass(targetClass);
        } catch (Exception ex) {
            throw new RuntimeException("Can't load class " + targetClass.getName(), ex);
        }

    }

    private static Class<?> loadClass(String fqcn, boolean throwException, ClassLoader classLoader) throws ClassNotFoundException {
        try {
            return Class.forName(fqcn, true, classLoader);
        } catch (ClassNotFoundException e) {
            if (throwException) {
                throw e;
            }
            LOGGER.error("Can't load class '{}'", fqcn);
        }

        return null;
    }


    private static ClassLoader getContextClassLoader() {
        return Thread.currentThread().getContextClassLoader();
    }

    private static ClassLoader getClassLoader(Class<?> clazz) {
        return clazz.getClassLoader();
    }

}
