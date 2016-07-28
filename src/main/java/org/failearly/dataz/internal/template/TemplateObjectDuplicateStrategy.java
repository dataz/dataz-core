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

import org.failearly.common.annotation.traverser.TraverseStrategy;
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
