/*
 * dataZ - Test Support For Data Stores.
 *
 * Copyright (C) 2014-2016 marko (http://fail-early.com)
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
 *
 */
package org.failearly.dataset.template.support.test.mb;

import org.failearly.common.test.mb.MessageBuilder;

/**
 * MissingEncoderObject is responsible for ...
 */
final class MissingEncoderObject extends TemplateObjectMessageBuilder {
    public MissingEncoderObject(MessageBuilder mb) {
        super(mb);
    }

    @Override
    protected TemplateObjectMessageBuilder errorMessage() {
        this.firstLine("Your __ton__ factory (__tof__) does not create an Encoder object.");
        return this;
    }

    @Override
    protected TemplateObjectMessageBuilder errorDescription() {
        this.lines(
            "You need an initial implementation for Encoder implementation.",
            "Please use for this the proposed base class."
        );
        return this;
    }


    @Override
    protected TemplateObjectMessageBuilder actions() {
        // TODO: Implement MissingEncoderObject#actions
        if(true) throw new UnsupportedOperationException("MissingEncoderObject.actions() not yet implemented");
        return this;
    }
}
