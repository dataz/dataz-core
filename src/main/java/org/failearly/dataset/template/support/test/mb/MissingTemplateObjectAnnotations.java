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
 * MissingTemplateObjectAnnotations is responsible for ...
 */
final class MissingTemplateObjectAnnotations extends TemplateObjectMessageBuilder {
    MissingTemplateObjectAnnotations(MessageBuilder mb) {
        super(mb);
    }

    @Override
    protected TemplateObjectMessageBuilder errorMessage() {
        return this.firstLine("Missing __toa__ annotation on your test fixture class __testfixture__.");
    }

    @Override
    protected TemplateObjectMessageBuilder errorDescription() {
        return this.line(
            "Your test fixture class (__testfixture__) should be annotated by at least one __toa__ annotation."
        );
    }

    @Override
    protected TemplateObjectMessageBuilder actions() {
        actionAssignAnnotationToTestFixture();
        return this;
    }


    // @formatter:off
    private void actionAssignAnnotationToTestFixture() {
        this.nextAction("Assign an instance of __toa__ annotation to the __testfixture__ class")
            .exampleStart("Just copy the entire snippet or just the annotation below:")
                .lines(
                    "@__toa__(name=TEMPLATE_OBJECT_NAME /* TODO: Add more attributes */)",
                    "private static class TestFixture {}"
                )
            .exampleEnd();
    }

}
