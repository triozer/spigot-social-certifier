package fr.triozer.smc.database;

import fr.triozer.smc.CertifierApp;
import fr.triozer.smc.certifier.Certifier;

import java.sql.*;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

/**
 * @author Cédric / Triozer
 */
public class Database {

	private final String host;
	private final int    port;
	private final String database;

	private Connection connection;

	public Database() {
		this.host = CertifierApp.getInstance().getSettings().getString("database.host");
		this.port = CertifierApp.getInstance().getSettings().getInt("database.port");
		this.database = CertifierApp.getInstance().getSettings().getString("database.name");
	}

	public Database auth() {
		CertifierApp.getInstance().getConsole().fine("Database: Trying to connect to '" + this.database + "' database at '" + this.host + ":" + this.port + "'.");

		try {
			Class.forName("com.mysql.jdbc.Driver");

			this.connection = DriverManager.getConnection("jdbc:mysql://" + this.host + ":" + this.port + "/" + this.database + "?autoReconnect=true&useSSL=true",
					CertifierApp.getInstance().getSettings().getString("database.user"),
					CertifierApp.getInstance().getSettings().getString("database.pass"));
			CertifierApp.getInstance().getConsole().fine("Database: Connected.");

		} catch (ClassNotFoundException | SQLException e) {
			CertifierApp.getInstance().getConsole().stacktrace("Database: Connection failed.", e);
			CertifierApp.getInstance().stop(0);
		}

		return this;
	}

	public Map<String, String> getPendingOf(Certifier certifier) {
		Map<String, String> settings = new HashMap<>();

		try {
			PreparedStatement preparedStatement = this.connection
					.prepareStatement("SELECT * FROM pending WHERE `social-id` = ?");
			preparedStatement.setString(1, certifier.getId());

			ResultSet resultSet = preparedStatement.executeQuery();

			if (resultSet.next())
				settings.put(resultSet.getString("token"), resultSet.getString("name"));
		} catch (SQLException e) {
			CertifierApp.getInstance().getConsole().stacktrace("", e);
		}

		return settings;
	}

	public UUID getUniqueIdFrom(Certifier certifier, String token) {
		UUID uniqueId = null;

		try {
			PreparedStatement preparedStatement = this.connection
					.prepareStatement("SELECT `player-id` FROM pending WHERE `social-id` = ? AND token= ?");
			preparedStatement.setString(1, certifier.getId());
			preparedStatement.setString(2, token);

			ResultSet resultSet = preparedStatement.executeQuery();
			if (resultSet.next()) uniqueId = UUID.fromString(resultSet.getString("player-id"));
		} catch (SQLException e) {
			CertifierApp.getInstance().getConsole().stacktrace("", e);
		}

		return uniqueId;
	}

	public void remove(Certifier certifier, UUID uniqueId) {
		CompletableFuture.runAsync(() -> {
			try {
				PreparedStatement preparedStatement = this.connection
						.prepareStatement("DELETE FROM pending WHERE `social-id` = ? AND `player-id` = ?");
				preparedStatement.setString(1, certifier.getId());
				preparedStatement.setString(2, uniqueId.toString());
				preparedStatement.execute();
			} catch (SQLException e) {
				e.printStackTrace();
				CertifierApp.getInstance().getConsole().stacktrace("", e);
			}
		});
	}

}
