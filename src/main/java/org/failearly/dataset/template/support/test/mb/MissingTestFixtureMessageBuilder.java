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

package org.failearly.dataset.template.support.test.mb;

import org.failearly.dataset.util.mb.MessageBuilder;

/**
 * Builds message in case of missing test fixture class.
 */
final class MissingTestFixtureMessageBuilder extends TemplateObjectMessageBuilder {
    MissingTestFixtureMessageBuilder(MessageBuilder mb) {
        super(mb);
    }

    // @formatter:off
    @Override
    protected TemplateObjectMessageBuilder errorDescription() {
         this.lines(
            );

        return this;
    }

    @Override
    protected TemplateObjectMessageBuilder actions() {
        actionCreateTestFixture();
        actionUpdateTestClassConstructor();
        return this;
    }

    private void actionUpdateTestClassConstructor() {
        this.nextAction("Update the test class constructor of __testclass__")
            .exampleStart()
                .testClassConstructor("__toa__.class","__tof__.class","__testfixture__.class")
            .exampleEnd();
    }

    private MissingTestFixtureMessageBuilder actionCreateTestFixture() {
         this.nextAction("Create the __ton__ Annotation")
            .exampleStart("Your __ton__ Annotation (proposed name is __toa__):")
                .lines(
                    "/**",
                    " * __toa__ is a __ton__ Annotation.",
                    " */",
                    "@Target({ElementType.METHOD, ElementType.TYPE})",
                    "@Retention(RetentionPolicy.RUNTIME)",
                    "@Documented",
                    "@Repeatable(__toa__.__toa__s.class)",
                    "// TODO @TemplateObjectFactoryDefinition(factory=__tof__.class)",
                    "public @interface __toa__ {"
                )
                .sub()
                    .lines(
                        "/**",
                        " * @return The name of the template object. Could be used in Velocity templates by {@code $<name>}.",
                        " */",
                        "String name();",
                        "",
                        "/**",
                        " * @return The name of the associated dataset.",
                        " */",
                        "String dataset() default Constants.DATASET_DEFAULT_NAME;",
                        "",
                        "/**",
                        " * @return The scope of the template object (either {@link Scope#LOCAL} or {@link Scope#GLOBAL}.",
                        " */",
                        "Scope scope() default Scope.DEFAULT;",
                        "",
                        "// TODO: Add Template Object specific attributes",
                        "",
                        "",
                        "/**",
                        " * Used by @Repeatable.",
                        " */",
                        "@Target({ElementType.METHOD, ElementType.TYPE})",
                        "@Retention(RetentionPolicy.RUNTIME)",
                        "@Documented",
                        "@interface __toa__s {"
                    )
                        .indent("__toa__[] value();")
                    .line("}")
                .end()
            .line("}")
            .exampleEnd();
        return this;
    }

}
