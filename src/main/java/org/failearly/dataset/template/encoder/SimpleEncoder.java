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

package org.failearly.dataset.template.encoder;

import org.failearly.dataset.internal.template.encoder.SimpleEncoderFactory;
import org.failearly.dataset.template.common.TemplateObjectFactoryDefinition;

import java.lang.annotation.*;

/**
 * SimpleEncoder is responsible for ...
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@TemplateObjectFactoryDefinition(factory = SimpleEncoderFactory.class)
@Documented
@Repeatable(SimpleEncoder.SimpleEncoders.class)
public @interface SimpleEncoder {


    /**
     * Containing Annotation Type.
     *
     * Remark: This will be used by Java8 compiler.
     *
     * @see java.lang.annotation.Repeatable
     */
    @Target({ElementType.METHOD, ElementType.TYPE})
    @Retention(RetentionPolicy.RUNTIME)
    @Documented
    @interface SimpleEncoders {
        SimpleEncoder[] value();
    }
}
