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

package org.failearly.dataz.internal.common.annotation.traverser;

import java.lang.reflect.Method;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * ClassesCollector collects classes.
 */
public final class ClassesCollector {

    public static int CLASS_HIERARCHY_DEPTH = Integer.MAX_VALUE;
    public static int ONLY_DECLARED_CLASS_DEPTH = 1;
    public static int NO_CLASS_DEPTH = 0;

    private final TraverseStrategy traverseStrategy;
    private final TraverseDepth traverseDepth;

    enum Order {
        TOP_DOWN,
        BOTTOM_UP
    }

    public ClassesCollector(TraverseStrategy traverseStrategy, TraverseDepth traverseDepth) {
        this.traverseStrategy = traverseStrategy;
        this.traverseDepth = traverseDepth;
    }

    /**
     * Collect the (super) classes including the declaring class (without interfaces and {@code Object.class}) from given {@code method}.
     *
     * @param method a method instances
     * @return the method's declared class hierarchy.
     * @see Method#getDeclaringClass()
     * @see Class#getSuperclass()
     */
    public List<Class<?>> collect(Method method) {
        return collect(method.getDeclaringClass());
    }

    /**
     * Collect all super classes including {@code clazz}. The order is from sub class to super classes excluding {@link Object}.
     *
     * @param clazz the class to collect from.
     * @return the clazz and all super classes (exclude {@code Object}).
     * @see Class#getSuperclass()
     */
    public List<Class<?>> collect(Class<?> clazz) {
        final List<Class<?>> classes = new LinkedList<>();
        collectClasses(clazz, classes);
        if( this.traverseDepth.useInterface() ) {
            classes.addAll(collectInterfaces(classes));
        }
        if (this.traverseStrategy == TraverseStrategy.BOTTOM_UP) {
            Collections.reverse(classes);
        }
        return classes;
    }

    private List<Class<?>> collectInterfaces(List<Class<?>> classes) {
        final List<Class<?>> result=new LinkedList<>();
        final DistinctInterfaces distinctInterfaces =new DistinctInterfaces();

        for(Class<?> clazz : classes) {
            result.addAll(collectDistinctInterfaces(clazz, distinctInterfaces));
        }

        return result;
    }

    private List<Class<?>> collectDistinctInterfaces(Class<?> currentClass, DistinctInterfaces distinctInterfaces) {
        final List<Class<?>> interfaces = doCollectDistinctInterfaces(currentClass, distinctInterfaces);
        final List<Class<?>> result=new LinkedList<>(interfaces);

        for (Class<?> anInterface : interfaces) {
            result.addAll(collectDistinctInterfaces(anInterface, distinctInterfaces));
        }

        return result;
    }

    private List<Class<?>> doCollectDistinctInterfaces(Class<?> currentClass, DistinctInterfaces distinctInterfaces) {
        return Stream.of(currentClass)
                .flatMap(cls -> Arrays.stream(cls.getInterfaces()))
                .filter(distinctInterfaces)
                .collect(Collectors.toList());
    }

    private void collectClasses(Class<?> clazz, List<Class<?>> classes) {
        int depth = this.traverseDepth.getDepth();
        while (clazz != null && clazz != Object.class && depth > 0) {
            classes.add(clazz);
            clazz = clazz.getSuperclass();
            depth--;
        }
    }

    private static class DistinctInterfaces implements Predicate<Class<?>> {
        private final Set<Class<?>> interfaces=new HashSet<>();
        @Override
        public boolean test(Class<?> aClass) {
            if( ! interfaces.contains(aClass) ) {
                interfaces.add(aClass);
                return true;
            }

            return false;
        }
    }

}
