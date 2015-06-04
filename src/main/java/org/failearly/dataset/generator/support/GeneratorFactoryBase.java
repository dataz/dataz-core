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

package org.failearly.dataset.generator.support;

import org.failearly.dataset.generator.GeneratorConstants;
import org.failearly.dataset.generator.Limit;
import org.failearly.dataset.internal.generator.decorator.GeneratorDecorators;
import org.failearly.dataset.template.TemplateObject;
import org.failearly.dataset.template.TemplateObjectFactoryBase;

import java.lang.annotation.Annotation;
import java.util.function.Function;

/**
 * GeneratorFactoryBase is the base class for GeneratorFactory.
 */
public abstract class GeneratorFactoryBase<T, A extends Annotation> extends TemplateObjectFactoryBase<A> implements GeneratorConstants {

    protected GeneratorFactoryBase(Class<A> annotationClass) {
        super(annotationClass);
    }

    /**
     * Creates an generator associated from {@code generatorAnnotation}.
     * <p>
     * Either {@link #doCreateLimitedGenerator(java.lang.annotation.Annotation, Integer)} or
     * {@link #doCreateUnlimitedGenerator(java.lang.annotation.Annotation, Integer)} must be overridden.
     * <p>
     * A good indicator which one to override is the default value for {@code Limit limit()} in the generator annotation.
     *
     * @param generatorAnnotation the generatorAnnotation
     * @param limit               the limit value of the annotation.
     * @param limitClosure        the limit evaluation closure in case of {@link Limit#LIMITED}.
     * @return the created (limited or unlimited) generator.
     */
    protected final TemplateObject doCreateGenerator(A generatorAnnotation, Limit limit, Function<A, Integer> limitClosure) {
        final GeneratorBase<T> generator;
        if (limit.isLimited()) {
            generator = doCreateLimitedGenerator(generatorAnnotation, limitClosure.apply(generatorAnnotation));
        } else {
            generator = doCreateUnlimitedGenerator(generatorAnnotation, limitClosure.apply(generatorAnnotation));
        }

        return generator.init();
    }

    /**
     * Creates an generator associated from {@code generatorAnnotation}.
     * <p>
     * Either {@link #doCreateLimitedGenerator(java.lang.annotation.Annotation, Integer)} or
     * {@link #doCreateUnlimitedGenerator(java.lang.annotation.Annotation, Integer)} must be overridden.
     * <p>
     * A good indicator which one to override is the default value for {@code Limit limit()} in the generator annotation.
     *
     * @param generatorAnnotation the generatorAnnotation
     * @param limitClosure        the limit evaluation closure in case of {@link Limit#LIMITED}.
     * @return the created (limited or unlimited) generator.
     */
    protected final TemplateObject doCreateUniqueGenerator(A generatorAnnotation, Function<A, Integer> limitClosure) {
        final int limitValue = limitClosure.apply(generatorAnnotation);
        final GeneratorBase<T> generator = GeneratorDecorators.makeUnique(
                doCreateUnlimitedGenerator(generatorAnnotation, limitValue),
                limitValue
        );

        return generator.init();
    }

    /**
     * Create a limited generator. Override if the naturally implementation is limited.
     *
     * @param generatorAnnotation the generator annotation.
     * @param limitValue          limit value will be used by
     *                            {@link org.failearly.dataset.internal.generator.decorator.GeneratorDecorators#makeLimited(org.failearly.dataset.generator.support.UnlimitedGeneratorBase, int)}
     *                            in {@link #doCreateUnlimitedGenerator(java.lang.annotation.Annotation, Integer)}
     * @return an decorated limited generator of an unlimited generator.
     * @see org.failearly.dataset.internal.generator.decorator.GeneratorDecorators#makeLimited(org.failearly.dataset.generator.support.UnlimitedGeneratorBase, int)
     */
    protected LimitedGeneratorBase<T> doCreateLimitedGenerator(A generatorAnnotation, Integer limitValue) {
        return GeneratorDecorators.makeLimited(doCreateUnlimitedGenerator(generatorAnnotation, limitValue), limitValue);
    }

    /**
     * Create an unlimited generator. Override if the naturally implementation is unlimited.
     *
     * @param generatorAnnotation the generator annotation.
     * @param limitValue          used by {@link #doCreateLimitedGenerator(java.lang.annotation.Annotation, Integer)}.
     * @return an decorated unlimited generator of the limited generator.
     * @see org.failearly.dataset.internal.generator.decorator.GeneratorDecorators#makeUnlimited(org.failearly.dataset.generator.support.GeneratorBase)
     */
    protected UnlimitedGeneratorBase<T> doCreateUnlimitedGenerator(A generatorAnnotation, Integer limitValue) {
        return GeneratorDecorators.makeUnlimited(doCreateLimitedGenerator(generatorAnnotation, limitValue));
    }


    protected static <A extends Annotation> Function<A, Integer> unlimited() {
        return (a) -> 0;
    }
}
