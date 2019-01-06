package fr.triozer.smc.certifier;

import fr.triozer.smc.CertifierApp;
import fr.triozer.smc.utils.AbstractManager;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

/**
 * @author Cédric / Triozer
 */
public class CertifierManager implements AbstractManager<String, Certifier> {

	private final Map<String, Certifier> certifiers;

	public CertifierManager() {
		this.certifiers = new HashMap<>();
	}

	@Override
	public Certifier get(String key) {
		return this.certifiers.get(key);
	}

	@Override
	public void add(Certifier value) {
		this.certifiers.put(value.getId(), value);
		CertifierApp.getInstance().getConsole().fine("	+ " + value.getId() + " " + value.getPending().size() + " pending verifications.");
	}

	@Override
	public void remove(Certifier value) {
		this.certifiers.remove(value.getId());
		CertifierApp.getInstance().getConsole().fine("	- " + value.getId());
	}

	@Override
	public Stream<Certifier> values() {
		return this.certifiers.values().stream();
	}

}
