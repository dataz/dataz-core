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

import java.lang.reflect.Method;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * ClassesCollector collects classes.
 */
public final class ClassesCollector {

    public static int CLASS_HIERARCHY_DEPTH = Integer.MAX_VALUE;
    public static int ONLY_DECLARED_CLASS_DEPTH = 1;
    public static int NO_CLASS_DEPTH = 0;

    private final Order order;
    private final int depth;

    public enum Order {
        TOP_DOWN,
        BOTTOM_UP
    }

    public ClassesCollector(Order order, int depth) {
        this.order = order;
        this.depth = depth;
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
        climbClassHierarchyAndCollectClasses(clazz, classes);
        if (this.order == Order.BOTTOM_UP) {
            Collections.reverse(classes);
        }
        return classes;
    }

    private void climbClassHierarchyAndCollectClasses(Class<?> clazz, List<Class<?>> classes) {
        int depth = this.depth;
        while (clazz != null && clazz != Object.class && depth > 0) {
            classes.add(clazz);
            clazz = clazz.getSuperclass();
            depth--;
        }
    }

}
