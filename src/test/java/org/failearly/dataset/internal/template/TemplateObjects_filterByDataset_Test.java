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

import org.failearly.dataset.internal.annotation.TraverseDepth;
import org.junit.Test;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.junit.Assert.assertThat;

/**
 * Tests for {@link TemplateObjects#filterByDataSet(String)}.
 */
public class TemplateObjects_filterByDataset_Test extends TemplateObjectsTestBase {

    private static TemplateObjectsResolver buildTemplateObjectsResolver() {
        return TemplateObjectsResolver.builder().withTraverseDepth(TraverseDepth.DECLARED_CLASS).build();
    }

    @Test
    public void should_remove_template_objects_which_does_not_belong_to_the_dataset() throws Exception {
        // arrange / given
        final TemplateObjects originTemplateObjects = buildTemplateObjectsResolver().resolveFromTestMethod0(withTemplateObjects());

        // act / when
        final TemplateObjects filteredTemplateObjects = originTemplateObjects.filterByDataSet("D3");

        // assert / then
        assertThat("All", extractDataSetNames(originTemplateObjects), containsInAnyOrder(
                "D3", // to be kept
                "D2", // to be removed
                "D1"  // to be removed
        ));
        assertThat("Filtered", extractDataSetNames(filteredTemplateObjects), containsInAnyOrder(
                "D3"
        ));
    }

}