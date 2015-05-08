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
package org.failearly.dataset.resource;

import org.failearly.dataset.internal.generator.resolver.GeneratorCreator;
import org.failearly.dataset.internal.resource.DataResourceFactory;
import org.failearly.dataset.internal.resource.ResourceType;
import org.failearly.dataset.internal.util.BuilderBase;

import java.util.List;

/**
 * DataResourceFactory is responsible for ...
 */
public final class DataResourceBuilder extends BuilderBase<DataResource> {
    private final DataResourceValues.Builder dataResourceValueBuilder;
    private Boolean mandatory;
    private List<GeneratorCreator> generatorCreators;

    private DataResourceBuilder(Class<?> testClass) {
        dataResourceValueBuilder = DataResourceValues.builder(testClass);
    }

    /**
     * Creates a new {@link org.failearly.dataset.resource.DataResource} builder.
     * @param testClass a test class
     * @return a newInstance instance.
     */
    public static DataResourceBuilder createBuilder(Class<?> testClass)  {
        return new DataResourceBuilder(testClass);
    }

    public DataResourceBuilder withResourceType(ResourceType resourceType) {
        return resourceType==ResourceType.SETUP ? this.mandatory() : this.optional();
    }

    public DataResourceBuilder mandatory() {
        this.mandatory = true;
        return this;
    }

    public DataResourceBuilder optional() {
        this.mandatory = false;
        return this;
    }

    public DataResourceBuilder withGeneratorCreators(List<GeneratorCreator> generatorCreators) {
        this.generatorCreators = generatorCreators;
        return this;
    }

    public DataResourceBuilder withDataStoreId(String dataStoreId) {
        dataResourceValueBuilder.withDataStoreId(dataStoreId);
        return this;
    }

    public DataResourceBuilder withFailOnError(boolean failOnError) {
        dataResourceValueBuilder.withFailOnError(failOnError);
        return this;
    }

    public DataResourceBuilder withDataSetName(String dataSetName) {
        dataResourceValueBuilder.withDataSetName(dataSetName);
        return this;
    }

    public DataResourceBuilder withResourceName(String resourceName) {
        dataResourceValueBuilder.withResourceName(resourceName);
        return this;
    }

    public DataResourceBuilder withTransactional(boolean transactional) {
        dataResourceValueBuilder.withTransactional(transactional);
        return this;
    }

    public <T> DataResourceBuilder withAdditionalValue(String key, T value) {
        dataResourceValueBuilder.withAdditionalValue(key, value);
        return this;
    }

    @Override
    protected DataResource doBuild() {
        final DataResource dataResource;
        final DataResourceValues dataResourceValues= dataResourceValueBuilder.build();
        if( dataResourceValues.doesResourceExists() ) {
            if( dataResourceValues.isTemplateResource() ) {
                dataResource = DataResourceFactory.createTemplateInstance(dataResourceValues, generatorCreators);
            }
            else {
                dataResource = DataResourceFactory.createStandardInstance(dataResourceValues);
            }
        }
        else if( mandatory ) {
            dataResource=DataResourceFactory.createMissingResourceInstance(dataResourceValues);
        }
        else {
            dataResource=DataResourceFactory.createIgnoringInstance(dataResourceValues);
        }

        return dataResource;
    }

    @Override
    protected void checkMandatoryFields() {
        checkMandatoryField(this.mandatory,"mandatory/optional");
        checkMandatoryField(this.generatorCreators,"generatorCreators");
    }

}
