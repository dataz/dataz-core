/*
 * dataSet - Test Support For Data Stores.
 *
 * Copyright (C) 2014-2015 Marko Umek (http://fail-early.com/contact)
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

import org.failearly.dataset.exception.DataSetException;
import org.failearly.dataset.internal.template.TemplateEngines;
import org.failearly.dataset.internal.template.TemplateObjects;
import org.failearly.dataset.internal.util.IOUtils;
import org.failearly.dataset.internal.util.ResourceUtils;
import org.failearly.dataset.resource.DataResourceValues;
import org.failearly.dataset.template.TemplateObject;
import org.failearly.dataset.template.engine.TemplateEngine;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * TemplateDataSetResource uses the {@code resourceName} as template, so the {@link TemplateEngine}
 * creates the actually (target) resource.
 *
 * @see TemplateEngine
 * @see TemplateObject
 */
final class TemplateDataResource extends DataResourceBase {
    private final Class<?> testClass;
    private final TemplateEngine templateEngine;
    private File generatedFile;

    TemplateDataResource(DataResourceValues dataResourceValues) {
        super(dataResourceValues);
        this.testClass = dataResourceValues.getTestClass();
        this.templateEngine = TemplateEngines.createTemplateEngine();
    }

    @Override
    public void generate(TemplateObjects templateObjects) throws DataResourceProcessingException {
        try {
            this.generatedFile = this.templateEngine.generate(
                    ResourceUtils.openResource(this.testClass, getResource()),
                    getResource(),
                    templateObjects.filterByDataSet(getDataSetName())
            );
        } catch (IOException e) {
            logger.error("Can't open/process template '{}'. Reason: {}!", getResource(), e.getMessage());
            throw new DataResourceProcessingException(getResource(), e);
        }
    }

    @Override
    public InputStream open() throws DataSetException {
        try {
            return IOUtils.autoClose(new FileInputStream(generatedFile));
        } catch (IOException e) {
            logger.error("Can't open file '{}'. Reason: {}!", generatedFile, e.getMessage());
            throw new DataResourceProcessingException(generatedFile.getAbsolutePath(), e);
        }
    }
}
