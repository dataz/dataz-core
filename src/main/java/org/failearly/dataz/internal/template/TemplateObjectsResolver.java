/*
 * dataZ - Test Support For Data Stores.
 *
 * Copyright (C) 2014-2016 'Marko Umek' (http://fail-early.com)
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

package org.failearly.dataz.internal.template;

import org.failearly.common.annotation.traverser.*;
import org.failearly.dataz.config.DataSetProperties;
import org.failearly.dataz.internal.util.BuilderBase;
import org.failearly.dataz.template.TemplateObjectFactory;
import org.failearly.common.classutils.ObjectCreator;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

import static org.failearly.common.annotation.traverser.AnnotationTraverserBuilder.metaAnnotationTraverser;
import static org.failearly.common.annotation.utils.AnnotationUtils.getMetaAnnotation;

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
     * Resolves {@link TemplateObjects} from a method.
     *
     * @param method the method.
     * @return the template {@link TemplateObjects}.
     */
    public static TemplateObjects resolveFromMethod(Method method) {
        return newStandardInstance().resolveFromMethod0(method);
    }

    TemplateObjects resolveFromMethod0(Method method) {
        final TemplateObjects creators = createTemplateObjects();
        doResolveTemplateObjectCreators(method, creators);
        return creators;
    }

    /**
     * Resolves {@link TemplateObjects} from a class.
     *
     * @param clazz the class.
     * @return the template {@link TemplateObjects}.
     */
    public static TemplateObjects resolveFromClass(Class<?> clazz) {
        return newStandardInstance().resolveFromClass0(clazz);
    }

    TemplateObjects resolveFromClass0(Class<?> clazz) {
        final TemplateObjects creators = createTemplateObjects();
        doResolveTemplateObjectCreators(clazz, creators);
        return creators;
    }

    private TemplateObjects createTemplateObjects() {
        return new TemplateObjects(duplicateHandler, templateObjectCreatorListOrder);
    }

    private void doResolveTemplateObjectCreators(Class<?> clazz, final TemplateObjects creators) {
        final MetaAnnotationTraverser<TemplateObjectFactory.Definition> traverser = metaAnnotationTraverser(TemplateObjectFactory.Definition.class)
                .withTraverseStrategy(TraverseStrategy.TOP_DOWN)
                .withTraverseDepth(traverseDepth)
                .build();
        traverser.traverse(clazz, new MetaAnnotationHandlerBase<TemplateObjectFactory.Definition>() {
            @Override
            public void handleAnnotation(Annotation annotation) {
                creators.add(createTemplateObjectCreator(annotation));
            }
        });
    }

    private void doResolveTemplateObjectCreators(Method clazz, final TemplateObjects creators) {
        final MetaAnnotationTraverser<TemplateObjectFactory.Definition> traverser = metaAnnotationTraverser(TemplateObjectFactory.Definition.class)
                .withTraverseStrategy(TraverseStrategy.TOP_DOWN)
                .withTraverseDepth(traverseDepth)
                .build();
        traverser.traverse(clazz, new MetaAnnotationHandlerBase<TemplateObjectFactory.Definition>() {
            @Override
            public void handleAnnotation(Annotation annotation) {
                creators.add(createTemplateObjectCreator(annotation));
            }
        });
    }

    private static TemplateObjectCreator createTemplateObjectCreator(Annotation annotation) {
        return new TemplateObjectCreator(
                createTemplateObjectFactoryFromDefinition(getMetaAnnotation(TemplateObjectFactory.Definition.class, annotation)),
                annotation
        );
    }

    private static TemplateObjectFactory createTemplateObjectFactoryFromDefinition(TemplateObjectFactory.Definition definition) {
        return ObjectCreator.createInstance(definition.value());
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
