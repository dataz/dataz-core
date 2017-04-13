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

package org.failearly.dataz.internal.template.generator;

import org.failearly.dataz.template.Scope;
import org.failearly.dataz.template.TemplateObject;
import org.failearly.dataz.template.TemplateObjectAnnotationContext;
import org.failearly.dataz.template.generator.DateGenerator;
import org.failearly.dataz.template.generator.Generator;
import org.failearly.dataz.template.generator.PredefinedTimeZones;
import org.failearly.dataz.template.generator.support.DateTime;
import org.failearly.dataz.template.generator.support.GeneratorFactoryBase;
import org.failearly.dataz.template.generator.support.LimitedGeneratorBase;
import org.failearly.dataz.template.generator.support.LongRangeGenerator;

import java.util.Iterator;

/**
 * DateGeneratorFactory creates a {@link Generator} from {@link DateGenerator} impl.
 */
public final class DateGeneratorFactory extends GeneratorFactoryBase<DateTime, DateGenerator> {
    public DateGeneratorFactory() {
        super(DateGenerator.class);
    }


    @Override
    protected TemplateObject doCreate(DateGenerator annotation, TemplateObjectAnnotationContext context) {
        return doCreateGenerator(annotation, context, annotation.limit());
    }

    @Override
    protected LimitedGeneratorBase<DateTime> doCreateLimitedGenerator(DateGenerator generatorAnnotation, TemplateObjectAnnotationContext context, Integer limitValue) {
        return new DateGeneratorImpl(generatorAnnotation, context);
    }

    @Override
    protected String doResolveName(DateGenerator annotation) {
        return annotation.name();
    }

    @Override
    protected String[] doResolveDataSetNames(DateGenerator annotation) {
        return annotation.datasets();
    }

    @Override
    protected Scope doResolveScope(DateGenerator annotation) {
        return annotation.scope();
    }

    // Public for Velocity
    public static class DateGeneratorImpl extends LimitedGeneratorBase<DateTime> {
        private final MillisecondsGenerator millisecondsGenerator;
        private final DateEncoder encoder;

        private DateGeneratorImpl(DateGenerator annotation, TemplateObjectAnnotationContext context) {
            super(annotation, context);
            this.encoder = createDateEncoder(annotation);
            this.millisecondsGenerator = createMillisecondsGenerator(context, annotation);
        }

        private DateEncoder createDateEncoder(DateGenerator annotation) {
            if (PredefinedTimeZones.USE_DEFAULT.equals(annotation.timeZone())) {
                return new DateEncoder(annotation.format());
            }

            return new DateEncoder(annotation.format(), annotation.timeZone());
        }

        private MillisecondsGenerator createMillisecondsGenerator(TemplateObjectAnnotationContext context, DateGenerator annotation) {
            final DateDecoder decoder = new DateDecoder(annotation.format());
            return new MillisecondsGenerator(annotation, context, decoder);
        }

        @Override
        public Iterator<DateTime> createIterator() {
            return new Iterator<DateTime>() {
                private final Iterator<Long> iterator = millisecondsGenerator.iterator();

                @Override
                public boolean hasNext() {
                    return iterator.hasNext();
                }

                @Override
                public DateTime next() {
                    return new DateTime(encoder, iterator.next());
                }
            };
        }
    }

    private static class MillisecondsGenerator extends LongRangeGenerator {
        private MillisecondsGenerator(DateGenerator annotation, TemplateObjectAnnotationContext context, DateDecoder decoder) {
            super(annotation, context,
                start(decoder, annotation),
                end(decoder, annotation),
                step(annotation)
            );
        }

        private static long start(DateDecoder decoder, DateGenerator annotation) {
            return decoder.toDate(annotation.from()).getTime();
        }

        private static long end(DateDecoder decoder, DateGenerator annotation) {
            return decoder.toDate(annotation.to()).getTime();
        }

        private static long step(DateGenerator annotation) {
            return annotation.unit().next(annotation.step());
        }
    }
}
