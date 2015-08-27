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

import org.apache.commons.lang.time.DateUtils;

/**
 * DateUnit defines the date/time units. Be carefully with MONTHS and YEARS, these are not correct.
 */
public enum DateUnit {
    MILLISECONDS,
    SECONDS(1000, MILLISECONDS),
    MINUTES(60,SECONDS),
    HOURS(60,MINUTES),
    DAYS(24,HOURS),
    WEEKS(7,DAYS),
    MONTHS(30,DAYS),
    YEARS(365, DAYS);

    private final long milliseconds;

    DateUnit() {
        this.milliseconds = 1;
    }

    DateUnit(long factor, DateUnit previous) {
        this.milliseconds = factor * previous.milliseconds;
    }

    public long next(int numberOf) {
        return numberOf * milliseconds;
    }
}
