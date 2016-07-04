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

import org.failearly.common.annotation.traverser.TraverseDepth;
import org.failearly.dataz.DataSet;
import org.failearly.dataz.NamedDataStore;
import org.failearly.dataz.datastore.DataStoreFactory;
import org.failearly.dataz.template.TemplateObject;

/**
 * DataSetConstants contains the name and position of property files and the property keys used by DataSet.
 * <br><br>
 * The properties/property files will be loaded and overridden in following order: <br><br>
 * <ol>
 * <li>{@link #DATAZ_DEFAULT_PROPERTY_FILE} ({@value #DATAZ_DEFAULT_PROPERTY_FILE}): The default settings for DataSet (loaded from classpath only).
 * <b>Internal use! Don't use!</b></li>
 * <li>{@link #DATAZ_DATASTORE_PROPERTY_FILE} ({@value #DATAZ_DATASTORE_PROPERTY_FILE}): The default settings for a DataSet DataStore properties
 * (loaded from classpath only). <b>Internal use! Don't use!</b></li>
 * <li>{@link #DATAZ_DEFAULT_CUSTOM_PROPERTY_FILE} ({@value #DATAZ_DEFAULT_CUSTOM_PROPERTY_FILE}): Customized settings (override existing properties)
 * for DataSet (lookup strategy 1. classpath, 2. file system).</li>
 * <li>{@link #DATAZ_CONFIG_OPTION} ({@value #DATAZ_CONFIG_OPTION}): Customized settings (override existing properties)
 * for DataSet (lookup strategy 1. file system, 2. classpath).</li>
 * <li>It's also possible to use {@link System#getProperties()} for setting a single DataSet property, by using for example
 * {@code -D}{@value #DATAZ_PROPERTY_DEFAULT_SETUP_SUFFIX}{@code ="setup.xml"}</li>
 * <li>Final possibility: Use {@link DataSetProperties#setProperty(String, String)}.</li>
 * </ol>
 */
public interface Constants {

    /**
     * Default property file. The {@code DATAZ_DEFAULT_PROPERTY_FILE} contains base property settings.
     * <p>
     * <b>Internal use! Don't use!</b>
     */
    String DATAZ_DEFAULT_PROPERTY_FILE = "/dataz-default.properties";

    /**
     * Default DataStore property file. The {@code DATAZ_DATASTORE_PROPERTY_FILE} contains some property settings for the current used DataStore.
     * <p>
     * <b>Internal use! Don't use!</b>
     */
    String DATAZ_DATASTORE_PROPERTY_FILE = "/dataz-datastore.properties";

    /**
     * (Optional) Custom property file. The optional {@code DATAZ_DEFAULT_CUSTOM_PROPERTY_FILE} property file could overwrite some of predefined properties.
     * <br><br>
     * <ul>
     * <li>{@link #DATAZ_PROPERTY_DEFAULT_SETUP_SUFFIX}, if you like more appropriate suffixes for DataSet setup files.</li>
     * <li>{@link #DATAZ_PROPERTY_DEFAULT_CLEANUP_SUFFIX}, the same for DataSet cleanup files.</li>
     * <li>{@link #DATAZ_PROPERTY_TEMPLATE_SUFFIX}, the same for DataSet template (recognition) suffix.</li>
     * <li>{@link #DATAZ_PROPERTY_DROP_TEMP_FILE}, for suppressing the automatic deletion of temporary files.</li>
     * <li>{@link #DATAZ_PROPERTY_TEMP_DIR}, for defining a different directory for temporary file creation.</li>
     * </ul>
     *
     * @see #DATAZ_CONFIG_OPTION
     */
    String DATAZ_DEFAULT_CUSTOM_PROPERTY_FILE = "/dataz.properties";


    /**
     * (Optional) config property which points to the actually property file.
     * <br><br>
     * You can use the system property <code>-Ddataset.config="&lt;path to custom property file&gt;"</code> which forces
     * DataSet to use these settings instead. It's an alternative to {@link #DATAZ_DEFAULT_CUSTOM_PROPERTY_FILE}.
     * <br><br>
     *
     * @see #DATAZ_DEFAULT_CUSTOM_PROPERTY_FILE
     */
    String DATAZ_CONFIG_OPTION = "dataz.config";


    /**
     * The setup suffix property (dataz.setup.suffix) will be used in case you use {@link org.failearly.dataz.DataSet} without
     * defining the {@code setup} resource(s).<br>
     * In this case DataSet will use this property for creating a resource name where DataSet expects the setup resource.<br><br>
     * The default value is {@code setup}.
     */
    String DATAZ_PROPERTY_DEFAULT_SETUP_SUFFIX = "dataz.default.setup.suffix";


    /**
     * The cleanup suffix property (dataz.cleanup.suffix) will be used in case you use {@link org.failearly.dataz.DataSet} without
     * defining the {@code cleanup} resource(s).
     * In this case DataSet will use this property for creating a resource name where DataSet expects the (optional) cleanup resource.<br><br>
     * The default value is {@code cleanup}.
     */
    String DATAZ_PROPERTY_DEFAULT_CLEANUP_SUFFIX = "dataz.default.cleanup.suffix";

