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

import org.failearly.dataz.internal.common.annotation.filter.AnnotationFilter;
import org.failearly.dataz.internal.common.annotation.filter.AnnotationFilters;

import java.lang.annotation.Annotation;

/**
 * AnnotationResolvers provides the actually implementations for {@link AnnotationResolver}.
 */
public final class AnnotationResolvers {
    private AnnotationResolvers() {
    }

    public static <T extends Annotation> AnnotationResolver<T> noneRepeatable(Class<T> annotationClass, AnnotationFilter annotationFilter) {
        return new NoneRepeatableAnnotationResolver<>(annotationClass, annotationFilter);
    }

    public static <T extends Annotation> AnnotationResolver<T> repeatable(Class<T> annotationClass, AnnotationFilter annotationFilter) {
        return new RepeatableAnnotationResolver<>(annotationClass, annotationFilter);
    }

    public static <T extends Annotation> AnnotationResolver<T> metaAnnotation(Class<T> metaAnnotationClass) {
        return new MetaAnnotationResolver<>(metaAnnotationClass, AnnotationFilters.hasMetaAnnotation(metaAnnotationClass));
    }
}
