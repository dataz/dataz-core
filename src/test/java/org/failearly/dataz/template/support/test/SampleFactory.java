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

package org.failearly.dataz.template.support.test;

import org.failearly.common.annotations.Tests;
import org.failearly.dataz.template.Scope;
import org.failearly.dataz.template.TemplateObject;
import org.failearly.dataz.template.TemplateObjectBase;
import org.failearly.dataz.template.TemplateObjectFactoryBase;

import java.lang.reflect.AnnotatedElement;

/**
 * SampleFactory creates a {@link TemplateObject} from {@link Sample}.
 */
@Tests("SampleTest")
public class SampleFactory extends TemplateObjectFactoryBase<Sample> {
    public SampleFactory() {
        super(Sample.class);
    }

    @Override
    protected TemplateObject doCreate(AnnotatedElement annotatedElement, Sample annotation) {
        return new SampleImpl(annotation);
    }

    @Override
    protected String doResolveName(Sample annotation) {
        return annotation.name();
    }

    @Override
    protected String[] doResolveDataSetNames(Sample annotation) {
        return annotation.datasets();
    }

    @Override
    protected Scope doResolveScope(Sample annotation) {
        return annotation.scope();
    }

    // Must be public for Velocity!
    @Tests("SampleTest")
    public static class SampleImpl extends TemplateObjectBase {
        SampleImpl(Sample annotation) {
            super(annotation);
            // TODO: For each (not standard) annotation element there should be an appropriate field assignment.
        }

        public String sample() {
            return super.getAnnotation(Sample.class).sample();
        }

        // TODO: Place here your TO's functionality
    }

}
