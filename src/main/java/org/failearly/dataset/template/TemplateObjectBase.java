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

import org.failearly.dataset.config.Constants;
import org.failearly.dataset.exception.DataSetException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.annotation.Annotation;
import java.util.Objects;

/**
 * TemplateObjectBase should be the base class for {@link TemplateObjectBase} implementations.
 */
public abstract class TemplateObjectBase implements TemplateObject {

    protected static final Annotation NO_ANNOTATION=null;
    protected final Logger LOGGER = LoggerFactory.getLogger(getClass());

    private final Annotation annotation;
    private final String dataset;
    private final String name;
    private final Scope scope;

    /**
     * For prototype based implementations like {@link org.failearly.dataset.template.simple.Adhoc}.
     */
    protected TemplateObjectBase() {
        this(NO_ANNOTATION, Constants.DATASET_DEFAULT_NAME, "<no name>", Scope.DEFAULT);
    }

    protected TemplateObjectBase(String dataset, String name, Scope scope) {
        this(NO_ANNOTATION, dataset, name, scope);
    }

    protected TemplateObjectBase(Annotation annotation, String dataset, String name, Scope scope) {
        this.annotation = annotation;
        this.dataset = dataset;
        this.name = name;
        this.scope = scope;
    }

    public void init() throws DataSetException {
        checkInvariant(notEmptyOrNull(name), "name must not be empty or null");
        checkInvariant(notEmptyOrNull(dataset), "dataset must not be empty or null");
        checkInvariant(scope != null, "scope must not be null");
        doInit();
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
    public final String id() {
        return dataset + "-" + name;
    }

    @Override
    public final String name() {
        return name;
    }

    @Override
    public final String dataset() {
        return dataset;
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
        return Objects.equals(id(), that.id());
    }

    @Override
    public int hashCode() {
        return Objects.hash(id());
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
                "dataset='" + dataset + '\'' +
                ", name='" + name + '\'' +
                ", scope='" + scope + '\'' +
                '}');
    }
}
