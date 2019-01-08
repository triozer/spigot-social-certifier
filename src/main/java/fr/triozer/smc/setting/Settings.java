package fr.triozer.smc.setting;

import org.simpleyaml.configuration.file.YamlFile;
import org.simpleyaml.exceptions.InvalidConfigurationException;

import java.io.File;
import java.io.IOException;
import java.util.Map;

/**
 * @author Cédric / Triozer
 */
public class Settings {

	private final YamlFile properties;
	private       File     file;

	public Settings() {
		this.properties = new YamlFile();
		this.file = null;
	}

	public Settings(File file) {
		this();
		this.file = file;
		if (!file.exists()) {
			// ERROR CANT OPEN file - CREATING IT
			try {
				file.createNewFile();
				// FILE CREATED
			} catch (IOException e) {
				// ERROR CANT CREATE file
				e.printStackTrace();
			}
		} else if (!file.isFile()) {
			// ERROR file IS NOT A FILE
			return;
		} else {
			try {
				this.properties.load(file);
			} catch (IOException | InvalidConfigurationException e) {
				e.printStackTrace();
			}
		}
	}

	public void set(String path, Object o) {
		this.properties.set(path, o.toString());
		save();
	}

	public void save() {
		try {
			this.properties.save(this.file);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public Object get(String path) {
		return this.properties.get(path);
	}

	public String getString(String path) {
		return this.properties.getString(path);
	}

	public boolean getBoolean(String path) {
		return this.properties.getBoolean(path);
	}

	public int getInt(String path) {
		return this.properties.getInt(path);
	}

	public Map<String, Object> getSettings() {
		return this.properties.getValues(true);
	}

	public final File getFile() {
		return this.file;
	}

}
