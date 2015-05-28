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
package org.failearly.dataset.template;

import java.lang.annotation.Annotation;

/**
 * TypedTemplateObjectFactory cast to the actually expected annotation and provides to type safe methods.
 *
 * @see #doCreate(Annotation)
 * @see #doResolveDataSetName(Annotation)
 */
public abstract class TypedTemplateObjectFactory<T extends Annotation> implements TemplateObjectFactory {
    private final Class<T> annotationClass;

    protected TypedTemplateObjectFactory(Class<T> annotationClass) {
        this.annotationClass = annotationClass;
    }

    @Override
    public final TemplateObject create(Annotation annotation) {
        return doCreate(annotationClass.cast(annotation));
    }

    /**
     * Type safe alternative for {@link #create(Annotation)}.
     * @param annotation the annotation.
     * @return the created template object.
     */
    protected abstract TemplateObject doCreate(T annotation);


    @Override
    public final String resolveDataSetName(Annotation annotation) {
        return doResolveDataSetName(annotationClass.cast(annotation));
    }

    /**
     * Type safe alternative for {@link #resolveDataSetName(Annotation)}.
     * @param annotation the annotation.
     * @return the data set name.
     */
    protected abstract String doResolveDataSetName(T annotation);
}
