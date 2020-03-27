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

package learning.velocity;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.exception.ResourceNotFoundException;
import org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mockito;

import java.io.InputStream;
import java.io.StringWriter;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import static org.apache.velocity.runtime.RuntimeConstants.RESOURCE_LOADER;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
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
        Properties properties=new Properties();
        properties.setProperty(RESOURCE_LOADER,"class");
        properties.setProperty("class.resource.loader.class", LocalClasspathResourceLoader.class.getName());
        engine.setApplicationAttribute("class", VelocityTest.class);
        engine.init(properties);
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
        context.put("val", "RangeGenerator{dataz=DS1, name=RG1, from=1, to=5}");

        // act / when
        engine.evaluate(context, stringWriter, "LOOP_TEMPLATE", VALUE_TEMPLATE);

        // assert / then
        assertThat(stringWriter.toString(), is("\tHello\tRangeGenerator{dataz=DS1, name=RG1, from=1, to=5}\n"));
        Mockito.verify(stringWriter, Mockito.times(0)).close();
        Mockito.verify(stringWriter, Mockito.times(0)).flush();
    }

    @Test
    public void useTemplate() throws Exception {
        // arrange / given

        // act / when
        final Template template = engine.getTemplate("any-template.vm");

        // assert / then
        assertThat(template, is(notNullValue()));
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

    public static class LocalClasspathResourceLoader extends ClasspathResourceLoader {
        @Override
        public InputStream getResourceStream(String name) throws ResourceNotFoundException {
            final Class<?> clazz= (Class<?>) super.rsvc.getApplicationAttribute("class");
            final InputStream result =  clazz.getResourceAsStream(name);
            if( result!=null ) {
                return result;
            }

            return super.getResourceStream(name);
        }
    }
}
