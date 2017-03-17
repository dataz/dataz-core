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

package org.failearly.dataz.internal.template.generator;

import org.apache.commons.lang.time.FastDateFormat;

import java.util.TimeZone;

/**
 * DateEncoder is a utility class which encodes a long value to a string using
 * {@link FastDateFormat}.
 */
public final class DateEncoder {
    private final FastDateFormat dateFormat;

    public DateEncoder(String format) {
        this(format, TimeZone.getDefault());
    }

    public DateEncoder(String format, String timeZone) {
        this(format, TimeZone.getTimeZone(timeZone));
    }

    public DateEncoder(String format, TimeZone timeZone) {
        this.dateFormat = FastDateFormat.getInstance(format, timeZone);
    }

    public String toDateString(long milliSeconds) {
        return dateFormat.format(milliSeconds);
    }
}
