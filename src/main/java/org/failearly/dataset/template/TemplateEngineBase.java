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
package org.failearly.dataset.template;

import org.failearly.dataset.config.DataSetProperties;
import org.failearly.dataset.generator.support.Generator;
import org.failearly.dataset.internal.generator.resolver.GeneratorCreator;
import org.failearly.dataset.internal.util.IOUtils;
import org.failearly.dataset.internal.util.ResourceNameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 * TemplateEngineBase is the base class for template engines.
 */
public abstract class TemplateEngineBase implements TemplateEngine {
    protected final Logger LOGGER = LoggerFactory.getLogger(getClass());
    private final List<GeneratorCreator> generatorCreators;
    private final String resourceName;
    private final String resourceSuffix;
    private final String resourcePath;

    protected TemplateEngineBase(String fullQualifiedResourceName, List<GeneratorCreator> generatorCreators) {
        this.generatorCreators = generatorCreators;
        this.resourceSuffix = ResourceNameUtils.getResourceSuffix(fullQualifiedResourceName);
        this.resourcePath = ResourceNameUtils.getResourcePath(fullQualifiedResourceName);
        this.resourceName = ResourceNameUtils.getResourceNameWithoutPathAndSuffix(fullQualifiedResourceName) + "-";
    }

    @Override
    public final InputStream mergeToInputStream(InputStream inputStream) throws IOException {
        return fileToInputStream(mergeToFile(inputStream));
    }

    /**
     * Merge the content of input stream to a new file.
     *
     * @param inputStream the content of the template.
     * @return a new (temp) file.
     *
     * @throws java.io.IOException in case the template engine has any IO issues with template resource.
     */
    protected abstract File mergeToFile(InputStream inputStream) throws IOException;


    private InputStream fileToInputStream(File targetFile) throws FileNotFoundException {
        return IOUtils.autoClose(new FileInputStream(targetFile));
    }

    protected final File createTempFile() throws IOException {
        final File targetFile = File.createTempFile(this.resourceName, this.resourceSuffix, DataSetProperties.createTempDir(this.resourcePath));
        LOGGER.debug("Create target file '{}'", targetFile);
        if (DataSetProperties.isDropTempFile()) {
            targetFile.deleteOnExit();
        } else {
            LOGGER.warn("The target file ({}) won't be deleted. Don't forget to remove the file.", targetFile);
        }
        return targetFile;
    }

    protected final List<GeneratorCreator> getGeneratorCreators() {
        return generatorCreators;
    }


    protected final List<Generator> createGenerators() {
        final List<Generator> generators=new LinkedList<>();
        final Set<String> generatorNames=new HashSet<>();
        for (GeneratorCreator generatorCreator : getGeneratorCreators()) {
            final Generator generator = generatorCreator.createGeneratorInstance();
            if( generatorNames.add(generator.name()) ) {
                LOGGER.debug("Use generator '{}' (annotation={})", generator.name(), generatorCreator.getAnnotation());
                generators.add(generator);
            }
            else {
                LOGGER.warn("Generator '{}' already defined (annotation={}). Ignored!", generator.name(), generatorCreator.getAnnotation());
            }
        }
        return generators;
    }
}
