/*
 * dataZ - Test Support For Data Stores.
 *
 * Copyright (C) 2014-2016 marko (http://fail-early.com)
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
 *
 */

package org.failearly.dataset.template.support.test.mb;

import org.failearly.common.test.mb.MessageBuilder;


/**
 * MissingTemplateObjectFactoryMessageBuilder is responsible for ...
 */
final class MissingTemplateObjectFactoryMessageBuilder extends TemplateObjectMessageBuilder {
    MissingTemplateObjectFactoryMessageBuilder(MessageBuilder mb) {
        super(mb);
    }

    @Override
    protected TemplateObjectMessageBuilder errorDescription() {
        this.lines(
            "The Template Object Factory is responsible for creating a __tot__ from __toa__.",
            "It is the glue between the annotation and it's actually implementation. So your",
            "annotation must know it's factory by annotating it with @TemplateObjectFactoryDefinition(...)."
        );

        return this;
    }

    @Override
    protected TemplateObjectMessageBuilder actions() {
        actionCreateTemplateObjectFactory();
        actionAssignTemplateObjectFactoryToAnnotation();
        actionUpdateTestClass();
        return this;
    }

    private void actionUpdateTestClass() {
        this.nextAction("Update your test class")
            .exampleStart("Replace __testclass__ with this code snippet.")
                .lines(
                    "public class __testclass__",
                    "\t\textends __testbase__<__testclass_generics__> {"
                )
                .sub()
                    .testClassConstructor("__toa__.class","__tof__.class","null")
                .end()
                .line("}")
            .exampleEnd();
    }

    private void actionAssignTemplateObjectFactoryToAnnotation() {
        this.nextAction("Assign __tof__ to your __ton__ Annotation @__toa__")
            .sub()
                .lines(
                    "a) Open your annotation (__toa__).",
                    "b) Remove the comment on the line with @TemplateObjectFactoryDefinition."
                )
            .end()
            .newline();
    }

    private void actionCreateTemplateObjectFactory() {
        this.nextAction("Create a Template Object Factory class")
            .exampleStart("Example for __tof__:")
                .lines(
                    "/**",
                    " * __tof__ creates a {@link __tot__} from {@link __toa__} annotation.",
                    " */",
                    "public class __tof__ extends __tofb__<__tofb_generics__> {"
                )
                    .sub()
                        .line("public __tof__() {")
                            .indent("super(__toa__.class);")
                        .line("}")
                        .newline()
                        .line("@Override")
                        .line("protected __tot__ doCreate(__toa__ annotation) {")
                            .indent("return null;")
                        .line("}")
                        .newline()
                        .line("@Override")
                        .line("protected String doResolveDataSetName(__toa__ annotation) {")
                            .indent("return annotation.dataset();")
                        .line("}")
                        .newline()
                        .line("@Override")
                        .line("protected Scope doResolveScope(__toa__ annotation) {")
                            .indent("return annotation.scope();")
                        .line("}")
                    .end()
                .line("}")
            .exampleEnd();
    }
}