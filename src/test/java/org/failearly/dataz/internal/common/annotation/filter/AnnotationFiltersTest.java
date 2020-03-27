/*
 * dataZ - Test Support For Data Stores.
 *
 * Copyright 2014-2020 the original author or authors.
 *
 * All rights reserved. This program and the accompanying materials are
 * made available under the terms of the Eclipse Public License v2.0 which
 * accompanies this distribution and is available at
 *
 * https://www.eclipse.org/legal/epl-v20.html
 */

package org.failearly.dataz.internal.common.annotation.filter;

import org.failearly.dataz.internal.common.annotation.test.MetaAnnotation;
import org.failearly.dataz.internal.common.annotation.test.UsingMetaAnnotation;
import org.failearly.dataz.internal.common.annotation.test.AnyOtherAnnotation;
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
        assertThat("Correct impl?", filter.test(resolveAnnotation(0)), is(true));
        assertThat("Wrong impl?", filter.test(resolveAnnotation(1)), is(false));
    }

    @Test
    public void hasMetaAnnotation() throws Exception {
        // arrange / given
        final AnnotationFilter filter = AnnotationFilters.hasMetaAnnotation(MetaAnnotation.class);

        // assert / then
        assertThat("Correct impl?", filter.test(resolveAnnotation(0)), is(true));
        assertThat("Wrong impl?", filter.test(resolveAnnotation(1)), is(false));
    }

    private Annotation resolveAnnotation(int index) {
        final Annotation annotation = MyClass.class.getAnnotations()[index];
        LOGGER.debug("Resolved impl = {}", annotation);
        return annotation;
    }


    @UsingMetaAnnotation(name="MyClass1")
    @AnyOtherAnnotation
    private static class MyClass {}
}