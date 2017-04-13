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

package org.failearly.dataz.template.engine;

import org.failearly.dataz.config.Constants;
import org.failearly.dataz.config.DataSetProperties;
import org.failearly.dataz.internal.template.TemplateEngines;
import org.failearly.dataz.internal.template.TemplateObjects;
import org.hamcrest.Matchers;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import static org.hamcrest.MatcherAssert.assertThat;

/**
 * TemplateEnginesTest contains tests for ... .
 */
public class CustomTemplateEngineTest extends TemplateEngineTestBase {

    @Test
    public void override_DATASET_PROPERTY_TEMPLATE_ENGINE_FACTORY__should_result_in_custom_template_engine() throws Exception {
        // act / when
        DataSetProperties.setProperty(Constants.DATAZ_PROPERTY_TEMPLATE_ENGINE_FACTORY, CustomTemplateEngineFactory.class.getName());
        final TemplateEngine templateEngine=TemplateEngines.createTemplateEngine();

        // assert / then
        assertThat("Engine's Type?", templateEngine, Matchers.instanceOf(CustomTemplateEngine.class));
    }


    // MUST BE PUBLIC, otherwise TemplateEngines will fail.
    public static class CustomTemplateEngineFactory implements TemplateEngineFactory {
        @Override
        public TemplateEngine createTemplateEngine() {
            return new CustomTemplateEngine();
        }
    }


    private static class CustomTemplateEngine implements TemplateEngine {

        @Override
        public File generate(InputStream templateStream, String templateResource, TemplateObjects templateObjects) throws IOException {
            throw new UnsupportedOperationException("generate not yet implemented");
        }
    }

}