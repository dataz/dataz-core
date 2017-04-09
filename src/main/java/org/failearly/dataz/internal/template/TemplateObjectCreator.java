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

package org.failearly.dataz.internal.template;


import org.failearly.dataz.template.Scope;
import org.failearly.dataz.template.TemplateObject;
import org.failearly.dataz.template.TemplateObjectAnnotationContext;
import org.failearly.dataz.template.TemplateObjectFactory;

import java.lang.annotation.Annotation;
import java.util.Objects;
import java.util.Set;

/**
 * TemplateObjectCreator is helper class which holds a specific impl and creates the actually {@link TemplateObject} from the impl using
 * the associated {@link TemplateObjectFactory}.
 */
final class TemplateObjectCreator {
    protected final TemplateObjectFactory factory;

    private final TemplateObjectAnnotationContext context;
    private final Annotation annotation;

    TemplateObjectCreator(TemplateObjectFactory factory, Annotation annotation, TemplateObjectAnnotationContext context) {
        this.annotation = annotation;
        this.factory = factory;
        this.context = context;
    }

    Annotation getAnnotation() {
        return annotation;
    }

    String getName() { return factory.resolveName(annotation); }

    Set<String> getDataSetNames() {
        return factory.resolveDataSetNames(getAnnotation());
    }

    TemplateObject createTemplateObjectInstance() {
        return factory.create(context, getAnnotation());
    }

    boolean hasScope(Scope scope) {
        return factory.resolveScope(annotation)==scope;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof TemplateObjectCreator))
            return false;
        final TemplateObjectCreator that = (TemplateObjectCreator) o;
        return Objects.equals(getName(), that.getName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getName());
    }
}
