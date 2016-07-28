/*
 * dataZ - Test Support For Data Stores.
 *
 * Copyright (C) 2014-2016 'Marko Umek' (http://fail-early.com)
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

package org.failearly.dataz.template.engine;

import org.failearly.dataz.config.Constants;
import org.failearly.dataz.config.DataSetProperties;
import org.failearly.dataz.internal.template.TemplateEngines;
import org.failearly.dataz.internal.template.TemplateObjects;
import org.failearly.dataz.internal.template.engine.velocity.VelocityTemplateEngine;
import org.failearly.dataz.test.CoreTestUtils;
import org.failearly.dataz.test.SimpleTemplateObject;
import org.hamcrest.Matchers;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

/**
 * TemplateEngineBaseTest is base class for tests of {@link TemplateEngines}
 */
public abstract class TemplateEngineTestBase {
    protected static final String DATA_SET = "dataz";
    protected static final String TEMPLATE_OBJECT_NAME_1 = "to1";
    protected static final String TEMPLATE_OBJECT_NAME_2 = "to2";

    protected static TemplateObjects resolveTemplateObjects(Class<?> aClass) throws NoSuchMethodException {
        return CoreTestUtils.resolveTemplateObjects("anyTestMethod", aClass);
    }

    @Before
    public void setUp() throws Exception {
        DataSetProperties.setProperty(Constants.DATAZ_PROPERTY_TEMP_DIR, "${user.dir}/temp");
    }

    @Test
    public void default_template_engine__should_be_VelocityTemplateEngine() throws Exception {
        // act / when
        final TemplateEngine templateEngine = TemplateEngines.createTemplateEngine();

        // assert / then
        assertThat("Engine's Type?", templateEngine, Matchers.is(Matchers.instanceOf(VelocityTemplateEngine.class)));
    }


    @After
    public void tearDown() throws Exception {
        DataSetProperties.reload();
    }


    @SuppressWarnings("UnusedDeclaration")
    @SimpleTemplateObject(name = TEMPLATE_OBJECT_NAME_1, datasets = DATA_SET, description = DATA_SET + "/" + TEMPLATE_OBJECT_NAME_1)
    @SimpleTemplateObject(name = TEMPLATE_OBJECT_NAME_2, datasets = DATA_SET, description = DATA_SET + "/" + TEMPLATE_OBJECT_NAME_2)
    protected static class TestFixture {
        public void anyTestMethod() {
        }
    }
}
