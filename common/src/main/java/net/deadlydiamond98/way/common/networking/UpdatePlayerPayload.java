package net.deadlydiamond98.way.common.networking;

import net.deadlydiamond98.way.common.command.WayServerCommands;
import net.deadlydiamond98.way.util.WayEntityUtil;
import net.deadlydiamond98.way.util.mixin.IWayPlayer;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentSerialization;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;

import java.util.UUID;

public record UpdatePlayerPayload(
        Component name,
        float nametagY,
        double x,
        double y,
        double z,
        double eyeHeight,
        UUID uuid,
        boolean showPlayer,
        int color,
        int hurtTime,
        float health,
        float maxHealth,
        boolean optedIn
) implements CustomPacketPayload {

    public static final StreamCodec<RegistryFriendlyByteBuf, UpdatePlayerPayload> CODEC =
            CustomPacketPayload.codec(UpdatePlayerPayload::write, UpdatePlayerPayload::new);

    private UpdatePlayerPayload(RegistryFriendlyByteBuf buf) {
        this(
                ComponentSerialization.STREAM_CODEC.decode(buf), buf.readFloat(),
                buf.readDouble(), buf.readDouble(), buf.readDouble(),
                buf.readDouble(),
                buf.readUUID(),
                buf.readBoolean(), buf.readInt(),
                buf.readInt(), buf.readFloat(), buf.readFloat(),
                buf.readBoolean()
        );
    }

    public static UpdatePlayerPayload of(ServerPlayer viewer, Player player) {
        IWayPlayer wayPlayer = (IWayPlayer) player;
        return new UpdatePlayerPayload(
                player.getName(),
                WayEntityUtil.nameTagOffsetY(player),
                player.getX(), player.getY(), player.getZ(),
                player.getEyeHeight(),
                player.getUUID(),
                wayPlayer.way$showPlayer(),
                wayPlayer.way$getColor(),
                player.hurtTime,
                player.getHealth(),
                player.getMaxHealth(),
                WayServerCommands.FORCE_OPT.getValue(viewer) || wayPlayer.way$showPlayer()
        );
    }

    private void write(RegistryFriendlyByteBuf buf) {
        ComponentSerialization.STREAM_CODEC.encode(buf, this.name);
        buf.writeFloat(this.nametagY);

        buf.writeDouble(this.x);
        buf.writeDouble(this.y);
        buf.writeDouble(this.z);

        buf.writeDouble(this.eyeHeight);

        buf.writeUUID(this.uuid);

        buf.writeBoolean(this.showPlayer);
        buf.writeInt(this.color);

        buf.writeInt(this.hurtTime);
        buf.writeFloat(this.health);
        buf.writeFloat(this.maxHealth);

        buf.writeBoolean(this.optedIn);
    }

    @Override
    public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
        return WayPayloads.UPDATE_PLAYER;
    }
}
