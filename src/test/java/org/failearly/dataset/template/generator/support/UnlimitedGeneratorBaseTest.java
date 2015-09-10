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

package org.failearly.dataset.template.generator.support;

import org.apache.velocity.exception.VelocityException;
import org.failearly.dataset.internal.template.generator.ListGeneratorFactory;
import org.failearly.dataset.template.generator.Limit;
import org.failearly.dataset.template.generator.ListGenerator;
import org.failearly.dataset.template.generator.support.test.GeneratorTestBase;
import org.failearly.dataset.util.ExceptionVerifier;
import org.junit.Test;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

/**
 * UnlimitedGeneratorBaseTest contains tests for {@link UnlimitedGeneratorBase} using {@link ListGenerator}.
 */
public class UnlimitedGeneratorBaseTest extends GeneratorTestBase<String, ListGenerator, ListGeneratorFactory> {

    public UnlimitedGeneratorBaseTest() {
        super(ListGenerator.class, ListGeneratorFactory.class, TestFixture.class);
    }


    @Test
    public void using_external_iterator__should_throw_exception() throws Exception {
        ExceptionVerifier.TestAction action=() -> generate(
                template(TEMPLATE_EXTERNAL_ITERATOR),
                createTemplateObject()
        );
        ExceptionVerifier.on(action).expect(VelocityException.class).expect("Error invoking the method 'iterator' on class 'org.failearly.dataset.internal.template.generator.decorator.UnlimitedGeneratorDecorator'").verify();
    }

    @Test
    public void using_internal_iterator_with_max_limit__should_generate_all_values() throws Exception {
        // act / when
        final String generated=generate(
                template(TEMPLATE_INTERNAL_ITERATOR, 3),
                createTemplateObject()
        );

        // assert / then
        assertThat(generated, is(
            "(1) next=val1,last=val1/" +
                "(2) next=val2,last=val2/" +
                "(3) next=val3,last=val3/"
        ));
    }

    @Test
    public void reseting_internal_iterator__should_repeat_the_same_value() throws Exception {
        // act / when
        final String generated=generate(
                template(TEMPLATE_INTERNAL_ITERATOR_USING_RESET, 4),
                createTemplateObject()
        );

        // assert / then
        assertThat(generated, is(
            "(1) next=val1/" +
                "(2) next=val1/" +
                "(3) next=val1/" +
                "(4) next=val1/"
        ));
    }

    @ListGenerator(name=TEMPLATE_OBJECT_NAME, dataset=DATASET, values={"val1", "val2", "val3"}, limit=Limit.UNLIMITED)
    private static class TestFixture {
    }

}