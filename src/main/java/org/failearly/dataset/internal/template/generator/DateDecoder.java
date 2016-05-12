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

package org.failearly.dataset.internal.template.generator;


import java.text.ParseException;
import java.text.ParsePosition;
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
