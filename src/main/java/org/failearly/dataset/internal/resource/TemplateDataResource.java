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
package org.failearly.dataset.internal.resource;

import org.failearly.dataset.internal.generator.resolver.GeneratorCreator;
import org.failearly.dataset.internal.template.TemplateEngines;
import org.failearly.dataset.internal.util.IOUtils;
import org.failearly.dataset.internal.util.ResourceUtils;
import org.failearly.dataset.resource.DataResourceValues;
import org.failearly.dataset.template.TemplateEngine;
import org.junit.Assert;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * TemplateDataSetResource uses the {@code resourceName} as template, so the {@link org.failearly.dataset.template.TemplateEngine}
 * creates the actually (target) resource.
 *
 * @see org.failearly.dataset.template.TemplateEngine
 * @see org.failearly.dataset.generator.support.Generator
 */
final class TemplateDataResource extends DataResourceBase {
    private final Class<?> testClass;
    private final TemplateEngine templateEngine;

    TemplateDataResource(DataResourceValues dataResourceValues, List<GeneratorCreator> generatorCreators) {
        super(dataResourceValues);
        this.testClass = dataResourceValues.getTestClass();
        this.templateEngine = TemplateEngines.createTemplateEngine(
                dataResourceValues.getName(),
                dataResourceValues.getResource(),
                generatorCreators
        );
    }

    @Override
    public InputStream open() {
        try {
            return IOUtils.autoClose(
                        this.templateEngine.mergeToInputStream(
                            ResourceUtils.openResource(this.testClass, getResource())
                        )
                    );
        } catch (IOException e) {
            logger.error("Can't open/process template '{}'. Reason: {}!", getResource(), e.getMessage());
            Assert.fail("Can't open/process template resource '" + getResource() + "'.");
        }

        return IOUtils.nullInputStream();
    }
}
