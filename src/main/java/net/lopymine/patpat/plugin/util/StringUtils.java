package net.lopymine.patpat.plugin.util;

import static net.lopymine.patpat.plugin.PatPatPlugin.MOD_ID;

public class StringUtils {

	private StringUtils() {
		throw new IllegalStateException("Utility class");
	}

	public static String modId(String path) {
		return "%s:%s".formatted(MOD_ID, path);
	}

	public static String permission(String permission) {
		return "%s.%s".formatted(MOD_ID, permission);
	}

}
