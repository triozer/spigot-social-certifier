package main;

import fr.triozer.smc.CertifierApp;
import fr.triozer.smc.impl.DiscordCertifier;

/**
 * @author Cédric / Triozer
 */
public class Launcher {

	public static void main(String[] args) {
		CertifierApp app = new CertifierApp();
		app.getManager().add(new DiscordCertifier());
		app.startAll();
	}

}
