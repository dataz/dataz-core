/*
 * dataSet - Test Support For Datastores.
 *
 * Copyright (C) 2014-2014 Marko Umek (http://fail-early.com/contact)
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

package org.failearly.dataset.internal.generator.resolver;

import org.failearly.dataset.generator.RangeGenerator;
import org.failearly.dataset.test.TestUtils;
import org.junit.Test;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.List;
import java.util.stream.Collectors;

import static org.hamcrest.Matchers.contains;
import static org.junit.Assert.assertThat;

public class GeneratorResolverTest {

    @Test
    public void resolveGeneratorsFromTestMethodWithoutGenerator() throws Exception {
        // act / when
        final List<GeneratorCreator> generatorCreators = GeneratorResolver.resolveFromTestMethod(getTestMethod("anyTestMethodWithoutGenerator"));

        // assert / then
        assertThat("Generator Creator(s)?", extractDataSetNames(generatorCreators), contains(
                "@org.failearly.dataset.generator.RangeGenerator(limit=LIMITED, start=0, end=10, dataset=D1, name=G1)",
                "@org.failearly.dataset.generator.RangeGenerator(limit=LIMITED, start=10, end=20, dataset=D2, name=G2)",
                "@org.failearly.dataset.generator.RangeGenerator(limit=LIMITED, start=0, end=10, dataset=D0, name=G1)"
            ));
    }

    @Test
    public void resolveGeneratorsFromTestMethodWithGenerator() throws Exception {
        // act / when
        final List<GeneratorCreator> generatorCreators = GeneratorResolver.resolveFromTestMethod(getTestMethod("anyTestMethodWithGenerator"));

        // assert / then
        assertThat("Generator Creator(s)?", extractDataSetNames(generatorCreators), contains(
                "@org.failearly.dataset.generator.RangeGenerator(limit=LIMITED, start=10, end=20, dataset=D3, name=G2)",
                "@org.failearly.dataset.generator.RangeGenerator(limit=LIMITED, start=10, end=20, dataset=D3, name=G3)",
                "@org.failearly.dataset.generator.RangeGenerator(limit=LIMITED, start=0, end=10, dataset=D1, name=G1)",
                "@org.failearly.dataset.generator.RangeGenerator(limit=LIMITED, start=10, end=20, dataset=D2, name=G2)",
                "@org.failearly.dataset.generator.RangeGenerator(limit=LIMITED, start=0, end=10, dataset=D0, name=G1)"
            ));
    }

    private List<String> extractDataSetNames(List<GeneratorCreator> generatorCreators) {
        return generatorCreators.stream().map(GeneratorCreator::getAnnotation).map(Annotation::toString).collect(Collectors.toList());
    }


    private static Method getTestMethod(String name) throws NoSuchMethodException {
        return TestUtils.resolveMethodFromClass(name, TestFixture.class);
    }

    @RangeGenerator(name = "G1", dataset = "D0", start = 0, end = 10)
    private static class BaseClass {
    }

    @SuppressWarnings("UnusedDeclaration")
    @RangeGenerator(name = "G1", dataset = "D1", start = 0, end = 10)
    @RangeGenerator(name = "G2", dataset = "D2", start = 10, end = 20)
    private static class TestFixture extends BaseClass {

        @RangeGenerator(name = "G2", dataset = "D3", start = 10, end = 20)
        @RangeGenerator(name = "G3", dataset = "D3", start = 10, end = 20)
        public void anyTestMethodWithGenerator() {
        }

        public void anyTestMethodWithoutGenerator() {
        }

    }
}