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

import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.failearly.dataz.config.DataSetProperties;
import org.failearly.dataz.internal.template.TemplateObjects;
import org.failearly.dataz.internal.util.ResourceNameUtils;
import org.failearly.dataz.template.engine.TemplateEngine;
import org.failearly.dataz.template.engine.TemplateEngineBase;
import org.failearly.dataz.template.engine.TemplateEngineFactory;

import java.io.*;

/**
 * VelocityTemplateEngine is the default implementation for {@link TemplateEngine}.
 * <br><br>
 * Remark: Overwrite property {@value DataSetProperties#DATAZ_PROPERTY_TEMPLATE_ENGINE_FACTORY} and provide your own
 *     {@link TemplateEngineFactory}.
 */
public final class VelocityTemplateEngine extends TemplateEngineBase {
    private final VelocityEngine engine;

    public VelocityTemplateEngine() {
        super();
        engine = new VelocityEngine();
        engine.init();
    }

    @Override
    public File generate(InputStream templateStream, String templateResource, TemplateObjects templateObjects) throws IOException {
        final File targetFile = createTargetFile(templateResource);
        doMerge(templateStream, templateResource, targetFile, createVelocityContext(templateObjects));
        return targetFile;
    }

    private File createTargetFile(String resource) throws IOException {
        final String resourceSuffix = ResourceNameUtils.getResourceSuffix(resource);
        final String resourcePath = ResourceNameUtils.getResourcePath(resource);
        final String resourceName = ResourceNameUtils.getResourceNameWithoutPathAndSuffix(resource) + "-";
        return createTempFile(resourceName, resourceSuffix, resourcePath);
    }

    private void doMerge(InputStream templateStream, String templateResource, File targetFile, VelocityContext context) throws IOException {
        try(final FileWriter fileWriter = new FileWriter(targetFile)) {
            engine.evaluate(context, fileWriter, "<template=" + templateResource + ">", new InputStreamReader(templateStream));
        }
    }

    private VelocityContext createVelocityContext(TemplateObjects templateObjects) {
        final VelocityContext context=new VelocityContext();
        templateObjects.apply((templateObject) -> {
            LOGGER.debug("Add template object '{}' to velocity context", templateObject.name());
            context.put(templateObject.name(), templateObject);
        });
        return context;
    }
}
