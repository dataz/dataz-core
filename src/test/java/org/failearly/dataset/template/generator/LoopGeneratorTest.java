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

import org.failearly.dataset.internal.template.generator.LoopGeneratorFactory;
import org.failearly.dataset.template.InvariantViolationException;
import org.failearly.dataset.template.generator.support.test.GeneratorTestBase;
import org.failearly.dataset.util.ExceptionVerifier;
import org.junit.Test;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

/**
 * LoopGeneratorTest contains tests for {@link LoopGenerator}.
 */
public class LoopGeneratorTest extends GeneratorTestBase<Integer, LoopGenerator, LoopGeneratorFactory> {

    private static final int FOUR_TIMES_LOOP=0;
    private static final int ONE_TIME_LOOP=1;
    private static final int NO_LOOP=2;

    public LoopGeneratorTest() {
        super(LoopGenerator.class, LoopGeneratorFactory.class, TestFixture.class);
    }

    @Test
    public void external_iterator_on_loop_4__should_generate_1_to_4() throws Exception {
        // act / when
        final String generated=generate(
                template(TEMPLATE_EXTERNAL_ITERATOR),
                createGenerator(FOUR_TIMES_LOOP)
        );

        // assert / then
        assertThat(generated, is("1;2;3;4;"));
    }

    @Test
    public void internal_iterator_on_loop_4__should_generate_1_to_4() throws Exception {
        // act / when
        final String generated=generate(
                template(TEMPLATE_INTERNAL_ITERATOR, 4),
                createGenerator(FOUR_TIMES_LOOP)
        );

        // assert / then
        assertThat(generated, is(
            "(1) next=1,last=1/" +
                "(2) next=2,last=2/" +
                "(3) next=3,last=3/" +
                "(4) next=4,last=4/"
        ));
    }

    @Test
    public void loop_1__should_generate_only_1() throws Exception {
        // act / when
        final String generated=generate(
                template(TEMPLATE_EXTERNAL_ITERATOR),
                createGenerator(ONE_TIME_LOOP)
        );

        // assert / then
        assertThat(generated, is("1;"));
    }

    @Test
    public void no_loop__should_throw_exception() throws Exception {
        // act / when
        ExceptionVerifier.on(() -> createGenerator(NO_LOOP)).expect(InvariantViolationException.class).expect("Invariant of LoopGenerator has been violated: size >= 1!" +
                "\nCurrent annotation is " +
                "'@org.failearly.dataset.template.generator.LoopGenerator(dataset=<dataset>, scope=DEFAULT, name=TO, size=0)'").verify();
    }

    @LoopGenerator(name=TEMPLATE_OBJECT_NAME, size=4)
    @LoopGenerator(name=TEMPLATE_OBJECT_NAME, size=1)
    @LoopGenerator(name=TEMPLATE_OBJECT_NAME, size=0)
    private static class TestFixture {
    }
}