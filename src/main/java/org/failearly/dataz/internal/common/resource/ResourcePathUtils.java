/*
 * dataZ - Test Support For Data Stores.
 *
 * Copyright 2014-2017 the original author or authors.
 *
 * All rights reserved. This program and the accompanying materials are
 * made available under the terms of the Eclipse Public License v1.0 which
 * accompanies this distribution and is available at
 *
 * http://www.eclipse.org/legal/epl-v10.html
 */

package org.failearly.dataz.internal.common.resource;

import com.google.common.base.CharMatcher;
import com.google.common.base.Joiner;
import com.google.common.base.Splitter;
import org.failearly.dataz.common.Tests;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * ResourcePathUtils contains some utility methods for resolving (default or absolute) resource path.
 */
@Tests("ResourcePathUtilsTest")
public final class ResourcePathUtils {
    private ResourcePathUtils() {
    }

    /**
     * Use if you need only the default naming strategy for the clazz.
     *
     * @param clazz  the clazz
     * @param suffix the suffix of the resource
     *
     * @return the default resource name
     */
    public static String defaultClassResource(Class<?> clazz, String suffix) {
        return resourcePath(clazz.getSimpleName() + "." + removeLeadingDot(suffix), clazz);
    }

    private static String removeLeadingDot(String suffix) {
        return suffix.replaceFirst("^\\.", "");
    }

    /**
     * Creates from the resource name the absolute path to the resource.
     *
     * @param resourceName the resource name
     * @param clazz        the (test) class
     *
     * @return the (absolute) path to the resource
     *
     * @throws InvalidResourcePathException in case of obviously invalid resource path.
     */
    public static String resourcePath(String resourceName, Class<?> clazz) throws InvalidResourcePathException {
        if (isValidResourceName(resourceName)) {
            final String path = resolveResourcePathFromClass(clazz);
            return toResourcePath(resourceName, path);
        }

        throw new InvalidResourcePathException(resourceName);
    }

    private static boolean isValidResourceName(String resourceName) {
        if (resourceName == null)
            return false;
        if (resourceName.isEmpty())
            return false;
        if( CharMatcher.whitespace().matchesAnyOf(resourceName))
            return false;
        if (resourceName.endsWith("/"))
            return false;
        return resourceName.equals(trim(resourceName));
    }

    private static String trim(String resourceName) {
        return CharMatcher.whitespace().trimFrom(resourceName);
    }

    private static String resolveResourcePathFromClass(Class<?> clazz) {
        return "/" + packageAsName(clazz).replace(".", "/") + "/";
    }

    private static String packageAsName(Class<?> clazz) {
        return clazz.getPackage().getName();
    }

    private static String toResourcePath(final String originResourceName, final String classPath) {
        final String actuallyResourcePath;
        if (originResourceName.startsWith("/"))
            actuallyResourcePath = originResourceName;
        else
            actuallyResourcePath = classPath + originResourceName;
        return checkForInvalidResourcePath(originResourceName, removeRelativePathElements(actuallyResourcePath, originResourceName));
    }

    private static String checkForInvalidResourcePath(String originResourceName, String resourcePath) {
        if ("/".equals(resourcePath) || resourcePath.endsWith("/")) {
            throw new InvalidResourcePathException(originResourceName);
        }
        return resourcePath;
    }

    private static String removeRelativePathElements(String absolutePath, String originResourceName) {
        assert absolutePath.startsWith("/") : "Resource path is not absolute!";
        return "/" + sanitizeRelativePathElements(absolutePath, originResourceName);
    }

    private static String sanitizeRelativePathElements(final String absolutePath, String originResourceName) {
        return removeDotDotPathElements(removeDotPathElements(absolutePath), originResourceName);
    }

    private static String removeDotDotPathElements(final String resourcePath, String originResourceName) {
        final List<String> pathElements = Splitter.on("/").omitEmptyStrings().splitToList(resourcePath);
        final List<String> result = new LinkedList<>(pathElements);
        int idx = 0;
        for (String pathElement : pathElements) {
            assert pathElement.equals(result.get(idx)) : "Index to path elements and iterator cursor does not point to the same path element!!";
            if ("..".equals(pathElement)) {
                if (idx == 0) {
                    throw new InvalidResourcePathException(originResourceName);
                }
                // remove ".."
                result.remove(idx);
                // remove previous path element
                result.remove(idx - 1);
                idx -= 2;
            }
            idx += 1;
        }

        return Joiner.on('/').join(result);
    }

    private static String removeDotPathElements(String resourcePath) {

        final List<String> pathElements = Splitter.on('/').splitToList(resourcePath).stream()
            .filter((pe) -> !pe.equals("."))
            .filter((pe) -> !pe.equals(""))
            .collect(Collectors.toList());

        return Joiner.on('/').join(pathElements);
        
    }
}