    /**
     * The template suffix (dataz.template.suffix) will be used for template recognition. A setup/cleanup resource name will be
     * handled as Velocity template if the resource name ends with this suffix.<br><br>
     * The default value is {@code vm}.
     */
    String DATAZ_PROPERTY_TEMPLATE_SUFFIX = "dataz.template.suffix";

    /**
     * DataSet uses temporary files (in conjunction with Velocity template engine) which will dropped (after exit).
     * With this property it's possible to suppress this behaviour.
     * <br><br>
     * The default value is {@code true}.
     *
     * @see java.io.File#deleteOnExit()
     */
    String DATAZ_PROPERTY_DROP_TEMP_FILE = "dataz.template.drop.tempfile";

    /**
     * DataSet uses {@code java.io.tmpdir} for the creation of temporary files (for Velocity).
     * <p>
     * Sometimes it's necessary to define another temporary directory, so with this property you can redirect the
     * generation of Velocity's template file to an appropriate directory. It's possible to use System properties by using {@code ${my.var}} notation.
     * <br><br>
     * Examples:
     * <ul>
     * <li>dataz.template.tempdir="/abs/path/to/anydir": This path will be used. If directory does not exist, DataSet will try to newInstance.</li>
     * <li>dataz.template.tempdir="rel/path/to/anydir": The execution directory will be used.</li>
     * <li>dataz.template.tempdir="${user.home}/rel/path/to/anydir": The home directory will be used.</li>
     * </ul>
     *
     * @see org.apache.velocity.app.VelocityEngine#evaluate(org.apache.velocity.context.Context, java.io.Writer, String, java.io.Reader)
     */
    String DATAZ_PROPERTY_TEMP_DIR = "dataz.template.tempdir";

    /**
     * The full qualified class name of the template engine factory.
     * The default is {@link org.failearly.dataz.internal.template.engine.velocity.VelocityTemplateEngineFactory}.
     */
    String DATAZ_PROPERTY_TEMPLATE_ENGINE_FACTORY = "dataz.template.engine.factory";

    /**
     * The default datastore name.
     */
    String DATAZ_DEFAULT_DATASTORE_NAME = "<master>";

    /**
     * Settings for the default DataStore (a {@link NamedDataStore}).
     * This will be used if for example {@link DataSet#datastores()} has not been set.
     *
     * @see DataSet#datastores()
     */
    String DATAZ_PROPERTY_DEFAULT_DATA_STORE = "dataz.default.datastore.class";


    /**
     * Defines the used depth for resolving {@link TemplateObject} annotations. Currently possible values are:<br><br>
     * <ul>
     * <li>{@link TraverseDepth#HIERARCHY}</li>
     * <li>{@link TraverseDepth#CLASS_HIERARCHY}</li>
     * <li>{@link TraverseDepth#DECLARED_CLASS} (default)</li>
     * <li>{@link TraverseDepth#METHOD_ONLY}</li>
     * </ul>
     *
     * @see TraverseDepth
     */
    String DATAZ_TEMPLATE_OBJECT_TRAVERSING_DEPTH = "dataz.template.object.traversing.depth";

    /**
     * Defines the used strategy for handling duplicate {@link TemplateObject}s. A template object should be unique
     * with it's name within the same dataset, otherwise the {@link org.failearly.dataz.internal.template.TemplateObjectDuplicateStrategy} will be used.
     * <br><br>
     * Currently possible values are:<br><br>
     * <br><br>
     * <ul>
     *    <li>{@link org.failearly.dataz.internal.template.TemplateObjectDuplicateStrategy#IGNORE}</li>
     *    <li>{@link org.failearly.dataz.internal.template.TemplateObjectDuplicateStrategy#OVERWRITE}</li>
     *    <li>{@link org.failearly.dataz.internal.template.TemplateObjectDuplicateStrategy#STRICT} (default)</li>
     * </ul>
     */
    String DATAZ_TEMPLATE_OBJECT_DUPLICATE_STRATEGY = "dataz.template.object.duplicate.strategy";

    /**
     * The default name of a data set if the name has been omitted.
     *
     * @see DataSet#name()
     */
    String DATASET_DEFAULT_NAME = "<dataset>";

    /**
     * No config file
     */
    String DATAZ_NO_CONFIG_FILE="<no-config-file>";

    /**
     * The default name of factory method used in conjunction with {@link org.failearly.dataz.datastore.ReflectionDataStoreFactory}
     * and {@link DataStoreFactory.Definition#factoryMethod()}.
     *
     * @see org.failearly.dataz.datastore.ReflectionDataStoreFactory
     * @see DataStoreFactory.Definition#factoryMethod()
     */
    String DATAZ_DEFAULT_DATASTORE_FACTORY_METHOD="createDataStore";
}
