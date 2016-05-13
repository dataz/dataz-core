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

package org.failearly.dataz.template;

import org.failearly.dataz.exception.DataSetException;

import java.lang.annotation.Annotation;

/**
 * InvariantViolationException will be thrown in case an invariant of an Annotation has been violated.
 */
public class InvariantViolationException extends DataSetException {
    /**
     * Constructor.
     *
     * @param annotation the annotation
     * @param invariant the description of the invariant
     */
    public InvariantViolationException(Annotation annotation, String invariant) {
        super(createMesage(annotation, invariant));
    }

    protected static String createMesage(Annotation annotation, String invariant) {
        return "Invariant of " + annotationName(annotation) + " has been violated: " + invariant
                + "!\nCurrent annotation is '" + annotation + "'";
    }

    protected static String annotationName(Annotation annotation) {
        if( annotation==null ) {
            return "<Unknown annotation>";
        }
        return annotation.annotationType().getSimpleName();
    }
}
