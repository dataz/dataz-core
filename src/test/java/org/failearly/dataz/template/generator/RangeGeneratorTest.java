/*
 * dataZ - Test Support For Data Stores.
 *
 * Copyright 2014-2017 the original author or authors.
 *
 * All rights reserved. This program and the accompanying materials are
 * made available under the terms of the Eclipse Public License v1.0 which
 * accompanies this distribution and is available at
 *
 * http://www.eclipse.org/legal/epl-v10.html
 */

package org.failearly.dataz.template.generator;

import org.failearly.dataz.common.test.ExceptionVerifier;
import org.failearly.dataz.common.test.annotations.Subject;
import org.failearly.dataz.internal.template.generator.RangeGeneratorFactory;
import org.failearly.dataz.internal.template.generator.RangeGeneratorFactory.RangeGeneratorImpl;
import org.failearly.dataz.template.InvariantViolationException;
import org.failearly.dataz.template.generator.support.test.LimitedGeneratorTestBase;
import org.junit.Test;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

/**
 * RangeGeneratorTest contains tests for {@link RangeGenerator}.
 */
@Subject({RangeGenerator.class, RangeGeneratorFactory.class, RangeGeneratorImpl.class})
public class RangeGeneratorTest extends LimitedGeneratorTestBase<Integer, RangeGenerator, RangeGeneratorFactory, RangeGeneratorImpl> {

    private static final int ZERO_TO_FOUR=0;
    private static final int WITH_STEP=1;
    private static final int FROM_EQUALS_TO=2;
    private static final int INVALID_FROM_TO=3;
    private static final int INVALID_STEP=4;

    public RangeGeneratorTest() {
        super(RangeGenerator.class, RangeGeneratorFactory.class, RangeGeneratorImpl.class, TestFixture.class);
    }

    @Test
    public void external_iterator_on_range_0__4__should_generate_0_to_4() throws Exception {
        // act / when
        final String generated=generate(
                template(TEMPLATE_EXTERNAL_ITERATOR),
                super.createTemplateObjectFromAnnotation(ZERO_TO_FOUR)
        );

        // assert / then
        assertThat(generated, is("0;1;2;3;4;"));
    }

    @Test
    public void internal_iterator_on_range_0__4__should_generate_0_to_4() throws Exception {
        // act / when
        final String generated=generate(
                template(TEMPLATE_INTERNAL_ITERATOR, 5),
                super.createTemplateObjectFromAnnotation(ZERO_TO_FOUR)
        );

        // assert / then
        assertThat(generated, is(
            "(1) next=0,last=0/" +
                "(2) next=1,last=1/" +
                "(3) next=2,last=2/" +
                "(4) next=3,last=3/" +
                "(5) next=4,last=4/"
        ));
    }

    @Test
    public void range_with_none_default_step__should_have_appropriate_value_gaps() throws Exception {
        // act / when
        final String generated=generate(
                template(TEMPLATE_EXTERNAL_ITERATOR),
                super.createTemplateObjectFromAnnotation(WITH_STEP)
        );

        // assert / then
        assertThat(generated, is("1;4;7;10;"));
    }

    @Test
    public void range_with_from_equals_to__should_generate_given_value() throws Exception {
        // act / when
        final String generated=generate(
                template(TEMPLATE_EXTERNAL_ITERATOR),
                super.createTemplateObjectFromAnnotation(FROM_EQUALS_TO)
        );

        // assert / then
        assertThat(generated, is("7;"));
    }

    @Test
    public void invalid_from_to__should_throw_exception() throws Exception {
        // act / when
        ExceptionVerifier.on(() -> super.createTemplateObjectFromAnnotation(INVALID_FROM_TO))
                .expect(InvariantViolationException.class)
                .expect(
                        "Invariant of RangeGenerator has been violated: from <= to!" +
                                "\nCurrent impl is '" + resolveTestFixtureAnnotation(INVALID_FROM_TO) + "'"
                )
                .verify();
    }

    @Test
    public void invalid_step__should_throw_exception() throws Exception {
        // act / when
        ExceptionVerifier.on(() -> super.createTemplateObjectFromAnnotation(INVALID_STEP))
                .expect(InvariantViolationException.class)
                .expect("Invariant of RangeGenerator has been violated: step > 0!" +
                        "\nCurrent impl is '" + resolveTestFixtureAnnotation(INVALID_STEP) + "'")
                .verify();
    }


    @RangeGenerator(name= DTON, from=0, to=4)
    @RangeGenerator(name= DTON, from=1, to=10, step=3)
    @RangeGenerator(name= DTON, from=7, to=7)
    @RangeGenerator(name= DTON, from=0, to=-1)
    @RangeGenerator(name= DTON, from=0, to=4, step=0)
    private static class TestFixture {
    }
}