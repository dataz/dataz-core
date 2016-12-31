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

package org.failearly.dataz.internal.template;


import org.failearly.dataz.template.Scope;
import org.failearly.dataz.template.TemplateObject;
import org.failearly.dataz.template.TemplateObjectAnnotationContext;
import org.failearly.dataz.template.TemplateObjectFactory;

import java.lang.annotation.Annotation;
import java.util.Objects;
import java.util.Set;

/**
 * TemplateObjectCreator is helper class which holds a specific annotation and creates the actually {@link TemplateObject} from the annotation using
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
