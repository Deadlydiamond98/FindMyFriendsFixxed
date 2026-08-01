package net.deadlydiamond98.way;

import net.deadlydiamond98.way.client.WayKeybindings;
import net.deadlydiamond98.way.client.renderer.WayNameplateRenderer;
import net.deadlydiamond98.way.networking.WayFabricNetworking;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;

public class WayClientFabric implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        ClientTickEvents.END_CLIENT_TICK.register(WayKeybindings::tickKeybinding);
        KeyBindingHelper.registerKeyBinding(WayKeybindings.TOGGLE_NAMEPLATE);
        WayFabricNetworking.Client.registerS2CPackets();
        WorldRenderEvents.LAST.register(context ->
                WayNameplateRenderer.render(context.matrixStack(), context.consumers(), context.world(),
                        context.tickCounter().getGameTimeDeltaPartialTick(false))
        );
    }
}
