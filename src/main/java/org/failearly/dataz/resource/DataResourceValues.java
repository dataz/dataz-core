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

package org.failearly.dataz.resource;

import org.failearly.dataz.internal.common.resource.ResourcePathUtils;
import org.failearly.dataz.NamedDataStore;
import org.failearly.dataz.config.Constants;
import org.failearly.dataz.config.DataSetProperties;
import org.failearly.dataz.internal.common.builder.BuilderBase;

/**
 * DataResourceValues is a parameter object containing the values of any dataSet impl (like {@link org.failearly.dataz.DataSet}).
 *
 * It will be used for creating the actually {@link DataResource} object.
 */
public final class DataResourceValues {

    private final String dataSetName;

    private final Class<? extends NamedDataStore> datastore;
    private final String resource;
    private final boolean transactional;
    private final boolean  failOnError;

    private final Class<?> testClass;

    /**
     * Creates a newInstance for {@link DataResourceValues}.
     *
     * @param testClass the associated test class.
     * @return the {@link DataResourceValues.Builder}.
     */
    public static Builder builder(Class<?> testClass) {
        return new Builder(testClass);
    }

    private DataResourceValues(Class<?> testClass, String dataSetName, Class<? extends NamedDataStore> datastore, String resource, boolean failOnError, boolean transactional) {
        this.testClass = testClass;
        this.dataSetName = dataSetName;
        this.datastore = datastore;
        this.resource = resource;
        this.failOnError = failOnError;
        this.transactional = transactional;
    }

    /**
     * @return The associated test class.
     */
    public Class<?> getTestClass() {
        return testClass;
    }

    /**
     * The logical name of a DataResource (not the resource itself). A DataSet could contain more then one resource.
     *
     * @return the logical name
     */
    public String getDataSetName() {
        return dataSetName;
    }

    public Class<? extends NamedDataStore> getDataStore() {
        return datastore;
    }

    /**
     * @return the resource name/path.
     *
     * @see org.failearly.dataz.DataSet#setup()
     * @see org.failearly.dataz.DataSet#cleanup()
     */
    public String getResource() {
        return resource;
    }

    /**
     * @return if the entire resource should be applied within a transaction (if supported by the DataStore implementation).
     */
    public boolean isTransactional() {
        return transactional;
    }

    /**
     * @return {@code true} if the processing of the resource should fail
     *          if there is any error, while processing the resource's content, otherwise {@code false}.
     *
     */
    public boolean isFailOnError() {
        return failOnError;
    }

    /**
     * @return {@code true} if the resource exists.
     */
    public boolean doesResourceExists() {
        return null != testClass.getResource(this.resource);
    }

    /**
     * @return {@code true} if given resource is a template resource.
     */
    public boolean isTemplateResource() {
        return resource.endsWith(DataSetProperties.getTemplateSuffix());
    }

    @Override
    public String toString() {
        return "DataResource{" +
                "name='" + dataSetName + '\'' +
                ", resource='" + resource + '\'' +
                ", transactional=" + transactional +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o instanceof DataResourceValues) {
            final DataResourceValues that = (DataResourceValues) o;

            return dataSetName.equals(that.dataSetName)       //
                    && resource.equals(that.resource);
        }
        return false;

    }

    @Override
    public int hashCode() {
        int result = dataSetName.hashCode();
        result = 31 * result + resource.hashCode();
        return result;
    }

    /**
     * Builder is responsible for creating a valid {@link DataResourceValues} object.
     */
    public static final class Builder extends BuilderBase<DataResourceValues> {
        private final Class<?> testClass;

        // mandatory
        private String dataSetName;
        private String resourceName;

        // optional
        private boolean transactional= Constants.DATASET_DEFAULT_TRANSACTIONAL_VALUE;
        private boolean  failOnError=Constants.DATASET_DEFAULT_FAIL_ON_ERROR_VALUE;
        private Class<? extends NamedDataStore> datastore;

        private Builder(Class<?> testClass) {
            this.testClass = testClass;
        }

        public Builder withFailOnError(boolean failOnError) {
            this.failOnError = failOnError;
            return this;
        }

        public Builder withDataSetName(String dataSetName) {
            this.dataSetName = dataSetName;
            return this;
        }

        public Builder withResourceName(String resourceName) {
            this.resourceName = resourceName;
            return this;
        }

        public Builder withTransactional(boolean transactional) {
            this.transactional = transactional;
            return this;
        }

        public Builder withNamedDataStore(Class<? extends NamedDataStore> datastore) {
            this.datastore = datastore;
            return this;
        }

        @Override
        protected DataResourceValues doBuild() {
            return new DataResourceValues(
                            testClass,
                            dataSetName,
                            datastore,
                            ResourcePathUtils.resourcePath(resourceName, testClass),
                            failOnError,
                            transactional
            );
        }

        @Override
        protected void checkMandatoryFields() {
            checkMandatoryField(this.testClass, "testClass");
            checkMandatoryField(this.dataSetName, "dataSetName");
            checkMandatoryField(this.resourceName, "resourceName");
            checkMandatoryField(this.datastore, "datastore");
        }
    }
}
