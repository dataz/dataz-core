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

import org.failearly.common.test.annotations.Subject;
import org.failearly.dataz.internal.template.generator.CsvGeneratorFactory;
import org.failearly.dataz.template.generator.support.test.DevelopmentGeneratorTestBase;
import org.junit.Ignore;

/**
 * Tests for {@link CsvGenerator} and {@link CsvGeneratorFactory}.
 */
@Subject({CsvGenerator.class, CsvGeneratorFactory.class})
@Ignore("TODO")
public class CsvGeneratorTest
    extends DevelopmentGeneratorTestBase {
    public CsvGeneratorTest() {
        super(
//            CsvGenerator.class,
//            CsvGeneratorFactory.class,
//            TestFixture.class
        );
    }

    @CsvGenerator(name= DTON /* TODO: Add more attributes */)
    private static class TestFixture {}
}
