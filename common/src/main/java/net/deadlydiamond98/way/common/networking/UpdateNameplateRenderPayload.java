package net.deadlydiamond98.way.common.networking;

import net.deadlydiamond98.way.common.command.WayServerCommands;
import net.deadlydiamond98.way.util.mixin.IWayPlayer;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;

public record UpdateNameplateRenderPayload(
        boolean toggle,
        boolean names,
        boolean distance,
        boolean colors,
        boolean outlines,
        boolean head,
        boolean headOutline,
        boolean colorDistance,
        boolean namePainFlash,
        boolean namePainGetRedder,
        int minRender,
        int maxRender,
        boolean bypassOpt
) implements CustomPacketPayload {

    public static final StreamCodec<RegistryFriendlyByteBuf, UpdateNameplateRenderPayload> CODEC =
            CustomPacketPayload.codec(UpdateNameplateRenderPayload::write, UpdateNameplateRenderPayload::new);

    private UpdateNameplateRenderPayload(RegistryFriendlyByteBuf buf) {
        this(
                buf.readBoolean(),
                buf.readBoolean(), buf.readBoolean(), buf.readBoolean(), buf.readBoolean(),
                buf.readBoolean(), buf.readBoolean(),
                buf.readBoolean(), buf.readBoolean(), buf.readBoolean(),
                buf.readInt(), buf.readInt(),
                buf.readBoolean()
        );
    }

    public static UpdateNameplateRenderPayload of(ServerPlayer player) {
        IWayPlayer wayPlayer = (IWayPlayer) player;
        return new UpdateNameplateRenderPayload(
                wayPlayer.way$getToggle(),

                wayPlayer.way$canSeeName(),
                wayPlayer.way$canSeeDist(),
                wayPlayer.way$canSeeColor(),
                wayPlayer.way$canSeeOutline(),

                wayPlayer.way$canSeeHead(),
                wayPlayer.way$canSeeHeadOutline(),

                WayServerCommands.COLOR_DISTANCE.getValue(player),
                WayServerCommands.NAME_PAIN_FLASH.getValue(player),
                WayServerCommands.NAME_PAIN_REDDER.getValue(player),

                WayServerCommands.MIN_DIST.getValue(player),
                WayServerCommands.MAX_DIST.getValue(player),

                wayPlayer.way$bypassOpt()
        );
    }

    private void write(RegistryFriendlyByteBuf buf) {
        buf.writeBoolean(this.toggle);

        buf.writeBoolean(this.names);
        buf.writeBoolean(this.distance);
        buf.writeBoolean(this.colors);
        buf.writeBoolean(this.outlines);

        buf.writeBoolean(this.head);
        buf.writeBoolean(this.headOutline);

        buf.writeBoolean(this.colorDistance);
        buf.writeBoolean(this.namePainFlash);
        buf.writeBoolean(this.namePainGetRedder);

        buf.writeInt(this.minRender);
        buf.writeInt(this.maxRender);

        buf.writeBoolean(this.bypassOpt);
    }

    @Override
    public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
        return WayPayloads.UPDATE_NAMEPLATE_RENDER;
    }
}
