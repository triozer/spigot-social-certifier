package fr.triozer.smc.certifier;

import fr.triozer.smc.setting.Settings;

import java.util.Set;
import java.util.UUID;
import java.util.function.Predicate;

/**
 * @author Cédric / Triozer
 */
public interface Certifier<T> {

	void run();

	void start();

	void stop();

	boolean validate(UUID uniqueId, String token, String name);

	boolean validate(UUID uniqueId, String name, Predicate<String> predicate);

	boolean isValid(String token);

	boolean isValid(String token, Predicate<String> format);

	boolean isRunning();

	Settings getSettings();

	String getId();

	String getName();

	Thread getThread();

	Set<String> getPending();

	T getClient();

}
