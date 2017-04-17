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
package org.failearly.dataz.template.generator.csv;

import org.failearly.dataz.common.test.annotations.Subject;
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
