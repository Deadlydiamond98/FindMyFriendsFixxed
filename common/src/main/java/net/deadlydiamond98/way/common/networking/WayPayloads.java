package net.deadlydiamond98.way.common.networking;

import net.deadlydiamond98.way.Way;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

public class WayPayloads {

    public static final CustomPacketPayload.Type<UpdatePlayerPayload> UPDATE_PLAYER =
            new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath(Way.MOD_ID, "update_players"));

    public static final CustomPacketPayload.Type<ClearPlayersPayload> CLEAR_PLAYERS =
            new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath(Way.MOD_ID, "clear_players"));

    public static final CustomPacketPayload.Type<UpdateNameplateRenderPayload> UPDATE_NAMEPLATE_RENDER =
            new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath(Way.MOD_ID, "update_nameplate_render"));
}
