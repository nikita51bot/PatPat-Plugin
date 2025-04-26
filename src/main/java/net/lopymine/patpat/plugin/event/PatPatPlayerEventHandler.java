package net.lopymine.patpat.plugin.event;

import org.bukkit.event.*;
import org.bukkit.event.player.*;

import net.lopymine.patpat.plugin.PatPatPlugin;
import net.lopymine.patpat.plugin.packet.PatPatPacketManager;

public class PatPatPlayerEventHandler implements Listener {

	public static void register() {
		PatPatPlugin plugin = PatPatPlugin.getInstance();
		plugin.getServer().getPluginManager().registerEvents(new PatPatPlayerEventHandler(), plugin);
	}

	@SuppressWarnings("unused")
	public void onJoin(PlayerJoinEvent event) {
		PatPatPacketManager.PLAYER_PROTOCOLS.put(event.getPlayer().getUniqueId(), false);
	}

	@SuppressWarnings("unused")
	public void onQuit(PlayerQuitEvent event) {
		PatPatPacketManager.PLAYER_PROTOCOLS.remove(event.getPlayer().getUniqueId());
	}

}
