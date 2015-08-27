/*
 * dataSet - Test Support For Data Stores.
 *
 * Copyright (C) 2014-2015 Marko Umek (http://fail-early.com/contact)
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

package org.failearly.dataset.template.generator;

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
