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

import org.failearly.dataz.config.DataSetProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;

/**
 * TemplateEngineBase is the base class for template engines.
 */
public abstract class TemplateEngineBase implements TemplateEngine {
    protected final Logger LOGGER = LoggerFactory.getLogger(getClass());

    protected TemplateEngineBase() {
    }

    protected final File createTempFile(String resourceName, String resourceSuffix, String resourcePath) throws IOException {
        final File targetFile = File.createTempFile(resourceName, resourceSuffix, DataSetProperties.createTempDir(resourcePath));
        LOGGER.debug("Create target file '{}'", targetFile);
        if (DataSetProperties.isDropTempFile()) {
            targetFile.deleteOnExit();
        } else {
            LOGGER.warn("The target file ({}) won't be deleted. Don't forget to remove the file.", targetFile);
        }
        return targetFile;
    }


}
