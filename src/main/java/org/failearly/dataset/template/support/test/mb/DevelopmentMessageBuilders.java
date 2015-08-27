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

package org.failearly.dataset.template.support.test.mb;

import org.failearly.dataset.util.mb.MessageBuilder;

/**
 * Creates message builder instances.
 */
public final class DevelopmentMessageBuilders {
    public DevelopmentMessageBuilders() {
    }

    /**
     * Message builder used in case of not yet created Template Object Annotation.
     *
     * @param mb the (raw) message builder
     *
     * @return new custom (TemplateObjectMessageBuilder) message builder
     */
    public static TemplateObjectMessageBuilder missingTemplateObjectAnnotation(MessageBuilder mb) {
        return new MissingTemplateObjectAnnotationMessageBuilder(mb);
    }

    /**
     * Message builder used in case of not yet created Template Object Factory.
     *
     * @param mb the (raw) message builder
     *
     * @return new custom (TemplateObjectMessageBuilder) message builder
     */
    public static TemplateObjectMessageBuilder missingTemplateObjectFactory(MessageBuilder mb) {
        return new MissingTemplateObjectFactoryMessageBuilder(mb);
    }

    /**
     * Message builder used in case of not yet created TestFixture class.
     *
     * @param mb the (raw) message builder
     *
     * @return new custom (TemplateObjectMessageBuilder) message builder
     */
    public static TemplateObjectMessageBuilder missingTestFixture(MessageBuilder mb) {
        return null;
    }
}