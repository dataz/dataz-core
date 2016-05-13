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

import org.failearly.common.annotations.Tests;
import org.failearly.dataz.template.Scope;
import org.failearly.dataz.template.TemplateObject;
import org.failearly.dataz.template.TemplateObjectBase;
import org.failearly.dataz.template.TemplateObjectFactoryBase;
import org.failearly.dataz.template.simple.Constant;

/**
 * ConstantGeneratorFactory is responsible for creating of implementation instances for {@link Constant}.
 */
@Tests("ConstantTest")
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
    @Tests("ConstantTest")
    public static class ConstantImpl extends TemplateObjectBase {
        private final String value;

        ConstantImpl(Constant annotation) {
            super(annotation);
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
