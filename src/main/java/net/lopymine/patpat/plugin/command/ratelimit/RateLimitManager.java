package net.lopymine.patpat.plugin.command.ratelimit;

import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitTask;

import net.lopymine.patpat.plugin.PatPatPlugin;
import net.lopymine.patpat.plugin.config.PatPatConfig;
import net.lopymine.patpat.plugin.config.RateLimitConfig;

import java.util.*;

public class RateLimitManager {

	private RateLimitManager() {
		throw new IllegalStateException("Manager class");
	}

	private static BukkitTask task;

	private static final Map<UUID, Integer> uuidToPat = new HashMap<>();

	public static int getAvailablePats(UUID uuid) {
		return uuidToPat.getOrDefault(uuid, PatPatConfig.getInstance().getRateLimit().getTokenLimit());
	}

	public static boolean canPat(UUID uuid) {
		RateLimitConfig config = PatPatConfig.getInstance().getRateLimit();
		if (!config.isEnabled()) {
			return true;
		}
		int availablePats = uuidToPat.getOrDefault(
				uuid,
				config.getTokenLimit()
		) - 1;
		if (availablePats < 0) {
			return false;
		}
		uuidToPat.put(uuid, availablePats);
		return true;
	}

	public static void addPats(int token) {
		int tokenLimit = PatPatConfig.getInstance().getRateLimit().getTokenLimit();
		for (Iterator<Map.Entry<UUID, Integer>> it = uuidToPat.entrySet().iterator(); it.hasNext(); ) {
			Map.Entry<UUID, Integer> entry = it.next();
			int value = entry.getValue() + token;
			if (value > tokenLimit) {
				it.remove();
				continue;
			}
			uuidToPat.put(entry.getKey(), value);
		}
	}

	public static void reloadTask() {
		if (task != null) {
			task.cancel();
			task = null;
		}
		RateLimitConfig config = PatPatConfig.getInstance().getRateLimit();
		if (!config.isEnabled()) {
			return;
		}
		Time configInterval = config.getTokenInterval();
		long period = configInterval.getValue() * configInterval.getUnit().getMultiplier() * 20L;
		task = Bukkit.getScheduler().runTaskTimerAsynchronously(
				PatPatPlugin.getInstance(),
				() -> RateLimitManager.addPats(config.getTokenIncrement()),
				0,
				period
		);
	}
}
