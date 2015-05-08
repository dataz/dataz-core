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
package org.failearly.dataset.resource;

import org.apache.commons.lang.StringUtils;
import org.failearly.dataset.internal.resource.InvalidResourcePathException;
import org.failearly.dataset.internal.resource.ResourceType;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * ResourcePathUtils contains some utility methods for resolving (default or absolute) resource path.
 */
public final class ResourcePathUtils {
    private ResourcePathUtils() {
    }

    /**
     * Creates a default resource name from {@code testClass}.
     * <br><br>
     * Example: Given test class com.mycompany.project.module.MyTest
     * <br><br>
     * The result: MyTest.&lt;datastore-suffix&gt;}.
     * The {@literal <datastore-suffix>} will be generated from {@code dataStoreId} and {@code resourceType}.
     *
     * @param testClass    the test class
     * @param dataStoreId  the ID of an data store
     * @param resourceType the resource type
     * @return the default resource path
     */
    public static String createDefaultResourceNameFromTestClass(Class<?> testClass, String dataStoreId, ResourceType resourceType) {
        return testClass.getSimpleName() + resolveDataStoreSuffix(dataStoreId, resourceType);
    }

    /**
     * Creates a default resource name from {@code testMethod}.
     * <br><br>
     * Example: Given test method com.mycompany.project.module.MyTest#myTestMethod
     * <br><br>
     * The result: {@code MyTest-myTestMethod.&lt;datastore-suffix&gt;}.
     * The {@literal <datastore-suffix>} will be generated from {@code dataStoreId} and {@code resourceType}.
     *
     * @param testMethod   the test method
     * @param dataStoreId  the data store
     * @param resourceType the resource type
     * @return the default resource path
     */
    public static String createDefaultResourceNameFromTestMethod(Method testMethod, String dataStoreId, ResourceType resourceType) {
        return testMethod.getDeclaringClass().getSimpleName() + "-" + testMethod.getName() + resolveDataStoreSuffix(dataStoreId, resourceType);
    }

    private static String resolveDataStoreSuffix(String dataStoreId, ResourceType resourceType) {
        String suffix = resourceType.resolveDataStoreSuffix(dataStoreId);
        if (!suffix.startsWith(".")) {
            suffix = "." + suffix;
        }
        return suffix;
    }

    /**
     * Creates from the resource name the absolute path to the resource.
     *
     * @param resourceName the resource name
     * @param clazz        the (test) class
     * @return the (absolute) path to the resource
     *
     * @throws org.failearly.dataset.internal.resource.InvalidResourcePathException in case of obviously invalid resource path.
     */
    static String resourcePath(String resourceName, Class<?> clazz) {
        final String trimmedResource = trim(resourceName);
        if (trimmedResource != null) {
            final String path = resolveResourcePathFromTestClass(clazz);
            return toResourcePath(trimmedResource, path);
        }

        throw new InvalidResourcePathException(resourceName);
    }

    private static String trim(String resourceName) {
        return StringUtils.trimToNull(resourceName);
    }

    private static String resolveResourcePathFromTestClass(Class<?> clazz) {
        return "/" + packageAsName(clazz).replace(".", "/") + "/";
    }

    private static String packageAsName(Class<?> clazz) {
        return clazz.getPackage().getName();
    }

    private static String toResourcePath(String resourceName, String resourcePath) {
        String actuallyResourcePath = resourcePath + resourceName;
        if (resourceName.startsWith("/"))
            actuallyResourcePath = resourceName;
        return checkForInvalidResourcePath(resourceName, removeRelativePathElements(actuallyResourcePath));
    }

    private static String checkForInvalidResourcePath(String resourceName, String resourcePath) {
        if ("/".equals(resourcePath) || resourceName.endsWith("/")) {
            throw new InvalidResourcePathException(resourceName);
        }
        return resourcePath;
    }

    private static String removeRelativePathElements(String resourcePath) {
        assert resourcePath.startsWith("/") : "Resource path is not absolute!";
        return removeDotDotPathElements(removeDotPathElements(resourcePath));
    }

    private static String removeDotDotPathElements(String resourcePath) {
        final String[] pathElements = StringUtils.split(resourcePath, "/");
        final List<String> result = new LinkedList<>(Arrays.asList(pathElements));
        int idx = 0;
        for (String pathElement : pathElements) {
            assert pathElement.equals(result.get(idx)) : "Index to path elements and iterator cursor does not point to the same path element!!";
            if ("..".equals(pathElement)) {
                if( idx==0 ) {
                    throw new InvalidResourcePathException(resourcePath);
                }
                // remove ".."
                result.remove(idx);
                // remove previous path element
                result.remove(idx - 1);
                idx -= 2;
            }
            idx += 1;
        }

        return "/" + StringUtils.join(result, "/");
    }

    private static String removeDotPathElements(String resourcePath) {
        String result = "/";
        for (String pathElement : StringUtils.split(resourcePath, "/")) {
            if (!".".equals(pathElement)) {
                result += pathElement + "/";
            }
        }

        return removeLastSlash(result);
    }

    private static String removeLastSlash(String result) {
        return result.substring(0, result.length() - 1);
    }
}
