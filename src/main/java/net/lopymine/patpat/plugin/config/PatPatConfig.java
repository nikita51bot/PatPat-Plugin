package net.lopymine.patpat.plugin.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.SerializedName;
import lombok.Getter;
import lombok.Setter;
import net.lopymine.patpat.plugin.PatPatPlugin;
import net.lopymine.patpat.plugin.command.ratelimit.RateLimitManager;
import net.lopymine.patpat.plugin.config.option.ListMode;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.logging.Level;

@Getter
@Setter
public class PatPatConfig {

	private static final Gson GSON = new GsonBuilder()
			.setPrettyPrinting()
			.create();

	@Getter
	private static PatPatConfig instance;

	@SerializedName("_comment")
	private String comment;

	private ListMode listMode;
	private RateLimitConfig rateLimit;

	public PatPatConfig(ListMode listMode) {
		this.listMode = listMode;
	}

	public static void reload() {
		File configPath = getConfigPath();
		if (!configPath.exists()) {
			instance = create();
		}

		try (FileReader reader = new FileReader(configPath)) {
			instance = GSON.fromJson(reader, PatPatConfig.class);
		} catch (Exception e) {
			PatPatPlugin.LOGGER.log(Level.WARNING, "Failed to read player list config!", e);
		}
		RateLimitManager.reloadTask();
	}

	private static File getConfigPath() {
		return new File(PatPatPlugin.getInstance().getDataFolder(), "config.json");
	}

	public void save() {
		this.comment = "Documentation: https://github.com/LopyMine/PatPat-Plugin/blob/main/doc/en/config.md"; // TODO: Set the comment in the field and serialize it, or switch to Jackson library
		String json = GSON.toJson(this, PatPatConfig.class);
		try (FileWriter writer = new FileWriter(getConfigPath())) {
			writer.write(json);
		} catch (Exception e) {
			PatPatPlugin.LOGGER.log(Level.WARNING, "Failed to save config!", e);
		}
	}

	private static String loadConfigFromJar() {
		try (InputStream inputStream = PatPatPlugin.getInstance().getClass().getClassLoader().getResourceAsStream("config.json")) {
			assert (inputStream != null);
			return new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
		} catch (Exception e) {
			PatPatPlugin.LOGGER.log(Level.SEVERE, "Failed to open `config.json` in jar", e);
		}
		return null;
	}

	private static PatPatConfig create() {
		String json = loadConfigFromJar();
		if (json == null) {
			PatPatPlugin.LOGGER.warning("Failed to create config!");
			return null;
		}
		PatPatConfig config = GSON.fromJson(json, PatPatConfig.class);
		try (FileWriter writer = new FileWriter(getConfigPath())) {
			writer.write(json);
		} catch (Exception e) {
			PatPatPlugin.LOGGER.log(Level.WARNING, "Failed to create config!", e);
		}
		return config;
	}
}
