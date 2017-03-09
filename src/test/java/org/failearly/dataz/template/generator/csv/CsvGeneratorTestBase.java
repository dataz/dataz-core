/*
 * dataZ - Test Support For Data Stores.
 *
 * Copyright (C) 2014-2017 'Marko Umek' (http://fail-early.com)
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
package org.failearly.dataz.template.generator.csv;

import org.failearly.common.test.annotations.Subject;
import org.failearly.dataz.internal.template.generator.csv.CsvGeneratorFactory;
import org.failearly.dataz.internal.template.generator.csv.CsvGeneratorImpl;
import org.failearly.dataz.template.generator.support.test.LimitedGeneratorTestBase;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

/**
 * CsvGeneratorTestBase is base class for all Test's of {@link CsvGenerator}
 */
@Subject({CsvGenerator.class, CsvGeneratorFactory.class, CsvGenerator.class})
abstract class CsvGeneratorTestBase extends LimitedGeneratorTestBase<CsvRecord, CsvGenerator, CsvGeneratorFactory, CsvGeneratorImpl> {
    CsvGeneratorTestBase(Class<?> testFixtureClass) {
        super(
            CsvGenerator.class, // TOA
            CsvGeneratorFactory.class, // TOF
            CsvGeneratorImpl.class,  // TO
            testFixtureClass
        );
    }

    static String templateAccessByColumnNames(String... columnNames) {
        return "#foreach($g in $TO)$g.recordNumber: '$g.getValue('${0}')', '$g.getValue('${1}')'\n#end" //
            .replace("${0}", columnNames[0])
            .replace("${1}", columnNames[1]);
    }

    final void assertCsvGenerator(final String annotationHolderName, final String template, String expectedGeneratedResult) {
        // act / when
        final String generated = generate(
            template,
            createTemplateObjectFromAnnotation(annotationHolderName)
        );

        // assert / then
        assertThat(generated, is(expectedGeneratedResult));
    }
}
