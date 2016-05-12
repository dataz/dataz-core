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

package org.failearly.dataset.template.encoder.support.test;

import org.failearly.dataset.template.encoder.Encoder;
import org.failearly.dataset.template.encoder.support.EncoderFactoryBase;
import org.failearly.dataset.template.support.test.DevelopmentTemplateObjectTestBase;

import java.lang.annotation.Annotation;

/**
 * DevelopmentEncoderTestBase is responsible for ...
 */
@SuppressWarnings("unused")
public abstract class DevelopmentEncoderTestBase<T, R, TOA extends Annotation, TOF extends EncoderFactoryBase, TO extends Encoder<T, R>>
    extends DevelopmentTemplateObjectTestBase<TOA, TOF, TO> {

    protected DevelopmentEncoderTestBase() {
    }

    protected DevelopmentEncoderTestBase(
            Class<TOA> templateObjectAnnotationClass,
            Class<TOF> templateObjectFactoryClass,
            Class<TO> templateObjectClass, Class<?> testFixtureClass
    ) {
        super(templateObjectAnnotationClass, templateObjectFactoryClass, templateObjectClass, testFixtureClass);
    }

    /**
     * Create a {@link Encoder} object from Test Fixture Class using the {@code annotationIndex} assigned TOA.
     * @param  annotationIndex the index of the available annotations.
     * @return the encoder object
     * @throws Exception
     */
    protected final TO createEncoder(int annotationIndex) throws Exception {
        return super.createTemplateObjectFromAnnotation(annotationIndex);
    }
}
