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
package org.failearly.dataset.generator;


/**
 * GeneratorConstants contains basic generator constants.
 */
public interface GeneratorConstants {
    /**
     * No seed for Random based {@link org.failearly.dataset.template.TemplateObject}. So
     * {@link java.util.Random} the random instance will be created with no seed value.
     *
     * @see org.failearly.dataset.generator.RandomRangeGenerator#seed()
     * @see org.failearly.dataset.generator.RandomBooleanGenerator#seed()
     */
    int NO_SEED=0;
}
