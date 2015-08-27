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
 * Builds message in case of missing annotation class.
 */
final class MissingTemplateObjectAnnotationMessageBuilder extends TemplateObjectMessageBuilder {
    MissingTemplateObjectAnnotationMessageBuilder(MessageBuilder mb) {
        super(mb);
    }

    // @formatter:off
    @Override
    protected TemplateObjectMessageBuilder errorDescription() {
         this.lines(
                "The __ton__ Annotation represents a template object which could be used within a template.",
                "You can provide one or more template objects to a test class, method or reusable data set.",
                "So the annotation builds the interface to your __ton__ implementation.",
                "",
                "Any Template Object must provide 3 mandatory attributes (or you have to provide sensible defaults):",
                "- name: the name of the Template Object, which will be used within a DataSet template. The name must be",
                "          unique within a DataSet!",
                "- dataset: The dataset name, the template object could be used.",
                "- scope: The scope of the template object. See Scope enumeration for further information."
            )
            .newlines(2)
            .lines(
                "The easiest way to create a valid template object or at least a good starting point is to follow ",
                "the proposed steps and copy the code between " + SNIPPET_START + " and " + SNIPPET_END + "."
            );

        return this;
    }

    @Override
    protected TemplateObjectMessageBuilder actions() {
        actionCreateAnnotation();
        actionCreateTestClassConstructor();
        return this;
    }

    private MissingTemplateObjectAnnotationMessageBuilder actionCreateAnnotation() {
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

    private MissingTemplateObjectAnnotationMessageBuilder actionCreateTestClassConstructor() {
        this.nextAction("Create the default constructor for __testclass__")
            .exampleStart("Replace your entire __testclass__ or just copy the constructor:")
                .lines(
                    "/**",
                    " * Tests for {@link __toa__} and {@link __tof__}.",
                    " */",
                    "public class __testclass__ ",
                    "\t\textends __testbase__/* generics omitted */ {"
                )
                .sub()
                    .testClassConstructor("__toa__.class", "null", "null")
                .end()
                .line("}")
            .exampleEnd();

        return this;
    }
}
