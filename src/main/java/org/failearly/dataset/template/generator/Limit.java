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


/**
 * Limit Type for generator annotation {@code Limit limit()}.
 */
public enum Limit {
    /**
     * The generator could be used within Velocities foreach loop.
     */
    LIMITED(true),
    /**
     * The generator MUST NOT be used within Velocities foreach loop - causes an exception.
     * Use {@link org.failearly.dataset.template.generator.support.Generator#next()} instead.
     */
    UNLIMITED(false);

    private final boolean limited;

    Limit(boolean limited) {
        this.limited = limited;
    }


    /**
     * @return  {@code true} if the generator is {@link #LIMITED} otherwise {@code false}.
     */
    public boolean isLimited() {
        return this.limited;
    }
}
