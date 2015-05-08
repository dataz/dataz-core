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

import org.failearly.dataset.config.DataSetProperties;
import org.failearly.dataset.internal.util.BuilderBase;

import java.util.*;

/**
 * DataResourceValues is a parameter object containing the values of any dataSet annotation (like {@link org.failearly.dataset.DataSet}).
 * It will be used for creating the actually {@link org.failearly.dataset.resource.DataResource} object.
 */
public final class DataResourceValues {

    private final String name;
    private final String dataStoreId;
    private final String resource;
    private final boolean transactional;
    private final Map<String,?> additionalValues;
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

    private DataResourceValues(Class<?> testClass, String name, String dataStoreId, String resource, boolean failOnError, boolean transactional, Map<String, ?> additionalValues) {
        this.testClass = testClass;
        this.name = name;
        this.dataStoreId = dataStoreId;
        this.resource = resource;
        this.failOnError = failOnError;
        this.transactional = transactional;
        this.additionalValues = additionalValues;
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
    public String getName() {
        return name;
    }

    /**
     * @return  The associated {@link org.failearly.dataset.datastore.DataStore} ID.
     */
    public String getDataStoreId() {
        return dataStoreId;
    }

    /**
     * @return the resource name/path.
     *
     * @see org.failearly.dataset.DataSet#setup()
     * @see org.failearly.dataset.DataSet#cleanup()
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
     * Return additional value for given {@code key} or if the {@code key} is unknown return the {@code defaultValue}.
     *
     * @param key the key or name of the additional value
     * @param defaultValue the value to be returned if there is no value stored for given {@code key}
     * @param <T> the expected type.
     * @return the additional value or {@code defaultValue}.
     */
    public <T> T getAdditionalValue(String key, T defaultValue) {
        final Object value=additionalValues.get(key);
        if( null==value ) {
            return defaultValue;
        }

        //noinspection unchecked
        return (T)value;
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
                "name='" + name + '\'' +
                ", dataStoreId='" + dataStoreId + '\'' +
                ", resource='" + resource + '\'' +
                ", transactional=" + transactional +
                ", additionalValues=" +  additionalValues +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DataResourceValues)) return false;

        DataResourceValues that = (DataResourceValues) o;

        if (!name.equals(that.name)) return false;
        if (!dataStoreId.equals(that.dataStoreId)) return false;
        return resource.equals(that.resource);

    }

    @Override
    public int hashCode() {
        int result = name.hashCode();
        result = 31 * result + dataStoreId.hashCode();
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
        private String dataStoreId;
        private String resourceName;

        // optional
        private boolean transactional=false;
        private boolean  failOnError=false;
        private final Map<String, Object> additionalValues=new HashMap<>();

        private Builder(Class<?> testClass) {
            this.testClass = testClass;
        }

        @SuppressWarnings("UnusedDeclaration")
        public <T> Builder withAdditionalValue(String key, T value) {
            this.additionalValues.put(key, value);
            return this;
        }

        public Builder withDataStoreId(String dataStoreId) {
            this.dataStoreId = dataStoreId;
            return this;
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

        @Override
        protected DataResourceValues doBuild() {
            return new DataResourceValues(
                            testClass,
                            dataSetName,
                            dataStoreId,
                            ResourcePathUtils.resourcePath(resourceName, testClass),
                            failOnError,
                            transactional,
                            additionalValues
                    );
        }

        @Override
        protected void checkMandatoryFields() {
            checkMandatoryField(this.testClass, "testClass");
            checkMandatoryField(this.dataSetName, "dataSetName");
            checkMandatoryField(this.dataStoreId, "dataStoreId");
            checkMandatoryField(this.resourceName, "resourceName");
        }
    }
}
