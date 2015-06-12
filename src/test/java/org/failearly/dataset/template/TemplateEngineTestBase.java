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

package org.failearly.dataset.template;

import org.failearly.dataset.config.Constants;
import org.failearly.dataset.config.DataSetProperties;
import org.failearly.dataset.template.generator.Limit;
import org.failearly.dataset.template.generator.ConstantGenerator;
import org.failearly.dataset.internal.template.TemplateEngines;
import org.failearly.dataset.internal.template.TemplateObjects;
import org.failearly.dataset.internal.template.engine.velocity.VelocityTemplateEngine;
import org.failearly.dataset.test.TestUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;

/**
 * TemplateEngineBaseTest is responsible for ...
 */
public abstract class TemplateEngineTestBase {
    protected static final String UNLIMITED_DATA_SET = "UNLIMITED_DS";
    public static final String SHARED_DATA_SET = "SHARED_DS";

    protected static TemplateObjects resolveTemplateObjects(Class<?> aClass) throws NoSuchMethodException {
        return TestUtils.resolveTemplateObjects("anyTestMethod", aClass);
    }

    @Before
    public void setUp() throws Exception {
        DataSetProperties.setProperty(Constants.DATASET_PROPERTY_TEMP_DIR, "${user.dir}/temp");
    }

    @Test
    public void default_template_engine__should_be_VelocityTemplateEngine() throws Exception {
        // act / when
        final TemplateEngine templateEngine = TemplateEngines.createTemplateEngine();

        // assert / then
        assertThat("Engine's Type?", templateEngine, is(instanceOf(VelocityTemplateEngine.class)));
    }


    @After
    public void tearDown() throws Exception {
        DataSetProperties.reload();
    }

    @SuppressWarnings("UnusedDeclaration")
    @ConstantGenerator(name = "unlimited", dataset = UNLIMITED_DATA_SET, constant = "unlimited constant", limit = Limit.UNLIMITED)
    @ConstantGenerator(name = "limited", dataset = "LIMITED_DS", constant = "limited constant", limit = Limit.LIMITED)
    protected static class GeneratorDifferentDataSets {
        public void anyTestMethod() {
        }
    }

    @SuppressWarnings("UnusedDeclaration")
    @ConstantGenerator(name = "unlimited", dataset = "SHARED_DS", constant = "unlimited constant", limit = Limit.UNLIMITED)
    @ConstantGenerator(name = "limited", dataset = "SHARED_DS", constant = "limited constant", limit = Limit.LIMITED)
    protected static class GeneratorsSharedDataSet {
        public void anyTestMethod() {
        }
    }
}
