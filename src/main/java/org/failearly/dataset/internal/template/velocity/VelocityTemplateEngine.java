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
package org.failearly.dataset.internal.template.velocity;

import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.failearly.dataset.internal.template.TemplateObjects;
import org.failearly.dataset.internal.util.ResourceNameUtils;
import org.failearly.dataset.template.TemplateEngineBase;

import java.io.*;

/**
 * VelocityTemplateEngine is the default implementation for {@link org.failearly.dataset.template.TemplateEngine}.
 * <br><br>
 * Remark: Overwrite property {@value org.failearly.dataset.config.DataSetProperties#DATASET_PROPERTY_TEMPLATE_ENGINE_FACTORY} and provide your own
 *     {@link org.failearly.dataset.template.TemplateEngineFactory}.
 */
public final class VelocityTemplateEngine extends TemplateEngineBase {
    private final VelocityEngine engine;

    public VelocityTemplateEngine() {
        super();
        engine = new VelocityEngine();
        engine.init();
    }

    @Override
    public File generate(InputStream templateStream, String resource, TemplateObjects templateObjects) throws IOException {
        final File targetFile = createTargetFile(resource);
        doMerge(templateStream, targetFile, createVelocityContext(templateObjects));
        return targetFile;
    }

    private File createTargetFile(String resource) throws IOException {
        final String resourceSuffix = ResourceNameUtils.getResourceSuffix(resource);
        final String resourcePath = ResourceNameUtils.getResourcePath(resource);
        final String resourceName = ResourceNameUtils.getResourceNameWithoutPathAndSuffix(resource) + "-";
        return createTempFile(resourceName, resourceSuffix, resourcePath);
    }

    private void doMerge(InputStream inputStream, File targetFile, VelocityContext context) throws IOException {
        try(final FileWriter fileWriter = new FileWriter(targetFile)) {
            engine.evaluate(context, fileWriter, "name of resource", new InputStreamReader(inputStream));
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
