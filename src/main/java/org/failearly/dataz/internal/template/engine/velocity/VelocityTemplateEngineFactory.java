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

package org.failearly.dataz.internal.template.engine.velocity;

import org.failearly.dataz.template.engine.TemplateEngine;
import org.failearly.dataz.template.engine.TemplateEngineFactory;

/**
 * VelocityTemplateEngineFactory creates instances of {@link VelocityTemplateEngine}.
 */
public final class VelocityTemplateEngineFactory implements TemplateEngineFactory {
    @Override
    public TemplateEngine createTemplateEngine() {
        return new VelocityTemplateEngine();
    }
}
