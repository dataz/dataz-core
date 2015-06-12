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

package org.failearly.dataset.internal.template.engine.velocity;

import org.failearly.dataset.template.generator.ConstantGenerator;
import org.failearly.dataset.template.generator.Limit;
import org.failearly.dataset.internal.template.TemplateEngines;
import org.failearly.dataset.template.TemplateEngine;
import org.failearly.dataset.template.TemplateEngineTestBase;
import org.failearly.dataset.test.TestUtils;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;


/**
 * Tests for {@link org.failearly.dataset.internal.template.engine.velocity.VelocityTemplateEngine}.
 */
public class VelocityTemplateEngineTest extends TemplateEngineTestBase {

    private static final String TEMPLATE = "$unlimited.next()\n$limited.next()\n";

    private static final String ANY_TEMPLATE_RESOURCE_NAME = "/any/path/to/dataset-resource.suffix.vm";

    private TemplateEngine templateEngine;

    private static InputStream templateInputStream() {
        return new ByteArrayInputStream(TEMPLATE.getBytes());
    }

    @Before
    public void setUp() throws Exception {
        templateEngine = TemplateEngines.createTemplateEngine();
    }

    @Test
    public void mergeMultipleGenerators_different_datasets() throws Exception {
        // act / when
        final File generatedFile = templateEngine.generate(
                    templateInputStream(),
                    ANY_TEMPLATE_RESOURCE_NAME,
                    resolveTemplateObjects(GeneratorDifferentDataSets.class).filterByDataSet(UNLIMITED_DATA_SET)
            );

        // assert / then
        assertThat("Use only UNLIMITED_DS dataset?", TestUtils.fileToString(generatedFile), is("unlimited constant\n$limited.next()\n"));
    }

    @Test
    public void mergeMultipleGenerators_shared_dataset() throws Exception {
        // act / when
        final File generatedFile = templateEngine.generate(
                templateInputStream(),
                ANY_TEMPLATE_RESOURCE_NAME,
                resolveTemplateObjects(GeneratorsSharedDataSet.class).filterByDataSet(SHARED_DATA_SET)
        );

        // assert / then
        assertThat("Use both generators?", TestUtils.fileToString(generatedFile), Matchers.is("unlimited constant\nlimited constant\n"));
    }


    private static class MyClass extends GeneratorsSharedDataSet {
        @ConstantGenerator(name = "unlimited", dataset = UNLIMITED_DATA_SET, constant = "override unlimited", limit = Limit.UNLIMITED)
        @ConstantGenerator(name = "limited", dataset = SHARED_DATA_SET, constant = "override limited", limit = Limit.LIMITED)
        public void anyTestMethod() {
        }
    }
}
