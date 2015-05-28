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

/**
 * Support classes for creating {@link org.failearly.dataset.generator.support.Generator} annotations.
 * <br><br>
 * Any generator annotation must define two elements:<br>
 * <ul>
 *    <li>{@code name()}: The name of the generator. Use this to access the generator within your template.</li>
 *    <li>{@code dataset()}: A generator must be associated to a specific {@link org.failearly.dataset.DataSet#name()}.</li>
 * </ul>
 * <br><br>
 * Steps to create your own Generator:
 * <ol>
 *    <li>Creating a generator annotation. (For example: Take a look at {@link org.failearly.dataset.generator.ConstantGenerator}.)</li>
 *    <li>Override {@link org.failearly.dataset.generator.support.GeneratorFactoryBase}.</li>
 *    <li>And last but not at least a {@link org.failearly.dataset.generator.support.GeneratorBase} implementation.</li>
 * </ol>
 * For that purpose dataSet provides following base or utility classes:<br>
 * <ul>
 *    <li>{@link org.failearly.dataset.generator.support.GeneratorFactoryBase}. You should extend your implementation from this base class.</li>
 *    <li>Typically you override either
 *    {@link org.failearly.dataset.generator.support.GeneratorFactoryBase#doCreateLimitedGenerator(java.lang.annotation.Annotation, Integer)} or
 *    {@link org.failearly.dataset.generator.support.GeneratorFactoryBase#doCreateLimitedGenerator(java.lang.annotation.Annotation, Integer)}.
 *    </li>
 *    <li>{@link org.failearly.dataset.generator.support.LimitedGeneratorBase}:
 *    You should extend from this class if the naturally usage is {@link org.failearly.dataset.generator.Limit#LIMITED}, or use</li>
 *    <li>{@link org.failearly.dataset.generator.support.UnlimitedGeneratorBase} if your generator is more of
 *    {@link org.failearly.dataset.generator.Limit#UNLIMITED} type.</li>
 * </ul>
 *
 * @see org.failearly.dataset.generator.support.Generator
 * @see org.failearly.dataset.generator.support.GeneratorFactoryBase
 */
package org.failearly.dataset.generator.support;