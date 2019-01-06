package fr.triozer.smc;

import fr.triozer.smc.certifier.Certifier;
import fr.triozer.smc.certifier.CertifierManager;
import fr.triozer.smc.database.Database;
import fr.triozer.smc.impl.DiscordCertifier;
import fr.triozer.smc.utils.Console;

import java.util.Arrays;

/**
 * @author Cédric / Triozer
 */
public class CertifierApp {

	private static CertifierApp     instance;
	private final  CertifierManager manager;
	private final  Console          console;
	private final  Database          database;

	public CertifierApp() {
		instance = this;
		this.console = new Console(CertifierApp.class);
		this.database = new Database("localhost",3306, "smc").auth("smc", "smc");
		this.manager = new CertifierManager();

		this.console.fine("App started.");
		this.manager.add(new DiscordCertifier());
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

}
