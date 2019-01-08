package fr.triozer.smc.certifier;

import fr.triozer.smc.CertifierApp;
import fr.triozer.smc.setting.Settings;

import java.io.File;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.function.Predicate;

/**
 * @author Cédric / Triozer
 */
public abstract class BaseCertifier<T> implements Certifier<T> {

	protected final CertifierSettings   settings;
	protected final Thread              thread;
	protected       Map<String, String> pending;
	protected       boolean             running;

	public BaseCertifier(String id, String name) {
		this.settings = new CertifierSettings(id, name) {
			@Override
			public void init() {

			}
		};
		this.running = false;
		this.thread = new Thread(null, this::run, id);
	}

	protected void _stop(Consumer<T> action) {
		this.running = false;
		action.accept(this.getClient());
		CertifierApp.getInstance().getConsole().fine("	" + this.getId() + ": stopped.");
	}

	@Override
	public void start() {
		CertifierApp.getInstance().getConsole().fine("	" + this.getId() + ": starting.");
		this.thread.run();
	}

	@Override
	public boolean validate(UUID uniqueId, String token, String name) {
		return validate(uniqueId, name, (test) -> pending.get(token).equalsIgnoreCase(test));
	}

	@Override
	public boolean validate(UUID uniqueId, String name, Predicate<String> predicate) {
		boolean test = predicate.test(name);
		if (test) CertifierApp.getInstance().getDatabase().remove(this, uniqueId);
		return test;
	}

	@Override
	public boolean isValid(String token, Predicate<String> format) {
		return this.getPending().contains(token) && format.test(token);
	}

	@Override
	public boolean isValid(String token) {
		return this.isValid(token, s -> s.length() == 8);
	}

	@Override
	public final boolean isRunning() {
		return this.running;
	}

	@Override
	public Settings getSettings() {
		return this.settings.get();
	}

	@Override
	public String getId() {
		return this.settings.get().getString("id");
	}

	@Override
	public String getName() {
		return this.settings.get().getString("name");
	}

	@Override
	public Thread getThread() {
		return this.thread;
	}

	@Override
	public Set<String> getPending() {
		this.pending = CertifierApp.getInstance().getDatabase().getPendingOf(this);
		return this.pending.keySet();
	}

	public static abstract class CertifierSettings {
		protected final Settings settings;

		public CertifierSettings(String id, String name) {
			this.settings = new Settings(new File(CertifierApp.getInstance().getDataFolder(), id + ".certifier"));
			this.settings.set("id", id);
			this.settings.set("name", name);
			this.init();
		}

		public abstract void init();

		public final Settings get() {
			return this.settings;
		}
	}


}
