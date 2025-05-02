package net.lopymine.patpat.plugin.config.migrate;

import com.google.gson.*;

import net.lopymine.patpat.plugin.PatLogger;
import net.lopymine.patpat.plugin.config.PatPatConfig;
import net.lopymine.patpat.plugin.config.PlayerListConfig;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.UUID;
import org.jetbrains.annotations.Nullable;

public class MigrateVersion0 implements MigrateHandler {

	private static final String OLD_CONFIG_FILENAME = "config.yml";

	private boolean createBackup(File file) {
		String filename = file.getName();
		File backupFolder = MigrateManager.CONFIG_FOLDER.toPath().resolve("backup").toFile();
		if ((!backupFolder.exists() || !backupFolder.isDirectory()) && !backupFolder.mkdir()) {
			PatLogger.error("Failed to create backup folder");
			return false;
		}

		try {
			Files.copy(file.toPath(), backupFolder.toPath().resolve(filename + ".bkp"), StandardCopyOption.REPLACE_EXISTING);
		} catch (IOException e) {
			PatLogger.error("Failed to create old config backup for " + filename, e);
			return false;
		}
		return true;
	}

	@Nullable
	public PlayerListConfig transformPlayerList(File oldPlayerList) {
		PlayerListConfig playerListConfig = new PlayerListConfig();
		try (FileReader reader = new FileReader(oldPlayerList)) {
			JsonObject rootObj = JsonParser.parseReader(reader).getAsJsonObject();
			JsonArray array = rootObj.get("uuids").getAsJsonArray();
			for (JsonElement element : array) {
				String uuid = element.getAsString();
				playerListConfig.getUuids().add(UUID.fromString(uuid));
			}
		} catch (FileNotFoundException e) {
			PatLogger.warn("Failed to read file " + oldPlayerList.getName(), e);
			return null;
		} catch (IOException e) {
			PatLogger.warn("Failed to parse file " + oldPlayerList.getName(), e);
			return null;
		}
		return playerListConfig;
	}

	@Override
	public boolean needMigrate() {
		File oldPlayerList = new File(MigrateManager.CONFIG_FOLDER, "player-list.json");
		File oldConfig = new File(MigrateManager.CONFIG_FOLDER, OLD_CONFIG_FILENAME);
		return oldPlayerList.exists() || oldConfig.exists();
	}

	@Override
	public boolean migrate() {
		File oldConfig = new File(MigrateManager.CONFIG_FOLDER, OLD_CONFIG_FILENAME);
		if (oldConfig.exists() && createBackup(oldConfig)) {
			try {
				Files.delete(oldConfig.toPath());
			} catch (IOException e) {
				PatLogger.warn("Failed to delete old config " + oldConfig.getName(), e);
				return false;
			}
		}
		PatPatConfig config = new PatPatConfig();
		config.save();

		File oldPlayerList = new File(MigrateManager.CONFIG_FOLDER, "player-list.json");
		if (!oldPlayerList.exists()) {
			new PlayerListConfig().save();
			return true;
		}

		if (!createBackup(oldPlayerList)) {
			PatLogger.warn("Failed to create backup for " + oldPlayerList.getName());
			return false;
		}
		PlayerListConfig playerListConfig = transformPlayerList(oldPlayerList);
		if (playerListConfig == null) {
			return false;
		}
		try {
			Files.delete(oldPlayerList.toPath());
		} catch (IOException e) {
			PatLogger.warn("Failed to delete old config " + oldPlayerList.getName(), e);
			return false;
		}
		playerListConfig.save();
		return true;
	}

	@Override
	public String getVersion() {
		return "0";
	}

}
