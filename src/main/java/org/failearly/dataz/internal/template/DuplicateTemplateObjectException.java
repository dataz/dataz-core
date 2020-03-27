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

import org.failearly.dataz.exception.DataSetException;
import org.failearly.dataz.template.TemplateObject;

/**
 * DuplicateTemplateObjectException used by {@link TemplateObjectDuplicateStrategy#STRICT}.
 */
public class DuplicateTemplateObjectException extends DataSetException {
    public DuplicateTemplateObjectException(TemplateObject templateObject) {
        super("Duplicate template object found: " + templateObject);
    }
}
