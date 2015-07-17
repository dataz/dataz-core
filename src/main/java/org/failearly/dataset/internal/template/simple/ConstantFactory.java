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

package org.failearly.dataset.internal.template.simple;

import org.failearly.dataset.template.common.TemplateObjectBase;
import org.failearly.dataset.template.common.TemplateObjectFactoryBase;
import org.failearly.dataset.template.simple.Constant;
import org.failearly.dataset.template.common.Scope;
import org.failearly.dataset.template.common.TemplateObject;

/**
 * ConstantGeneratorFactory is responsible for creating of implementation instances for {@link Constant}.
 */
public final class ConstantFactory extends TemplateObjectFactoryBase<Constant> {

    public ConstantFactory() {
        super(Constant.class);
    }

    @Override
    protected TemplateObject doCreate(Constant annotation) {
        return new ConstantImpl(annotation);
    }

    @Override
    protected String doResolveDataSetName(Constant annotation) {
        return annotation.dataset();
    }


    @Override
    protected Scope doResolveScope(Constant annotation) {
        return annotation.scope();
    }

    // Must be public for Velocity!
    public static class ConstantImpl extends TemplateObjectBase {
        private final String value;

        ConstantImpl(Constant annotation) {
            super(annotation, annotation.dataset(), annotation.name(), annotation.scope());
            this.value = annotation.value();
        }

        public String getValue() {
            return value;
        }


        @Override
        public String toString() {
            return getValue();
        }
    }
}
