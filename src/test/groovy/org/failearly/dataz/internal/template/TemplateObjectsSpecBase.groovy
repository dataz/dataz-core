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
package org.failearly.dataz.internal.template

import spock.lang.Specification

import java.util.stream.Collectors

/**
 * Base class for spock specifications on {@link TemplateObjects} and {@link TemplateObjectsResolver}.
 */
abstract class TemplateObjectsSpecBase extends Specification {
    protected static List<String> collectDescriptions(TemplateObjects templateObjects) {
        templateObjects.collectTemplateObjectInstances().stream()  //
                .map({ it.description })                                      //
                .collect(Collectors.toList())
    }

    protected static List<String> collectNames(TemplateObjects templateObjects) {
        templateObjects.collectTemplateObjectInstances().stream()  //
                .map({ it.name() })                                      //
                .collect(Collectors.toList())
    }

    protected static Set<String> collectDataSets(TemplateObjects templateObjects) {
        templateObjects.collectTemplateObjectInstances().stream()  //
                .flatMap({ it.datasets().stream() })                                      //
                .collect(Collectors.toSet())
    }
}