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

package org.failearly.dataz.internal.common.annotation.resolver;

import org.failearly.dataz.internal.common.annotation.elementresolver.AnnotationElementResolver;
import org.failearly.dataz.internal.common.annotation.elementresolver.AnnotationElementResolvers;
import org.failearly.dataz.internal.common.annotation.traverser.MetaAnnotationHandler;
import org.failearly.dataz.internal.common.annotation.utils.AnnotationUtils;
import org.failearly.dataz.internal.common.annotation.filter.AnnotationFilter;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Consumer;

import static java.util.Arrays.asList;
import static org.failearly.dataz.internal.common.annotation.utils.AnnotationUtils.getMetaAnnotation;

/**
 * MetaAnnotationResolver is responsible for ...
 */
final class MetaAnnotationResolver<T extends Annotation> extends AnnotationResolverBase<T> {
    private static final AnnotationElementResolver<Annotation[]> annotationElementResolver=AnnotationElementResolvers.createResolver(
                        Annotation[].class,
                        "value",
                        MetaAnnotationResolver::isValueMethod
                );

    MetaAnnotationResolver(Class<T> metaAnnotationClass, AnnotationFilter annotationFilter) {
        super(metaAnnotationClass, annotationFilter);
    }

    private void doResolveMetaAnnotations(AnnotatedElement annotatedElement, Consumer<Annotation> consumer) {
        doResolveAnnotations(annotatedElement).stream()
                .filter(annotationFilter)
                .forEach(consumer);
    }

    @Override
    public void resolveClassMetaAnnotations(Class<?> clazz, MetaAnnotationHandler<T> annotationHandler) {
        doResolveMetaAnnotations(clazz, (annotation) -> annotationHandler.handleMetaClassAnnotation(
                clazz,
                annotation,
                getMetaAnnotation(annotationClass, annotation)
            )
        );
    }

    @Override
    public void resolveMethodMetaAnnotations(Method method, MetaAnnotationHandler<T> annotationHandler) {
        doResolveMetaAnnotations(method, (annotation)->annotationHandler.handleMetaMethodAnnotation(
                method,
                annotation,
                getMetaAnnotation(annotationClass, annotation)
            )
        );
    }

    @Override
    protected List<Annotation> doResolveAnnotations(AnnotatedElement annotatedElement) {
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
        Collections.addAll(annotations, annotatedElement.getDeclaredAnnotations());
        return annotations;
    }

    private static boolean isAnnotation(AnnotatedElement annotatedElement) {
        return annotatedElement instanceof Class<?> && ((Class<?>)annotatedElement).isAnnotation();
    }

    private List<Annotation> resolveMetaAnnotations(Annotation annotation) {
        return doResolveAnnotations(AnnotationUtils.getAnnotationClass(annotation));
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
