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

package org.failearly.dataset.internal.annotation.filter;

import org.failearly.dataset.internal.annotation.MetaAnnotation;
import org.failearly.dataset.internal.annotation.meta.UsingMetaAnnotation;
import org.failearly.dataset.internal.annotation.AnyOtherAnnotation;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.annotation.Annotation;

import static org.junit.Assert.*;
import static org.hamcrest.Matchers.*;

/**
 * AnnotationFiltersTest contains tests for ... .
 */
public class AnnotationFiltersTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(AnnotationFiltersTest.class);

    @Test
    public void instanceOf() throws Exception {
        // arrange / given
        final AnnotationFilter filter = AnnotationFilters.isInstance(UsingMetaAnnotation.class);

        // assert / then
        assertThat("Correct annotation?", filter.test(resolveAnnotation(0)), is(true));
        assertThat("Wrong annotation?", filter.test(resolveAnnotation(1)), is(false));
    }

    @Test
    public void hasMetaAnnotation() throws Exception {
        // arrange / given
        final AnnotationFilter filter = AnnotationFilters.hasMetaAnnotation(MetaAnnotation.class);

        // assert / then
        assertThat("Correct annotation?", filter.test(resolveAnnotation(0)), is(true));
        assertThat("Wrong annotation?", filter.test(resolveAnnotation(1)), is(false));
    }

    private Annotation resolveAnnotation(int index) {
        final Annotation annotation = MyClass.class.getAnnotations()[index];
        LOGGER.debug("Resolved annotation = {}", annotation);
        return annotation;
    }


    @UsingMetaAnnotation(name="MyClass1")
    @AnyOtherAnnotation
    private static class MyClass {}
}