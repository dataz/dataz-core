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

package com.company.project;

import org.failearly.dataz.DataSet;

/**
 * AnyDataSetBaseWithResources is responsible for ...
 */
@DataSet(setup = {"AnyDataSetBaseWithResources.setup1", "AnyDataSetBaseWithResources.setup2"},
         cleanup = {"AnyDataSetBaseWithResources.cleanup1", "AnyDataSetBaseWithResources.cleanup2"})
public class AnyDataSetBaseWithResources {

    @DataSet(name="D1", setup={"anyTestMethod.setup1", "anyTestMethod.setup2"}, cleanup={"anyTestMethod.cleanup1", "anyTestMethod.cleanup2"})
    @DataSet(name="D2")
    public void anyTestMethod() {}
}
