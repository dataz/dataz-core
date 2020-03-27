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

package org.failearly.dataz.internal.template.generator;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * DateEncoder is a utility class which decodes a date (String) value to a date using
 * {@link SimpleDateFormat#parse(String)}.
 */
public final class DateDecoder {
    private final SimpleDateFormat simpleDateFormat;
    private final String format;

    public DateDecoder(String format) {
        this.simpleDateFormat = new SimpleDateFormat(format);
        this.format = format;
    }

    public Date toDate(String dateAsString) {
        try {
            return simpleDateFormat.parse(dateAsString);
        } catch (ParseException e) {
            throw new IllegalArgumentException("Can't parse '" + dateAsString + "' by format '" + format + "'", e);
        }
    }
}
