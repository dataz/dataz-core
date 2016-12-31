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

package org.failearly.dataz.template.generator.support;

import org.failearly.dataz.internal.template.generator.decorator.GeneratorDecorators;
import org.failearly.dataz.template.TemplateObject;
import org.failearly.dataz.template.TemplateObjectFactoryBase;
import org.failearly.dataz.template.generator.Generator;
import org.failearly.dataz.template.generator.GeneratorConstants;
import org.failearly.dataz.template.generator.Limit;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;

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
     * Either {@link #doCreateLimitedGenerator(AnnotatedElement, Annotation, Integer)} or
     * {@link #doCreateUnlimitedGenerator(AnnotatedElement, Annotation, Integer)} must be overridden.
     * <p>
     * A good indicator which one to override is the default value for {@code Limit limit()} in the generator annotation.
     *
     *
     * @param annotatedElement    the annotated element (class, method)
     * @param generatorAnnotation the generator annotation
     * @param limit               the limit type of the annotation.
     * @param limitValue          the limit value used in case of an UNLIMITED generator which could be make to a LIMITED one.
     * @return the created (limited or unlimited) generator.
     */
    protected final TemplateObject doCreateGenerator(AnnotatedElement annotatedElement, A generatorAnnotation, Limit limit, Integer limitValue) {
        final GeneratorBase<T> generator;
        if (limit.isLimited()) {
            generator = doCreateLimitedGenerator(annotatedElement, generatorAnnotation, limitValue);
        } else {
            generator = doCreateUnlimitedGenerator(annotatedElement, generatorAnnotation, limitValue);
        }

        generator.init();
        return generator;
    }

    /**
     * Creates a {@link Generator} instance from given {@code generatorAnnotation}.
     * <p>
     * Either {@link #doCreateLimitedGenerator(AnnotatedElement, Annotation, Integer)} or
     * {@link #doCreateUnlimitedGenerator(AnnotatedElement, Annotation, Integer)} must be overridden.
     * <p>
     * A good indicator which one to override is the default value for {@code limit()} in the generator annotation.
     *
     *
     * @param annotatedElement    the annotated element (class, method)
     * @param generatorAnnotation the generator annotation
     * @param limit               the limit type of the annotation.
     * @return the created (limited or unlimited) generator.
     */
    protected final TemplateObject doCreateGenerator(AnnotatedElement annotatedElement, A generatorAnnotation, Limit limit) {
        return doCreateGenerator(annotatedElement, generatorAnnotation, limit, ANY_LIMIT_VALUE);
    }

    /**
     * Creates an generator associated from {@code generatorAnnotation}.
     * <p>
     * Either {@link #doCreateLimitedGenerator(AnnotatedElement, Annotation, Integer)} or
     * {@link #doCreateUnlimitedGenerator(AnnotatedElement, Annotation, Integer)} must be overridden.
     * <p>
     * A good indicator which one to override is the default value for {@code limit()} in the generator annotation.
     *
     *
     * @param annotatedElement the annotated element (class, method)
     * @param generatorAnnotation the generator annotation
     * @param limitValue          the limit value used in case of an UNLIMITED generator which could be make to a LIMITED one.
     * @return the created (limited or unlimited) generator.
     */
    protected final TemplateObject doCreateUniqueGenerator(AnnotatedElement annotatedElement, A generatorAnnotation, Integer limitValue) {
        final GeneratorBase<T> generator = GeneratorDecorators.makeUnique(
                doCreateUnlimitedGenerator(annotatedElement, generatorAnnotation, limitValue),
                limitValue
        );

        generator.init();
        return generator;
    }

    /**
     * Create a limited generator. Override if the naturally implementation is {@link Limit#LIMITED}.
     *
     *
     * @param annotatedElement the annotated element (class, method)
     * @param generatorAnnotation the generator annotation.
     * @param limitValue          limit value will be used by
     *                            {@link GeneratorDecorators#makeLimited(UnlimitedGeneratorBase, int)}
     *                            in {@link #doCreateUnlimitedGenerator(AnnotatedElement, Annotation, Integer)}
     * @return an decorated limited generator of an unlimited generator.
     * @see GeneratorDecorators#makeLimited(UnlimitedGeneratorBase, int)
     */
    protected LimitedGeneratorBase<T> doCreateLimitedGenerator(AnnotatedElement annotatedElement, A generatorAnnotation, Integer limitValue) {
        return GeneratorDecorators.makeLimited(doCreateUnlimitedGenerator(annotatedElement, generatorAnnotation, limitValue), limitValue);
    }

    /**
     * Create an unlimited generator. Override if the naturally implementation is {@link Limit#UNLIMITED}.
     *
     * @param annotatedElement the annotated element (class, method)
     * @param generatorAnnotation the generator annotation.
     * @param limitValue          used by {@link #doCreateLimitedGenerator(AnnotatedElement, Annotation, Integer)}.
     * @return an decorated unlimited generator of the limited generator.
     * @see GeneratorDecorators#makeUnlimited(GeneratorBase)
     */
    protected UnlimitedGeneratorBase<T> doCreateUnlimitedGenerator(AnnotatedElement annotatedElement, A generatorAnnotation, Integer limitValue) {
        return GeneratorDecorators.makeUnlimited(doCreateLimitedGenerator(annotatedElement, generatorAnnotation, limitValue));
    }

}
