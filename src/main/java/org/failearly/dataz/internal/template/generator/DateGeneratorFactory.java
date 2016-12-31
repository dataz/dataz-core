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
 * DateGeneratorFactory creates a {@link Generator} from {@link DateGenerator} annotation.
 */
public final class DateGeneratorFactory extends GeneratorFactoryBase<DateTime, DateGenerator> {
    public DateGeneratorFactory() {
        super(DateGenerator.class);
    }


    @Override
    protected TemplateObject doCreate(TemplateObjectAnnotationContext context, DateGenerator annotation) {
        return doCreateGenerator(context, annotation, annotation.limit());
    }

    @Override
    protected LimitedGeneratorBase<DateTime> doCreateLimitedGenerator(TemplateObjectAnnotationContext context, DateGenerator generatorAnnotation, Integer limitValue) {
        return new DateGeneratorImpl(context, generatorAnnotation);
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

        private DateGeneratorImpl(TemplateObjectAnnotationContext context, DateGenerator annotation) {
            super(context, annotation);
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
            return new MillisecondsGenerator(context, annotation, decoder);
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
        private MillisecondsGenerator(TemplateObjectAnnotationContext context, DateGenerator annotation, DateDecoder decoder) {
            super(context, annotation,
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
