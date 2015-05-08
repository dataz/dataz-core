/*
 * dataSet - Test Support For Datastores.
 *
 * Copyright (C) 2014-2014 Marko Umek (http://fail-early.com/contact)
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
package org.failearly.dataset.internal.template.velocity;

import org.failearly.dataset.generator.ConstantGenerator;
import org.failearly.dataset.generator.Limit;
import org.failearly.dataset.internal.template.TemplateEngines;
import org.failearly.dataset.template.TemplateEngine;
import org.failearly.dataset.template.TemplateEngineTestBase;
import org.failearly.dataset.test.TestUtils;
import org.hamcrest.Matchers;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import static org.hamcrest.MatcherAssert.assertThat;


/**
 * Tests for {@link org.failearly.dataset.internal.template.velocity.VelocityTemplateEngine}.
 */
public class VelocityTemplateEngineTest extends TemplateEngineTestBase {

    private static final String TEMPLATE = "$unlimited.next()\n$limited.next()\n";

    @Test
    public void mergeMultipleGenerators_different_datasets() throws Exception {
        // arrange / given
        final TemplateEngine templateEngine = TemplateEngines.createTemplateEngine(
                "UNLIMITED_DS",
                "/any/path/to/dataset-resource.suffix.vm",
                TemplateEngineTestBase.generatorCreators(GeneratorDifferentDataSets.class));

        // act / when
        final InputStream mergedInputStream = templateEngine.mergeToInputStream(new ByteArrayInputStream(TEMPLATE.getBytes()));

        // assert / then
        assertThat("Use only UNLIMITED_DS dataset?", TestUtils.inputStreamToString(mergedInputStream), Matchers.is("unlimited constant\n$limited.next()\n"));
    }

    @Test
    public void mergeMultipleGenerators_shared_dataset() throws Exception {
        // arrange / given
        final TemplateEngine templateEngine = TemplateEngines.createTemplateEngine(
                "SHARED_DS",
                "/any/path/to/dataset-resource.suffix.vm",
                TemplateEngineTestBase.generatorCreators(GeneratorsSharedDataSet.class));

        // act / when
        final InputStream mergedInputStream = templateEngine.mergeToInputStream(new ByteArrayInputStream(TEMPLATE.getBytes()));

        // assert / then
        assertThat("Use both generators?", TestUtils.inputStreamToString(mergedInputStream), Matchers.is("unlimited constant\nlimited constant\n"));
    }


    @Test
    public void mergeMultipleGenerators_duplicated_names() throws Exception {
        // arrange / given
        final TemplateEngine templateEngine = TemplateEngines.createTemplateEngine(
                "SHARED_DS",
                "/any/path/to/dataset-resource.suffix.vm",
                TemplateEngineTestBase.generatorCreators(MyClass.class));

        // act / when
        final InputStream mergedInputStream = templateEngine.mergeToInputStream(new ByteArrayInputStream(TEMPLATE.getBytes()));

        // assert / then
        assertThat("Use overridden generators?", TestUtils.inputStreamToString(mergedInputStream), Matchers.is("override unlimited\noverride limited\n"));
    }


    private static class MyClass extends GeneratorsSharedDataSet {
        @ConstantGenerator(name = "unlimited", dataset = "SHARED_DS", constant = "override unlimited", limit = Limit.UNLIMITED)
        @ConstantGenerator(name = "limited", dataset = "SHARED_DS", constant = "override limited", limit = Limit.LIMITED)
        public void anyTestMethod() {
        }
    }
}
