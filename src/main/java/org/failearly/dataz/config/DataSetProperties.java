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

package org.failearly.dataz.config;

import org.apache.commons.lang.StringUtils;
import org.failearly.common.annotation.traverser.TraverseDepth;
import org.failearly.common.test.ExtendedProperties;
import org.failearly.dataz.internal.template.TemplateObjectDuplicateStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Map;
import java.util.Properties;

/**
 * DataSetProperties loads properties from property files.
 * <br>
 * <ul>
 * <li>{@link #DATAZ_DEFAULT_PROPERTY_FILE}</li>
 * <li>{@link #DATAZ_DATASTORE_PROPERTY_FILE}</li>
 * <li>{@link #DATAZ_DEFAULT_CUSTOM_PROPERTY_FILE}</li>
 * <li>{@link #DATAZ_CONFIG_OPTION}</li>
 * <li>{@link System#getProperties()}</li>
 * <li>{@link #setProperty(String, String)}</li>
 * </ul>
 */
public final class DataSetProperties implements Constants {

    private static final Logger LOGGER = LoggerFactory.getLogger(DataSetProperties.class);

    private static final DataSetPropertiesImpl properties = new DataSetPropertiesImpl();


    private static final boolean OPTIONAL = true;
    private static final boolean MANDATORY = false;


    static {
        reload();
    }


    private DataSetProperties() {
    }

    /**
     * Reloads the properties (used for test purposes).
     */
    public static void reload() {
        properties.clear();
        properties.loadPropertyFileFromClassPath(DATAZ_DEFAULT_PROPERTY_FILE);
        properties.loadPropertyFileFromClassPath(DATAZ_DATASTORE_PROPERTY_FILE);
        properties.loadCustomPropertyFile(DATAZ_DEFAULT_CUSTOM_PROPERTY_FILE);
        properties.loadCustomPropertyFileFromFileSystemFirst(System.getProperty(DATAZ_CONFIG_OPTION));
        properties.loadFromSystemProperties();
        properties.resolveReferences();
    }


    /**
     * @return value of {@link Constants#DATAZ_PROPERTY_DEFAULT_SETUP_SUFFIX}.
     */
    public static String getDefaultSetupSuffix() {
        return properties.getDefaultSetupSuffix();
    }

    /**
     * @return value of {@link Constants#DATAZ_PROPERTY_DEFAULT_CLEANUP_SUFFIX}.
     */
    public static String getDefaultCleanupSuffix() {
        return properties.getDefaultCleanupSuffix();
    }

    /**
     * @return value of {@link Constants#DATAZ_PROPERTY_TEMPLATE_SUFFIX}.
     */
    public static String getTemplateSuffix() {
        return properties.getTemplateSuffix();
    }

    /**
     * @return value of {@link Constants#DATAZ_PROPERTY_DATASTORE_TYPE_CLASS_NAME}
     */
    public static String getDefaultDataStoreTypeClassName() {
        return properties.getDefaultDataStoreTypeClassName();
    }

    /**
     * @return value of {@link Constants#DATAZ_PROPERTY_DROP_TEMP_FILE}.
     */
    public static boolean isDropTempFile() {
        return properties.isDropTempFile();
    }

    /**
     * @return value of {@link Constants#DATAZ_PROPERTY_TEMP_DIR}.
     */
    @SuppressWarnings("UnusedDeclaration")
    public static String getTempDir() {
        return properties.getTempDir();
    }

    /**
     * @return the full qualified class name of the template engine factory.
     */
    public static String getTemplateEngineFactoryClass() {
        return properties.getTemplateEngineFactoryClass();
    }

    /**
     * @return the template object (annotation) traverse depth.
     *
     * @see TraverseDepth
     * @see org.failearly.dataz.internal.template.TemplateObjectsResolver.Builder
     */
    public static TraverseDepth getTemplateObjectTraverseDepth() {
        return properties.getTemplateObjectTraverseDepth();
    }

    /**
     * @return the strategy for duplicated template objects
     *
     * @see TemplateObjectDuplicateStrategy
     * @see org.failearly.dataz.internal.template.TemplateObjectsResolver.Builder
     */
    public static TemplateObjectDuplicateStrategy getTemplateObjectDuplicateStrategy() {
        return properties.getTemplateObjectDuplicateStrategy();
    }

    /**
     * Create temp directory including {@link #getTempDir()}.
     *
     * @param subDir the subdirectory name
     * @return the file object of created temporary directory.
     */
    public static File createTempDir(String subDir) {
        if (subDir.startsWith("/"))
            return new File(createDirectory(properties.getTempDir() + subDir));
        return new File(createDirectory(properties.getTempDir() + "/" + subDir));
    }

    private static String createDirectory(String tempDir) {
        if (tempDir.endsWith("/")) {
            tempDir = tempDir.substring(0, tempDir.length() - 1);
        }
        if (new File(tempDir).mkdirs()) {
            LOGGER.info("Directory '{}' created!", tempDir);
        }

        return tempDir;
    }


    /**
     * Set dataz property to specific values. A dataz property starts with {@code dataz.}.
     *
     * @param key   the dataz property key.
     * @param value the dataz value.
     */
    public static void setProperty(String key, String value) {
        properties.setProperty(key, value);
        properties.resolveReferences(key);
    }

    /**
     * Get property value for {@code key} (contains also {@link System#getProperties()}).
     *
     * @param key the property key
     * @return the property value or {@code null}.
     */
    public static String getProperty(String key) {
        return properties.getProperty(key);
    }

