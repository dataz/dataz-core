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

package org.failearly.dataz.template.generator;

/**
 * PredefinedDateFormats contains some predefined formats for {@link DateGenerator#format()}.
 *
 * Inspired by {@code org.apache.commons.lang.time.DateFormatUtils}.
 */
@SuppressWarnings("unused")
public final class PredefinedDateFormats {
    private PredefinedDateFormats() {
    }

    /**
     * ISO8601 formatter for date-time without time zone.
     * The format used is <tt>yyyy-MM-dd'T'HH:mm:ss</tt>.
     */
    public static final String ISO_DATETIME_FORMAT
            = "yyyy-MM-dd'T'HH:mm:ss";

    /**
     * ISO8601 formatter for date-time with time zone.
     * The format used is <tt>yyyy-MM-dd'T'HH:mm:ssZZ</tt>.
     */
    public static final String ISO_DATETIME_TIME_ZONE_FORMAT
            = "yyyy-MM-dd'T'HH:mm:ssZZ";

    /**
     * ISO8601 formatter for date without time zone.
     * The format used is <tt>yyyy-MM-dd</tt>.
     */
    public static final String ISO_DATE_FORMAT
            = "yyyy-MM-dd";

    /**
     * ISO8601-like formatter for date with time zone.
     * The format used is <tt>yyyy-MM-ddZZ</tt>.
     * This pattern does not comply with the formal ISO8601 specification
     * as the standard does not allow a time zone  without a time.
     */
    public static final String ISO_DATE_TIME_ZONE_FORMAT
            = "yyyy-MM-ddZZ";

    /**
     * ISO8601 formatter for time without time zone.
     * The format used is <tt>'T'HH:mm:ss</tt>.
     */
    public static final String ISO_TIME_FORMAT
            = "'T'HH:mm:ss";

    /**
     * ISO8601 formatter for time with time zone.
     * The format used is <tt>'T'HH:mm:ssZZ</tt>.
     */
    public static final String ISO_TIME_TIME_ZONE_FORMAT
            = "'T'HH:mm:ssZZ";

    /**
     * ISO8601-like formatter for time without time zone.
     * The format used is <tt>HH:mm:ss</tt>.
     * This pattern does not comply with the formal ISO8601 specification
     * as the standard requires the 'T' prefix for times.
     */
    public static final String ISO_TIME_NO_T_FORMAT
            = "HH:mm:ss";

    /**
     * ISO8601-like formatter for time with time zone.
     * The format used is <tt>HH:mm:ssZZ</tt>.
     * This pattern does not comply with the formal ISO8601 specification
     * as the standard requires the 'T' prefix for times.
     */
    public static final String ISO_TIME_NO_T_TIME_ZONE_FORMAT
            = "HH:mm:ssZZ";

}