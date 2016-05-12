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

/**
 * Off the shelf {@link org.failearly.dataset.template.generator.Generator} annotations.
 * <br><br>
 * A {@code Generator} could be used with a {@link org.failearly.dataset.DataSet#setup()} or {@link org.failearly.dataset.DataSet#cleanup()}
 * resource template. A resource template will be identified by the file names suffix.
 * ({@link org.failearly.dataset.config.Constants#DATASET_PROPERTY_TEMPLATE_SUFFIX}. If the resource is not a template your specified
 * generator won't have any effect.
 * <br><br>
 * An example:<br><br>
 * <pre>
 *    {@literal @DataSet}(name="DS1", setup={"users.setup.vm", "none-template.setup"})
 *    {@literal @}{@link org.failearly.dataset.template.generator.ListGenerator}(name="fname", dataset="DS1", values={"Smith","Miller","White","Heisenberg"})
 *    {@literal @}{@link org.failearly.dataset.template.generator.ListGenerator}(name="cname", dataset="DS1", values={"Frank","Walter","Skyla"})
 *    {@literal @}{@link org.failearly.dataset.template.generator.RandomRangeGenerator}(name="ids", dataset="DS1", unique=true, seed=1, to=12)
 *    public class MyTest {
 *        // ... omitted for brevity
 *    }
 * </pre>
 * and here the content of template file: {@code users.setup.vm}
 * <pre>
 * #foreach( $fn in $fname )
 *    #foreach( $cn in $cname )
 *          INSERT INTO USERS (ID, NAME) VALUES ($ids.next(), "$cn $fn");
 *    #end
 * #end
 * </pre>
 * The generators {@code fname}, {@code cname} and {@code ids} will only applied on {@code users.setup.vm}, but not on {@code none-template.setup}.
 * <br><br>
 * It's possible to create your own generators, like any template object.
 *
 * @see org.failearly.dataset.DataSet
 * @see org.failearly.dataset.DataSetup
 * @see org.failearly.dataset.DataCleanup
 * @see <a href="{@docRoot}/org/failearly/dataset/template/generator/support/package-summary.html#package_description">generator support</a>
 * @see <a href="http://velocity.apache.org/engine/releases/velocity-1.7/">Velocity 1.7</a>
 */
package org.failearly.dataset.template.generator;