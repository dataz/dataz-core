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

package org.failearly.dataz.template.generator.support;

import org.failearly.dataz.internal.template.generator.decorator.GeneratorDecorators;
import org.failearly.dataz.template.TemplateObject;
import org.failearly.dataz.template.TemplateObjectAnnotationContext;
import org.failearly.dataz.template.TemplateObjectFactoryBase;
import org.failearly.dataz.template.generator.Generator;
import org.failearly.dataz.template.generator.GeneratorConstants;
import org.failearly.dataz.template.generator.Limit;

import java.lang.annotation.Annotation;

/**
 * GeneratorFactoryBase is the base class for GeneratorFactory.
 */
public abstract class GeneratorFactoryBase<T, A extends Annotation> extends TemplateObjectFactoryBase<A> implements GeneratorConstants {

    private static final int ANY_LIMIT_VALUE = -1;

    protected GeneratorFactoryBase(Class<A> annotationClass) {
        super(annotationClass);
    }

    /**
     * Creates a {@link Generator} instance from given {@code generatorAnnotation}.
     * <p>
     * Either {@link #doCreateLimitedGenerator(Annotation, TemplateObjectAnnotationContext, Integer)} or
     * {@link #doCreateUnlimitedGenerator(Annotation, TemplateObjectAnnotationContext, Integer)} must be overridden.
     * <p>
     * A good indicator which one to override is the default value for {@code Limit limit()} in the generator impl.
     *
     *
     * @param generatorAnnotation the generator impl
     * @param context             the context of template object impl
     * @param limit               the limit type of the impl.
     * @param limitValue          the limit value used in case of an UNLIMITED generator which could be make to a LIMITED one.
     * @return the created (limited or unlimited) generator.
     */
    protected final TemplateObject doCreateGenerator(A generatorAnnotation, TemplateObjectAnnotationContext context, Limit limit, Integer limitValue) {
        final GeneratorBase<T> generator;
        if (limit.isLimited()) {
            generator = doCreateLimitedGenerator(generatorAnnotation, context, limitValue);
        } else {
            generator = doCreateUnlimitedGenerator(generatorAnnotation, context, limitValue);
        }

        generator.init();
        return generator;
    }

    /**
     * Creates a {@link Generator} instance from given {@code generatorAnnotation}.
     * <p>
     * Either {@link #doCreateLimitedGenerator(Annotation, TemplateObjectAnnotationContext, Integer)} or
     * {@link #doCreateUnlimitedGenerator(Annotation, TemplateObjectAnnotationContext, Integer)} must be overridden.
     * <p>
     * A good indicator which one to override is the default value for {@code limit()} in the generator impl.
     *
     *
     * @param generatorAnnotation the generator impl
     * @param context             the context of template object impl
     * @param limit               the limit type of the impl.
     * @return the created (limited or unlimited) generator.
     */
    protected final TemplateObject doCreateGenerator(A generatorAnnotation, TemplateObjectAnnotationContext context, Limit limit) {
        return doCreateGenerator(generatorAnnotation, context, limit, ANY_LIMIT_VALUE);
    }

    /**
     * Creates an generator associated from {@code generatorAnnotation}.
     * <p>
     * Either {@link #doCreateLimitedGenerator(Annotation, TemplateObjectAnnotationContext, Integer)} or
     * {@link #doCreateUnlimitedGenerator(Annotation, TemplateObjectAnnotationContext, Integer)} must be overridden.
     * <p>
     * A good indicator which one to override is the default value for {@code limit()} in the generator impl.
     *
     *
     * @param generatorAnnotation the generator impl
     * @param context             the context of template object impl
     * @param limitValue          the limit value used in case of an UNLIMITED generator which could be make to a LIMITED one.
     * @return the created (limited or unlimited) generator.
     */
    protected final TemplateObject doCreateUniqueGenerator(A generatorAnnotation, TemplateObjectAnnotationContext context, Integer limitValue) {
        final GeneratorBase<T> generator = GeneratorDecorators.makeUnique(
                doCreateUnlimitedGenerator(generatorAnnotation, context, limitValue),
                limitValue
        );

        generator.init();
        return generator;
    }

    /**
     * Create a limited generator. Override if the naturally implementation is {@link Limit#LIMITED}.
     *
     * @param generatorAnnotation the generator impl.
     * @param context             the context of template object impl
     * @param limitValue          limit value will be used by
     *                            {@link GeneratorDecorators#makeLimited(UnlimitedGeneratorBase, int)}
     *                            in {@link #doCreateUnlimitedGenerator(Annotation, TemplateObjectAnnotationContext, Integer)}
     * @return an decorated limited generator of an unlimited generator.
     * @see GeneratorDecorators#makeLimited(UnlimitedGeneratorBase, int)
     */
    protected LimitedGeneratorBase<T> doCreateLimitedGenerator(A generatorAnnotation, TemplateObjectAnnotationContext context, Integer limitValue) {
        return GeneratorDecorators.makeLimited(doCreateUnlimitedGenerator(generatorAnnotation, context, limitValue), limitValue);
    }

    /**
     * Create an unlimited generator. Override if the naturally implementation is {@link Limit#UNLIMITED}.
     *
     *
     * @param generatorAnnotation the generator impl.
     * @param context             the context of template object impl
     * @param limitValue          used by {@link #doCreateLimitedGenerator(Annotation, TemplateObjectAnnotationContext, Integer)}.
     * @return an decorated unlimited generator of the limited generator.
     * @see GeneratorDecorators#makeUnlimited(GeneratorBase)
     */
    protected UnlimitedGeneratorBase<T> doCreateUnlimitedGenerator(A generatorAnnotation, TemplateObjectAnnotationContext context, Integer limitValue) {
        return GeneratorDecorators.makeUnlimited(doCreateLimitedGenerator(generatorAnnotation, context, limitValue));
    }
}
