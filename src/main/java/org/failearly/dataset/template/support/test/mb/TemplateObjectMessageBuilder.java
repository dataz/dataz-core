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

import org.failearly.dataset.template.TemplateObjectFactory;
import org.failearly.common.test.mb.MessageBuilder;
import org.failearly.common.test.mb.MessageBuilderDelegate;

import java.lang.annotation.Annotation;

/**
 * TemplateObjectMessageBuilder is responsible for ...
 */
public abstract class TemplateObjectMessageBuilder
    extends MessageBuilderDelegate<TemplateObjectMessageBuilder> {

    private Class<? extends Annotation> templateObjectAnnotationClass;
    private Class<? extends TemplateObjectFactory> templateObjectFactoryClass;
    private Class<?> testFixtureClass;

    private String templateObjectFactoryBaseClassName;
    private String templateObjectFactoryGenerics;
    private String testClassName;
    private String testClassBaseName;
    private String templateObjectName;
    private String templateObjectType;
    private String missingVariable;
    private String additionalGenerics;

    @SuppressWarnings("FieldCanBeLocal")
    private int actionNumber=0;

    TemplateObjectMessageBuilder(MessageBuilder messageBuilder) {
        super(messageBuilder);
    }

    public final TemplateObjectMessageBuilder withTestClass(Class<?> testClass) {
        this.testClassName=className(testClass);
        this.testClassBaseName=className(testClass.getSuperclass());
        return this;
    }

    public final TemplateObjectMessageBuilder withTestFixtureClass(Class<?> testFixtureClass) {
        this.testFixtureClass=testFixtureClass;
        return this;
    }

    public final TemplateObjectMessageBuilder withTemplateObjectName(String templateObjectName) {
        this.templateObjectName=templateObjectName;
        return this;
    }

    public final TemplateObjectMessageBuilder withTemplateObjectType(String templateObjectType) {
        this.templateObjectType=templateObjectType;
        return this;
    }

    public final TemplateObjectMessageBuilder withTemplateObjectAnnotationClass(
        Class<? extends Annotation> templateObjectAnnotationClass
    ) {
        this.templateObjectAnnotationClass=templateObjectAnnotationClass;
        return this;
    }

    public final TemplateObjectMessageBuilder withTemplateObjectFactoryClass(
        Class<? extends TemplateObjectFactory> templateObjectFactoryClass
    ) {
        this.templateObjectFactoryClass=templateObjectFactoryClass;
        return this;
    }

    public final TemplateObjectMessageBuilder withTemplateObjectFactoryBaseClassName(
        String templateObjectFactoryBaseClassName
    ) {
        this.templateObjectFactoryBaseClassName=templateObjectFactoryBaseClassName;
        return this;
    }

    public final TemplateObjectMessageBuilder withMissingVariable(String missingVariable) {
        this.missingVariable=missingVariable;
        return this;
    }

    public final TemplateObjectMessageBuilder withTestClassAdditionalGenerics(String... additionalGenerics) {
        this.additionalGenerics=createGenericList(additionalGenerics, "/*TODO replace */", true);
        return this;
    }

    public TemplateObjectMessageBuilder withTemplateObjectFactoryGenerics(String... templateObjectFactoryGenerics) {
        this.templateObjectFactoryGenerics=createGenericList(templateObjectFactoryGenerics,"", false);
        return this;
    }


    private String createGenericList(String[] generics, String comment, boolean appendComma) {
        boolean first=true;
        final StringBuilder builder=new StringBuilder();
        for (String generic : generics) {
            if( ! first ) {
                builder.append(",");
            }
            builder.append(comment).append(generic);
            first = false;
        }

        if( appendComma )
            builder.append(",");


        return builder.toString();
    }

    @Override
    protected void doInit() {
        super.doInit();
        this.argument("misvar", missingVariable)
            .argument("testclass", testClassName)
            .argument("testbase", testClassBaseName)
            .argument("testfixture", resolveTestFixtureClass())
            .argument("ton", templateObjectName)
            .argument("tot", templateObjectType)
            .argument("toa", resolveTemplateObjectAnnotationName())
            .argument("tof", resolveTemplateObjectFactoryName())
            .argument("tofb", templateObjectFactoryBaseClassName)
            .argument("tofb_generics", templateObjectFactoryGenerics)
            .argument("testclass_generics", "__additional____toa__,__tof__")
            .argument("additional",additionalGenerics);
    }

    @Override
    protected final void prepare() {
        this.errorMessage()
            .newline()
            .errorDescription()
            .newline()
            .actions()
            .finalAction();
    }

    protected TemplateObjectMessageBuilder errorMessage() {
        return this.firstLine("Missing parameter '__misvar__'.");
    }

    protected TemplateObjectMessageBuilder errorDescription() {
        return this;
    }

    protected abstract TemplateObjectMessageBuilder actions();

    protected void finalAction() {
        this.nextAction("Compile and rerun your test class")
            .newlines(2);
    }

    private String resolveTestFixtureClass() {
        return this.testFixtureClass != null ? className(this.testFixtureClass) : "TestFixture";
    }

    private String resolveTemplateObjectAnnotationName() {
        return this.templateObjectAnnotationClass != null ?
            className(this.templateObjectAnnotationClass) :
            this.testClassName.replace("Test", "");
    }

    private String resolveTemplateObjectFactoryName() {
        return this.templateObjectFactoryClass != null ?
            className(this.templateObjectFactoryClass) :
            this.resolveTemplateObjectAnnotationName() + "Factory";
    }

    private static String className(Class<?> clazz) {
        return clazz.getSimpleName();
    }

    private TemplateObjectMessageBuilder separateActions() {
        return newlines(2);
    }

    final TemplateObjectMessageBuilder nextAction(String action) {
        this.separateActions();
        this.actionNumber+=1;
        return this.line("Step " + this.actionNumber + ") " + action + ".").newline();
    }

    // @formatter:off
    protected final TemplateObjectMessageBuilder testClassConstructor(
            String annotationClass,
            String factoryClass,
            String testFixtureClass) {

        this.line("public __testclass__() {")
            .sub()
                .line("super(")
                .sub()
                    .line(annotationClass+",")
                    .line(factoryClass+",")
                    .line(testFixtureClass)
                .end()
                .line(");")
            .end()
            .line("}");

        return this;
    }

    // @formatter:on
}
