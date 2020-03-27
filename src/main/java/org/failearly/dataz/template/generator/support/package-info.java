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

/**
 * Support classes for creating {@link org.failearly.dataz.template.generator.Generator} annotations.
 * <br><br>
 * Any generator impl must define following attributes:<br>
 * <ul>
 *    <li>{@code name()}: The name of the generator. Use this to reserve the generator within your template.</li>
 *    <li>{@code dataz()}: A generator must be associated to a specific {@link org.failearly.dataz.DataSet#name()}.</li>
 *    <li>{@code scope()}: A generator has .</li>
 * </ul>
 * <br><br>
 * Steps to create your own Generator:
 * <ol>
 *    <li>Creating a generator impl. (For example: Take a look at {@link org.failearly.dataz.template.generator.ListGenerator}.)</li>
 *    <li>Override {@link org.failearly.dataz.template.generator.support.GeneratorFactoryBase}.</li>
 *    <li>And last but not at least a {@link org.failearly.dataz.template.generator.support.GeneratorBase} implementation.</li>
 * </ol>
 * For that purpose dataSet provides following base or utility classes:<br>
 * <ul>
 *    <li>{@link org.failearly.dataz.template.generator.support.GeneratorFactoryBase}. You should extend your implementation from this base class.</li>
 *    <li>Typically you override either
 *    {@link org.failearly.dataz.template.generator.support.GeneratorFactoryBase#doCreateLimitedGenerator(java.lang.annotation.Annotation, org.failearly.dataz.template.TemplateObjectAnnotationContext, Integer)} or
 *    {@link org.failearly.dataz.template.generator.support.GeneratorFactoryBase#doCreateLimitedGenerator(java.lang.annotation.Annotation, org.failearly.dataz.template.TemplateObjectAnnotationContext, Integer)}.
 *    </li>
 *    <li>{@link org.failearly.dataz.template.generator.support.LimitedGeneratorBase}:
 *    You should extend from this class if the naturally usage is {@link org.failearly.dataz.template.generator.Limit#LIMITED}, or use</li>
 *    <li>{@link org.failearly.dataz.template.generator.support.UnlimitedGeneratorBase} if your generator is more of
 *    {@link org.failearly.dataz.template.generator.Limit#UNLIMITED} type.</li>
 * </ul>
 *
 * @see org.failearly.dataz.template.generator.Generator
 * @see org.failearly.dataz.template.generator.support.GeneratorFactoryBase
 */
package org.failearly.dataz.template.generator.support;