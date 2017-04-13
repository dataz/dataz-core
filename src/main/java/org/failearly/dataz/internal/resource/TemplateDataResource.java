/*
 * dataZ - Test Support For Data Stores.
 *
 * Copyright 2014-2017 the original author or authors.
 *
 * All rights reserved. This program and the accompanying materials are
 * made available under the terms of the Eclipse Public License v1.0 which
 * accompanies this distribution and is available at
 *
 * http://www.eclipse.org/legal/epl-v10.html
 */

package org.failearly.dataz.internal.resource;

import org.failearly.dataz.exception.DataSetException;
import org.failearly.dataz.internal.template.TemplateEngines;
import org.failearly.dataz.internal.template.TemplateObjects;
import org.failearly.dataz.internal.util.IOUtils;
import org.failearly.dataz.internal.util.ResourceUtils;
import org.failearly.dataz.resource.DataResourceValues;
import org.failearly.dataz.template.TemplateObject;
import org.failearly.dataz.template.engine.TemplateEngine;

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
