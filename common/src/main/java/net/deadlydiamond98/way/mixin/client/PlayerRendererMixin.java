package net.deadlydiamond98.way.mixin.client;

import com.mojang.blaze3d.vertex.PoseStack;
import net.deadlydiamond98.way.client.WayKeybindings;
import net.deadlydiamond98.way.common.events.WayTickingEvent;
import net.deadlydiamond98.way.util.PlayerLocation;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerRenderer.class)
public abstract class PlayerRendererMixin {

    @Inject(method = "renderNameTag(Lnet/minecraft/client/player/AbstractClientPlayer;Lnet/minecraft/network/chat/Component;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;IF)V", at = @At("HEAD"), cancellable = true)
    private void way$renderNameTag(AbstractClientPlayer $$0, Component $$1, PoseStack $$2, MultiBufferSource $$3, int $$4, float $$5, CallbackInfo ci) {
        if (WayKeybindings.renderNameOverlay() && way$canSeePlayer($$0)) {
            ci.cancel();
        }
    }

    @Unique
    private boolean way$canSeePlayer(AbstractClientPlayer player) {
        for (PlayerLocation playerData : WayTickingEvent.PLAYER_POS) {
            if (playerData.name.getString().equals(player.getName().getString())) {
                return true;
            }
        }
        return false;
    }
}
