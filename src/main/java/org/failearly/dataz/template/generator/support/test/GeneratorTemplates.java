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

package org.failearly.dataz.template.generator.support.test;

/**
 * GeneratorTemplates contains string (velocity) templates shared by
 * {@link GeneratorTestBase} and {@link DevelopmentLimitedGeneratorTestBase}.
 */
interface GeneratorTemplates {
    String TEMPLATE_EXTERNAL_ITERATOR=//
            "#foreach($g in %ton%)" +                      //
                    "$g;" +                                //
                    "#end";
    String TEMPLATE_EXTERNAL_TWO_ITERATORS=//
            "#foreach($g1 in %ton%)" +                      //
                    "$g1=" +                                //
                    "{#foreach($g2 in %ton%)" +             //
                    "$g2," +                                //
                    "#end};" +
                    "#end";

    String TEMPLATE_EXTERNAL_AND_INTERNAL_ITERATOR=                      //
            "Internal iterator value is %ton%.next(). External iterator=#foreach($g in %ton%)" + //
                    "(ext=$g,int=%ton%.lastValue)/" +                                            //
                    "#end";

    String TEMPLATE_INTERNAL_ITERATOR=//
            "($i) next=%ton%.next,last=%ton%.lastValue/";

    String TEMPLATE_INTERNAL_ITERATOR_SIMPLE=//
            "%ton%.next(),";

    String TEMPLATE_INTERNAL_ITERATOR_USING_RESET=//
            "($i) next=%ton%.next/" +              //
                    "%ton%.reset()";

}
