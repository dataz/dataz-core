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

/**
 * Off the shelf {@link org.failearly.dataz.template.generator.Generator} annotations.
 * <br><br>
 * A {@code Generator} could be used with a {@link org.failearly.dataz.DataSet#setup()} or {@link org.failearly.dataz.DataSet#cleanup()}
 * resource template. A resource template will be identified by the file names suffix.
 * ({@link org.failearly.dataz.config.Constants#DATAZ_PROPERTY_TEMPLATE_SUFFIX}. If the resource is not a template your specified
 * generator won't have any effect.
 * <br><br>
 * An example:<br><br>
 * <pre>
 *    {@literal @DataSet}(name="DS1", setup={"users.setup.vm", "none-template.setup"})
 *    {@literal @}{@link org.failearly.dataz.template.generator.ListGenerator}(name="fname", dataz="DS1", values={"Smith","Miller","White","Heisenberg"})
 *    {@literal @}{@link org.failearly.dataz.template.generator.ListGenerator}(name="cname", dataz="DS1", values={"Frank","Walter","Skyla"})
 *    {@literal @}{@link org.failearly.dataz.template.generator.RandomRangeGenerator}(name="ids", dataz="DS1", unique=true, seed=1, to=12)
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
 * @see org.failearly.dataz.DataSet
 * @see org.failearly.dataz.DataSetup
 * @see org.failearly.dataz.DataCleanup
 * @see <a href="{@docRoot}/org/failearly/dataz/template/generator/support/package-summary.html#package_description">generator support</a>
 * @see <a href="http://velocity.apache.org/engine/releases/velocity-1.7/">Velocity 1.7</a>
 */
package org.failearly.dataz.template.generator;