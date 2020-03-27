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

package org.failearly.dataz;

import java.lang.annotation.*;

/**
 * Use this for suppressing DataSet functionality on a test method. This could be useful, if all test share the same {@link DataSet}, by annotating
 * the test class, but you have also some test methods which does not need any DataSet functionality.
 * <br><br>
 * Remark: In case you use it for example with {@link DataSet} on the same
 * test method, {@code NoDataSet} will supersede {@code DataSet}.
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface NoDataSet {
}
