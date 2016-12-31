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

package org.failearly.dataz.template;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * TemplateObjectFactoryBase is the base class for implementing {@link TemplateObjectFactory}. It also cast to the
 * actually expected annotation and provides type safe methods.
 *
 * @see #doCreate(AnnotatedElement, Annotation)
 * @see #doResolveDataSetNames(Annotation)
 */
public abstract class TemplateObjectFactoryBase<TOA extends Annotation> implements TemplateObjectFactory {
    private final Class<TOA> annotationClass;

    protected TemplateObjectFactoryBase(Class<TOA> annotationClass) {
        Objects.requireNonNull(annotationClass,"Missing annotation class.");
        this.annotationClass = annotationClass;
    }

    @Override
    public TemplateObject create(AnnotatedElement annotatedElement, Annotation annotation) {
        Objects.requireNonNull(annotation,"Missing annotation of type " + annotationClass.getName());
        Objects.requireNonNull(annotatedElement,"Missing annotated element of type " + annotationClass.getName());
        final TemplateObject templateObject = doCreate(annotatedElement, annotationClass.cast(annotation));
        return templateObject;
    }

    /**
     * Type safe alternative for {@link TemplateObjectFactory#create(AnnotatedElement, Annotation)}.
     *
     * @param annotatedElement the annotated element (the class or method where the TOA has been applied)
     * @param annotation the annotation.
     *
     * @return the created template object.
     */
    protected abstract TemplateObject doCreate(AnnotatedElement annotatedElement, TOA annotation);


    @Override
    public final Set<String> resolveDataSetNames(Annotation annotation) {
        return toSet(doResolveDataSetNames(annotationClass.cast(annotation)));
    }

    protected static Set<String> toSet(String[] dataSetNames) {
        return new HashSet<>(Arrays.asList(dataSetNames));
    }

    /**
     * Type safe alternative for {@link #resolveDataSetNames(Annotation)}.
     * @param annotation the annotation.
     * @return the data set name.
     */
    protected abstract String[] doResolveDataSetNames(TOA annotation);


    @Override
    public final Scope resolveScope(Annotation annotation) {
        final Scope scope = doResolveScope(annotationClass.cast(annotation));
        return scope.getScopeValue();
    }

    @Override
    public final String resolveName(Annotation annotation) {
        return doResolveName(annotationClass.cast(annotation));
    }

    /**
     * Type safe alternative for {@link #resolveName(Annotation)}.
     * @param annotation the annotation.
     * @return the name.
     */
    protected abstract String doResolveName(TOA annotation);


    /**
     * Type safe alternative for {@link #resolveScope(Annotation)}.
     * @param annotation the annotation.
     * @return the scope.
     */
    protected abstract Scope doResolveScope(TOA annotation);

    @Override
    public final void __extend_TemplateObjectFactoryBase__instead_of_implementing_TemplateObjectFactory() {
        throw new UnsupportedOperationException("__extend_TemplateObjectFactoryBase__instead_of_implementing_TemplateObjectFactory should not be called");
    }
}
