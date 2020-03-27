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
        return ClosureMatcher.closureMatcher(TemplateObject::datasets, equalTo(expectedDataset), "dataz");
    }

    private static Matcher<TemplateObject> hasTemplateObjectName(String expectedName) {
        return ClosureMatcher.closureMatcher(TemplateObject::name, equalTo(expectedName), "name");
    }
}
