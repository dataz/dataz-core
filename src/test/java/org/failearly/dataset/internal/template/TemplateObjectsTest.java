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

package org.failearly.dataset.internal.template;

import org.junit.Test;

import static org.hamcrest.Matchers.contains;
import static org.junit.Assert.assertThat;

/**
 * Tests for {@link TemplateObjectsResolver}.
 */
public class TemplateObjectsTest extends TemplateObjectsTestBase {

    @Test
    public void filter_data_set_by_name__should_remove_template_objects() throws Exception {
        // arrange / given
        final TemplateObjects originTemplateObjects = TemplateObjectsResolver.resolveFromTestMethod(getTestMethod("anyTestMethodWithGenerator"));

        // act / when
        final TemplateObjects templateObjects=originTemplateObjects.filterByDataSet("D3");

        // assert / then
        assertThat(extractAnnotations(templateObjects), contains(
                "@org.failearly.dataset.generator.RangeGenerator(limit=LIMITED, start=10, end=20, dataset=D3, name=G2)",
                "@org.failearly.dataset.generator.RangeGenerator(limit=LIMITED, start=10, end=20, dataset=D3, name=G3)"
        ));
    }

    @Test
    public void apply__should_execute_all_unique_template_objects() throws Exception {
        // arrange / given

        // act / when

        // assert / then
    }

    @Test
    public void apply__should_drop_template_objects_with_same_name__keeping_the_first() throws Exception {
        // arrange / given

        // act / when

        // assert / then
    }
}