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
