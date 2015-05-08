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

package org.failearly.dataset.template;

import org.failearly.dataset.config.Constants;
import org.failearly.dataset.config.DataSetProperties;
import org.failearly.dataset.internal.generator.resolver.GeneratorCreator;
import org.failearly.dataset.internal.template.TemplateEngines;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.instanceOf;

/**
 * TemplateEnginesTest contains tests for ... .
 */
public class TemplateEnginesTest extends TemplateEngineTestBase {

    @Test
    public void checkForCustomTemplateEngine() throws Exception {
        // arrange / given
        DataSetProperties.setProperty(Constants.DATASET_PROPERTY_TEMPLATE_ENGINE_FACTORY, CustomTemplateEngineFactory.class.getName());

        // act / when
        final TemplateEngine templateEngine = TemplateEngines.createTemplateEngine("UNLIMITED_DS",
                "/any/path/to/dataset-resource.suffix.vm", generatorCreators(GeneratorDifferentDataSets.class));

        // assert / then
        assertThat("Engine's Type?", templateEngine, instanceOf(CustomTemplateEngine.class));
    }


    public static class CustomTemplateEngineFactory implements TemplateEngineFactory {

        @Override
        public TemplateEngine createTemplateEngine(String fullQualifiedResourceTemplate, List<GeneratorCreator> generatorCreators) {
            return new CustomTemplateEngine();
        }
    }


    private static class CustomTemplateEngine implements TemplateEngine {
        @Override
        public InputStream mergeToInputStream(InputStream inputStream) throws IOException {
            throw new UnsupportedOperationException("mergeToInputStream not yet implemented");
        }
    }

}