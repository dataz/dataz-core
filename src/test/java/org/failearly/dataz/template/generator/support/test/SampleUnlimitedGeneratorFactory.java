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
package org.failearly.dataz.template.generator.support.test;

import org.failearly.common.annotations.Tests;
import org.failearly.dataz.template.Scope;
import org.failearly.dataz.template.TemplateObject;
import org.failearly.dataz.template.generator.support.GeneratorFactoryBase;
import org.failearly.dataz.template.generator.support.UnlimitedGeneratorBase;

import java.lang.reflect.AnnotatedElement;
import java.util.Collections;
import java.util.Iterator;

/**
 * SampleUnlimitedGeneratorFactory creates a {@link SampleUnlimitedGeneratorImpl} from {@link SampleUnlimitedGenerator}.
 */
@Tests("SampleUnlimitedGeneratorTest")
public class SampleUnlimitedGeneratorFactory extends GeneratorFactoryBase<Integer,SampleUnlimitedGenerator> {
    public SampleUnlimitedGeneratorFactory() {
        super(SampleUnlimitedGenerator.class);
    }

    @Override
    protected String doResolveName(SampleUnlimitedGenerator annotation) {
        return annotation.name();
    }

    @Override
    protected String[] doResolveDataSetNames(SampleUnlimitedGenerator annotation) {
        return annotation.datasets();
    }

    @Override
    protected Scope doResolveScope(SampleUnlimitedGenerator annotation) {
        return annotation.scope();
    }

    @Override
    protected TemplateObject doCreate(AnnotatedElement annotatedElement, SampleUnlimitedGenerator annotation) {
        return doCreateGenerator(annotatedElement, annotation, annotation.limit());
    }

    @Override
    protected UnlimitedGeneratorBase<Integer> doCreateUnlimitedGenerator(AnnotatedElement annotatedElement, SampleUnlimitedGenerator annotation, Integer limitValue) {
        return new SampleUnlimitedGeneratorImpl(annotation);
    }

    // Must be public for Velocity!
    @Tests("SampleUnlimitedGeneratorTest")
    public static class SampleUnlimitedGeneratorImpl extends UnlimitedGeneratorBase<Integer> {
        SampleUnlimitedGeneratorImpl(SampleUnlimitedGenerator annotation) {
            super(annotation);
            // TODO: For each (not standard) annotation element there should be an appropriate field assignment.
        }

        @Override
        public Iterator<Integer> createIterator() {
            // TODO: Implement SampleUnlimitedGeneratorImpl#createIterator
            return Collections.emptyIterator();
        }
    }
}