package net.lopymine.patpat.plugin.packet;

import org.bukkit.plugin.messaging.Messenger;

import net.lopymine.patpat.plugin.PatPatPlugin;
import net.lopymine.patpat.plugin.Version;
import net.lopymine.patpat.plugin.packet.handler.*;
import net.lopymine.patpat.plugin.util.StringUtils;

import java.util.*;

public class PatPatPacketManager {

	private PatPatPacketManager() {
		throw new IllegalStateException("Manager class");
	}

	public static final Map<UUID, Version> PLAYER_PROTOCOLS = new HashMap<>();

	public static final String PATPAT_C2S_PACKET_ID = StringUtils.modId("pat_entity_c2s_packet");
	public static final String PATPAT_S2C_PACKET_ID = StringUtils.modId("pat_entity_s2c_packet");

	public static final String PATPAT_C2S_PACKET_V2_ID = StringUtils.modId("pat_entity_c2s_packet_v2");
	public static final String PATPAT_S2C_PACKET_V2_ID = StringUtils.modId("pat_entity_s2c_packet_v2");

	public static final String HELLO_PATPAT_SERVER_C2S_PACKET = StringUtils.modId("hello_patpat_server_c2s_packet");
	public static final String HELLO_PATPAT_PLAYER_S2C_PACKET = StringUtils.modId("hello_patpat_player_s2c_packet");

	public static void register() {
		// Create Main Listener for PatPat Packets
		PatPatPacketListener listener = new PatPatPacketListener();

		// Listening PatPat Packets Here
		listener.registerPacket(new PatPacketHandler());
		listener.registerPacket(new PatPacketHandlerV2());
		listener.registerPacket(new HelloPacketHandler());

		// Registering Main Listener
		PatPatPlugin plugin = PatPatPlugin.getInstance();
		Messenger messenger = plugin.getServer().getMessenger();

		messenger.registerIncomingPluginChannel(plugin, PATPAT_C2S_PACKET_ID, listener);
		messenger.registerOutgoingPluginChannel(plugin, PATPAT_S2C_PACKET_ID);

		messenger.registerIncomingPluginChannel(plugin, PATPAT_C2S_PACKET_V2_ID, listener);
		messenger.registerOutgoingPluginChannel(plugin, PATPAT_S2C_PACKET_V2_ID);

		messenger.registerIncomingPluginChannel(plugin, HELLO_PATPAT_SERVER_C2S_PACKET, listener);
		messenger.registerOutgoingPluginChannel(plugin, HELLO_PATPAT_PLAYER_S2C_PACKET);
	}
}
