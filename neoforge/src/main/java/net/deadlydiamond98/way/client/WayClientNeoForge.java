package net.deadlydiamond98.way.client;

import net.deadlydiamond98.way.Way;
import net.deadlydiamond98.way.client.renderer.WayNameplateRenderer;
import net.minecraft.client.Minecraft;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.event.InputEvent;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;
import net.neoforged.neoforge.client.event.RenderLevelStageEvent;

@Mod(value = Way.MOD_ID, dist = Dist.CLIENT)
public class WayClientNeoForge {

    public WayClientNeoForge(IEventBus modEventBus) {
        modEventBus.addListener(WayClientNeoForge::registerKeybindings);
    }

    private static void registerKeybindings(RegisterKeyMappingsEvent event) {
        event.register(WayKeybindings.TOGGLE_NAMEPLATE);
    }

    @EventBusSubscriber(modid = Way.MOD_ID, value = Dist.CLIENT)
    public static class ClientGameEvents {
        @SubscribeEvent
        public static void renderNameplate(RenderLevelStageEvent event) {
            if (event.getStage() != RenderLevelStageEvent.Stage.AFTER_TRANSLUCENT_BLOCKS) {
                return;
            }
            Minecraft client = Minecraft.getInstance();
            WayNameplateRenderer.render(event.getPoseStack(), client.renderBuffers().bufferSource(), client.level,
                    event.getPartialTick().getGameTimeDeltaPartialTick(false));
        }

        @SubscribeEvent
        public static void keybindingInputEvent(InputEvent.Key event) {
            WayKeybindings.tickKeybinding(Minecraft.getInstance());
        }
    }
}
