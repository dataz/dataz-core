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

package org.failearly.dataz.internal.template.support.test.message.basic;

import org.apache.commons.lang.StringUtils;
import org.failearly.dataz.internal.common.internal.message.MessageArgumentsImpl;
import org.failearly.dataz.internal.common.message.Message;
import org.failearly.dataz.internal.common.message.MessageArguments;
import org.failearly.dataz.internal.common.message.MessageBuilderBase;
import org.failearly.dataz.internal.common.message.TemplateParameters;
import org.failearly.dataz.template.TemplateObject;
import org.failearly.dataz.template.TemplateObjectFactory;
import org.failearly.dataz.template.support.test.TemplateObjectTestBase;

import java.lang.annotation.Annotation;
import java.util.Objects;

import static org.failearly.dataz.internal.template.support.test.message.basic.AbstractTemplateObjectMessage.ARG_TEST_CLASS;

/**
 * AbstractTemplateObjectMessage is responsible for ...
 */
@SuppressWarnings("WeakerAccess")
@TemplateParameters({ARG_TEST_CLASS})
public abstract class AbstractTemplateObjectMessage<T extends AbstractTemplateObjectMessage<T>>
        extends MessageBuilderBase<T>
        implements TemplateObjectMessage {

    public static final String ARG_TEMPLATE_OBJECT_FACTORY    = "TOF";
    public static final String ARG_TEMPLATE_OBJECT_ANNOTATION = "TOA";
    public static final String ARG_TEST_FIXTURE = "tf";
    public static final String ARG_TEMPLATE_OBJECT = "TO";
    public static final String ARG_TEST_CLASS = "tc";

    private static final String _ARG_BASE_NAME = "_baseName";
    private static final String _ARG_TEST_BASE_CLASS = "tbc";
    private static final String _ARG_TEST_BASE_SIMPLE_NAME = "stbc";
    private static final String _ARG_DEV_TEST_BASE  = "dtbc";
    private static final String _ARG_NORM_TEST_BASE  = "ntbc";
    private static final String _ARG_TOFD = "TOFD";
    private static final String UNKNOWN = "<Unknown>";
    private static final String _ARG_FULL_TEMPLATE_OBJECT = "fullTO";

    protected AbstractTemplateObjectMessage(Class<T> messageBuilderClass) {
        super(messageBuilderClass);
    }

    @Override
    protected void standardArgumentsHook(MessageArguments messageArguments) {
        messageArguments
                .addMandatoryArgument("snippetBegin", "8><------------")
                .addMandatoryArgument("snippetEnd",   "------------><8")
                .addMandatoryArgument("ommitted", "// ... (ommitted for brevity)")
                .addMandatoryArgument("ommitted2", "/* ommitted for brevity */")
                .addMandatoryArgument(_ARG_TOFD, stripPackageName(TemplateObjectFactory.Definition.class))
                .addOptionalArgument("subject", "org.failearly.dataz.common.test.annotations.Subject")
                .addOptionalArgument(ARG_TEST_FIXTURE, "TestFixture");

        messageArguments.addDerivedArgument(
                _ARG_BASE_NAME,
                this::generateBaseName
        );

        messageArguments.addDerivedArgument(
                ARG_TEMPLATE_OBJECT_ANNOTATION,
                this::baseName
        );

        messageArguments.addDerivedArgument(ARG_TEMPLATE_OBJECT_FACTORY, (MessageArguments.Accessor accessor) ->
                baseName(accessor) + "Factory"
        );

        messageArguments.addDerivedArgument(ARG_TEMPLATE_OBJECT, (MessageArguments.Accessor accessor) ->
                baseName(accessor) + "Impl"
        );


        messageArguments.addDerivedArgument(_ARG_FULL_TEMPLATE_OBJECT, (MessageArguments.Accessor accessor) ->
                accessor.getValue(ARG_TEMPLATE_OBJECT_FACTORY, String.class, UNKNOWN) + "." +
                accessor.getValue(ARG_TEMPLATE_OBJECT, String.class, UNKNOWN)
        );
    }

    private String stripPackageName(Class<?> clazz) {
        return StringUtils.removeStart(clazz.getCanonicalName(), clazz.getPackage().getName()+".");
    }

    private String baseName(MessageArguments.Accessor accessor) {
        return accessor.getValue(_ARG_BASE_NAME, String.class, UNKNOWN);
    }

    private String generateBaseName(MessageArgumentsImpl.Accessor accessor) {
        return StringUtils.removeEnd(
                            accessor.getValue(ARG_TEST_CLASS, String.class, UNKNOWN),
                            "Test"
            );
    }

    @Override
    public T withTestClass(TemplateObjectTestBase testObject) {
        Objects.requireNonNull(testObject, "null is not permitted");
        final Class<? extends TemplateObjectTestBase> testClass = testObject.getClass();
        final Class<?> testBaseClass = testClass.getSuperclass();
        final String stbc = testBaseClass.getSimpleName();
        final String ntbc = stbc.replaceFirst("Development", "");

        return  this.with(ARG_TEST_CLASS, testClass.getSimpleName())
                    .with(_ARG_TEST_BASE_CLASS, testBaseClass)
                    .with(_ARG_TEST_BASE_SIMPLE_NAME, stbc)
                    .with(_ARG_NORM_TEST_BASE, ntbc)
                    .with(_ARG_DEV_TEST_BASE, "Development"+ ntbc);
    }

    @Override
    public final T withTemplateObjectAnnotationClass(Class<? extends Annotation> annotationClass) {
        Objects.requireNonNull(annotationClass, "null is not permitted");
        return with(ARG_TEMPLATE_OBJECT_ANNOTATION, annotationClass.getSimpleName());
    }

    @Override
    public final T withTemplateObjectFactoryClass(Class<? extends TemplateObjectFactory> templateObjectFactoryClass) {
        Objects.requireNonNull(templateObjectFactoryClass, "null is not permitted");
        return with(ARG_TEMPLATE_OBJECT_FACTORY, templateObjectFactoryClass.getSimpleName());
    }

    @Override
    public final T withTestFixtureClass(Class<?> testFixtureClass) {
        Objects.requireNonNull(testFixtureClass, "null is not permitted");
        return with(ARG_TEST_FIXTURE, testFixtureClass.getSimpleName());
    }

    @Override
    public final T withTemplateObjectClass(Class<? extends TemplateObject> templateObjectClass) {
        Objects.requireNonNull(templateObjectClass, "null is not permitted");
        return with(ARG_TEMPLATE_OBJECT, templateObjectClass.getSimpleName())
               .with(_ARG_FULL_TEMPLATE_OBJECT, stripPackageName(templateObjectClass));
    }

    public final Message buildLazyMessage(TemplateObjectMessage.Initializer initializer) {
        return super.buildLazyMessage(messageBuilder -> {
            initializer.init(messageBuilder); return messageBuilder;
        });
    }
}
