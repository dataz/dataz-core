/*
 * dataZ - Test Support For Data Stores.
 *
 * Copyright 2014-2017 the original author or authors.
 *
 * All rights reserved. This program and the accompanying materials are
 * made available under the terms of the Eclipse Public License v1.0 which
 * accompanies this distribution and is available at
 *
 * http://www.eclipse.org/legal/epl-v10.html
 */

package org.failearly.dataz.internal.template;

import java.util.List;

/**
 * TemplateObjectListOrderCorrection is responsible for changing the order of a list of templateObjectCreatorList.
 *
 * @see TemplateObjectDuplicateStrategy
 * @see TemplateObjects
 */
interface TemplateObjectListOrderCorrection {
    List<TemplateObjectCreator> correctOrder(List<TemplateObjectCreator> templateObjectCreatorList);
}
