/*
 * dataZ - Test Support For Data Stores.
 *
 * Copyright 2014-2020 the original author or authors.
 *
 * All rights reserved. This program and the accompanying materials are
 * made available under the terms of the Eclipse Public License v2.0 which
 * accompanies this distribution and is available at
 *
 * https://www.eclipse.org/legal/epl-v20.html
 */

package org.failearly.dataz.internal.template;

import org.failearly.dataz.internal.common.annotation.traverser.TraverseStrategy;
import org.failearly.dataz.template.TemplateObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.List;

/**
 * TemplateObjectDuplicateStrategy is responsible for defining the strategy, to handle
 * duplicated template objects (same name associated to the same dataz).
 */
public enum TemplateObjectDuplicateStrategy implements DuplicateHandler, TemplateObjectListOrderCorrection {
    /**
     * In case of a duplicate template object a {@link DuplicateTemplateObjectException} will be thrown. (DEFAULT)
     */
    STRICT {
        @Override
        public void handleDuplicate(TemplateObject templateObject) {
            throw new DuplicateTemplateObjectException(templateObject);
        }

    },
    /**
     * Overwrites the last template object with the same name and prints a warning. The last template object will win.
     */
    OVERWRITE {
        @Override
        public List<TemplateObjectCreator> correctOrder(List<TemplateObjectCreator> templateObjectCreatorList) {
            Collections.reverse(templateObjectCreatorList);
            return templateObjectCreatorList;
        }

        @Override
        TraverseStrategy traverseStrategy() {
            return TraverseStrategy.BOTTOM_UP;
        }
    },
    /**
     * Ignores the last template object with the same name and prints a warning. The first template object will win.
     */
    IGNORE;

    private static final Logger LOGGER = LoggerFactory.getLogger(TemplateObjectDuplicateStrategy.class);

    @Override
    public void handleDuplicate(TemplateObject templateObject) {
        LOGGER.warn("Duplicated template object with name {} ({}). Ignored.", templateObject.name(), templateObject);
    }

    @Override
    public List<TemplateObjectCreator> correctOrder(List<TemplateObjectCreator> templateObjectCreatorList) {
        return templateObjectCreatorList;
    }

    TraverseStrategy traverseStrategy() {
        return TraverseStrategy.TOP_DOWN;
    }


}
