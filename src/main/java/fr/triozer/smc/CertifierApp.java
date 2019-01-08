package fr.triozer.smc;

import fr.triozer.smc.certifier.Certifier;
import fr.triozer.smc.certifier.CertifierManager;
import fr.triozer.smc.database.Database;
import fr.triozer.smc.setting.Settings;
import fr.triozer.smc.utils.Console;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;

/**
 * @author Cédric / Triozer
 */
public class CertifierApp {

	private static CertifierApp instance;

	private final CertifierManager manager;
	private final Console          console;
	private final Database         database;
	private final Settings         settings;
	private final File             dataFolder;

	public CertifierApp() {
		instance = this;
		this.console = new Console(CertifierApp.class);
		this.dataFolder = Paths.get("data").toAbsolutePath().normalize().toFile();
		if (!this.dataFolder.exists() && !this.dataFolder.mkdirs()) { /* ERROR WHILE CREATING dataFolder */ }
		File file = new File(this.dataFolder, "app.yml");
		if (!file.exists()) {
			try {
				Files.copy(CertifierApp.class.getClassLoader().getResourceAsStream("app.yml"), file.toPath());
			} catch (IOException e) {
				this.console.stacktrace("Can't create the properties file.", e);
			}
		}
		this.settings = new Settings(file);

		this.manager = new CertifierManager();
		this.database = new Database().auth();

		Runtime.getRuntime().addShutdownHook(new Thread(() -> CertifierApp.this.stop(0)));

		this.console.fine("App started.");
	}

	public void start(Certifier certifier) {
		certifier.start();
	}

	public void start(String... ids) {
		Arrays.stream(ids).forEach(id -> this.start(this.manager.get(id)));
	}

	public void startAll() {
		this.manager.values().forEach(this::start);
	}

	public void stop(int code) {
		this.console.fine("App is ending.");
		this.manager.values().forEach(Certifier::stop);
		this.console.fine("App ended.");
		System.exit(code);
	}

	public static CertifierApp getInstance() {
		return instance;
	}

	public CertifierManager getManager() {
		return this.manager;
	}

	public Console getConsole() {
		return this.console;
	}

	public final Database getDatabase() {
		return this.database;
	}

	public final File getDataFolder() {
		return this.dataFolder;
	}

	public final Settings getSettings() {
		return this.settings;
	}

}
