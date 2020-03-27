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

package com.company.project.module;

import com.company.project.DataSetBaseClass;
import org.failearly.dataz.DataSet;
import org.junit.Test;

/**
* No tests at all.
*/
@DataSet(name="MiddleDataSetBase", cleanup = {"middle1.cleanup", "/middle2.cleanup"})
public abstract class MiddleDataSetBaseClass extends DataSetBaseClass {
    @Test
    public void middleTest() {}
}
