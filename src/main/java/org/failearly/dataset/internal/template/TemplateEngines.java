/*
 * dataSet - Test Support For Datastores.
 *
 * Copyright (C) 2014-2014 Marko Umek (http://fail-early.com/contact)
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
 */
package org.failearly.dataset.internal.template;

import org.failearly.dataset.config.DataSetProperties;
import org.failearly.dataset.internal.generator.resolver.GeneratorCreator;
import org.failearly.dataset.util.ClassUtils;
import org.failearly.dataset.template.TemplateEngine;
import org.failearly.dataset.template.TemplateEngineFactory;

import java.util.List;
import java.util.stream.Collectors;

/**
 * TemplateEngines provides a single factory method for TemplateEngines.
 */
public final class TemplateEngines {

    private TemplateEngines() {
    }

    /**
     * Creates a template engine using {@link org.failearly.dataset.config.DataSetProperties#getTemplateEngineFactoryClass()}.
     * @param dataset the data set
     * @param fullQualifiedResourceTemplate the resource name
     * @param generatorCreators all {@link org.failearly.dataset.internal.generator.resolver.GeneratorCreator}s.
     * @return a template engine.
     */
    public static TemplateEngine createTemplateEngine(String dataset, String fullQualifiedResourceTemplate, List<GeneratorCreator> generatorCreators) {
        final TemplateEngineFactory factory=resolveTemplateEngineFactory();
        return factory.createTemplateEngine(fullQualifiedResourceTemplate, filterDataSetAssociatedGenerators(dataset, generatorCreators));
    }

    private static TemplateEngineFactory resolveTemplateEngineFactory() {
        final String factoryClass=DataSetProperties.getTemplateEngineFactoryClass();
        return ClassUtils.createInstance(TemplateEngineFactory.class, factoryClass);
    }

    private static List<GeneratorCreator> filterDataSetAssociatedGenerators(String dataSet, List<GeneratorCreator> generatorCreators) {
        return generatorCreators.stream().filter(gc->dataSet.equals(gc.dataSet())).collect(Collectors.toList());
    }

}
