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

package org.failearly.dataz.template.generator.support;

import org.failearly.dataz.template.TemplateObject;
import org.failearly.dataz.template.TemplateObjectAnnotationContext;
import org.failearly.dataz.template.simple.Constant;

import java.util.Arrays;
import java.util.HashSet;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * TemplateObjectMock is responsible for ...
 */
public final class TemplateObjectMock {
    public static TemplateObject createTemplateObjectMock(Class<?> annotationHolderClass) {
        final Constant annotation = annotationHolderClass.getAnnotation(Constant.class);
        TemplateObject templateObjectMock = mock(TemplateObject.class);
        when(templateObjectMock.getContext()).thenReturn(TemplateObjectAnnotationContext.createAnnotationContext(annotationHolderClass));
        when(templateObjectMock.getAnnotation()).thenReturn(annotation);
        when(templateObjectMock.datasets()).thenReturn(toSet(annotation.datasets()));
        when(templateObjectMock.name()).thenReturn(annotation.name());
        when(templateObjectMock.scope()).thenReturn(annotation.scope());
        return templateObjectMock;
    }

    private static HashSet<String> toSet(String[] datasets) {
        return new HashSet<>(Arrays.asList(datasets));
    }

}
