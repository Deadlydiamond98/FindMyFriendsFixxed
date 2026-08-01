package net.deadlydiamond98.way.networking;

import net.deadlydiamond98.way.client.WayClientPacketHandler;
import net.deadlydiamond98.way.common.networking.ClearPlayersPayload;
import net.deadlydiamond98.way.common.networking.UpdateNameplateRenderPayload;
import net.deadlydiamond98.way.common.networking.UpdatePlayerPayload;
import net.deadlydiamond98.way.common.networking.WayPayloads;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;

public class WayFabricNetworking {

    public static void registerPayloads() {
        PayloadTypeRegistry.playS2C().register(WayPayloads.UPDATE_PLAYER, UpdatePlayerPayload.CODEC);
        PayloadTypeRegistry.playS2C().register(WayPayloads.CLEAR_PLAYERS, ClearPlayersPayload.CODEC);
        PayloadTypeRegistry.playS2C().register(WayPayloads.UPDATE_NAMEPLATE_RENDER, UpdateNameplateRenderPayload.CODEC);
    }

    public static class Client {
        public static void registerS2CPackets() {
            ClientPlayNetworking.registerGlobalReceiver(WayPayloads.UPDATE_PLAYER,
                    (payload, context) -> WayClientPacketHandler.onUpdatePlayer(payload));
            ClientPlayNetworking.registerGlobalReceiver(WayPayloads.CLEAR_PLAYERS,
                    (payload, context) -> WayClientPacketHandler.onClearPlayers());
            ClientPlayNetworking.registerGlobalReceiver(WayPayloads.UPDATE_NAMEPLATE_RENDER,
                    (payload, context) -> WayClientPacketHandler.onUpdateNameplateRender(payload));
        }
    }
}
