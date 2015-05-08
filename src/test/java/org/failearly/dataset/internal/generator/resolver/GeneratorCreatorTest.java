/*
 * dataSet - Test Support For Datastores.
 *
 * Copyright (C) 2014-2014 Marko Umek (http://fail-early.com/contact)
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

package org.failearly.dataset.internal.generator.resolver;

import org.failearly.dataset.generator.support.Generator;
import org.failearly.dataset.generator.Limit;
import org.failearly.dataset.generator.support.UnlimitedGenerator;
import org.failearly.dataset.generator.ConstantGenerator;
import org.failearly.dataset.generator.support.LimitedGenerator;
import org.failearly.dataset.internal.generator.standard.ConstantGeneratorFactory;
import org.junit.Test;

import java.lang.annotation.Annotation;

import static org.junit.Assert.*;
import static org.hamcrest.Matchers.*;

/**
 * GeneratorCreatorTest contains tests for ... .
 */
public class GeneratorCreatorTest {
    @Test
    public void createUnlimitedGeneratorInstance() throws Exception {
        // arrange / given
        final GeneratorCreator creator = new GeneratorCreator(new ConstantGeneratorFactory(), getAnnotation(0));


        // act / when
        final Generator generator = creator.createGeneratorInstance();

        // assert / then
        assertThat("Creator DataSet?", creator.dataSet(), is("DS1"));
        assertThat("Generator DataSet?", generator.dataset(), is("DS1"));
        assertThat("Generator name?", generator.name(), is("unlimited"));
        assertThat("Generator?", generator, instanceOf(UnlimitedGenerator.class));
    }

    @Test
    public void createLimitedGeneratorInstance() throws Exception {
        // arrange / given
        final GeneratorCreator creator = new GeneratorCreator(new ConstantGeneratorFactory(), getAnnotation(1));


        // act / when
        final Generator generator = creator.createGeneratorInstance();

        // assert / then
        assertThat("Creator DataSet?", creator.dataSet(), is("DS2"));
        assertThat("Generator DataSet?", generator.dataset(), is("DS2"));
        assertThat("Generator name?", generator.name(), is("limited"));
        assertThat("Generator?", generator, instanceOf(LimitedGenerator.class));
    }

    private Annotation getAnnotation(int annotationNumber) {
        return AnyClass.class.getAnnotationsByType(ConstantGenerator.class)[annotationNumber];
    }


    @ConstantGenerator(name = "unlimited", dataset = "DS1", constant = "Not Important", limit = Limit.UNLIMITED)
    @ConstantGenerator(name = "limited", dataset = "DS2", constant = "Not Important", limit = Limit.LIMITED)
    private final static class AnyClass {}
}