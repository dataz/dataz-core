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

package org.failearly.dataset.internal.annotation.repeatable;

import org.failearly.dataset.internal.annotation.AnyOtherAnnotation;

@RepeatableAnnotation(name = "ClassHierarchy")
@AnyOtherAnnotation
final class ClassHierarchy extends SuperClass {
    @RepeatableAnnotation(name = "ClassHierarchy.anyMethod#1")
    @RepeatableAnnotation(name = "ClassHierarchy.anyMethod#2")
    @AnyOtherAnnotation
    public void anyMethod() {
    }
}

@RepeatableAnnotation(name = "BaseClass#1")
@RepeatableAnnotation(name = "BaseClass#2")
@AnyOtherAnnotation
abstract class BaseClass {
    @RepeatableAnnotation(name = "BaseClass#inheritedMethod")
    @AnyOtherAnnotation
    public void inheritedMethod() {
    }
}

@RepeatableAnnotation(name = "SuperClass")
abstract class SuperClass extends BaseClass {
}
