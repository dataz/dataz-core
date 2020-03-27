/*
 * dataZ - Test Support For Data Stores.
 *
 * Copyright 2014-2020 the original author or authors.
 *
 * All rights reserved. This program and the accompanying materials are
 * made available under the terms of the Eclipse Public License v2.0 which
 * accompanies this distribution and is available at
 *
 * https://www.eclipse.org/legal/epl-v20.html
 */

package org.failearly.dataz.internal.template.engine.velocity;

import org.failearly.dataz.template.engine.TemplateEngineTestBase;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;

import static org.failearly.dataz.test.CoreTestUtils.fileToString;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;


/**
 * Tests for {@link org.failearly.dataz.internal.template.engine.velocity.VelocityTemplateEngine}.
 */
public class VelocityTemplateEngineTest extends TemplateEngineTestBase {

    private static final String TEMPLATE = "$to1.description\n$to2.description\n$unknown";

    private static final String ANY_TEMPLATE_RESOURCE_NAME = "/any/path/to/dataz-resource.suffix.vm";

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
        assertThat("Content of generated file?", fileToString(generatedFile), is("dataz/to1\ndataz/to2\n$unknown\n"));
    }
}
