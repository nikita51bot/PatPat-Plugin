package net.lopymine.patpat.plugin.packet.handler;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import lombok.experimental.ExtensionMethod;
import net.lopymine.patpat.plugin.PatPatPlugin;
import net.lopymine.patpat.plugin.command.ratelimit.RateLimitManager;
import net.lopymine.patpat.plugin.config.PatPatConfig;
import net.lopymine.patpat.plugin.config.option.ListMode;
import net.lopymine.patpat.plugin.extension.ByteArrayDataExtension;
import net.lopymine.patpat.plugin.packet.PatPatPacketManager;

import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import java.util.*;
import java.util.stream.Stream;
import org.jetbrains.annotations.*;

@ExtensionMethod(ByteArrayDataExtension.class)
public class PatPacketHandler implements IPacketHandler {

	@Override
	public void handle(Player sender, ByteArrayDataInput buf) {
		PatPatPlugin plugin = PatPatPlugin.getInstance();
		if (!this.canHandle(sender, plugin)) {
			PatPatPlugin.LOGGER.info("Can handle");
			return;
		}

		Entity pattedEntity = this.getPattedEntity(plugin, sender, buf);
		if (!(pattedEntity instanceof LivingEntity livingEntity)) {
			PatPatPlugin.LOGGER.info("not a entity");
			return;
		}

		if (livingEntity.isInvisible()) {
			PatPatPlugin.LOGGER.info("invisible");
			return;
		}

		double patVisibilityRadius = plugin.getServer().getViewDistance() * 16;

		List<Player> nearbyPlayers = new ArrayList<>(pattedEntity
				.getNearbyEntities(patVisibilityRadius, patVisibilityRadius, patVisibilityRadius)
				.stream()
				.flatMap(entity -> {
					if (entity instanceof Player player) {
						return Stream.of(player);
					}
					return Stream.empty();
				}).toList());

		if (pattedEntity instanceof Player player) {
			nearbyPlayers.add(player);
		}

		for (Player player : nearbyPlayers) {
			UUID senderUuid = sender.getUniqueId();
			if (player.getUniqueId().equals(senderUuid)) {
				continue;
			}

			PatPatPlugin.LOGGER.info("Sending out packet to %s".formatted(player.getName()));
			byte[] byteArray = this.getOutgoingPacketBytes(pattedEntity, sender, ByteStreams.newDataOutput());
			PatPatPlugin.LOGGER.info(Arrays.toString(byteArray));
			player.sendPluginMessage(plugin, this.getOutgoingPacketID(senderUuid), byteArray);
		}
	}

	protected byte[] getOutgoingPacketBytes(Entity pattedEntity, Entity whoPattedEntity, ByteArrayDataOutput output) {
		output.writeUuid(pattedEntity.getUniqueId());
		output.writeUuid(whoPattedEntity.getUniqueId());
		return output.toByteArray();
	}

	@Nullable
	protected Entity getPattedEntity(PatPatPlugin plugin, Player player, ByteArrayDataInput buf) {
		return plugin.getServer().getEntity(buf.readUuid());
	}

	private boolean canHandle(Player sender, PatPatPlugin plugin) {
		if (!sender.hasPermission(PatPatConfig.getInstance().getRateLimit().getPermissionBypass()) && !RateLimitManager.canPat(sender.getUniqueId())) {
			return false;
		}

		Set<UUID> uuids = plugin.getPlayerListConfig().getUuids();
		ListMode listMode = PatPatConfig.getInstance().getListMode();

		return switch (listMode) {
			case DISABLED -> true;
			case WHITELIST -> uuids.contains(sender.getUniqueId());
			case BLACKLIST -> !uuids.contains(sender.getUniqueId());
		};
	}

	@Override
	public String getIncomingPacketID() {
		return PatPatPacketManager.PATPAT_C2S_PACKET_ID;
	}

	public String getOutgoingPacketID(UUID player) {
		System.out.println("-----");
		System.out.println(player);
		boolean equals = Boolean.TRUE.equals(PatPatPacketManager.PLAYER_PROTOCOLS.get(player));
		System.out.println(equals);
		System.out.println("-----");
		if (equals) {
			return PatPatPacketManager.PATPAT_S2C_PACKET_V2_ID;
		}
		return PatPatPacketManager.PATPAT_S2C_PACKET_ID;
	}
}
