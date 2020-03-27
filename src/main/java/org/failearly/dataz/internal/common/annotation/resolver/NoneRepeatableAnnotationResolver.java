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

import org.failearly.dataz.internal.common.annotation.filter.AnnotationFilter;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.util.Arrays;
import java.util.List;

/**
* NoneRepeatableAnnotationResolver is responsible for ...
*/
final class NoneRepeatableAnnotationResolver<T extends Annotation> extends AnnotationResolverBase<T> {
    NoneRepeatableAnnotationResolver(Class<T> annotationClass, AnnotationFilter annotationFilter) {
        super(annotationClass, annotationFilter);
    }

    @Override
    protected List<Annotation> doResolveAnnotations(AnnotatedElement annotatedElement) {
        return Arrays.asList(annotatedElement.getDeclaredAnnotations());
    }
}
