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

package com.company.project;

import org.failearly.dataz.DataSet;
import org.junit.Test;

/**
* DataSetBaseClass is responsible for ...
*/
@DataSet(name = "DataSetBaseClass", setup = "DataSetBaseClass.setup")
public abstract class DataSetBaseClass {
    @Test
    @DataSet(name="anyTest")
    public void anyTest() {}
}
