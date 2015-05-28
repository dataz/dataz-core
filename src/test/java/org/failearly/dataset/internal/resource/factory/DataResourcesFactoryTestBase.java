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
package org.failearly.dataset.internal.resource.factory;

import org.failearly.dataset.config.Constants;
import org.failearly.dataset.internal.model.TestMethodImplSetupHandlerTest;
import org.failearly.dataset.internal.template.TemplateObjectsTestHelper;
import org.failearly.dataset.resource.DataResource;
import org.failearly.dataset.resource.DataResourcesFactory;
import org.failearly.dataset.resource.GenericDataResourcesFactory;
import org.failearly.dataset.test.FakeDataStoreRule;
import org.failearly.dataset.test.TestUtils;
import org.junit.ClassRule;
import org.junit.rules.TestRule;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.List;

import static org.failearly.dataset.test.AnnotationInstanceResolver.annotationResolver;

/**
 * DataResourcesFactoryTestBase provides support functionality for {@link org.failearly.dataset.resource.GenericDataResourcesFactory} based
 * implementations of {@link DataResourcesFactory}.
 */
public class DataResourcesFactoryTestBase<T extends Annotation, R extends GenericDataResourcesFactory<T>> {
    public static final String OTHER_DATASTORE_ID = "OTHER-DATASTORE";

    @ClassRule
    public static final TestRule fakeDataStoreRule = FakeDataStoreRule.createFakeDataStoreRule(TestMethodImplSetupHandlerTest.class)
            .addDataStore(Constants.DATASET_DEFAULT_DATASTORE_ID)
            .addDataStore(OTHER_DATASTORE_ID);
    public static final String OTHER_DATA_SET_NAME = "other-data-set-name";

    private final Class<T> annotationClass;
    private final R dataResourcesFactory;
    private final Class<?> testSubjectClass;

    public DataResourcesFactoryTestBase(Class<T> annotationClass, R dataResourcesFactory, Class<?> testSubjectClass) {
        this.annotationClass = annotationClass;
        this.dataResourcesFactory = dataResourcesFactory;
        this.testSubjectClass = testSubjectClass;
    }

    private Method resolveMethod(String methodName) throws NoSuchMethodException {
        return TestUtils.resolveMethodFromClass(methodName, testSubjectClass);
    }

    private T resolveAnnotation(String methodName) {
        return annotationResolver(annotationClass) //
                    .fromClass(testSubjectClass)
                    .fromMethodName(methodName)
                    .build();

    }

    protected List<DataResource> createDataResourcesFromMethod(String methodName) throws NoSuchMethodException {
        return dataResourcesFactory.createDataResources(resolveMethod(methodName), resolveAnnotation(methodName), TemplateObjectsTestHelper.noTemplateObjects());
    }
}
