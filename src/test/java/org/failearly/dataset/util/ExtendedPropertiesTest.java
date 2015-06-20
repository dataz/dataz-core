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
package org.failearly.dataset.util;
import org.junit.Test;

import static org.failearly.dataset.test.AssertException.assertException;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.is;

/**
 * ExtendedPropertiesTest contains tests for ExtendedProperties
 */
public class ExtendedPropertiesTest {
    private final ExtendedProperties properties = new ExtendedProperties();

    @Test
    public void resolveNoVariable() throws Exception {
        // arrange / given
        properties.put("var0","value0");
        properties.put("var1","value1");

        // act / when
        properties.resolveReferences();

        // assert / then
        assertThat("Value of var1?", properties.getProperty("var1"), is("value1"));
    }

     @Test
    public void resolveUnknownVariable() throws Exception {
        // arrange / given
        properties.put("var1","${unknown.var}");

        // act / when
        properties.resolveReferences();

        // assert / then
        assertThat("Value of var1?", properties.getProperty("var1"), is("unknown.var"));
    }

    @Test
    public void resolveOneVariable() throws Exception {
        // arrange / given
        properties.put("var0","value0");
        properties.put("var1","${var0}");

        // act / when
        properties.resolveReferences();

        // assert / then
        assertThat("Value of var1?", properties.getProperty("var1"), is("value0"));
    }

    @Test
    public void resolveMultiVariable() throws Exception {
        // arrange / given
        properties.put("var0","value0");
        properties.put("var1","${var0},${var0}");

        // act / when
        properties.resolveReferences();

        // assert / then
        assertThat("Value of var1?", properties.getProperty("var1"), is("value0,value0"));
    }

    @Test
    public void resolveDeepVariables() throws Exception {
        // arrange / given
        properties.put("var0","value0");
        properties.put("var1","${var0}");
        properties.put("var2","${var1}");
        properties.put("var3","${var2}");

        // act / when
        properties.resolveReferences();

        // assert / then
        assertThat("Value of var1?", properties.getProperty("var3"), is("value0"));
    }

    @Test
    public void resolveDeepVariablesAndMultipleUse() throws Exception {
        // arrange / given
        properties.put("var0","value0");
        properties.put("var1","${var0}");
        properties.put("var2","${var1}");
        properties.put("var3","v2=${var2},v1=${var1},${var4}");

        // act / when
        properties.resolveReferences();

        // assert / then
        assertThat("Value of var1?", properties.getProperty("var3"), is("v2=value0,v1=value0,var4"));
    }


    @Test
    public void detectedEndless() throws Exception {
        // arrange / given
        properties.put("var0","${var0}");

        // act / when
        assertEndlessDetection("var0");
    }

    @Test
    public void detectedEndless0() throws Exception {
        // arrange / given
        properties.put("var0","${var0}");
        properties.put("var1","${var0}");

        // act / when
        assertEndlessDetection("var0");
        assertEndlessDetection("var1");
    }

    @Test
    public void detectedEndless1() throws Exception {
        // arrange / given
        properties.put("var0","${var1}");
        properties.put("var1","${var0}");

        // act / when
        assertEndlessDetection("var0");
        assertEndlessDetection("var1");
    }

    @Test
    public void detectedEndless2() throws Exception {
        // arrange / given
        properties.put("var0","${var2}");
        properties.put("var1","${var0}");
        properties.put("var2","${var1}");

        // act / when
        assertEndlessDetection("var2");
    }

    @Test
    public void detectEndlessWithValidReferencesAndUnknownIntermixed() throws Exception {
        // arrange / given
        properties.put("var0","value0");
        properties.put("var1","${var0}");
        properties.put("var2","${var1}");
        properties.put("var4","${var3}");
        properties.put("var3","v2=${var2},v1=${var1},${var4}");

        // act / when
        assertEndlessDetection();
    }

    public void assertEndlessDetection(String key) {
        try {
            properties.resolveReferences(key);
            fail("Runtime Exception expected!");
        } catch( Exception ex ) {
            // ok.
        }
    }
    public void assertEndlessDetection() {
        try {
            properties.resolveReferences();
            fail("Runtime Exception expected");
        } catch( Exception ex ) {
            // ok.
        }
    }

    @Test
    public void getMandatoryProperty() throws Exception {
        // arrange / given
        properties.setProperty("existing","  existing-value  ");
        properties.setProperty("existing-but-empty","");
        properties.setProperty("existing-but-blank","   \n\r\t ");
        properties.setProperty("existing-but-use-empty","(empty)");
        properties.setProperty("existing-but-use-null","(null)");

        // assert / then
        assertThat("Existing?", properties.getMandatoryProperty("existing"), is(equalTo("existing-value")));
        assertThat("Existing?", properties.getMandatoryProperty("existing-but-empty"), is(equalTo("")));
        assertThat("Existing?", properties.getMandatoryProperty("existing-but-blank"), is(equalTo("")));
        assertThat("Existing?", properties.getMandatoryProperty("existing-but-use-empty"), is(equalTo("")));
        assertException(MissingPropertyException.class,
                "Missing property 'existing-but-use-null'. The property is either missing or uses '(null)'.",
                () -> properties.getMandatoryProperty("existing-but-use-null")
        );
        assertException(MissingPropertyException.class,
                "Missing property 'unknown.property'. The property is either missing or uses '(null)'.",
                () -> properties.getMandatoryProperty("unknown.property")
        );
    }

    @Test
    public void getProperty() throws Exception {
        // arrange / given
        properties.setProperty("existing","  existing-value  ");
        properties.setProperty("existing-but-empty","");
        properties.setProperty("existing-but-blank","   \n\r\t ");
        properties.setProperty("existing-but-use-empty","(empty)");
        properties.setProperty("existing-but-use-null","(null)");


        // assert / then
        assertThat("Existing property?", properties.getProperty("existing"), is("existing-value"));
        assertThat("Existing but empty?", properties.getProperty("existing-but-empty"), is(""));
        assertThat("Existing but blank?", properties.getProperty("existing-but-blank"), is(""));
        assertThat("Existing with using (empty)?", properties.getProperty("existing-but-use-empty"), is(equalTo("")));
        assertThat("Existing with using (null)?", properties.getProperty("existing-but-use-null"), nullValue());
        assertThat("Not existing?", properties.getProperty("not-existing"), nullValue());
    }
}
