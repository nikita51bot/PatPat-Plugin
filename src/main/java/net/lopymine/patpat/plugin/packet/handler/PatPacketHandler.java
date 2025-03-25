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

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Stream;

@ExtensionMethod(ByteArrayDataExtension.class)
public class PatPacketHandler implements PacketHandler {

	@Override
	public void handle(Player sender, ByteArrayDataInput buf) {
		PatPatPlugin plugin = PatPatPlugin.getInstance();
		if (!this.canHandle(sender, plugin)) {
			return;
		}

		UUID pattedEntityUuid = buf.readUuid();
		Entity pattedEntity = plugin.getServer().getEntity(pattedEntityUuid);
		if (!(pattedEntity instanceof LivingEntity livingEntity)) {
			return;
		}

		if (livingEntity.isInvisible()) {
			return;
		}

		double patVisibilityRadius = plugin.getServer().getViewDistance();

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
			if (player.getUniqueId().equals(sender.getUniqueId())) {
				continue;
			}

			ByteArrayDataOutput out = ByteStreams.newDataOutput();
			out.writeUuid(pattedEntityUuid);
			out.writeUuid(sender.getUniqueId());

			player.sendPluginMessage(plugin, PatPatPacketManager.PATPAT_S2C_PACKET_ID, out.toByteArray());
		}
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
	public String getPacketID() {
		return PatPatPacketManager.PATPAT_C2S_PACKET_ID;
	}
}
