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

package org.failearly.dataz.common;

import java.lang.annotation.*;

/**
 * Tests documents where are the tests.
 *
 * The opposite annotations are {@code Subject} or {@code TestFor} impl within module {@code common-test}.
 */
@Target({ElementType.TYPE,ElementType.METHOD, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.SOURCE)
@Documented
public @interface Tests {
    String[] value();
}
