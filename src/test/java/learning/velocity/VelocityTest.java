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
package learning.velocity;

import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mockito;

import java.io.StringWriter;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

/**
 * VelocityTest is responsible for ...
 */
public class VelocityTest {
    private static String NEW_LINE=System.getProperty("line.separator");
    private static VelocityEngine engine=new VelocityEngine();
    private static final String NO_TEMPLATE="This is no template";
    private static final String LOOP_TEMPLATE ="#foreach( $v in $values)" +
            "\tHello\t$v" + NEW_LINE +
            "#end" + NEW_LINE +
            "##Any comment" + NEW_LINE;
    private static final String VALUE_TEMPLATE =
            "\tHello\t$val" + NEW_LINE +
            "##Any comment";
    private final VelocityContext context=new VelocityContext();

    @BeforeClass
    public static void engineInit() throws Exception {
        engine.init();
    }

    @Before
    public void initContext() throws Exception {
        context.put("values", Arrays.asList("Marko", "Jana", "Emma", "Femke"));
    }

    @Test
    public void noTemplate() throws Exception {
        // arrange / given
        final StringWriter stringWriter= Mockito.spy(new StringWriter());

        // act / when
        engine.evaluate(context, stringWriter, "NO_TEMPLATE", NO_TEMPLATE);

        // assert / then
        assertThat(stringWriter.toString(), is(NO_TEMPLATE));
        Mockito.verify(stringWriter, Mockito.times(0)).close();
        Mockito.verify(stringWriter, Mockito.times(0)).flush();
    }

    @Test
    public void withTemplate() throws Exception {
        // arrange / given
        final StringWriter stringWriter= Mockito.spy(new StringWriter());

        // act / when
        engine.evaluate(context, stringWriter, "LOOP_TEMPLATE", LOOP_TEMPLATE);

        // assert / then
        assertThat(stringWriter.toString(), is("\tHello\tMarko\n\tHello\tJana\n\tHello\tEmma\n\tHello\tFemke\n"));
        Mockito.verify(stringWriter, Mockito.times(0)).close();
        Mockito.verify(stringWriter, Mockito.times(0)).flush();
    }

    @Test
    public void with2Foreach() throws Exception {
        // arrange / given
        final StringWriter stringWriter= Mockito.spy(new StringWriter());

        // act / when
        engine.evaluate(context, stringWriter, "LOOP_TEMPLATE2", LOOP_TEMPLATE+LOOP_TEMPLATE);

        // assert / then
        assertThat(stringWriter.toString(), is(
                "\tHello\tMarko\n\tHello\tJana\n\tHello\tEmma\n\tHello\tFemke\n" +
                "\tHello\tMarko\n\tHello\tJana\n\tHello\tEmma\n\tHello\tFemke\n"
            )
        );
        Mockito.verify(stringWriter, Mockito.times(0)).close();
        Mockito.verify(stringWriter, Mockito.times(0)).flush();
    }

    @Test
    public void withGenerator() throws Exception {
        // arrange / given
        final StringWriter stringWriter= Mockito.spy(new StringWriter());
        context.put("values", new AnyIterable());

        // act / when
        engine.evaluate(context, stringWriter, "LOOP_TEMPLATE", LOOP_TEMPLATE);

        // assert / then
        assertThat(stringWriter.toString(), is("\tHello\t1\n\tHello\t2\n\tHello\t3\n\tHello\t4\n\tHello\t5\n"));
        Mockito.verify(stringWriter, Mockito.times(0)).close();
        Mockito.verify(stringWriter, Mockito.times(0)).flush();
    }

    @Test
    public void withGeneratorAsStaticValue() throws Exception {
        // arrange / given
        final StringWriter stringWriter= Mockito.spy(new StringWriter());
        context.put("val", "RangeGenerator{dataset=DS1, name=RG1, from=1, to=5}");

        // act / when
        engine.evaluate(context, stringWriter, "LOOP_TEMPLATE", VALUE_TEMPLATE);

        // assert / then
        assertThat(stringWriter.toString(), is("\tHello\tRangeGenerator{dataset=DS1, name=RG1, from=1, to=5}\n"));
        Mockito.verify(stringWriter, Mockito.times(0)).close();
        Mockito.verify(stringWriter, Mockito.times(0)).flush();
    }

    public static class AnyIterable implements Iterable<Integer> {
        private static final List<Integer> LIST = Arrays.asList(1, 2, 3, 4, 5);

        public AnyIterable() {
        }

        @Override
        public Iterator<Integer> iterator() {
            return LIST.iterator();
        }
    }
}
