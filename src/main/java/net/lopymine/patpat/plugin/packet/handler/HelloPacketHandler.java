package net.lopymine.patpat.plugin.packet.handler;

import com.google.common.io.*;
import org.bukkit.entity.Player;

import net.lopymine.patpat.plugin.PatPatPlugin;
import net.lopymine.patpat.plugin.packet.PatPatPacketManager;

public class HelloPacketHandler implements IPacketHandler {

	@Override
	public void handle(Player sender, ByteArrayDataInput buf) {
		sender.sendPluginMessage(PatPatPlugin.getInstance(), PatPatPacketManager.HELLO_PATPAT_PLAYER_S2C_PACKET, ByteStreams.newDataOutput().toByteArray());
		PatPatPacketManager.PLAYER_PROTOCOLS.put(sender.getUniqueId(), true);
	}

	@Override
	public String getIncomingPacketID() {
		return PatPatPacketManager.HELLO_PATPAT_SERVER_C2S_PACKET;
	}

}
