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
package org.failearly.dataset.internal.annotation;

/**
* TraverseDirection defines strategies, how the {@link org.failearly.dataset.internal.annotation.AnnotationTraverser} will work.
 *
 * @see AnnotationTraversers#createAnnotationTraverser(Class, TraverseStrategy, TraverseDepth)
 * @see AnnotationTraversers#createMetaAnnotationTraverser(Class, TraverseStrategy, TraverseDepth)
*/
public enum TraverseStrategy {
    /**
     * Traverse from current test method or class to the base classes.
     * <br><br>
     * Example with {@link org.failearly.dataset.DataSet}:<br><br>
     * <pre>
     *     {@literal @DataSet(name="DS0", datastore="mysql")}
     *     public class MySuperClass {}
     *
     *     {@literal @DataSet(name="DS1")}
     *     public class MyMiddleBaseClass extends MySuperClass {}
     *
     *     {@literal @DataSet(name="DS2.0")}
     *     {@literal @DataSet(name="DS2.1")}
     *     public class MyClass extends MyMiddleBaseClass {
     *
     *       {@literal @DataSet(name="myTest")}
     *       {@literal @DataSet(name="DS0", datastore="oracle")}
     *       public void myTest();
     *     }
     * </pre>
     * <br><br>
     * Traversing with this strategy will result in:
     * <br><br>
     * <ol>
     *    <li>{@literal @DataSet(name="myTest")}</li>
     *    <li>{@literal @DataSet(name="DS0", datastore="oracle")}</li>
     *    <li>{@literal @DataSet(name="DS2.0")}: Caution! On the same annotated element the order will be kept.</li>
     *    <li>{@literal @DataSet(name="DS2.1")}</li>
     *    <li>{@literal @DataSet(name="DS1")}</li>
     *    <li>{@literal @DataSet(name="DS0", datastore="mysql")}</li>
     * </ol>
     */
    TOP_DOWN,

    /**
     * Traverse from base classes to current test method or class. So it's a depth first strategy.
     * <br><br>
     * Example with {@link org.failearly.dataset.DataSet}:<br><br>
     * <pre>
     *     {@literal @DataSet(name="DS0", datastore="mysql")}
     *     public class MySuperClass {}
     *
     *     {@literal @DataSet(name="DS1")}
     *     public class MyMiddleBaseClass extends MySuperClass {}
     *
     *     {@literal @DataSet(name="DS2.0")}
     *     {@literal @DataSet(name="DS2.1")}
     *     public class MyClass extends MyMiddleBaseClass {
     *
     *       {@literal @DataSet(name="myTest")}
     *       {@literal @DataSet(name="DS0", datastore="oracle")}
     *       public void myTest();
     *     }
     * </pre>
     * <br><br>
     * Traversing with this strategy will result in:
     * <br><br>
     * <ol>
     *    <li>{@literal @DataSet(name="DS0", datastore="mysql")}</li>
     *    <li>{@literal @DataSet(name="DS1")}</li>
     *    <li>{@literal @DataSet(name="DS2.0")}: Caution! On the same annotated element the order will be kept.</li>
     *    <li>{@literal @DataSet(name="DS2.1")}</li>
     *    <li>{@literal @DataSet(name="myTest")}</li>
     *    <li>{@literal @DataSet(name="DS0", datastore="oracle")}</li>
     * </ol>
     */
    BOTTOM_UP
}
