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

import java.lang.reflect.Constructor;

/**
 * ObjectCreator provides utility methods for creating objects by reflection
 * (either {@link Class#newInstance()} or {@link Class#getDeclaredConstructor(Class[])}.
 */
public final class ObjectCreator {
    private static final Logger LOGGER = LoggerFactory.getLogger(ObjectCreator.class);

    private ObjectCreator() {
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


    public static class ObjectCreateException extends DataSetException {
        ObjectCreateException(Class<?> clazz, Exception ex) {
            super("Can't create instance of class " + clazz.getName(), ex);
        }
    }
}
