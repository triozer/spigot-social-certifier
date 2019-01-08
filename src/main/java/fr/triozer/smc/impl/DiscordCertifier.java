package fr.triozer.smc.impl;

import fr.triozer.smc.CertifierApp;
import fr.triozer.smc.certifier.BaseCertifier;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

import javax.security.auth.login.LoginException;

/**
 * @author Cédric / Triozer
 */
public class DiscordCertifier extends BaseCertifier<JDA> {

	private JDA client;

	public DiscordCertifier() {
		super("discord", "SMC-DISCORD");
	}

	@Override
	public void run() {
		try {
			this.client = new JDABuilder(this.settings.get().getString("token"))
					.addEventListener(new ListenerAdapter() {
						@Override
						public void onMessageReceived(MessageReceivedEvent event) {
							if (!event.getChannel().getId().equalsIgnoreCase(settings.get().getString("default_channel")))
								return;
							String token = event.getMessage().getContentRaw();
							if (isValid(token)) {
								String name = event.getAuthor().getName() + "#" + event.getAuthor().getDiscriminator();
								if (validate(CertifierApp.getInstance().getDatabase()
										.getUniqueIdFrom(DiscordCertifier.this, token), token, name)) {
									event.getMessage().addReaction("✔").complete();
								}
							} else
								event.getMessage().addReaction("❌").complete();
						}
					})
					.build();
			this.running = true;
		} catch (LoginException e) {
			CertifierApp.getInstance().getConsole()
					.stacktrace("	" + this.getId() + " error on starting the discord bot.", e);
		}
		CertifierApp.getInstance().getConsole().fine("	" + this.getId() + ": started.");
	}

	@Override
	public void stop() {
		this._stop((jda) -> this.client.shutdown());
	}

	@Override
	public final JDA getClient() {
		return this.client;
	}

}
