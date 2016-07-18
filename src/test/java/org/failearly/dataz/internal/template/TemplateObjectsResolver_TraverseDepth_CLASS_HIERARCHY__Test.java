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

import org.failearly.common.annotation.traverser.TraverseDepth;
import org.junit.Test;

import java.lang.reflect.Method;

/**
 * Tests for {@link TemplateObjectsResolver#resolveFromMethod0(Method)} and {@link TemplateObjectsResolver#resolveFromClass0(Class)} and
 * {@link TraverseDepth#CLASS_HIERARCHY}.
 */
public class TemplateObjectsResolver_TraverseDepth_CLASS_HIERARCHY__Test extends TemplateObjectsTestBase {

    private TemplateObjectsResolver buildTemplateObjectsResolver() {
        return buildTemplateObjectsResolver(TraverseDepth.CLASS_HIERARCHY);
    }

    @Test
    public void on_method_without_template_objects__should_resolve_declared_class_template_objects() throws Exception {
        // arrange / given
        final TemplateObjectsResolver templateObjectsResolver = buildTemplateObjectsResolver();

        // act / when
        final TemplateObjects templateObjects = templateObjectsResolver.resolveFromMethod0(withoutTemplateObjects());

        // assert / then
        assertTemplateObjects(templateObjects,
                "@org.failearly.dataz.test.SimpleTemplateObject(description=(0) on class AClass, dataz=<don't care>, name=GLOBAL)",
                "@org.failearly.dataz.test.SimpleTemplateObject(description=(1) on class AClass, dataz=D1, name=G1)",
                "@org.failearly.dataz.test.SimpleTemplateObject(description=(2) on class AClass, dataz=D2, name=G2)",
                "@org.failearly.dataz.test.SimpleTemplateObject(description=(0) on class BaseClass, dataz=D0, name=G1)"
         );
    }

    @Test
    public void on_method_with_template_objects__should_resolve_methods__and__declared_class_template_objects() throws Exception {
        // arrange / given
        final TemplateObjectsResolver templateObjectsResolver = buildTemplateObjectsResolver();

        // act / when
        final TemplateObjects templateObjects = templateObjectsResolver.resolveFromMethod0(withTemplateObjects());

        // assert / then
        assertTemplateObjects(templateObjects,
                "@org.failearly.dataz.test.SimpleTemplateObject(description=(0) on class AClass, dataz=<don't care>, name=GLOBAL)",
                "@org.failearly.dataz.test.SimpleTemplateObject(description=(1) on class AClass, dataz=D1, name=G1)",
                "@org.failearly.dataz.test.SimpleTemplateObject(description=(2) on class AClass, dataz=D2, name=G2)",
                "@org.failearly.dataz.test.SimpleTemplateObject(description=(0) on class BaseClass, dataz=D0, name=G1)",
                "@org.failearly.dataz.test.SimpleTemplateObject(description=(3) On method withTemplateObjects, dataz=D3, name=G2)",
                "@org.failearly.dataz.test.SimpleTemplateObject(description=(4) On method withTemplateObjects, dataz=D3, name=G3)"
        );
    }

    @Test
    public void on_class__should_resolve_only_class_hierarchy_template_objects() throws Exception {
        // arrange / given
        final TemplateObjectsResolver templateObjectsResolver = buildTemplateObjectsResolver();

        // act / when
        final TemplateObjects templateObjects = templateObjectsResolver.resolveFromClass0(AClass.class);

        // assert / then
        assertTemplateObjects(templateObjects,
                "@org.failearly.dataz.test.SimpleTemplateObject(description=(0) on class AClass, dataz=<don't care>, name=GLOBAL)",
                "@org.failearly.dataz.test.SimpleTemplateObject(description=(1) on class AClass, dataz=D1, name=G1)",
                "@org.failearly.dataz.test.SimpleTemplateObject(description=(2) on class AClass, dataz=D2, name=G2)",
                "@org.failearly.dataz.test.SimpleTemplateObject(description=(0) on class BaseClass, dataz=D0, name=G1)"
        );
    }


}