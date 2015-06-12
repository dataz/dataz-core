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

/**
 * This package contains some standard {@link org.failearly.dataset.template.generator.support.Generator} annotations.
 * <br><br>
 * A {@code Generator} could be used with a {@link org.failearly.dataset.DataSet#setup()} or {@link org.failearly.dataset.DataSet#cleanup()}
 * resource template. A resource template will be identified by the file names suffix
 * ({@link org.failearly.dataset.config.Constants#DATASET_PROPERTY_TEMPLATE_SUFFIX}. If the resource is not a template your specified
 * generator won't have any effect.
 * <br><br>
 * An example:<br><br>
 * <pre>
 *    {@literal @DataSet}(name="DS", setup={"users.setup.vm", "none-template.setup"}
 *    {@literal @}{@link org.failearly.dataset.template.generator.ListGenerator}(name="fname", dataset="DS1", values={"Smith","Miller","White","Heisenberg"})
 *    {@literal @}{@link org.failearly.dataset.template.generator.ListGenerator}(name="cname", dataset="DS1", values={"Frank","Walter","Skyla"})
 *    {@literal @}{@link org.failearly.dataset.template.generator.RandomRangeGenerator}(name="ids", dataset="DS1", unique=true, seed=1, end=100_000)
 *    public class MyTest {
 *        // ... omitted for brevity
 *    }
 * </pre>
 * and here the content of template file: {@code DS1.setup.vm}
 * <pre>
 * #foreach( $fn in $fname )
 *    #foreach( $cn in $cname )
 *          INSERT INTO USERS (ID, NAME) VALUES ($ids.next(), "$cn $fn");
 *    #end
 * #end
 * </pre>
 * The generators {@code fname}, {@code cname} and {@code ids} will only applied on {@code DS1.setup.vm}, but not on {@code none-template.setup}.
 * <br><br>
 * It's possible to
 * <a href="{@docRoot}/org/failearly/dataset/generator/resource/package-summary.html#package_description">newInstance your own customized generator</a>.
 *
 * @see org.failearly.dataset.template.TemplateEngine
 * @see org.failearly.dataset.DataSet
 * @see <a href="{@docRoot}/org/failearly/dataset/generator/resource/package-summary.html#package_description">generator resource</a>
 * @see <a href="http://velocity.apache.org/engine/releases/velocity-1.7/">Velocity 1.7</a>
 */
package org.failearly.dataset.template.generator;