package net.lopymine.patpat.plugin.util;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import lombok.experimental.UtilityClass;

import net.lopymine.patpat.plugin.PatLogger;
import net.lopymine.patpat.plugin.PatPatPlugin;

import java.io.*;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import org.jetbrains.annotations.Nullable;

@UtilityClass
public class ResourceUtils {

	private static final Type MAP_TYPE = new TypeToken<Map<String, String>>() {}.getType();

	private static final Gson GSON = new GsonBuilder()
			.setPrettyPrinting()
			.create();

	@Nullable
	public static String loadFileFromJar(String filename) {
		try (InputStream inputStream = PatPatPlugin.getInstance().getClass().getClassLoader().getResourceAsStream(filename)) {
			assert (inputStream != null);
			return new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
		} catch (Exception e) {
			PatLogger.error("Failed to open `%s` in jar".formatted(filename), e);
		}
		return null;
	}

	@Nullable
	public static Map<String, String> loadLangFromJar(String filename){
		try (InputStream inputStream = PatPatPlugin.getInstance().getClass().getClassLoader().getResourceAsStream(filename)) {
			assert (inputStream != null);
			return GSON.fromJson(new InputStreamReader(inputStream), MAP_TYPE);
		} catch (JsonSyntaxException e) {
			PatLogger.error("Failed to read json from `%s` in jar".formatted(filename), e);
		} catch (IOException e) {
			PatLogger.error("Failed to open `%s` in jar".formatted(filename), e);
		}
		return null;
	}
}
