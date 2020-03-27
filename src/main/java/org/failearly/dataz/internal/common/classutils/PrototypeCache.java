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

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * PrototypeCache supports the prototype pattern.
 */
public final class PrototypeCache {
    private static final ConcurrentMap<Class<?>,Object> cache=new ConcurrentHashMap<>();

    private PrototypeCache() throws IllegalAccessException {
        throw new IllegalAccessException("Do not call constructor");
    }

    /**
     * Fetches the prototype instances or create the prototype object by using the default constructor (even a
     * private one).
     * @param prototypeClass the clazz object
     * @param <T> The expected type
     * @return the prototype instance.
     *
     * @see ObjectCreatorUtil#createInstanceByConstructor(Class)
     */
    public static <T> T fetchPrototype(Class<T> prototypeClass) {
        return fetchPrototype(prototypeClass, ObjectCreatorUtil::createInstanceByConstructor);
    }

    /**
     * Fetches the prototype instances or create the prototype object by using {@code creator} function.
     * @param prototypeClass the clazz object
     * @param creator the creator
     * @param <T> The expected type
     * @return the prototype instance.
     */
    public static <T> T fetchPrototype(Class<T> prototypeClass, ObjectCreator creator) {
        return prototypeClass.cast(cache.computeIfAbsent(prototypeClass, creator::createPrototype));
    }

    /**
     * ObjectCreator creates an object from class.
     */
    public interface ObjectCreator {
        Object createPrototype(Class<?> clazz);
    }
}
