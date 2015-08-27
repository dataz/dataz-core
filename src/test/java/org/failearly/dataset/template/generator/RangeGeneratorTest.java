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

package org.failearly.dataset.template.generator;

import org.failearly.dataset.internal.template.generator.RangeGeneratorFactory;
import org.failearly.dataset.template.InvariantViolationException;
import org.failearly.dataset.template.generator.support.test.GeneratorTestBase;
import org.failearly.dataset.util.ExceptionVerifier;
import org.junit.Test;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

/**
 * RangeGeneratorTest contains tests for {@link RangeGenerator}.
 */
public class RangeGeneratorTest extends GeneratorTestBase<Integer, RangeGenerator, RangeGeneratorFactory> {

    private static final int ZERO_TO_FOUR=0;
    private static final int WITH_STEP=1;
    private static final int FROM_EQUALS_TO=2;
    private static final int INVALID_FROM_TO=3;
    private static final int INVALID_STEP=4;

    public RangeGeneratorTest() {
        super(RangeGenerator.class, RangeGeneratorFactory.class, TestFixture.class);
    }

    @Test
    public void external_iterator_on_range_0__4__should_generate_0_to_4() throws Exception {
        // act / when
        final String generated=generate(
                template(TEMPLATE_EXTERNAL_ITERATOR),
                createGenerator(ZERO_TO_FOUR)
        );

        // assert / then
        assertThat(generated, is("0;1;2;3;4;"));
    }

    @Test
    public void internal_iterator_on_range_0__4__should_generate_0_to_4() throws Exception {
        // act / when
        final String generated=generate(
                template(TEMPLATE_INTERNAL_ITERATOR, 5),
                createGenerator(ZERO_TO_FOUR)
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
                createGenerator(WITH_STEP)
        );

        // assert / then
        assertThat(generated, is("1;4;7;10;"));
    }

    @Test
    public void range_with_from_equals_to__should_generate_given_value() throws Exception {
        // act / when
        final String generated=generate(
                template(TEMPLATE_EXTERNAL_ITERATOR),
                createGenerator(FROM_EQUALS_TO)
        );

        // assert / then
        assertThat(generated, is("7;"));
    }

    @Test
    public void invalid_from_to__should_throw_exception() throws Exception {
        // act / when
        ExceptionVerifier.on(() -> createGenerator(INVALID_FROM_TO))
                .expect(InvariantViolationException.class)
                .expect(
                        "Invariant of RangeGenerator has been violated: from <= to!" +
                                "\nCurrent annotation is '" + getDeclaredAnnotation(INVALID_FROM_TO) + "'"
                )
                .verify();
    }

    @Test
    public void invalid_step__should_throw_exception() throws Exception {
        // act / when
        ExceptionVerifier.on(() -> createGenerator(INVALID_STEP))
                .expect(InvariantViolationException.class)
                .expect("Invariant of RangeGenerator has been violated: step > 0!" +
                        "\nCurrent annotation is '" + getDeclaredAnnotation(INVALID_STEP) + "'")
                .verify();
    }


    @RangeGenerator(name=TEMPLATE_OBJECT_NAME, from=0, to=4)
    @RangeGenerator(name=TEMPLATE_OBJECT_NAME, from=1, to=10, step=3)
    @RangeGenerator(name=TEMPLATE_OBJECT_NAME, from=7, to=7)
    @RangeGenerator(name=TEMPLATE_OBJECT_NAME, from=0, to=-1)
    @RangeGenerator(name=TEMPLATE_OBJECT_NAME, from=0, to=4, step=0)
    private static class TestFixture {
    }
}