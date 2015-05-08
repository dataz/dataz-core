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
package org.failearly.dataset.internal.annotation;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 * AnnotationUtils provides some basic utility methods for annotations.
 */
public final class AnnotationUtils {
    private AnnotationUtils() {
    }

    /**
     * Resolve the annotation's class.
     * @param annotation the annotation
     * @return the class object of the annotation.
     */
    public static Class<? extends Annotation> getAnnotationClass(Annotation annotation) {
        return annotation.annotationType();
    }

    /**
     * Get the meta annotation from given annotation.
     * @param <T> the expect meta annotation
     * @param metaAnnotationClass the meta annotation class
     * @param annotation the annotation
     * @return the meta annotation
     */
    public static <T extends Annotation> T getMetaAnnotation(Class<T> metaAnnotationClass, Annotation annotation) {
        return getAnnotationClass(annotation).getAnnotation(metaAnnotationClass);
    }

    /**
     * Check if there is at least one annotation with given meta annotation. The method is the starting point and checks also the entire class hierarchy of the
     * declaring class.
     *
     * @param method the starting method
     * @param metaAnnotationClass the meta annotation to check for.
     *
     * @return {@code true} if the method, the declaring class or any of the super classes has an annotation with given meta annotation.
     */
    public static boolean checkForMetaAnnotation(Method method, Class<? extends Annotation> metaAnnotationClass) {
        final AnnotationTraverser<Annotation> traverser = AnnotationTraversers.createMetaAnnotationTraverser( //
                metaAnnotationClass,                //
                TraverseStrategy.TOP_DOWN,          //
                Order.UNCHANGED                     //
            );
        final List<Annotation> annotationsWithMetaAnnotation=new LinkedList<>();
        traverser.traverse(method, new AnnotationHandlerBase<Annotation>() {
            @Override
            public void handleAnnotation(Annotation annotation) {
                annotationsWithMetaAnnotation.add(annotation);
            }
        });
        return ! annotationsWithMetaAnnotation.isEmpty();
    }
}
