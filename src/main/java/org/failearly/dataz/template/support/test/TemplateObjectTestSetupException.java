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

package org.failearly.dataz.template.support.test;

/**
 * TemplateObjectTestSetupException thrown in case of any setup error.
 */
public class TemplateObjectTestSetupException extends RuntimeException {
    public TemplateObjectTestSetupException(String message) {
        super(message);
    }
}
