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
package org.failearly.dataset.config;

/**
 * DataSetConstants contains the name and position of property files and the property keys used by DataSet.
 * <br><br>
 * The properties/property files will be loaded and overridden in following order: <br><br>
 * <ol>
 *     <li>{@link #DATASET_DEFAULT_PROPERTY_FILE} ({@value #DATASET_DEFAULT_PROPERTY_FILE}): The default settings for DataSet (loaded from classpath only).
 *     <b>Internal use! Don't use!</b></li>
 *     <li>{@link #DATASET_DATASTORE_PROPERTY_FILE} ({@value #DATASET_DATASTORE_PROPERTY_FILE}): The default settings for a DataSet DataStore properties
 *          (loaded from classpath only). <b>Internal use! Don't use!</b></li>
 *     <li>{@link #DATASET_DEFAULT_CUSTOM_PROPERTY_FILE} ({@value #DATASET_DEFAULT_CUSTOM_PROPERTY_FILE}): Customized settings (override existing properties)
 *          for DataSet (lookup strategy 1. classpath, 2. file system).</li>
 *     <li>{@link #DATASET_CONFIG_OPTION} ({@value #DATASET_CONFIG_OPTION}): Customized settings (override existing properties)
 *          for DataSet (lookup strategy 1. file system, 2. classpath).</li>
 *     <li>It's also possible to use {@link System#getProperties()} for setting a single DataSet property, by using for example
 *     {@code -D}{@value #DATASET_PROPERTY_DEFAULT_SETUP_SUFFIX}{@code ="setup.xml"}</li>
 *     <li>Final possibility: Use {@link org.failearly.dataset.config.DataSetProperties#setProperty(String, String)}.</li>
 * </ol>
 *
 */
public interface Constants {

    /**
     * Default property file. The {@code DATASET_DEFAULT_PROPERTY_FILE} contains base property settings.
     *
     * <b>Internal use! Don't use!</b>
     */
    String DATASET_DEFAULT_PROPERTY_FILE = "/dataset-default.properties";

    /**
     * Default DataStore property file. The {@code DATASET_DATASTORE_PROPERTY_FILE} contains some property settings for the current used DataStore.
     *
     * <b>Internal use! Don't use!</b>
     */
    String DATASET_DATASTORE_PROPERTY_FILE = "/dataset-datastore.properties";

    /**
     * (Optional) Custom property file. The optional {@code DATASET_DEFAULT_CUSTOM_PROPERTY_FILE} property file could overwrite some of predefined properties.
     * <br><br>
     * <ul>
     *     <li>{@link #DATASET_PROPERTY_DEFAULT_SETUP_SUFFIX}, if you like more appropriate suffixes for DataSet setup files.</li>
     *     <li>{@link #DATASET_PROPERTY_DEFAULT_CLEANUP_SUFFIX}, the same for DataSet cleanup files.</li>
     *     <li>{@link #DATASET_PROPERTY_TEMPLATE_SUFFIX}, the same for DataSet template (recognition) suffix.</li>
     *     <li>{@link #DATASET_PROPERTY_DROP_TEMP_FILE}, for suppressing the automatic deletion of temporary files.</li>
     *     <li>{@link #DATASET_PROPERTY_TEMP_DIR}, for defining a different directory for temporary file creation.</li>
     * </ul>
     *
     * @see #DATASET_CONFIG_OPTION
     */
    String DATASET_DEFAULT_CUSTOM_PROPERTY_FILE = "/dataset.properties";


    /**
     * (Optional) config property which points to the actually property file.
     * <br><br>
     * You can use the system property <code>-Ddataset.config="&lt;path to custom property file&gt;"</code> which forces
     * DataSet to use these settings instead. It's an alternative to {@link #DATASET_DEFAULT_CUSTOM_PROPERTY_FILE}.
     * <br><br>
     * @see #DATASET_DEFAULT_CUSTOM_PROPERTY_FILE
     */
    String DATASET_CONFIG_OPTION = "dataset.config";


    /**
     * The setup suffix property (dataset.setup.suffix) will be used in case you use {@link org.failearly.dataset.DataSet} without
     * defining the {@code setup} resource(s).<br>
     * In this case DataSet will use this property for creating a resource name where DataSet expects the setup resource.<br><br>
     * The default value is {@code setup}.
     */
    String DATASET_PROPERTY_DEFAULT_SETUP_SUFFIX = "dataset.default.setup.suffix";


