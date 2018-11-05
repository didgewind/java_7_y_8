package profe.java7y8.util;

import java.time.Duration;
import java.time.Instant;

public class Crono {

	private static Instant instanteInicial;

	public static void start() {
		instanteInicial = Instant.now();
	}
	
	public static long stop() {
		return Duration.between(instanteInicial, Instant.now()).toMillis();
	}
}
