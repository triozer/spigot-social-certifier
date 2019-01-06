package fr.triozer.smc.certifier;

import fr.triozer.smc.CertifierApp;
import fr.triozer.smc.setting.Settings;

import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.function.Predicate;

/**
 * @author Cédric / Triozer
 */
public abstract class BaseCertifier implements Certifier {

	protected final CertifierSettings   settings;
	protected final String              id;
	protected final String              name;
	protected final Thread              thread;
	protected       Map<String, String> pending;

	public BaseCertifier(CertifierSettings settings, String id, String name) {
		this.pending = CertifierApp.getInstance().getDatabase().getPendingOf(this);
		this.settings = settings;
		this.id = id;
		this.name = name;
		this.thread = new Thread(null, this::run, id);
	}

	@Override
	public void start() {
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
	public Settings getSettings() {
		return this.settings.get();
	}

	@Override
	public String getId() {
		return this.id;
	}

	@Override
	public String getName() {
		return this.name;
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

		public CertifierSettings() {
			this.settings = new Settings();
			init();
		}

		protected abstract void init();

		public final Settings get() {
			return this.settings;
		}
	}


}
