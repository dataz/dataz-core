/*
 * dataZ - Test Support For Data Stores.
 *
 * Copyright (C) 2014-2016 'Marko Umek' (http://fail-early.com)
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

package org.failearly.dataz.internal.resource.factory;

import org.failearly.common.test.utils.ReflectionUtils;
import org.failearly.dataz.NamedDataStore;
import org.failearly.dataz.config.Constants;
import org.failearly.dataz.config.DataSetProperties;
import org.failearly.dataz.datastore.DataStore;
import org.failearly.dataz.datastore.DataStoreBase;
import org.failearly.dataz.datastore.DataStoreException;
import org.failearly.dataz.internal.template.TemplateObjectsResolver;
import org.failearly.dataz.resource.DataResource;
import org.failearly.dataz.resource.DataResourcesFactory;
import org.failearly.dataz.resource.GenericDataResourcesFactory;
import org.failearly.dataz.resource.TypedDataResourcesFactory;
import org.failearly.dataz.test.CoreTestUtils;
import org.failearly.dataz.test.DataResourceMatcherBuilder;
import org.failearly.dataz.test.datastore.AdhocDataStore;
import org.hamcrest.Matcher;
import org.junit.AfterClass;
import org.junit.BeforeClass;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.List;

import static org.failearly.dataz.test.AnnotationInstanceResolver.annotationResolver;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.contains;
import static org.junit.Assert.assertThat;

/**
 * DataResourcesFactoryTestBase provides support functionality for {@link GenericDataResourcesFactory} based
 * implementations of {@link DataResourcesFactory}.
 */
public abstract class DataResourcesFactoryTestBase<T extends Annotation, R extends TypedDataResourcesFactory<T>> {
    protected static final String OTHER_DATA_SET_NAME = "other-data-set-name";
    private static final String OTHER_DATASTORE_ID = "OTHER-DATASTORE";

    private final Class<T> annotationClass;
    private final R dataResourcesFactory;
    protected final Class<?> testFixtureClass;

    @BeforeClass
    public static void setUp() throws Exception {
        System.setProperty(Constants.DATAZ_PROPERTY_DEFAULT_DATA_STORE, DefaultDataStore.class.getName());
        System.setProperty(Constants.DATAZ_PROPERTY_DEFAULT_SETUP_SUFFIX, "setup");
        System.setProperty(Constants.DATAZ_PROPERTY_DEFAULT_CLEANUP_SUFFIX, "cleanup");
        DataSetProperties.reload();
    }

    @AfterClass
    public static void tearDown() throws Exception {
        System.clearProperty(Constants.DATAZ_PROPERTY_DEFAULT_DATA_STORE);
        System.clearProperty(Constants.DATAZ_PROPERTY_DEFAULT_SETUP_SUFFIX);
        System.clearProperty(Constants.DATAZ_PROPERTY_DEFAULT_CLEANUP_SUFFIX);
        DataSetProperties.reload();
    }

    public DataResourcesFactoryTestBase(Class<T> annotationClass, R dataResourcesFactory, Class<?> testFixtureClass) {
        this.annotationClass = annotationClass;
        this.dataResourcesFactory = dataResourcesFactory;
        this.testFixtureClass = testFixtureClass;
    }

    private Method resolveMethod(String methodName) throws NoSuchMethodException {
        return ReflectionUtils.resolveMethodFromClass(methodName, testFixtureClass);
    }

    private T resolveAnnotation(String methodName) {
        return annotationResolver(annotationClass) //
                .fromClass(testFixtureClass)   //
                .fromMethodName(methodName)    //
                .build();

    }

    protected List<DataResource> createDataResourcesFromMethod(String methodName) throws NoSuchMethodException {
        final Method testMethod = resolveMethod(methodName);
        return dataResourcesFactory.createDataResources(
                testMethod,
                resolveAnnotation(methodName),
                TemplateObjectsResolver.resolveFromMethod(testMethod)
        );
    }

    @SafeVarargs
    protected static void assertResolvedDataResources(List<DataResource> dataResources, Matcher<DataResource>... expectedResources) {
        assertThat("Data Resources matches and in correct order?", dataResources, contains(expectedResources));
    }

    protected static void assertDataResourceContent(DataResource dataResource, String expectedContent) {
        assertThat("Data Resource expected content?", CoreTestUtils.inputStreamToString(dataResource.open()), is(expectedContent));
    }

    protected final Matcher<DataResource> isDefaultDataResource(String defaultResourceName) {
        return DataResourceMatcherBuilder.createWithDefaults()
            .withResource(this.testFixtureClass, defaultResourceName)
            .build();
    }

    protected final Matcher<DataResource> isPartlyCustomizedDataResource(String resource) {
        return DataResourceMatcherBuilder.createWithDefaults()
            .withResource(this.testFixtureClass, resource)
            .build();
    }

    protected final Matcher<DataResource> isDataResourceWithNamedDataStore(String resource, Class<? extends NamedDataStore> namedDataStores) {
        return DataResourceMatcherBuilder.createWithDefaults()
            .withNamedDataStore(namedDataStores)
            .withResource(resource)
            .build();
    }


    @SuppressWarnings("WeakerAccess")
    @AdhocDataStore(name = OTHER_DATASTORE_ID, implementation = TestDataStore.class)
    protected static class OtherDataStore extends NamedDataStore {
    }

    @SuppressWarnings("WeakerAccess")
    @AdhocDataStore(implementation = TestDataStore.class)
    protected static class DefaultDataStore extends NamedDataStore {
    }

    private static class TestDataStore extends DataStoreBase {
        @SuppressWarnings("unused")
        static DataStore createDataStore(Class<? extends NamedDataStore> namedDataStore, AdhocDataStore annotation) {
            return new TestDataStore(namedDataStore, annotation);
        }

        private TestDataStore(Class<? extends NamedDataStore> namedDataStore, AdhocDataStore annotation) {
            super(namedDataStore, annotation);
        }

        @Override
        public void initialize() throws DataStoreException {
            // nothing to do
        }

        @Override
        protected void doApplyResource(DataResource dataResource) throws DataStoreException {
            // nothing to do
        }
    }
}
