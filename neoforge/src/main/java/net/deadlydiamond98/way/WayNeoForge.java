package net.deadlydiamond98.way;

import net.deadlydiamond98.way.common.command.WayServerCommands;
import net.deadlydiamond98.way.common.events.WayRespawnEvent;
import net.deadlydiamond98.way.common.events.WayTickingEvent;
import net.deadlydiamond98.way.networking.WayNeoForgeNetworking;
import net.deadlydiamond98.way.platform.NeoForgePlatformHelper;
import net.deadlydiamond98.way.util.mixin.IWayPlayer;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.event.RegisterCommandsEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.tick.ServerTickEvent;

@Mod(Way.MOD_ID)
public class WayNeoForge {

    public WayNeoForge(IEventBus modEventBus) {
        Way.init();
        NeoForgePlatformHelper.registerArgTypes(modEventBus);
        modEventBus.addListener(WayNeoForgeNetworking::register);
    }

    @EventBusSubscriber(modid = Way.MOD_ID)
    public static class TickEvents {
        @SubscribeEvent
        public static void tickEvents(ServerTickEvent.Post event) {
            WayTickingEvent.tick(event.getServer());
        }

        @SubscribeEvent
        public static void respawnEvents(PlayerEvent.Clone event) {
            WayRespawnEvent.respawn(event.getOriginal(), event.getEntity(), event.isWasDeath());
        }

        @SubscribeEvent
        public static void registerCommands(RegisterCommandsEvent event) {
            WayServerCommands.register(event.getDispatcher(), event.getBuildContext(), event.getCommandSelection());
        }

        @SubscribeEvent
        public static void onPlayerJoin(PlayerEvent.PlayerLoggedInEvent event) {
            ((IWayPlayer) event.getEntity()).way$updateRenderPreferences();
        }

        @SubscribeEvent
        public static void onPlayerChangeDimensions(PlayerEvent.PlayerChangedDimensionEvent event) {
            ((IWayPlayer) event.getEntity()).way$updateRenderPreferences();
        }
    }
}
