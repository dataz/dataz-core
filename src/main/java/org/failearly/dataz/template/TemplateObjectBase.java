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

import org.failearly.dataz.exception.DataSetException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import static org.failearly.common.annotation.utils.AnnotationUtils.resolveValueOfAnnotationAttribute;

/**
 * TemplateObjectBase should be the base class for {@link TemplateObjectBase} implementations.
 */
public abstract class TemplateObjectBase implements TemplateObject {

    private static final TemplateObjectAnnotationContext NO_TEMPLATE_OBJECT_ANNOTATION_CONTEXT = null;
    protected final Logger LOGGER = LoggerFactory.getLogger(getClass());

    private static final Annotation NO_ANNOTATION=null;
    private static final String[] NO_DATSETS = {};
    private static final String NO_NAME = "<no name>";

    private TemplateObjectAnnotationContext templateObjectAnnotationContext;
    private final Annotation annotation;
    private final Set<String> datasets;
    private final String name;
    private final Scope scope;

    /**
     * For prototype based implementations like {@link org.failearly.dataz.template.simple.Adhoc}.
     */
    protected TemplateObjectBase() {
        this(NO_TEMPLATE_OBJECT_ANNOTATION_CONTEXT, NO_ANNOTATION, NO_NAME, NO_DATSETS, Scope.DEFAULT);
    }

    protected TemplateObjectBase(TemplateObject other) {
        this(other.getContext(), other.getAnnotation(), other.name(), toArray(other.datasets()), other.scope());
    }

    private static String[] toArray(Set<String> datasets) {
        return datasets.toArray(new String[datasets.size()]);
    }


    protected TemplateObjectBase(TemplateObjectAnnotationContext context, Annotation annotation) {
        this(context,
            annotation,
            resolveValueOfAnnotationAttribute(annotation,"name",String.class),
            resolveValueOfAnnotationAttribute(annotation,"datasets",String[].class),
            resolveValueOfAnnotationAttribute(annotation,"scope",Scope.class));
    }

    private TemplateObjectBase(TemplateObjectAnnotationContext templateObjectAnnotationContext, Annotation annotation, String name, String[] dataset, Scope scope) {
        this.templateObjectAnnotationContext = templateObjectAnnotationContext;
        this.annotation = annotation;
        this.datasets = new HashSet<>(Arrays.asList(dataset));
        this.name = name;
        this.scope = scope;
    }

    public void init() throws DataSetException {
        checkInvariant(notEmptyOrNull(name), "name() must not be empty or null");
        checkInvariant(datasets!=null, "datasets() must not be null");
        checkInvariant(scope != null, "scope() must not be null");
        doInit();
    }

    @Override
    public Annotation getAnnotation() {
        return annotation;
    }

    @Override
    public final TemplateObjectAnnotationContext getContext() {
        return templateObjectAnnotationContext;
    }

    protected void doInit() {
        // do nothing
    }

    protected static boolean notEmptyOrNull(String annotationAttribute) {
        return ! Objects.toString(annotationAttribute, "").isEmpty();
    }

    protected final void checkInvariant(boolean condition, String invariant) {
        if( ! condition ) {
            invariantViolated(invariant);
        }
    }

    protected final void invariantViolated(String invariant) {
        throw new InvariantViolationException(annotation, invariant);
    }


    @Override
    public final String name() {
        return name;
    }

    @Override
    public final Set<String> datasets() {
        return datasets;
    }

    @Override
    public final Scope scope() {
        return scope.getScopeValue();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TemplateObjectBase)) return false;

        TemplateObjectBase that = (TemplateObjectBase) o;
        return Objects.equals(this.name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    @Override
    public final void __extend_TemplateObjectBase__instead_of_implementing_TemplateObject() {
        throw new UnsupportedOperationException("do not call __extend_TemplateObjectBase__instead_of_implementing_TemplateObject()");
    }

    protected final <T extends Annotation> T getAnnotation(Class<T> annotationClass) {
        return annotationClass.cast(annotation);
    }

    protected final String getAnnotationName() {
        return annotation.annotationType().getSimpleName();
    }


    @Override
    public String toString() {
        return Objects.toString(annotation, "TemplateObject{" +
                "name='" + name + '\'' +
                ", datasets='" + datasets + '\'' +
                ", scope='" + scope + '\'' +
                ", annotationType=@" + getAnnotationName() +
                '}');
    }
}
