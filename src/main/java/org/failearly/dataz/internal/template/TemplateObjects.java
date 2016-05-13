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

import org.failearly.dataz.template.Scope;
import org.failearly.dataz.template.TemplateObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * TemplateObjects holds all resolved (see {@link TemplateObjectsResolver}) from a test method or test class.
 */
public final class TemplateObjects {
    private static final Logger LOGGER = LoggerFactory.getLogger(TemplateObjects.class);

    private final List<TemplateObjectCreator> templateObjectCreators;
    private final DuplicateHandler duplicateHandler;
    private final TemplateObjectCreatorListOrder templateObjectCreatorListOrder;

    TemplateObjects(DuplicateHandler duplicateHandler, TemplateObjectCreatorListOrder templateObjectCreatorListOrder) {
        this(new LinkedList<>(), duplicateHandler, templateObjectCreatorListOrder);
    }

    private TemplateObjects(List<TemplateObjectCreator> templateObjectCreators, DuplicateHandler duplicateHandler, TemplateObjectCreatorListOrder templateObjectCreatorListOrder) {
        this.templateObjectCreators = templateObjectCreators;
        this.duplicateHandler = duplicateHandler;
        this.templateObjectCreatorListOrder = templateObjectCreatorListOrder;
    }

    private TemplateObjects(TemplateObjectDuplicateStrategy templateObjectDuplicateStrategy) {
        this(templateObjectDuplicateStrategy, templateObjectDuplicateStrategy);
    }

    public static TemplateObjects empty() {
        return new TemplateObjects(TemplateObjectDuplicateStrategy.STRICT);
    }

    /**
     * Add a template object creator instance. Called by {@link TemplateObjectsResolver}.
     *
     * @param templateObjectCreator a new instance.
     */
    void add(TemplateObjectCreator templateObjectCreator) {
        this.templateObjectCreators.add(templateObjectCreator);
    }

    /**
     * creates all {@link TemplateObject} instances.
     *
     * @return template object instances
     */
    public List<TemplateObject> collectTemplateObjectInstances() {
        return templateObjectCreators.stream().map(TemplateObjectCreator::createTemplateObjectInstance).collect(Collectors.toList());
    }

    /**
     * Collects the data set names.
     *
     * @return dataz names
     */
    public Set<String> collectDataSets() {
        return templateObjectCreators.stream().map(TemplateObjectCreator::getDataSetName).collect(Collectors.toSet());
    }


    /**
     * Filter the template objects which belongs to given data set.
     *
     * @param dataSetName the name of the data set.
     * @return filtered instance of template objects.
     */
    public TemplateObjects filterByDataSet(String dataSetName) {
        return new TemplateObjects(doFilterByDataSetName(dataSetName), duplicateHandler, templateObjectCreatorListOrder);
    }

    /**
     * Return all template objects with {@link Scope#GLOBAL}.
     *
     * @return new template objects instance.
     */
    public TemplateObjects filterGlobalScope() {
        return new TemplateObjects(doFilterByGlobalScope(), duplicateHandler, templateObjectCreatorListOrder);
    }


    /**
     * Merge two template objects.
     *
     * @param templateObjects the other template objects instance.
     * @return a new instance.
     */
    public TemplateObjects merge(TemplateObjects templateObjects) {
        return new TemplateObjects(doMerge(templateObjects), duplicateHandler, templateObjectCreatorListOrder);
    }


    /**
     * Applies all (newly created) template objects and send them to {@code templateObjectConsumer}. If two template objects have the same {@code name},
     * the second will be ignored.
     *
     * @param templateObjectConsumer a consumer function
     * @see TemplateObject#name()
     */
    public void apply(Consumer<TemplateObject> templateObjectConsumer) {
        final Set<String> templateObjectNames = new HashSet<>();
        for (TemplateObjectCreator templateObjectCreator : templateObjectCreatorListOrder.order(templateObjectCreators)) {
            final TemplateObject templateObject = templateObjectCreator.createTemplateObjectInstance();
            if (templateObjectNames.add(templateObject.name())) {
                LOGGER.debug("Use template object '{}' (annotation={})", templateObject.name(), templateObjectCreator.getAnnotation());
                templateObjectConsumer.accept(templateObject);
            } else {
                duplicateHandler.handleDuplicate(templateObject);
                LOGGER.warn("Template object '{}' already defined (annotation={}). Ignored!", templateObject.name(), templateObjectCreator.getAnnotation());
            }
        }
    }


    private List<TemplateObjectCreator> doFilterByDataSetName(String dataSetName) {
        return templateObjectCreators.stream()  //
                .filter(
                        hasGlobalScope().or(
                                hasLocalScope().and(hasDataSetName(dataSetName)))
                )
                .collect(Collectors.toList());
    }

    private static Predicate<TemplateObjectCreator> hasLocalScope() {
        return toc -> toc.hasScope(Scope.LOCAL);
    }

    private static Predicate<TemplateObjectCreator> hasGlobalScope() {
        return toc -> toc.hasScope(Scope.GLOBAL);
    }

    private static Predicate<TemplateObjectCreator> hasDataSetName(String dataSetName) {
        return toc -> dataSetName.equals(toc.getDataSetName());
    }

    private List<TemplateObjectCreator> doMerge(TemplateObjects templateObjects) {
        final LinkedList<TemplateObjectCreator> templateObjectCreators = new LinkedList<>(this.templateObjectCreators);
        templateObjectCreators.addAll(templateObjects.templateObjectCreators);
        return templateObjectCreators;

    }

    private List<TemplateObjectCreator> doFilterByGlobalScope() {
        return templateObjectCreators.stream()  //
                .filter(hasGlobalScope())
                .collect(Collectors.toList());
    }
}
