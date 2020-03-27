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

package org.failearly.dataz.resource;

import org.failearly.dataz.NamedDataStore;
import org.failearly.dataz.config.DataSetProperties;
import org.failearly.dataz.internal.resource.DataResourceFactory;
import org.failearly.dataz.internal.resource.ResourceType;
import org.failearly.dataz.internal.template.TemplateObjects;
import org.failearly.dataz.internal.common.builder.BuilderBase;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * DataResourceFactory is responsible for ...
 */
public final class DataResourceBuilder extends BuilderBase<List<DataResource>> {
    private final DataResourceValues.Builder dataResourceValueBuilder;
    private Boolean mandatory;
    private TemplateObjects templateObjects;
    private List<Class<? extends NamedDataStore>> namedDataStores=Collections.emptyList();

    private DataResourceBuilder(Class<?> testClass) {
        dataResourceValueBuilder = DataResourceValues.builder(testClass);
    }

    /**
     * Creates a new {@link DataResource} builder.
     *
     * @param testClass a test class
     * @return a newInstance instance.
     */
    public static DataResourceBuilder createBuilder(Class<?> testClass) {
        return new DataResourceBuilder(testClass);
    }

    public DataResourceBuilder withResourceType(ResourceType resourceType) {
        return resourceType == ResourceType.SETUP ? this.mandatory() : this.optional();
    }

    public DataResourceBuilder mandatory() {
        this.mandatory = true;
        return this;
    }

    public DataResourceBuilder optional() {
        this.mandatory = false;
        return this;
    }

    public DataResourceBuilder withTemplateObjects(TemplateObjects templateObjects) {
        this.templateObjects = templateObjects;
        return this;
    }

    @SafeVarargs
    public final DataResourceBuilder withNamedDataStore(Class<? extends NamedDataStore>... datastores) {
        this.namedDataStores = distinctNamedDataStores(datastores);

        return this;
    }

    private static List<Class<? extends NamedDataStore>> distinctNamedDataStores(Class<? extends NamedDataStore>[] namedDataStores) {
        return Arrays.stream(namedDataStores).distinct().collect(Collectors.toList());
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

    @Override
    protected List<DataResource> doBuild() {
        if( this.namedDataStores.isEmpty() ) {
            this.namedDataStores = Collections.singletonList(DataSetProperties.loadDefaultNamedDataStore());
        }
        return namedDataStores.stream()
                .map(this::doBuildSingleDataResource)
                .collect(Collectors.toList());
    }

    private DataResource doBuildSingleDataResource(Class<? extends NamedDataStore> namedDataStore) {
        final DataResource dataResource;
        final DataResourceValues dataResourceValues = dataResourceValueBuilder.withNamedDataStore(namedDataStore).build();
        if (dataResourceValues.doesResourceExists()) {
            if (dataResourceValues.isTemplateResource()) {
                dataResource = DataResourceFactory.createTemplateInstance(dataResourceValues);
            } else {
                dataResource = DataResourceFactory.createStandardInstance(dataResourceValues);
            }
        } else if (mandatory) {
            dataResource = DataResourceFactory.createMissingResourceInstance(dataResourceValues);
        } else {
            dataResource = DataResourceFactory.createIgnoringInstance(dataResourceValues);
        }

        dataResource.generate(templateObjects);
        return dataResource;
    }

    @Override
    protected void checkMandatoryFields() {
        checkMandatoryField(this.mandatory, "mandatory/optional");
        checkMandatoryField(this.templateObjects, "templateObjects");
    }

}
