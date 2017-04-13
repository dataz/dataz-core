/*
 * dataZ - Test Support For Data Stores.
 *
 * Copyright 2014-2017 the original author or authors.
 *
 * All rights reserved. This program and the accompanying materials are
 * made available under the terms of the Eclipse Public License v1.0 which
 * accompanies this distribution and is available at
 *
 * http://www.eclipse.org/legal/epl-v10.html
 */

package org.failearly.dataz.internal.common.classutils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.*;
import java.lang.reflect.Constructor;

/**
 * ObjectCreatorUtil provides utility methods for creating objects by reflection
 * (either {@link Class#newInstance()} or {@link Class#getDeclaredConstructor(Class[])}.
 */
public final class ObjectCreatorUtil {
    private static final Logger LOGGER = LoggerFactory.getLogger(ObjectCreatorUtil.class);

    private ObjectCreatorUtil() {
    }

    /**
     * Create an instance of {@code fqcn} which will be casted to {@code targetClass}.
     *
     * @param targetClass  the target class.
     * @param fqcn the full qualified class name.
     * @param <T> the target type.
     * @return  an instance of T
     *
     * @see Class#forName(String, boolean, java.lang.ClassLoader)
     * @see Class#newInstance()
     * @see Class#cast(Object)
     */
    public static <T> T createInstance(Class<T> targetClass, String fqcn) {
        return createInstance(ClassLoader.loadClass(targetClass, fqcn));
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
            LOGGER.error("Can't create instance of class '{}'", clazz.getName());
            throw new ObjectCreateException(clazz, ex);
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
            LOGGER.error("Can't create instance of class '{}'", clazz.getName());
            throw new ObjectCreateException(clazz, ex);
        }
    }


    public static class ObjectCreateException extends RuntimeException {
        ObjectCreateException(Class<?> clazz, Exception ex) {
            super("Can't create instance of class " + clazz.getName(), ex);
        }
    }
}
