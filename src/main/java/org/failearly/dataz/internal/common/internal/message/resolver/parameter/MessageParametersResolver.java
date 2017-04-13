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

package org.failearly.dataz.internal.common.internal.message.resolver.parameter;

import org.failearly.dataz.internal.common.internal.message.MessageParameters;
import org.failearly.dataz.internal.common.message.MessageBuilder;

/**
 * MessageParametersResolver is responsible for ...
 */
public interface MessageParametersResolver {
    MessageParameters resolveMessageParameters(Class<? extends MessageBuilder> messageBuilderClass);
}
