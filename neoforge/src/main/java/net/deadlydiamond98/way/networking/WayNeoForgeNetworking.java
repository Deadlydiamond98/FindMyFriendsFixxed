package net.deadlydiamond98.way.networking;

import net.deadlydiamond98.way.client.WayClientPacketHandler;
import net.deadlydiamond98.way.common.networking.ClearPlayersPayload;
import net.deadlydiamond98.way.common.networking.UpdateNameplateRenderPayload;
import net.deadlydiamond98.way.common.networking.UpdatePlayerPayload;
import net.deadlydiamond98.way.common.networking.WayPayloads;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

public class WayNeoForgeNetworking {

    public static void register(RegisterPayloadHandlersEvent event) {
        PayloadRegistrar registrar = event.registrar("1.0");

        registrar.playToClient(WayPayloads.UPDATE_PLAYER, UpdatePlayerPayload.CODEC,
                (payload, context) -> WayClientPacketHandler.onUpdatePlayer(payload));
        registrar.playToClient(WayPayloads.CLEAR_PLAYERS, ClearPlayersPayload.CODEC,
                (payload, context) -> WayClientPacketHandler.onClearPlayers());
        registrar.playToClient(WayPayloads.UPDATE_NAMEPLATE_RENDER, UpdateNameplateRenderPayload.CODEC,
                (payload, context) -> WayClientPacketHandler.onUpdateNameplateRender(payload));
    }
}
