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
package org.failearly.dataset.internal.template.velocity;

import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.failearly.dataset.generator.support.Generator;
import org.failearly.dataset.internal.generator.resolver.GeneratorCreator;
import org.failearly.dataset.template.TemplateEngineBase;

import java.io.*;
import java.util.List;

/**
 * VelocityTemplateEngine is a wrapper around VelocityEngine.
 */
final class VelocityTemplateEngine extends TemplateEngineBase {
    private final VelocityEngine engine;

    VelocityTemplateEngine(String fullQualifiedResourceName, List<GeneratorCreator> generatorCreators) {
        super(fullQualifiedResourceName, generatorCreators);
        engine = new VelocityEngine();
        engine.init();
    }

    protected File mergeToFile(InputStream inputStream) throws IOException {
        final File targetFile = createTempFile();
        final VelocityContext context = createVelocityContext();
        doMerge(inputStream, targetFile, context);

        return targetFile;
    }

    private void doMerge(InputStream inputStream, File targetFile, VelocityContext context) throws IOException {
        try(final FileWriter fileWriter = new FileWriter(targetFile)) {
            engine.evaluate(context, fileWriter, "name of resource", new InputStreamReader(inputStream));
        }
    }

    private VelocityContext createVelocityContext() {
        final VelocityContext context=new VelocityContext();
        for (Generator generator : createGenerators()) {
            LOGGER.debug("Add generator '{}' to velocity context", generator.name());
            context.put(generator.name(), generator);
        }
        return context;
    }


}
