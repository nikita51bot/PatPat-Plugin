package net.lopymine.patpat.plugin.packet.handler;

import com.google.common.io.ByteArrayDataInput;
import org.bukkit.entity.Player;

import net.lopymine.patpat.plugin.*;
import net.lopymine.patpat.plugin.packet.PatPatPacketManager;

public class HelloPacketHandler implements IPacketHandler {

	@Override
	public void handle(Player sender, ByteArrayDataInput buf) {
		PatLogger.debug("[HELLO PACKET] from %s".formatted(sender.getName()));
		sender.sendPluginMessage(PatPatPlugin.getInstance(), PatPatPacketManager.HELLO_PATPAT_PLAYER_S2C_PACKET, new byte[0]);
		PatPatPacketManager.PLAYER_PROTOCOLS.put(sender.getUniqueId(), Version.PACKET_V2_VERSION); // TODO прочитать версию клиента (можно передавать 3 short/int)
	}

	@Override
	public String getIncomingPacketID() {
		return PatPatPacketManager.HELLO_PATPAT_SERVER_C2S_PACKET;
	}

}
