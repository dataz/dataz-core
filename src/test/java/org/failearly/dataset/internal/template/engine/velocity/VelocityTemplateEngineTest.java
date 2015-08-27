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

import org.failearly.dataset.template.engine.TemplateEngineTestBase;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;

import static org.failearly.dataset.test.CoreTestUtils.fileToString;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;


/**
 * Tests for {@link org.failearly.dataset.internal.template.engine.velocity.VelocityTemplateEngine}.
 */
public class VelocityTemplateEngineTest extends TemplateEngineTestBase {

    private static final String TEMPLATE = "$to1.description\n$to2.description\n$unknown";

    private static final String ANY_TEMPLATE_RESOURCE_NAME = "/any/path/to/dataset-resource.suffix.vm";

    private VelocityTemplateEngine templateEngine;

    private static InputStream templateInputStream() {
        return new ByteArrayInputStream(TEMPLATE.getBytes());
    }

    @Before
    public void setUp() throws Exception {
        templateEngine = new VelocityTemplateEngine();
    }

    @Test
    public void generate_from_file__should_use_known_template_objects__and__leave_unknown_unchanged() throws Exception {
        // act / when
        final File generatedFile = templateEngine.generate(
                templateInputStream(),
                ANY_TEMPLATE_RESOURCE_NAME,
                resolveTemplateObjects(TestFixture.class).filterByDataSet(DATA_SET)
        );

        // assert / then
        assertThat("Content of generated file?", fileToString(generatedFile), is("dataset/to1\ndataset/to2\n$unknown\n"));
    }
}
