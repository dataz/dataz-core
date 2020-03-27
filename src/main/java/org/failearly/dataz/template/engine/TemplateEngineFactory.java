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

package org.failearly.dataz.template.engine;


/**
 * TemplateEngineFactory creates a template engine.
 */
public interface TemplateEngineFactory {
    /**
     * Create an instance of TemplateEngine.
     *
     * @return a new template engine.
     */
    TemplateEngine createTemplateEngine();
}
