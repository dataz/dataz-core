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
package org.failearly.dataset.internal.annotation.resolver;

import org.failearly.dataset.internal.annotation.AnnotationUtils;
import org.failearly.dataset.internal.annotation.filter.AnnotationFilter;
import org.failearly.dataset.internal.annotation.invoker.AnnotationElementResolver;
import org.failearly.dataset.internal.annotation.invoker.AnnotationElementResolvers;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Method;
import java.util.*;

import static java.util.Arrays.asList;

/**
 * MetaAnnotationResolver is responsible for ...
 */
final class MetaAnnotationResolver extends AnnotationResolverBase<Annotation> {
    private static final AnnotationElementResolver<Annotation[]> annotationElementResolver=AnnotationElementResolvers.createResolver(
                        Annotation[].class,
                        "value",
                        MetaAnnotationResolver::isValueMethod
                );

    MetaAnnotationResolver(AnnotationFilter annotationFilter) {
        super(Annotation.class, annotationFilter);
    }

    @Override
    protected List<Annotation> resolveAnnotations(AnnotatedElement annotatedElement) {
        if( isAnnotation(annotatedElement)) {
           return doResolveAnnotationsFromMetaAnnotation(annotatedElement);
        }

        return doResolveAnnotationsFromClassesAndMethods(annotatedElement);
    }

    private List<Annotation> doResolveAnnotationsFromClassesAndMethods(AnnotatedElement annotatedElement) {
        final List<Annotation> annotations = new LinkedList<>();

        for (Annotation annotation : annotatedElement.getDeclaredAnnotations()) {
            if (isContainerAnnotation(annotation)) {
                annotations.addAll(resolveContainingAnnotations(annotation));
            } else {
                annotations.add(annotation);
            }

            annotations.addAll(resolveMetaAnnotations(annotation));
        }

        return annotations;
    }

    private List<Annotation> doResolveAnnotationsFromMetaAnnotation(AnnotatedElement annotatedElement) {
        final List<Annotation> annotations = new LinkedList<>();

        for (Annotation annotation : annotatedElement.getDeclaredAnnotations()) {
                annotations.add(annotation);
        }

        return annotations;
    }

    private static boolean isAnnotation(AnnotatedElement annotatedElement) {
        return annotatedElement instanceof Class<?> && ((Class<?>)annotatedElement).isAnnotation();
    }

    private List<Annotation> resolveMetaAnnotations(Annotation annotation) {
        return resolveAnnotations(AnnotationUtils.getAnnotationClass(annotation));
    }

    private static boolean isContainerAnnotation(Annotation annotation) {
        return annotationElementResolver.hasElement(annotation);
    }

    private static List<Annotation> resolveContainingAnnotations(Annotation containerAnnotation) {
        return asList(annotationElementResolver.resolveElementValue(containerAnnotation));
    }

    private static boolean isValueMethod(Method method) {
        final Class<?> returnType = method.getReturnType();
        return returnType.isArray()
               && returnType.getComponentType().isAnnotation();
    }
}
