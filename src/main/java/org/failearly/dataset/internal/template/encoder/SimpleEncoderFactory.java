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

package org.failearly.dataset.internal.template.encoder;

import org.failearly.dataset.template.Scope;
import org.failearly.dataset.template.TemplateObject;
import org.failearly.dataset.template.TemplateObjectFactoryBase;
import org.failearly.dataset.template.encoder.Encoder;
import org.failearly.dataset.template.encoder.SimpleEncoder;
import org.failearly.dataset.template.encoder.support.EncoderFactoryBase;

/**
 * SimpleEncoderFactory is responsible for ...
 */
public final class SimpleEncoderFactory extends EncoderFactoryBase<SimpleEncoder> {
    public SimpleEncoderFactory() {
        super(SimpleEncoder.class);
    }

    @Override
    protected Encoder doCreate(SimpleEncoder annotation) {
        return null;
    }

    @Override
    protected String doResolveDataSetName(SimpleEncoder annotation) {
        return annotation.dataset();
    }

    @Override
    protected Scope doResolveScope(SimpleEncoder annotation) {
        return annotation.scope();
    }
}