    /**
     * Just for test purpose.
     */
    public static void resolveReferences() {
        properties.resolveReferences();
    }

    /**
     * Access to all properties loaded from classpath/file system/system properties.
     *
     * @return all properties.
     */
    public static Properties getProperties() {
        return properties.getProperties();
    }


    static class DataSetPropertiesImpl {
        private final ExtendedProperties properties = new ExtendedProperties();

        DataSetPropertiesImpl() {
        }

        void loadPropertyFileFromClassPath(String file) {
            loadFromClassPath(file, MANDATORY);
        }

        void loadCustomPropertyFile(String file) {
            if (file != null) {
                loadFromClassPath(file, OPTIONAL);
            }
        }

        void loadCustomPropertyFileFromFileSystemFirst(String file) {
            if (file != null) {
                if (!loadFromSystemPath(file, MANDATORY)) {
                    loadFromClassPath(file, OPTIONAL);
                }
            }
        }

        void loadFromSystemProperties() {
            LOGGER.info("Load system properties.");
            final Properties props = System.getProperties();
            for (Map.Entry<Object, Object> sysProp : props.entrySet()) {
                setProperty0(sysProp.getKey(), sysProp.getValue());
            }
        }

        private void setProperty0(Object key, Object value) {
            if( value==null )
                properties.setProperty(key.toString(), null);
            else
                properties.setProperty(key.toString(), value.toString());
        }

        void setProperty(String key, String value) {
            properties.setProperty(key, value);
            if (key.startsWith("dataz.")) {
                LOGGER.debug("DataSet Property '{}' set to value '{}'", key, value);
            }
        }

        void resolveReferences() {
            properties.resolveReferences();
        }

        void resolveReferences(String key) {
            properties.resolveReferences(key);
        }

        private boolean loadProperties(InputStream resource, boolean optional, String type) {
            if( resource==null ) {
                if (!optional) {
                    LOGGER.warn("(Mandatory) No data set property file found in {}.", type);
                }
                else {
                    LOGGER.debug("(Optional) No data set property file found in {}.", type);
                }
                return false;
            }

            try {
                properties.load(resource);
            } catch (Exception e) {
                if (!optional) {
                    LOGGER.warn("(Mandatory) No data set property file found in " + type + ". Reason: ", e);
                }
                else {
                    LOGGER.debug("(Optional) No data set property file found in "+ type +". Reason:", e);
                }
                return false;
            }

            return true;

        }

        private boolean loadFromClassPath(String file, boolean optional) {
            LOGGER.info("Load properties from file {} from classpath", file);
            final InputStream resource=DataSetProperties.class.getResourceAsStream(file);
            return loadProperties(resource, optional, "classpath");
        }

        private boolean loadFromSystemPath(String file, boolean optional) {
            FileInputStream fileInputStream=null;
            try {
                LOGGER.info("Load properties from file {} from file system", file);
                fileInputStream = new FileInputStream(file);
            } catch (Exception ignored) {
                // ignored!
            }

            return loadProperties(fileInputStream, optional, "file system");
        }

        String getDefaultSetupSuffix() {
            return properties.getProperty(DATAZ_PROPERTY_DEFAULT_SETUP_SUFFIX);
        }

        String getDefaultCleanupSuffix() {
            return properties.getProperty(DATAZ_PROPERTY_DEFAULT_CLEANUP_SUFFIX);
        }

        String getTemplateSuffix() {
            return "." + properties.getProperty(DATAZ_PROPERTY_TEMPLATE_SUFFIX);
        }

        String getDefaultDataStoreTypeClassName() {
            return properties.getProperty(DATAZ_PROPERTY_DATASTORE_TYPE_CLASS_NAME);

        }

        boolean isDropTempFile() {
            return Boolean.parseBoolean(properties.getProperty(Constants.DATAZ_PROPERTY_DROP_TEMP_FILE, "true"));
        }

        TraverseDepth getTemplateObjectTraverseDepth() {
            return TraverseDepth.valueOf(getProperty(DATAZ_TEMPLATE_OBJECT_TRAVERSING_DEPTH, TraverseDepth.DECLARED_CLASS.name()));
        }

        TemplateObjectDuplicateStrategy getTemplateObjectDuplicateStrategy() {
            return TemplateObjectDuplicateStrategy.valueOf(getProperty(DATAZ_TEMPLATE_OBJECT_DUPLICATE_STRATEGY, TemplateObjectDuplicateStrategy.STRICT.name()));
        }

        String getProperty(String key) {
            return properties.getProperty(key);
        }

        String getProperty(String key, String defaultValue) {
            final String value = properties.getProperty(key, defaultValue);
            LOGGER.info("Property {}='{}'",key, value);
            return value;
        }

        void clear() {
            properties.clear();
        }

        String getTempDir() {
            return checkTempDir(getProperty(DATAZ_PROPERTY_TEMP_DIR));
        }

        private String checkTempDir(String dataSetTempDir) {
            StringUtils.trimToEmpty(dataSetTempDir);
            if (StringUtils.isEmpty(dataSetTempDir)) {
                return System.getProperty("java.io.tmpdir");
            }

            return createDirectory(dataSetTempDir);
        }


        String getTemplateEngineFactoryClass() {
            return properties.getProperty(DATAZ_PROPERTY_TEMPLATE_ENGINE_FACTORY);
        }

        Properties getProperties() {
            return properties.toStandardProperties();
        }
    }
}
