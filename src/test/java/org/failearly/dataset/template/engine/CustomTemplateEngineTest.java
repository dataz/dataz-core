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

package org.failearly.dataset.template.engine;

import org.failearly.dataset.config.Constants;
import org.failearly.dataset.config.DataSetProperties;
import org.failearly.dataset.internal.template.TemplateEngines;
import org.failearly.dataset.internal.template.TemplateObjects;
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
        DataSetProperties.setProperty(Constants.DATASET_PROPERTY_TEMPLATE_ENGINE_FACTORY, CustomTemplateEngineFactory.class.getName());
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