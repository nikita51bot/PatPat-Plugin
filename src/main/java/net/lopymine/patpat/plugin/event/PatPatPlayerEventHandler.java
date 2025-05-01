package net.lopymine.patpat.plugin.event;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import net.lopymine.patpat.plugin.*;
import net.lopymine.patpat.plugin.packet.PatPatPacketManager;

public class PatPatPlayerEventHandler implements Listener {

	public static void register() {
		PatPatPlugin plugin = PatPatPlugin.getInstance();
		plugin.getServer().getPluginManager().registerEvents(new PatPatPlayerEventHandler(), plugin);
	}

	@EventHandler
	public void onJoin(PlayerJoinEvent event) {
		PatPatPacketManager.PLAYER_PROTOCOLS.put(event.getPlayer().getUniqueId(), Version.PACKET_V1_VERSION);
		PatLogger.debug("Player joined " + event.getPlayer().getName());
	}

	@EventHandler
	public void onQuit(PlayerQuitEvent event) {
		PatPatPacketManager.PLAYER_PROTOCOLS.remove(event.getPlayer().getUniqueId());
		PatLogger.debug("Player quit " + event.getPlayer().getName());
	}

}
