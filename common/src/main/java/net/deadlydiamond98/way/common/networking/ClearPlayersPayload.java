package net.deadlydiamond98.way.common.networking;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

public record ClearPlayersPayload() implements CustomPacketPayload {

    public static final ClearPlayersPayload INSTANCE = new ClearPlayersPayload();

    public static final StreamCodec<RegistryFriendlyByteBuf, ClearPlayersPayload> CODEC = StreamCodec.unit(INSTANCE);

    @Override
    public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
        return WayPayloads.CLEAR_PLAYERS;
    }
}
