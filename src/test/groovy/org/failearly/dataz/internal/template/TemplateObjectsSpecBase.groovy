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
package org.failearly.dataz.internal.template

import spock.lang.Specification

import java.util.stream.Collectors

/**
 * Base class for spock specifications on {@link TemplateObjects} and {@link TemplateObjectsResolver}.
 */
abstract class TemplateObjectsSpecBase extends Specification {
    protected static List<String> collectDescriptions(TemplateObjects templateObjects) {
        templateObjects.collectTemplateObjectInstances().stream()  //
                .map({ it.description })                                      //
                .collect(Collectors.toList())
    }

    protected static List<String> collectNames(TemplateObjects templateObjects) {
        templateObjects.collectTemplateObjectInstances().stream()  //
                .map({ it.name() })                                      //
                .collect(Collectors.toList())
    }

    protected static Set<String> collectDataSets(TemplateObjects templateObjects) {
        templateObjects.collectTemplateObjectInstances().stream()  //
                .flatMap({ it.datasets().stream() })                                      //
                .collect(Collectors.toSet())
    }
}