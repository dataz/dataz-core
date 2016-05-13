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

package org.failearly.dataz.template.generator.support.test;

/**
 * GeneratorTemplates contains string (velocity) templates shared by
 * {@link GeneratorTestBase} and {@link DevelopmentGeneratorTestBase}.
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
