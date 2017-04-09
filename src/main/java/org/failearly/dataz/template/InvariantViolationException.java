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
     * @param annotation the impl
     * @param invariant the description of the invariant
     */
    public InvariantViolationException(Annotation annotation, String invariant) {
        super(createMessage(annotation, invariant));
    }

    protected static String createMessage(Annotation annotation, String invariant) {
        return "Invariant of " + annotationName(annotation) + " has been violated: " + invariant
                + "!\nCurrent impl is '" + annotation + "'";
    }

    protected static String annotationName(Annotation annotation) {
        if( annotation==null ) {
            return "<Unknown impl>";
        }
        return annotation.annotationType().getSimpleName();
    }
}
