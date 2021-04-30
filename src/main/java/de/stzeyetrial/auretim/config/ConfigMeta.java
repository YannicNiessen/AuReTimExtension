package de.stzeyetrial.auretim.config;

import de.stzeyetrial.auretim.input.Input;
import de.stzeyetrial.auretim.util.Stimulus;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author strasser
 */
public class ConfigMeta {
	private static final String CONFIG_FILENAME							= "meta.config.properties";

	private static final String CONFIG_COMMENT							= "AuReTim configuration meta";

	private static final String PROPERTY_ACTIVE_CONFIG						= "activeConfig";

	private static final String PROPERTY_ACTIVE_CONFIG_DEFAULT				= "config.properties";

	private final StringProperty _activeConfig				= new SimpleStringProperty();



	private static ConfigMeta _instance;


	private ConfigMeta() {
	}

	public static synchronized ConfigMeta getInstance() {
		if (_instance == null) {
			_instance = new ConfigMeta();
		}

		return _instance;
	}

	public void load() {
		final Properties p = new Properties();
		try {
			p.load(new FileInputStream(CONFIG_FILENAME));
		} catch (final IOException ex) {
			Logger.getLogger(ConfigMeta.class.getName()).log(Level.WARNING, "Could not load config file.", ex);
		}

		activeConfigProperty().setValue(p.getProperty(PROPERTY_ACTIVE_CONFIG, PROPERTY_ACTIVE_CONFIG_DEFAULT));

	}

	public void save() throws IOException {
		final Properties p = new Properties();

		p.setProperty(PROPERTY_ACTIVE_CONFIG,activeConfigProperty().getValue());


		p.store(new FileOutputStream(CONFIG_FILENAME), CONFIG_COMMENT);

		Config.getInstance().load();


	}

	public StringProperty activeConfigProperty() {
		return _activeConfig;
	}



}