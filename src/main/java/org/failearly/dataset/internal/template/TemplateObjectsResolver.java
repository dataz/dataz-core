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

package org.failearly.dataset.internal.template;

import org.failearly.dataset.config.DataSetProperties;
import org.failearly.dataset.internal.annotation.*;
import org.failearly.dataset.internal.util.BuilderBase;
import org.failearly.dataset.template.common.TemplateObjectFactory;
import org.failearly.dataset.template.common.TemplateObjectFactoryDefinition;
import org.failearly.dataset.util.ObjectCreator;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

import static org.failearly.dataset.internal.annotation.AnnotationUtils.getMetaAnnotation;

/**
 * TemplateObjectsResolver resolves {@link TemplateObjects} from a test class or test method.
 */
public final class TemplateObjectsResolver {

    private final TraverseDepth traverseDepth;
    private final DuplicateHandler duplicateHandler;
    private final TemplateObjectCreatorListOrder templateObjectCreatorListOrder;

    private TemplateObjectsResolver(TraverseDepth traverseDepth, DuplicateHandler duplicateHandler, TemplateObjectCreatorListOrder templateObjectCreatorListOrder) {
        this.traverseDepth = traverseDepth;
        this.duplicateHandler = duplicateHandler;
        this.templateObjectCreatorListOrder = templateObjectCreatorListOrder;
    }

    /**
     * For testing purposes.
     * @return returns a builder object.
     */
    static Builder builder() {
        return new Builder();
    }

    /**
     * Create a standard template resolver.
     * @return a template object resolver
     */
    static TemplateObjectsResolver newStandardInstance() {
        return builder().withStandardSettings().build();
    }

    /**
     * Resolves {@link TemplateObjects} from a test method.
     *
     * @param testMethod the test method.
     * @return the template {@link TemplateObjects}.
     */
    public static TemplateObjects resolveFromTestMethod(Method testMethod) {
        return newStandardInstance().resolveFromTestMethod0(testMethod);
    }

    TemplateObjects resolveFromTestMethod0(Method testMethod) {
        final TemplateObjects creators = createTemplateObjects();
        doResolveTemplateObjectCreators(testMethod, creators);
        return creators;
    }

    /**
     * Resolves {@link TemplateObjects} from a test class.
     *
     * @param testClass the test class.
     * @return the template {@link TemplateObjects}.
     */
    public static TemplateObjects resolveFromTestClass(Class<?> testClass) {
        return newStandardInstance().resolveFromTestClass0(testClass);
    }

    TemplateObjects resolveFromTestClass0(Class<?> testClass) {
        final TemplateObjects creators = createTemplateObjects();
        doResolveTemplateObjectCreators(testClass, creators);
        return creators;
    }

    private TemplateObjects createTemplateObjects() {
        return new TemplateObjects(duplicateHandler, templateObjectCreatorListOrder);
    }

    private void doResolveTemplateObjectCreators(Class<?> testClass, final TemplateObjects creators) {
        final AnnotationTraverser<Annotation> traverser = AnnotationTraversers.createMetaAnnotationTraverser(
                TemplateObjectFactoryDefinition.class,
                TraverseStrategy.TOP_DOWN,
                traverseDepth
        );
        traverser.traverse(testClass, new AnnotationHandlerBase<Annotation>() {
            @Override
            public void handleAnnotation(Annotation annotation) {
                creators.add(createTemplateObjectCreator(annotation));
            }
        });
    }

    private void doResolveTemplateObjectCreators(Method testClass, final TemplateObjects creators) {
        final AnnotationTraverser<Annotation> traverser = AnnotationTraversers.createMetaAnnotationTraverser(
                TemplateObjectFactoryDefinition.class,
                TraverseStrategy.TOP_DOWN,
                traverseDepth
        );
        traverser.traverse(testClass, new AnnotationHandlerBase<Annotation>() {
            @Override
            public void handleAnnotation(Annotation annotation) {
                creators.add(createTemplateObjectCreator(annotation));
            }
        });
    }

    private static TemplateObjectCreator createTemplateObjectCreator(Annotation annotation) {
        return new TemplateObjectCreator(
                createTemplateObjectFactoryFromDefinition(getMetaAnnotation(TemplateObjectFactoryDefinition.class, annotation)),
                annotation
        );
    }

    private static TemplateObjectFactory createTemplateObjectFactoryFromDefinition(TemplateObjectFactoryDefinition templateObjectFactoryDefinition) {
        return ObjectCreator.createInstance(templateObjectFactoryDefinition.factory());
    }


    public static final class Builder extends BuilderBase<TemplateObjectsResolver> {

        private TraverseDepth traverseDepth;
        private DuplicateHandler duplicateHandler;
        private TemplateObjectCreatorListOrder templateObjectCreatorListOrder;

        private Builder() {
        }

        public Builder withStandardSettings() {
            withTraverseDepth(DataSetProperties.getTemplateObjectTraverseDepth());
            withTemplateObjectDuplicateStrategy(DataSetProperties.getTemplateObjectDuplicateStrategy());
            return this;
        }

        public Builder withTraverseDepth(TraverseDepth traverseDepth) {
            this.traverseDepth = traverseDepth;
            return this;
        }

        public Builder withTemplateObjectDuplicateStrategy(TemplateObjectDuplicateStrategy templateObjectDuplicateStrategy) {
            this.templateObjectCreatorListOrder = templateObjectDuplicateStrategy;
            this.duplicateHandler = templateObjectDuplicateStrategy;
            return this;
        }

        // For test purposes
        Builder withDuplicateHandler(DuplicateHandler duplicateHandler) {
            this.duplicateHandler = duplicateHandler;
            return this;
        }

        @Override
        protected TemplateObjectsResolver doBuild() {
            return new TemplateObjectsResolver(traverseDepth, duplicateHandler, templateObjectCreatorListOrder);
        }
    }

}
