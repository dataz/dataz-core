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

package org.failearly.dataz.internal.common.annotation.resolver;

import org.failearly.dataz.internal.common.annotation.traverser.AnnotationHandler;
import org.failearly.dataz.internal.common.annotation.traverser.MetaAnnotationHandler;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

/**
 * An {@code AnnotationResolver} resolves annotations from given class/method and applies the {@link AnnotationHandler}
 * on each resolved and accepted impl(s).
 *
 * @see AnnotationResolvers
 * @see org.failearly.dataz.internal.common.annotation.filter.AnnotationFilter
 */
public interface AnnotationResolver<T extends Annotation> {
    void resolveClassAnnotations(Class<?> clazz, AnnotationHandler<T> annotationHandler);

    void resolveMethodAnnotations(Method method, AnnotationHandler<T> annotationHandler);

    void resolveClassMetaAnnotations(Class<?> clazz, MetaAnnotationHandler<T> annotationHandler);

    void resolveMethodMetaAnnotations(Method method, MetaAnnotationHandler<T> annotationHandler);


}
