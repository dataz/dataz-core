/*
 * dataSet - Test Support For Data Stores.
 *
 * Copyright (C) 2014-2015 Marko Umek (http://fail-early.com/contact)
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

import org.failearly.dataset.exception.DataSetException;
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

    public static class ClassLoaderException extends DataSetException {
        ClassLoaderException(String className, Exception ex) {
            super(message(className), ex);
        }
        private static String message(String className) {
            return "Can't load class '"+ className +"'";
        }

    }
}