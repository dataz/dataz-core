/*
 * dataZ - Test Support For Data Stores.
 *
 * Copyright (C) 2014-2016 marko (http://fail-early.com)
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
 *
 */

package org.failearly.dataset.template.encoder.support.test;

import org.failearly.dataset.template.encoder.Encoder;
import org.failearly.dataset.template.encoder.support.EncoderFactoryBase;
import org.failearly.dataset.template.support.test.DevelopmentTemplateObjectTestBase;
import org.failearly.dataset.template.support.test.mb.DevelopmentMessageBuilders;
import org.failearly.dataset.template.support.test.mb.TemplateObjectMessageBuilder;
import org.failearly.common.test.mb.MessageBuilder;

import java.lang.annotation.Annotation;

/**
 * DevelopmentEncoderTestBase is responsible for ...
 */
@SuppressWarnings("unused")
public abstract class DevelopmentEncoderTestBase<R, T, A extends Annotation, EF extends EncoderFactoryBase>
    extends DevelopmentTemplateObjectTestBase<A, EF> {

    protected DevelopmentEncoderTestBase() {
    }

    protected DevelopmentEncoderTestBase(
            Class<A> templateObjectAnnotationClass,
            Class<EF> templateObjectFactoryClass,
            Class<?> testFixtureClass
        ) {
        super(templateObjectAnnotationClass, templateObjectFactoryClass, testFixtureClass);
    }

    @Override
    protected String getTemplateObjectFactoryBaseClass() {
        return EncoderFactoryBase.class.getSimpleName();
    }

    @Override
    protected String getTemplateObjectName() {
        return "Encoder";
    }

    @Override
    protected String getTemplateObjectType() {
        return Encoder.class.getSimpleName();
    }

    @Override
    protected String[] getAdditionalGenerics() {
        return toTypeNames(String.class, String.class);
    }

    @Override
    protected TemplateObjectMessageBuilder missingTemplateObjectMessage(MessageBuilder mb) {
        return DevelopmentMessageBuilders.missingEncoderMessage(mb);
    }

    @SuppressWarnings("unchecked")
    protected Encoder<T, R> createEncoder(int annotationNumber) throws Exception {
        return (Encoder<T, R>) super.createTemplateObjectFromAnnotationIndex(annotationNumber);
    }


}
