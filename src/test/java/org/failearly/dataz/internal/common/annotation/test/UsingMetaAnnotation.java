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

package org.failearly.dataz.internal.common.annotation.test;

import java.lang.annotation.*;

/**
* MyAnnotation is responsible for ...
*/
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Repeatable(UsingMetaAnnotation.UsingMetaAnnotations.class)
@MetaAnnotation
public @interface UsingMetaAnnotation {
    String name() default "UsingMetaAnnotation";

    @Target({ElementType.METHOD, ElementType.TYPE})
    @Retention(RetentionPolicy.RUNTIME)
    @Inherited
    @interface UsingMetaAnnotations {
        UsingMetaAnnotation[] value();
    }
}
