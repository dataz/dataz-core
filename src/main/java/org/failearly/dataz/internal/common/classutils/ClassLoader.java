/*
 * dataZ - Test Support For Data Stores.
 *
 * Copyright 2014-2020 the original author or authors.
 *
 * All rights reserved. This program and the accompanying materials are
 * made available under the terms of the Eclipse Public License v2.0 which
 * accompanies this distribution and is available at
 *
 * https://www.eclipse.org/legal/epl-v20.html
 */

package org.failearly.dataz.internal.common.classutils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Utility class for loading classes.
 */
public final class ClassLoader {
    private static final Logger LOGGER = LoggerFactory.getLogger(ClassLoader.class);

    private ClassLoader() {
    }

    /**
     * Load class of specified {@code targetClass}.
     *
     * @param targetClass the expected target class.
     * @param fqcn        the full qualified class name.
     * @param <T>         the target class type
     * @return the class object
     */
    public static <T> Class<? extends T> loadClass(Class<T> targetClass, String fqcn) {
        try {
            Class<?> clazz = loadClass(fqcn, false, getContextClassLoader());
            if (clazz == null) {
                clazz = loadClass(fqcn, true, getClassLoader(targetClass));
            }
            assert clazz!=null : "loadClass(fqcn,true,..) must not return null";
            return clazz.asSubclass(targetClass);
        } catch (Exception ex) {
            throw new ClassLoaderException(fqcn, ex);
        }

    }

    private static Class<?> loadClass(String fqcn, boolean throwException, java.lang.ClassLoader classLoader) throws ClassNotFoundException {
        try {
            return Class.forName(fqcn, true, classLoader);
        } catch (ClassNotFoundException e) {
            LOGGER.error("Can't load class '{}'", fqcn);
            if (throwException) {
                throw e;
            }
        }

        return null;
    }

    public static java.lang.ClassLoader getContextClassLoader() {
        return Thread.currentThread().getContextClassLoader();
    }

    public static java.lang.ClassLoader getClassLoader(Class<?> clazz) {
        return clazz.getClassLoader();
    }

    public static class ClassLoaderException extends RuntimeException {
        ClassLoaderException(String className, Exception ex) {
            super(message(className), ex);
        }
        private static String message(String className) {
            return "Can't load class '"+ className +"'";
        }

    }
}