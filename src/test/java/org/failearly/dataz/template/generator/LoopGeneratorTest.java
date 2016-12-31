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

package org.failearly.dataz.template.generator;

import org.failearly.common.test.ExceptionVerifier;
import org.failearly.common.test.annotations.Subject;
import org.failearly.dataz.internal.template.generator.LoopGeneratorFactory;
import org.failearly.dataz.internal.template.generator.LoopGeneratorFactory.LoopGeneratorImpl;
import org.failearly.dataz.template.InvariantViolationException;
import org.failearly.dataz.template.generator.support.test.LimitedGeneratorTestBase;
import org.junit.Test;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

/**
 * LoopGeneratorTest contains tests for {@link LoopGenerator}.
 */
@Subject({LoopGenerator.class, LoopGeneratorFactory.class, LoopGeneratorImpl.class})
public class LoopGeneratorTest extends LimitedGeneratorTestBase<Integer, LoopGenerator, LoopGeneratorFactory, LoopGeneratorImpl> {

    private static final int FOUR_TIMES_LOOP=0;
    private static final int ONE_TIME_LOOP=1;
    private static final int NO_LOOP=2;

    public LoopGeneratorTest() {
        super(LoopGenerator.class, LoopGeneratorFactory.class, LoopGeneratorImpl.class, TestFixture.class);
    }

    @Test
    public void external_iterator_on_loop_4__should_generate_1_to_4() throws Exception {
        // act / when
        final String generated=generate(
                template(TEMPLATE_EXTERNAL_ITERATOR),
                super.createTemplateObjectFromAnnotation(FOUR_TIMES_LOOP)
        );

        // assert / then
        assertThat(generated, is("1;2;3;4;"));
    }

    @Test
    public void internal_iterator_on_loop_4__should_generate_1_to_4() throws Exception {
        // act / when
        final String generated=generate(
                template(TEMPLATE_INTERNAL_ITERATOR, 4),
                createTemplateObjectFromAnnotation(FOUR_TIMES_LOOP)
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
                createTemplateObjectFromAnnotation(ONE_TIME_LOOP)
        );

        // assert / then
        assertThat(generated, is("1;"));
    }

    @Test
    public void no_loop__should_throw_exception() throws Exception {
        // act / when
        ExceptionVerifier.on(() -> createTemplateObjectFromAnnotation(NO_LOOP))
                .expect(InvariantViolationException.class)
                .expect(
                    "Invariant of LoopGenerator has been violated: size >= 1!" +
                    "\nCurrent annotation is " +
                    "'@org.failearly.dataz.template.generator.LoopGenerator(scope=DEFAULT, datasets=[], name=TO, size=0)'"
                )
                .verify();
    }

    @LoopGenerator(name= DTON, size=4)
    @LoopGenerator(name= DTON, size=1)
    @LoopGenerator(name= DTON, size=0)
    private static class TestFixture {
    }
}