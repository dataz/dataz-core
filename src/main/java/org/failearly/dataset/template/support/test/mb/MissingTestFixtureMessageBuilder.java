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
                "A test fixture class has no other purpose then to hold the __ton__ Annotations (__toa__),",
                "which should be used by your tests."
            );

        return this;
    }

    @Override
    protected TemplateObjectMessageBuilder actions() {
        actionCreateTestFixture();
        actionUpdateTestClassConstructor();
        actionAssignAnnotationToTestFixture();
        return this;
    }

    private void actionAssignAnnotationToTestFixture() {
        this.nextAction("Assign an instance of __toa__ annotation to the __testfixture__")
            .lines(
                "The name 'TEMPLATE_OBJECT_NAME' is used by some of template(..) methods. ",
                "You can use your own name, if necessary, but it's recommended to use this one. In case",
                "you use a different name, use the appropriate template(..) methods.",
                ""
            )
            .exampleStart()
            .lines(
                "@__toa__(name=TEMPLATE_OBJECT_NAME /* TODO: Add more attributes */)",
                "private static class TestFixture {}"
                )
            .exampleEnd();
    }

    private void actionUpdateTestClassConstructor() {
        this.nextAction("Update the test class constructor of __testclass__")
            .exampleStart()
                .testClassConstructor("__toa__.class","__tof__.class","__testfixture__.class")
            .exampleEnd();
    }

    private MissingTestFixtureMessageBuilder actionCreateTestFixture() {
         this.nextAction("Create a test fixture class")
            .exampleStart("Add this as inner class to your test class (__testclass__)")
                .line("private static class __testfixture__ {}")
             .exampleEnd();
        return this;
    }

}
