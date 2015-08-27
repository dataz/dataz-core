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

package org.failearly.dataset.template.generator;

import org.failearly.dataset.DataSet;
import org.failearly.dataset.config.Constants;
import org.failearly.dataset.template.Scope;
import org.failearly.dataset.template.TemplateObjectFactoryDefinition;
import org.failearly.dataset.internal.template.generator.RandomBooleanGeneratorFactory;

import java.lang.annotation.*;

/**
 * RandomBooleanGenerator generates a random stream of {@code true} and {@code false}. It's only available as
 * {@link org.failearly.dataset.template.generator.Limit#UNLIMITED} generator.
 * Usage Example:<br><br>
 * <pre>
 *   {@literal @Test}
 *   {@literal @}{@link DataSet}(dataset="d1", setup="t1.vm") // content see below
 *   {@literal @}{@link DataSet}(dataset="d2", setup="t2.vm") // content see below
 *   {@literal @}RandomBooleanGenerator(name="ten", dataset="d1", percent=10, seed=314)
 *   {@literal @}RandomBooleanGenerator(name="eighty", dataset="d2", percent=80, seed=42)
 *    public void my_test() {
 *        // The 'ten' with template t1.vm generates:
 *        // 0, 0, 0, 0, 0, 1, 0, 1, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0,
 *
 *        // The 'eighty' with template t2.vm generates:
 *        // true, true, true, false, true, true, true, true, false, true,
 *    }
 * </pre>
 * Content of the templates:<br><br>
 * <pre>
 *    // Content of t1.vm
 *    #foreach( $i in [1 .. 20] )if(${ten.next()}) 1,#else 0,#end#end
 *
 *    // Content of t2.vm
 *    #foreach( $i in [1 .. 10] )${eighty.next()}#end
 * </pre>
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Repeatable(RandomBooleanGenerator.RandomBooleanGenerators.class)
@TemplateObjectFactoryDefinition(factory = RandomBooleanGeneratorFactory.class)
public @interface RandomBooleanGenerator {
    /**
     * @return The name of the template object. Could be used in Velocity templates by {@code $<name>}.
     */
    String name();

    /**
     * @return The name of the associated dataset.
     *
     * @see DataSet#name()
     */
    String dataset() default Constants.DATASET_DEFAULT_NAME;

    /**
     * @return The scope of the template object (either {@link Scope#LOCAL} or {@link Scope#GLOBAL}.
     */
    Scope scope() default Scope.DEFAULT;

    /**
     * @return the percentage ({@code >0} and {@code < 100}) of {@code true} values.
     */
    float percent();

    /**
     * Seed value for {@link java.util.Random}.
     *
     * @return the seed value or {@link org.failearly.dataset.template.generator.GeneratorConstants#DEFAULT_SEED}.
     *
     * @see GeneratorConstants#DEFAULT_SEED
     * @see java.util.Random#seed
     */
    int seed() default GeneratorConstants.DEFAULT_SEED;

    /**
     * Containing Annotation Type.
     *
     * Remark: This will be used by Java8 compiler.
     *
     * @see java.lang.annotation.Repeatable
     */
    @Target({ElementType.METHOD, ElementType.TYPE})
    @Retention(RetentionPolicy.RUNTIME)
    @Documented
    @interface RandomBooleanGenerators {
        RandomBooleanGenerator[] value();
    }
}
