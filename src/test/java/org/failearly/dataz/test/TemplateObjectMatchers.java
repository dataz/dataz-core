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

package org.failearly.dataz.test;

import org.failearly.dataz.template.Scope;
import org.failearly.dataz.template.TemplateObject;
import org.hamcrest.Matcher;

import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.CoreMatchers.equalTo;

/**
 * TemplateObjectMatchers is responsible for ...
 */
public final class TemplateObjectMatchers {

    public static Matcher<TemplateObject> isTemplateObjectAttributes(String expectedName, String expectedDataset, Scope expectedScope) {
        return allOf(
                hasTemplateObjectName(expectedName),
                hasTemplateObjectDataset(expectedDataset),
                hasTemplateObjectScope(expectedScope)
        );
    }

    private static Matcher<TemplateObject> hasTemplateObjectScope(Scope expectedScope) {
        return ClosureMatcher.closureMatcher(TemplateObject::scope, equalTo(expectedScope), "scope");
    }

    private static Matcher<TemplateObject> hasTemplateObjectDataset(String expectedDataset) {
        return ClosureMatcher.closureMatcher(TemplateObject::dataset, equalTo(expectedDataset), "dataz");
    }

    private static Matcher<TemplateObject> hasTemplateObjectName(String expectedName) {
        return ClosureMatcher.closureMatcher(TemplateObject::name, equalTo(expectedName), "name");
    }
}