/*
 * dataZ - Test Support For Data Stores.
 *
 * Copyright (C) 2014-2016 'Marko Umek' (http://fail-early.com)
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
