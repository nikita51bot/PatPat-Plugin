package net.lopymine.patpat.plugin.config.option;

public enum ListMode {
	WHITELIST,
	BLACKLIST,
	DISABLED;

	public static ListMode getOrDisabled(String modeId) { // TODO remove?
		try {
			return ListMode.valueOf(modeId);
		} catch (IllegalArgumentException ignored) {
			return DISABLED;
		}
	}
}
