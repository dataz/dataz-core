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

package org.failearly.dataz.template;

/**
 * The scope of {@link TemplateObject}. The scope defines, if the template object will be visible within
 * {@link org.failearly.dataz.Use} templates and if {@link TemplateObject#datasets()} will be used or not.
 */
public enum Scope {
    /**
     * Use {@link TemplateObject#datasets()}. Not visible for {@link org.failearly.dataz.Use}.
     */
    LOCAL,
    /**
     * Independent from {@link TemplateObject#datasets()}. Global visibility.
     */
    GLOBAL,
    /**
     * The default scope is <i>LOCAL</i>.
     */
    DEFAULT {
        @Override
        public Scope getScopeValue() {
            return LOCAL;
        }
    };

    public Scope getScopeValue() {
        return this;
    }
}
