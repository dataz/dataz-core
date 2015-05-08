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

import org.failearly.dataset.internal.annotation.filter.AnnotationFilter;
import org.failearly.dataset.internal.annotation.filter.AnnotationFilters;

import java.lang.annotation.Annotation;

/**
 * AnnotationResolvers is responsible for ...
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

    public static AnnotationResolver<Annotation> metaAnnotation(Class<? extends Annotation> metaAnnotationClass) {
        return new MetaAnnotationResolver(AnnotationFilters.hasMetaAnnotation(metaAnnotationClass));
    }
}
