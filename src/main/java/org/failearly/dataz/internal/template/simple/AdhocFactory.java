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

package org.failearly.dataz.internal.template.simple;

import org.failearly.dataz.template.Scope;
import org.failearly.dataz.template.TemplateObject;
import org.failearly.dataz.template.TemplateObjectAnnotationContext;
import org.failearly.dataz.template.TemplateObjectFactoryBase;
import org.failearly.dataz.template.simple.Adhoc;
import org.failearly.common.classutils.ObjectCreator;

/**
 * AdhocFactory creates {@link org.failearly.dataz.template.simple.Adhoc.AdhocTemplateObject} from {@link Adhoc} template object annotation.
 */
public final class AdhocFactory extends TemplateObjectFactoryBase<Adhoc> {
    public AdhocFactory() {
        super(Adhoc.class);
    }

    @Override
    protected TemplateObject doCreate(TemplateObjectAnnotationContext context, Adhoc annotation) {
        final Adhoc.AdhocTemplateObject templateObjectPrototype = ObjectCreator.createInstance(annotation.value());
        return templateObjectPrototype.create(context, annotation);
    }

    @Override
    protected String[] doResolveDataSetNames(Adhoc annotation) {
        return annotation.datasets();
    }

    @Override
    protected Scope doResolveScope(Adhoc annotation) {
        return annotation.scope();
    }

    @Override
    protected String doResolveName(Adhoc annotation) {
        return annotation.name();
    }
}

