package fr.triozer.smc.setting;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Cédric / Triozer
 */
public class Settings {

	private final Map<String, Object> settings;

	public Settings() {
		this.settings = new HashMap<>();
	}

	public void set(String path, Object o) {
		this.settings.put(path, o);
	}

	public Object get(String path) {
		return this.settings.get(path);
	}

	public String getString(String path) {
		return (String) this.get(path);
	}

	public boolean getBoolean(String path) {
		return (boolean) this.get(path);
	}

	public int getInt(String path) {
		return (int) this.get(path);
	}

	public Map<String, Object> getSettings() {
		return this.settings;
	}

}
