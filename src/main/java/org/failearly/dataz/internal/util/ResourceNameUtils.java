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

package org.failearly.dataz.internal.util;

import org.apache.commons.lang.StringUtils;
import org.failearly.dataz.config.DataSetProperties;

/**
 * Utility method for handling resource names.
 */
public class ResourceNameUtils {
    /**
     * Remove path and suffix from {@code fullQualifiedResourceName}.
     * <br><br>
     * Examples:<br>
     * <ul>
     * <li>{@code "/path/to/resource.suffix"} becomes {@code "resource"}</li>
     * <li>{@code "resource.suffix.vm"} becomes {@code "resource"}</li>
     * <li>{@code "resource"} becomes {@code "resource"}</li>
     * <li>{@code ""} becomes {@code ""}</li>
     * <li>{@code null} becomes {@code ""}</li>
     * </ul>
     *
     * @param fullQualifiedResourceName the full qualified resource name.
     * @return the resource name without any path or any suffix.
     */
    public static String getResourceNameWithoutPathAndSuffix(String fullQualifiedResourceName) {
        return getResourceNameWithoutPath(fullQualifiedResourceName).split("\\.")[0];
    }

    /**
     * Return suffix from {@code fullQualifiedResourceName} (without {@link DataSetProperties#DATAZ_PROPERTY_TEMPLATE_SUFFIX}.
     * <br><br>
     * Examples:<br>
     * <ul>
     * <li>{@code "/path/to/resource.suffix.xml"} becomes {@code ".suffix.xml"}</li>
     * <li>{@code "resource.suffix.vm"} becomes {@code ".suffix"}</li>
     * <li>{@code "resource"} becomes {@code ""}</li>
     * <li>{@code ""} becomes {@code ""}</li>
     * <li>{@code null} becomes {@code ""}</li>
     * </ul>
     *
     * @param fullQualifiedResourceName the full resource name
     * @return the resource suffix or empty string.
     */
    public static String getResourceSuffix(String fullQualifiedResourceName) {
        final String rawResourceName = getResourceNameWithoutPath(fullQualifiedResourceName);

        final String[] splittedName = rawResourceName.split("\\.");
        final StringBuilder suffixBuilder = new StringBuilder("");
        for (int i = 1; i < splittedName.length; i++) {
            suffixBuilder.append(splittedName[i]).append(".");
        }

        return cleanupSuffix(suffixBuilder);
    }

    /**
     * Return the path from {@code fullQualifiedResourceName}.
     * <br><br>
     * Examples:<br>
     * <ul>
     * <li>{@code "/path/to/resource.suffix.xml"} becomes {@code "/path/to/"}</li>
     * <li>{@code "path/to/resource.suffix"} becomes {@code "path/to"}</li>
     * <li>{@code "/resource.txt"} becomes {@code "/"}</li>
     * <li>{@code "resource.txt"} becomes {@code ""}</li>
     * <li>{@code ""} becomes {@code ""}</li>
     * <li>{@code null} becomes {@code ""}</li>
     * </ul>
     *
     * @param fullQualifiedResourceName the full resource name
     * @return the resource path or empty string.
     */
    public static String getResourcePath(String fullQualifiedResourceName) {
        if (StringUtils.isEmpty(fullQualifiedResourceName)) {
            return "";
        }

        final StringBuilder pathBuilder = new StringBuilder();
        final String[] pathFragment = fullQualifiedResourceName.split("/");
        for (int i = 0; i < pathFragment.length - 1; i++) {
            pathBuilder.append(pathFragment[i]).append("/");
        }

        return cleanupPath(pathBuilder);
    }

    private static String cleanupPath(StringBuilder pathBuilder) {
        final String path = pathBuilder.toString();
        if (path.equals("/") || path.isEmpty())
            return path;

        return path.substring(0, path.length() - 1);
    }

    private static String cleanupSuffix(StringBuilder suffixBuilder) {
        String suffix = suffixBuilder.toString();
        if (!suffix.isEmpty()) {
            // drop last "."
            suffix = "." + suffix.substring(0, suffix.length() - 1);

            // remove template suffix from to.
            final String templateSuffix = DataSetProperties.getTemplateSuffix();

            if (suffix.endsWith(templateSuffix)) {
                suffix = suffix.substring(0, suffix.length() - templateSuffix.length());
            }
        }
        return suffix;
    }

    private static String getResourceNameWithoutPath(String fullQualifiedResourceName) {
        if (StringUtils.isEmpty(fullQualifiedResourceName)) {
            return "";
        }

        final String[] split = fullQualifiedResourceName.split("/");
        return split[split.length - 1];
    }
}