package net.lopymine.patpat.plugin.config.migrate;

import net.lopymine.patpat.plugin.*;
import net.lopymine.patpat.plugin.config.PatPatConfig;

import java.io.File;
import java.util.*;

public class MigrateManager {

	private static final String ISSUE_LINK = "https://github.com/LopyMine/PatPat-Plugin/issues";

	private static final Set<MigrateHandler> HANDLERS = new HashSet<>();

	public static final File CONFIG_FOLDER = PatPatPlugin.getInstance().getDataFolder();

	private MigrateManager() {
		throw new IllegalStateException("Manager class");
	}

	public static void onInitialize() {
		HANDLERS.clear();
		addHandlers(new MigrateVersion0());
	}

	public static void migrate() {
		onInitialize();
		for (MigrateHandler handler : HANDLERS) {
			if (!handler.needMigrate()) {
				continue;
			}
			String migrateVersion = handler.getVersion();
			if (!handler.migrate()) {
				PatLogger.warn("Error migrate config from version: " + migrateVersion);
				PatLogger.warn("Report the issue at github page: {}, attaching your config and specifying the mod and server versions.", ISSUE_LINK);
				return;
			}
			PatLogger.info("Config successful migrated from version: " + migrateVersion);
		}
	}

	public static void checkVersion() {
		Version version = PatPatConfig.getInstance().getInfo().getVersion();
		if (version.is(Version.SERVER_CONFIG_VERSION)) {
			return;
		}
		if (version.isMoreThan(Version.SERVER_CONFIG_VERSION)) {
			PatLogger.warn("Your config version is higher than the mod's (%s > %s). This may cause errors!".formatted(version, Version.SERVER_CONFIG_VERSION));
			PatLogger.warn("Update the mod to avoid issues.");
			return;
		}
		PatLogger.warn("Your config version is lower than the mod's (%s < %s). This may cause errors!".formatted(version, Version.SERVER_CONFIG_VERSION));
		PatLogger.warn("Back up your config and report the issue at github page: {}, attaching your config and specifying the mod and server versions.", ISSUE_LINK);
	}

	private static void addHandlers(MigrateHandler... handlers) {
		HANDLERS.addAll(List.of(handlers));
	}
}
