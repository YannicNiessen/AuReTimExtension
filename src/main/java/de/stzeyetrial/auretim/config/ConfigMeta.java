package de.stzeyetrial.auretim.config;

import de.stzeyetrial.auretim.input.Input;
import de.stzeyetrial.auretim.util.Stimulus;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.File;
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
	private static final String CONFIG_FILENAME								= "config/meta.config.properties";

	private static final String CONFIG_COMMENT								= "AuReTim configuration meta";

	private static final String PROPERTY_CONFIG_DIRECTORY					= "configDirectory";

	private static final String PROPERTY_RESULT_DIRECTORY					= "resultDirectory";

	private static final String PROPERTY_ACTIVE_CONFIG						= "activeConfig";

	private static final String PROPERTY_ACTIVE_CONFIG_DEFAULT				= "default.config.properties";

	private static final String PROPERTY_CONFIG_DIRECTORY_DEFAULT 			= "config/";

	private static final String PROPERTY_RESULT_DIRECTORY_DEFAULT 			= "results/";

	private final StringProperty _activeConfig								= new SimpleStringProperty();

	private final StringProperty _configDirectory							= new SimpleStringProperty();

	private final StringProperty _resultDirectory 							= new SimpleStringProperty();

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

			System.out.println("failed");

			File directory = new File("config");
			if (! directory.exists()){
				directory.mkdir();
			}

			File metaConfigFile = new File(CONFIG_FILENAME);
			try {
				if(metaConfigFile.createNewFile()){
					p.store(new FileOutputStream(CONFIG_FILENAME), CONFIG_COMMENT);
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			Logger.getLogger(ConfigMeta.class.getName()).log(Level.WARNING, "Could not load config file.", ex);
		}

		activeConfigProperty().setValue(p.getProperty(PROPERTY_ACTIVE_CONFIG, PROPERTY_ACTIVE_CONFIG_DEFAULT));
		configDirectoryProperty().setValue(p.getProperty(PROPERTY_CONFIG_DIRECTORY, PROPERTY_CONFIG_DIRECTORY_DEFAULT));
		resultDirectoryProperty().setValue(p.getProperty(PROPERTY_RESULT_DIRECTORY, PROPERTY_RESULT_DIRECTORY_DEFAULT));

	}

	public void save() throws IOException {
		final Properties p = new Properties();

		p.setProperty(PROPERTY_ACTIVE_CONFIG,activeConfigProperty().getValue());
		p.setProperty(PROPERTY_CONFIG_DIRECTORY,configDirectoryProperty().getValue());
		p.setProperty(PROPERTY_RESULT_DIRECTORY,resultDirectoryProperty().getValue());

		p.store(new FileOutputStream(CONFIG_FILENAME), CONFIG_COMMENT);

	}

	public StringProperty activeConfigProperty() {
		return _activeConfig;
	}

	public StringProperty configDirectoryProperty() {
		return _configDirectory;
	}

	public StringProperty resultDirectoryProperty() {
		return _resultDirectory;
	}




}