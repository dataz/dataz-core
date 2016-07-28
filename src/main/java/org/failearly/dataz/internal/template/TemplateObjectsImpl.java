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
 * The implementation of {@link TemplateObjects}.
 *
 * @see TemplateObjectsResolver
 */
final class TemplateObjectsImpl implements TemplateObjects {
    static final TemplateObjects NO_TEMPLATE_OBJECTS = new TemplateObjectsImpl(TemplateObjectDuplicateStrategy.STRICT, TemplateObjectDuplicateStrategy.STRICT);

    private static final Logger LOGGER = LoggerFactory.getLogger(TemplateObjects.class);

    private final List<TemplateObjectCreator> templateObjectCreators;
    private final DuplicateHandler duplicateHandler;
    private final TemplateObjectListOrderCorrection templateObjectListOrderCorrection;

    private TemplateObjectsImpl(DuplicateHandler duplicateHandler, TemplateObjectListOrderCorrection templateObjectListOrderCorrection) {
        this(new LinkedList<>(), duplicateHandler, templateObjectListOrderCorrection);
    }

    TemplateObjectsImpl(List<TemplateObjectCreator> templateObjectCreators, DuplicateHandler duplicateHandler, TemplateObjectListOrderCorrection templateObjectListOrderCorrection) {
        this.templateObjectCreators = templateObjectCreators;
        this.duplicateHandler = duplicateHandler;
        this.templateObjectListOrderCorrection = templateObjectListOrderCorrection;
    }

    @Override
    public void add(TemplateObjectCreator templateObjectCreator) {
        this.templateObjectCreators.add(templateObjectCreator);
    }

    @Override
    public List<TemplateObject> collectTemplateObjectInstances() {
        return createUniqueTemplateObjectList();
    }


    @Override
    public TemplateObjects filterByDataSet(String dataSetName) {
        return new TemplateObjectsImpl(doFilterByDataSetName(dataSetName), duplicateHandler, templateObjectListOrderCorrection);
    }

    @Override
    public TemplateObjects filterGlobalScope() {
        return new TemplateObjectsImpl(doFilterByGlobalScope(), duplicateHandler, templateObjectListOrderCorrection);
    }


    @Override
    public TemplateObjects merge(TemplateObjects templateObjects) {
        return new TemplateObjectsImpl(
            doMerge((TemplateObjectsImpl) templateObjects),
            duplicateHandler,
            templateObjectListOrderCorrection
        );
    }


    @Override
    public void apply(Consumer<TemplateObject> templateObjectConsumer) {
        createUniqueTemplateObjectList().forEach(templateObjectConsumer);
    }

    private List<TemplateObject> createUniqueTemplateObjectList() {
        final List<TemplateObject> templateObjectList = new LinkedList<>();
        final Set<String> checkForDuplicates = new HashSet<>();
        for (TemplateObjectCreator templateObjectCreator : templateObjectListOrderCorrection.correctOrder(templateObjectCreators)) {

            final TemplateObject templateObject = templateObjectCreator.createTemplateObjectInstance();
            final String templateObjectName = templateObject.name();

            if (checkForDuplicates.add(templateObjectName)) {
                LOGGER.debug("Use template object '{}' (annotation={})", templateObjectName, templateObjectCreator.getAnnotation());
                templateObjectList.add(templateObject);
            } else {
                duplicateHandler.handleDuplicate(templateObject);
            }
        }

        return templateObjectList;
    }

    private List<TemplateObjectCreator> createUniqueTemplateObjectCreatorList() {
        final List<TemplateObjectCreator> templateObjectCreatorList = new LinkedList<>();
        final Set<String> checkForDuplicates = new HashSet<>();
        for (TemplateObjectCreator templateObjectCreator : templateObjectListOrderCorrection.correctOrder(templateObjectCreators)) {
            final String templateObjectName = templateObjectCreator.getName();
            if (checkForDuplicates.add(templateObjectName)) {
                templateObjectCreatorList.add(templateObjectCreator);
            } else {
                duplicateHandler.handleDuplicate(templateObjectCreator.createTemplateObjectInstance());
            }
        }

        return templateObjectCreatorList;
    }


    private List<TemplateObjectCreator> doFilterByDataSetName(String dataSetName) {
        return templateObjectCreators.stream()  //
            .filter(hasDataSetName(dataSetName))
            .collect(Collectors.toList());
    }

    private static Predicate<TemplateObjectCreator> hasGlobalScope() {
        return toc -> toc.hasScope(Scope.GLOBAL);
    }

    private static Predicate<TemplateObjectCreator> hasDataSetName(String dataSetName) {
        return toc -> {
            final Set<String> dataSetNames = toc.getDataSetNames();
            return dataSetNames.isEmpty() || dataSetNames.contains(dataSetName);
        };
    }

    private List<TemplateObjectCreator> doMerge(TemplateObjectsImpl templateObjects) {
        final List<TemplateObjectCreator> templateObjectCreators = this.createUniqueTemplateObjectCreatorList();
        templateObjectCreators.addAll(templateObjects.createUniqueTemplateObjectCreatorList());

        return makeUniqueIgnoringDuplicates(templateObjectCreators);

    }

    private static List<TemplateObjectCreator> makeUniqueIgnoringDuplicates(List<TemplateObjectCreator> templateObjectCreators) {
        return templateObjectCreators.stream()  //
            .distinct()                         //
            .collect(Collectors.toList());      //
    }

    private List<TemplateObjectCreator> doFilterByGlobalScope() {
        return templateObjectCreators.stream()  //
            .filter(hasGlobalScope())
            .collect(Collectors.toList());
    }

    @Override
    public String toString() {
        return createUniqueTemplateObjectList().toString();
    }
}
