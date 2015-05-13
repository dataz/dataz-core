/*
 * Copyright (c) 2009.
 *
 * Date: 12.05.15
 * 
 */
package org.failearly.dataset.internal.resource.factory;

import org.failearly.dataset.DataSetup;
import org.failearly.dataset.config.Constants;
import org.failearly.dataset.internal.generator.resolver.GeneratorCreator;
import org.failearly.dataset.internal.model.TestMethodImplSetupHandlerTest;
import org.failearly.dataset.resource.DataResource;
import org.failearly.dataset.resource.DataResourcesFactory;
import org.failearly.dataset.test.FakeDataStoreRule;
import org.failearly.dataset.test.TestUtils;
import org.junit.ClassRule;
import org.junit.rules.TestRule;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.List;

import static org.failearly.dataset.test.AnnotationInstanceResolver.annotationResolver;

/**
 * DataResourcesFactoryTestBase is responsible for ...
 */
public class DataResourcesFactoryTestBase<T extends Annotation, R extends DataResourcesFactory> {
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
        return dataResourcesFactory.createDataResources(resolveMethod(methodName), resolveAnnotation(methodName), Collections.<GeneratorCreator>emptyList());
    }
}
