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

package org.failearly.dataz.internal.common.annotation.traverser;


@RepeatableAnnotation(id = "TFC1")
@RepeatableAnnotation(id = "TFC2")
@StandardAnnotation(id = "TFC")
@OtherAnnotation(id = "O-TFC")
final class TestFixtureClass extends MidClass {
    @RepeatableAnnotation(id = "dim1")
    @RepeatableAnnotation(id = "dim2")
    @OtherAnnotation(id = "O-dim")
    @StandardAnnotation(id = "dim")
    public void declaredInstanceMethod() {
    }
}

@RepeatableAnnotation(id = "MC1")
@StandardAnnotation(id = "MC")
abstract class MidClass extends BaseClass {
    @RepeatableAnnotation(id = "cm1")
    @RepeatableAnnotation(id = "cm2")
    @StandardAnnotation(id = "cm")
    public static void classMethod() {
    }

    @RepeatableAnnotation(id = "oim1")
    @RepeatableAnnotation(id = "oim2")
    @StandardAnnotation(id = "oim")
    @Override
    public void overriddenInstanceMethod() {
    }

    public void noAnnotations() {}
}

@SuppressWarnings("unused")
@RepeatableAnnotation(id = "BC1")
@RepeatableAnnotation(id = "BC2")
@OtherAnnotation(id = "O-BC")
@StandardAnnotation(id = "BC")
abstract class BaseClass implements Interface1 {
    @RepeatableAnnotation(id = "im1")
    @RepeatableAnnotation(id = "im2")
    @StandardAnnotation(id = "im")
    public void inheritedMethod() {
    }

    @RepeatableAnnotation(id = "never seen")
    @RepeatableAnnotation(id = "never seen")
    @StandardAnnotation(id = "never seen")
    public void overriddenInstanceMethod() {
    }
}

@RepeatableAnnotation( id = "IF01" )
@RepeatableAnnotation( id = "IF02" )
@StandardAnnotation( id = "IF0" )
@OtherAnnotation(id = "O-IF0")
interface Interface0 {

}

@RepeatableAnnotation( id = "IF11" )
@RepeatableAnnotation( id = "IF12" )
@StandardAnnotation( id = "IF1" )
interface Interface1 extends Interface0 {
    @RepeatableAnnotation(id = "ifm1")
    @RepeatableAnnotation(id = "ifm2")
    @StandardAnnotation( id = "ifm" )
    default void interfaceMethod() {}
}
