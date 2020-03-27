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