    /**
     * The cleanup suffix property (dataset.cleanup.suffix) will be used in case you use {@link org.failearly.dataset.DataSet} without
     * defining the {@code cleanup} resource(s).
     * In this case DataSet will use this property for creating a resource name where DataSet expects the (optional) cleanup resource.<br><br>
     * The default value is {@code cleanup}.
     */
    String DATASET_PROPERTY_DEFAULT_CLEANUP_SUFFIX = "dataset.default.cleanup.suffix";

    /**
     * The template suffix (dataset.template.suffix) will be used for template recognition. A setup/cleanup resource name will be
     * handled as Velocity template if the resource name ends with this suffix.<br><br>
     * The default value is {@code vm}.
     */
    String DATASET_PROPERTY_TEMPLATE_SUFFIX = "dataset.template.suffix";

    /**
     * DataSet uses temporary files (in conjunction with Velocity template engine) which will dropped (after exit).
     * With this property it's possible to suppress this behaviour.
     * <br><br>
     * The default value is {@code true}.
     *
     *
     * @see java.io.File#deleteOnExit()
     */
    String DATASET_PROPERTY_DROP_TEMP_FILE = "dataset.template.drop.tempfile";

    /**
     * DataSet uses {@code java.io.tmpdir} for the creation of temporary files (for Velocity).
     *
     * Sometimes it's necessary to define another temporary directory, so with this property you can redirect the
     * generation of Velocity's template file to an appropriate directory. It's possible to use System properties by using {@code ${my.var}} notation.
     * <br><br>
     * Examples:
     * <ul>
     *     <li>dataset.template.tempdir="/abs/path/to/anydir": This path will be used. If directory does not exist, DataSet will try to newInstance.</li>
     *     <li>dataset.template.tempdir="rel/path/to/anydir": The execution directory will be used.</li>
     *     <li>dataset.template.tempdir="${user.home}/rel/path/to/anydir": The home directory will be used.</li>
     * </ul>
     *
     * @see org.apache.velocity.app.VelocityEngine#evaluate(org.apache.velocity.context.Context, java.io.Writer, String, java.io.Reader)
     */
    String DATASET_PROPERTY_TEMP_DIR = "dataset.template.tempdir";

    /**
     * The full qualified class name of the template engine factory.
     * The default is {@link org.failearly.dataset.internal.template.velocity.VelocityTemplateEngineFactory}.
     */
    String DATASET_PROPERTY_TEMPLATE_ENGINE_FACTORY = "dataset.template.engine.factory";

    /**
     * The default datastore id used for {@link org.failearly.dataset.DataSet#datastore()} and {@link org.failearly.dataset.DataStoreDefinition#id()}.
     */
    String DATASET_DEFAULT_DATASTORE_ID = "<master>";

    /**
     * The property used by {@link org.failearly.dataset.datastore.DefaultDataStoreFactory} to resolve the actually
     * {@link org.failearly.dataset.datastore.DataStoreType}.
     * <br><br>
     * Remark: It's also possible to set this property in a custom dataset property file. But usually this is not necessary, because
     * almost a single database type will be used.
     *
     * @see org.failearly.dataset.datastore.DefaultDataStoreFactory
     * @see org.failearly.dataset.DataStoreDefinition
     */
    String DATASET_PROPERTY_DATASTORE_TYPE_CLASS_NAME = "dataset.default.datastore.type.class";

    /**
     * Defines the used strategy for resolving {@link org.failearly.dataset.generator.support.Generator} annotations. Currently possible values are:<br><br>
     * <ul>
     *    <li>BOTTOM_UP</li>
     *    <li>TOP_DOWN (default)</li>
     * </ul>
     *
     *
     * @see org.failearly.dataset.internal.annotation.TraverseStrategy
     */
    String DATASET_GENERATOR_TRAVERSING_STRATEGY="dataset.generator.traversing.strategy";

    /**
     * Used by {@link org.failearly.dataset.DataStoreDefinition#setupSuffix()} and
     */
    String DATASET_USE_DEFAULT_SUFFIX="<use_default>";
}
