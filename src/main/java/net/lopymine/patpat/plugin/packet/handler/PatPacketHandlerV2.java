package net.lopymine.patpat.plugin.packet.handler;

import com.google.common.io.*;
import org.bukkit.entity.*;

import net.lopymine.patpat.plugin.PatPatPlugin;
import net.lopymine.patpat.plugin.packet.PatPatPacketManager;

import java.io.*;
import java.util.logging.Level;
import org.jetbrains.annotations.Nullable;

public class PatPacketHandlerV2 extends PatPacketHandler {

	@Override
	public String getIncomingPacketID() {
		return PatPatPacketManager.PATPAT_C2S_PACKET_V2_ID;
	}

	@Override
	protected byte[] getOutgoingPacketBytes(Entity pattedEntity, Entity whoPattedEntity, ByteArrayDataOutput output) {
		writeVarInt(output, pattedEntity.getEntityId());
		writeVarInt(output, whoPattedEntity.getEntityId());
		return output.toByteArray();
	}

	@Override
	@Nullable
	protected Entity getPattedEntity(PatPatPlugin plugin, Player sender, ByteArrayDataInput buf) {
		try {
			int entityId = readVarInt(buf);
			for (Entity entity : sender.getWorld().getEntities()) {
				if (entity.getEntityId() != entityId) {
					continue;
				}
				return entity;
			}
		} catch (IOException e) {
			PatPatPlugin.LOGGER.log(Level.WARNING, "Failed parse entityId from incoming packet from player %s[%s]!".formatted(sender.getName(), sender.getUniqueId()), e);
		}
		return null;
	}

	private static int readVarInt(ByteArrayDataInput buf) throws IOException {
		int i = 0;
		int j = 0;

		byte b;
		do {
			b = buf.readByte();
			i |= (b & 127) << j++ * 7;
			if (j > 5) {
				throw new RuntimeException("VarInt too big");
			}
		} while((b & 128) == 128);

		return i;
	}

	private static void writeVarInt(ByteArrayDataOutput output, int value) {
		while((value & -128) != 0) {
			output.writeByte(value & 127 | 128);
			value >>>= 7;
		}

		output.writeByte(value);
	}
}
