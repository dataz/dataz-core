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

package org.failearly.dataset.internal.template.generator.standard;

import org.failearly.dataset.template.generator.support.Generator;
import org.failearly.dataset.template.generator.LoopGenerator;
import org.failearly.dataset.internal.template.generator.GeneratorTestBase;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.hamcrest.Matchers.*;

/**
 * LoopGeneratorFactoryTest contains tests for ... .
 */
public class LoopGeneratorFactoryTest extends GeneratorTestBase<Integer, LoopGenerator, LoopGeneratorFactory> {

    public LoopGeneratorFactoryTest() {
        super(LoopGeneratorFactory.class, LoopGenerator.class);
    }

    @Test
    public void loopGenerator() throws Exception {
        final Generator<Integer> generator=defaultGenerator();
        assertThat("Expected values?", generator, contains(1, 2, 3, 4));
    }

    @Test
    public void loopGenerator_size_1() throws Exception {
        final Generator<Integer> generator=createGenerator(TestFixture.class, 1);
        assertThat("Expected values?", generator, contains(1));
    }

    @Override
    protected Generator<Integer> defaultGenerator() throws Exception {
        return createGenerator(TestFixture.class);
    }


    @LoopGenerator(name="LG", dataset = "DS", size = 4)
    @LoopGenerator(name="LG", dataset = "DS", size = 1)
    private static class TestFixture {}
}