package fr.triozer.smc.utils;

import java.security.spec.ECField;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

/**
 * @author Cédric / Triozer
 */
public class Console {

	public static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("'('dd/MM/yyyy 'at' HH:mm:ss.SS'ms)'");

	private final String name;

	public Console(String name) {
		this.name = name;
	}

	public Console(Class zClass) {
		this(zClass.getName());
	}

	public void print(String message) {
		System.out.println(DATE_FORMAT.format(new Date()) + message);
	}

	public void printErr(String message) {
		System.out.println(DATE_FORMAT.format(new Date()) + " (error) : " + message);
	}

	public void fine(String message) {
		print(" (fine) : " + message);
	}

	public void error(String message) {
		printErr(message);
	}

	public void stacktrace(String message, Exception e) {
		printErr(message);
		printErr("	Caused by : " + e.getLocalizedMessage() + " at: ");
		Arrays.stream(e.getStackTrace()).forEach(ste -> printErr("		" + ste));
	}

	public void warning(String message) {
		print(" (warning) : " + message);
	}

}
