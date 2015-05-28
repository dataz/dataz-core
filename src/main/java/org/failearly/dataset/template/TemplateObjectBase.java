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
package org.failearly.dataset.template;

import java.util.Objects;

/**
 * TemplateObjectBase is responsible for ...
 */
public abstract class TemplateObjectBase implements TemplateObject {

    private final String dataset;
    private final String name;

    protected TemplateObjectBase(String dataset, String name) {
        assert ! Objects.toString(name,"").isEmpty() : "name is empty or null";
        assert ! Objects.toString(dataset,"").isEmpty() : "dataset is empty or null";

        this.dataset = dataset;
        this.name = name;
    }

    @Override
    public final String id() {
        return dataset + "-" + name;
    }

    @Override
    public final String name() {
        return name;
    }

    @Override
    public final String dataset() {
        return dataset;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TemplateObjectBase)) return false;

        TemplateObjectBase that = (TemplateObjectBase) o;
        return Objects.equals(id(), that.id());
    }

    @Override
    public int hashCode() {
        return Objects.hash(id());
    }

    @Override
    public final void __use_TemplateObjectBase__instead_of_implementing_TemplateObject() {
        throw new UnsupportedOperationException("do not call __use_TemplateObjectBase__instead_of_implementing_TemplateObject()");
    }

}
