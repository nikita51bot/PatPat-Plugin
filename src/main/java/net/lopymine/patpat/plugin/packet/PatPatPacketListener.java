package net.lopymine.patpat.plugin.packet;

import com.google.common.io.ByteStreams;
import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;

import net.lopymine.patpat.plugin.packet.handler.IPacketHandler;

import java.util.*;
import org.jetbrains.annotations.NotNull;

public class PatPatPacketListener implements PluginMessageListener {

	private final Map<String, IPacketHandler> handlers = new HashMap<>();

	@Override
	public void onPluginMessageReceived(@NotNull String s, @NotNull Player sender, byte[] bytes) {
		IPacketHandler packetHandler = this.handlers.get(s);
		if (packetHandler == null) {
			return;
		}
		packetHandler.handle(sender, ByteStreams.newDataInput(bytes));
	}

	public void registerPacket(IPacketHandler handler) {
		this.handlers.put(handler.getIncomingPacketID(), handler);
	}
}
