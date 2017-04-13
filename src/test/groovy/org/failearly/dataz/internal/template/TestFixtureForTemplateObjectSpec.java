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
package org.failearly.dataz.internal.template;

import org.failearly.dataz.template.Scope;
import org.failearly.dataz.test.SimpleTemplateObject;

/**
 * TestFixture for TemplateObjectSpec
 */
@SuppressWarnings("unused")
@SimpleTemplateObject(name = "LTO-0", datasets = {"DS1", "DS2", "DS3"}, scope = Scope.LOCAL, description = "on class object and scope=LOCAL")
@SimpleTemplateObject(name = "GTO-0", datasets = {"DS1", "DS2", "DS3"}, scope = Scope.GLOBAL, description = "on class object and scope=GLOBAL")
abstract class TestFixtureForTemplateObjectSpec {
    private TestFixtureForTemplateObjectSpec() {
    }

    @SimpleTemplateObject(name = "LTO-1", datasets = {"DS1", "DS2"}, scope = Scope.LOCAL)
    @SimpleTemplateObject(name = "LTO-2", datasets = {"DS1"}, scope = Scope.LOCAL)
    public void localScope() {}

    @SimpleTemplateObject(name = "LTO-NO-DS", scope = Scope.LOCAL, description = "scope=LOCAL and no datasets")
    public void localScopeButNoDataSet() {}

    @SimpleTemplateObject(name = "GTO-1", datasets = {"DS1", "DS2"}, scope = Scope.GLOBAL)
    @SimpleTemplateObject(name = "GTO-2", datasets = {"DS1"}, scope = Scope.GLOBAL)
    public void globalScope() {}

    @SimpleTemplateObject(name = "GTO-NO-DS", scope = Scope.GLOBAL, description = "scope=GLOBAL and no datasets")
    public void globalScopeButNoDataSet() {}


    @SimpleTemplateObject(name = "TO-5", datasets = {"DS1"}, description = "using the default scope")
    public void defaultScope() {}

    @SimpleTemplateObject(name = "MERGE-1", description = "used for merge")
    @SimpleTemplateObject(name = "MERGE-2", description = "used for merge")
    public void mergeFixture() {}
}
