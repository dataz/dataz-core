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

import java.util.TimeZone;

/**
 * PredefinedTimeZones contains some predefined time zones for {@link DateGenerator#timeZone()}. This string will be
 * converted to a {@link TimeZone} by {@link TimeZone#getTimeZone(String)}
 *
 * @see TimeZone
 * @see DateGenerator#timeZone()
 */
@SuppressWarnings("unused")
public final class PredefinedTimeZones {

    private PredefinedTimeZones() {
    }

    /**
     * Use the default time zone of the local machine.
     */
    public static final String USE_DEFAULT="<use-default-timezone>";

    /**
     * Greenwich Mean Time.
     */
    public static final String GMT="GMT";

    /**
     * Central European Time.
     */
    public static final String CET="CET";

    /**
     * Central European SUMMER Time.
     */
    public static final String CEST="CEST";
}
