/*
 * dataZ - Test Support For Data Stores.
 *
 * Copyright (C) 2014-2016 marko (http://fail-early.com)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>
 *
 */

package org.failearly.dataset.internal.template;

import org.failearly.dataset.config.DataSetProperties;
import org.failearly.common.test.ObjectCreator;
import org.failearly.dataset.template.engine.TemplateEngine;
import org.failearly.dataset.template.engine.TemplateEngineFactory;

/**
 * TemplateEngines provides a single factory method for TemplateEngines.
 */
public final class TemplateEngines {

    private TemplateEngines() {
    }

    /**
     * Creates a template engine using {@link org.failearly.dataset.config.DataSetProperties#getTemplateEngineFactoryClass()}.
     * @return a template engine.
     */
    public static TemplateEngine createTemplateEngine() {
        final TemplateEngineFactory factory=resolveTemplateEngineFactory();
        return factory.createTemplateEngine();
    }

    private static TemplateEngineFactory resolveTemplateEngineFactory() {
        final String factoryClass=DataSetProperties.getTemplateEngineFactoryClass();
        return ObjectCreator.createInstance(TemplateEngineFactory.class, factoryClass);
    }
}
