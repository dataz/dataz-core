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
package org.failearly.dataz.internal.template;

import org.failearly.dataz.test.SimpleTemplateObject;

/**
 * TestFixtureForDuplicateStrategy is a test fixture class for {@link TemplateObjectDuplicateStrategy} tests.
 */
@SuppressWarnings("unused")
@SimpleTemplateObject(name = "TO-2", description = "TO-2 (1st)")
@SimpleTemplateObject(name = "TO-1", description = "TO-1 (1st)")
@SimpleTemplateObject(name = "TO-1", description = "TO-1 (2nd)")
abstract class TestFixtureForDuplicateStrategy {

    private TestFixtureForDuplicateStrategy() {
    }

    public void methodWithoutTOs() {
    }

    @SimpleTemplateObject(name = "TO-3", description = "TO-3 (1st)")
    @SimpleTemplateObject(name = "TO-3", description = "TO-3 (2nd)")
    public void methodWithDuplicates() {
    }

    @SimpleTemplateObject(name = "TO-2", description = "TO-2 (2nd)")
    public void methodWithOneTO() {
    }
}

