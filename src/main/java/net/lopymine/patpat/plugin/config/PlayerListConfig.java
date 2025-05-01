package net.lopymine.patpat.plugin.config;

import lombok.Getter;

import net.lopymine.patpat.plugin.PatLogger;
import net.lopymine.patpat.plugin.PatPatPlugin;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.*;
import java.util.stream.Collectors;

@Getter
public class PlayerListConfig {

	@Getter
	private static PlayerListConfig instance;

	private static final String FILENAME = "player-list.txt";
	private static final File CONFIG_FILE = new File(PatPatPlugin.getInstance().getDataFolder(), FILENAME);

	private final Set<UUID> uuids;

	public PlayerListConfig() {
		this.uuids = new HashSet<>();
	}

	private static boolean create() {
		if (!CONFIG_FILE.exists()) {
			try {
				Files.createFile(CONFIG_FILE.toPath());
			} catch (IOException e) {
				PatLogger.error("Failed to create %s config file!".formatted(FILENAME), e);
			}
		}
		return CONFIG_FILE.exists();
	}

	public static void reload() {
		if (!create()) {
			PatLogger.error("Failed to reload PlayerListConfig file!");
			return;
		}
		PlayerListConfig config = new PlayerListConfig();

		int lineNumber = 0;
		String line = null;
		try (BufferedReader reader = new BufferedReader(new FileReader(CONFIG_FILE))) {
			line = reader.readLine();
			while (line != null) {
				lineNumber++;
				config.uuids.add(UUID.fromString(line));
				line = reader.readLine();
			}
			instance = config;
		} catch (IllegalArgumentException e) {
			PatLogger.error("Error line %d: '%s' is not uuid, file %s", lineNumber, line == null ? "null" : line, FILENAME);
		} catch (IOException e) {
			PatLogger.error("Failed to read " + FILENAME, e);
		}
	}

	public void save() {
		try (FileWriter writer = new FileWriter(CONFIG_FILE, StandardCharsets.UTF_8)) {
			writer.write(uuids.stream().map(UUID::toString).collect(Collectors.joining("\n")));
		} catch (Exception e) {
			PatLogger.error("Failed to save " + FILENAME, e);
		}
	}
}
