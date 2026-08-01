package net.deadlydiamond98.way.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.deadlydiamond98.way.Way;
import net.deadlydiamond98.way.client.WayKeybindings;
import net.deadlydiamond98.way.common.events.WayTickingEvent;
import net.deadlydiamond98.way.util.ColorUtil;
import net.deadlydiamond98.way.util.FaceRenderingUtil;
import net.deadlydiamond98.way.util.PlayerLocation;
import net.deadlydiamond98.way.util.WayEntityUtil;
import net.deadlydiamond98.way.util.mixin.IGlowingWayPlayer;
import net.deadlydiamond98.way.util.mixin.IWayPlayer;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.client.multiplayer.PlayerInfo;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.UUID;

public class WayNameplateRenderer {

    // This class is an absolute mess, but that's a problem for future me

    // Future me here, what form of crack was I on?!?!?!? This is now cleaned up

    private static final ResourceLocation FIX_RENDERING_THING = ResourceLocation.fromNamespaceAndPath(Way.MOD_ID, "textures/plz_work.png");
    private static final float[] UV = {0.125f, 0.25f};
    private static final int NAMETAG_RENDER_CUTTOFF = 100;

    public static void render(PoseStack poseStack, MultiBufferSource bufferSource, ClientLevel level, float renderTick) {

        if (WayKeybindings.renderNameOverlay()) {

            Minecraft client = Minecraft.getInstance();
            LocalPlayer user = client.player;

            if (user instanceof IWayPlayer wayPlayer) {

                level.players().forEach(player -> ((IGlowingWayPlayer) player).way$setGlowRendering(false));

                // Done like this to prevent crash due to list being updating via a packet on fabric
                new ArrayList<>(WayTickingEvent.PLAYER_POS).forEach(playerData -> {
                    if (playerData.getEyePosition().distanceTo(user.getEyePosition(renderTick)) > NAMETAG_RENDER_CUTTOFF) {
                        Vec3 pos = playerData.getPosition().add(0, playerData.nametagY, 0);
                        renderNameplate(poseStack, bufferSource, renderTick, wayPlayer, playerData, pos, null);

                    } else {
                        level.players().forEach(player -> {
                            boolean thirdPerson = user.equals(player) && !client.getEntityRenderDispatcher().camera.isDetached();
                            boolean invis = player.isDiscrete() || player.isInvisible();
                            boolean isSame = playerData.name.getString().equals(player.getName().getString());

                            if (isSame && !thirdPerson && (!invis || wayPlayer.way$bypassOpt()) && player.distanceTo(user) <= NAMETAG_RENDER_CUTTOFF) {
                                Vec3 pos = player.getPosition(renderTick).add(0, WayEntityUtil.nameTagOffsetY(player), 0);
                                renderNameplate(poseStack, bufferSource, renderTick, wayPlayer, playerData, pos, player);

                                if (((IWayPlayer) user).way$canSeeOutline()) {
                                    ((IGlowingWayPlayer) player).way$setOutlineColor(playerData.hex);
                                    ((IGlowingWayPlayer) player).way$setGlowRendering(true);
                                } else {
                                    ((IGlowingWayPlayer) player).way$setGlowRendering(false);
                                }
                            }
                        });
                    }
                });
            }
        }
    }

    private static void renderNameplate(PoseStack poseStack, MultiBufferSource bufferSource, float renderTick, IWayPlayer viewer, PlayerLocation data, Vec3 pos, @Nullable Player player) {
        if (data.isOptedIn || viewer.way$bypassOpt()) {
            poseStack.pushPose();

            int distance = getDistance(poseStack, renderTick, pos);
            Component dist = Component.translatable("gui.way.distance", distance);

            float mainScale = 0.0625f;
            float nameScale = 0.5f * mainScale;
            float distScale = 0.25f * mainScale;

            if (viewer.way$canSeeName()) {

                // This is to avoid an iris bug
                if (Way.hasIris()) {
                    renderText(poseStack, bufferSource, data.name, 0, 0, 0, data.nametagY, nameScale, getNameHex(data, viewer, distance, player));
                    renderBackplate(poseStack, bufferSource, data.name, -10.5f, 0, nameScale);
                } else {
                    renderBackplate(poseStack, bufferSource, data.name, -10.5f, 0, nameScale);
                    renderText(poseStack, bufferSource, data.name, 0, 0, 0, data.nametagY, nameScale, getNameHex(data, viewer, distance, player));
                }
            }

            if (viewer.way$canSeeDist()) {
                int distHex = viewer.way$canSeeColor() ? getNameHex(data, viewer, distance, player) : 0x55FFFF;
                distHex = viewer.way$canSeeName() ? 0xFFFFFF : distHex;

                // This is to avoid an iris bug
                if (Way.hasIris()) {
                    renderText(poseStack, bufferSource, dist, 0, 0, 0, data.nametagY + (viewer.way$canSeeName() ? -7.5f : 2), distScale, distHex);
                    renderBackplate(poseStack, bufferSource, dist, viewer.way$canSeeName() ? -2.75f : -12, 0, distScale);
                } else {
                    renderBackplate(poseStack, bufferSource, dist, viewer.way$canSeeName() ? -2.75f : -12, 0, distScale);
                    renderText(poseStack, bufferSource, dist, 0, 0, 0, data.nametagY + (viewer.way$canSeeName() ? -7.5f : 2), distScale, distHex);
                }
            }

            if (viewer.way$canSeeHead()) {
                float yOffset = -0.2f;
                if (viewer.way$canSeeDist()) {
                    yOffset += 0.25f;
                }
                if (viewer.way$canSeeName()) {
                    yOffset += 0.35f;
                }
                int headHex = viewer.way$canSeeName() || viewer.way$canSeeDist() ? data.hex : getNameHex(data, viewer, distance, player);
                renderPlayerIcon(poseStack, bufferSource, data.uuid, headHex, yOffset, 0.45f, viewer);
            }

            poseStack.popPose();
        }
    }

    private static int getNameHex(PlayerLocation data, IWayPlayer viewer, int distance, @Nullable Player player) {
        int nameHex = viewer.way$canSeeColor() ? data.hex : 0xFFFFFFFF;

        if (Way.colorDistance) {
            nameHex = ColorUtil.blendHexColors(nameHex, 0xFFFFFFFF, (distance - Way.minRender) / (float) Math.max(1, Way.maxRender));
        }

        if (player != null && Way.namePainFlash) {
            nameHex = ColorUtil.blendHexColors(nameHex, 0xFFFF0000, player.hurtTime / 10.0f);
        }

        if (Way.namePainGetRedder) {
            nameHex = ColorUtil.blendHexColors(0xFFFF0000, nameHex, data.health / data.maxHealth);
        }

        return nameHex | 0xFF000000;
    }

    private static void renderText(PoseStack poseStack, MultiBufferSource bufferSource, Component name, float x, float y, float z, float yOffset, float scale, int color) {
        Minecraft client = Minecraft.getInstance();
        Camera camera = client.getEntityRenderDispatcher().camera;
        Font font = client.font;

        poseStack.pushPose();
        poseStack.mulPose(Axis.ZP.rotationDegrees(180));
        poseStack.mulPose(Axis.YP.rotationDegrees(camera.getYRot()));
        poseStack.mulPose(Axis.XN.rotationDegrees(camera.getXRot()));
        poseStack.translate(x, y, z);
        poseStack.scale(scale, scale, scale);

        font.drawInBatch(name, (float) (-font.width(name) / 2), yOffset + 0.1f, color, false, poseStack.last().pose(), bufferSource, Font.DisplayMode.SEE_THROUGH, 0, LightTexture.FULL_BRIGHT);

        poseStack.popPose();
    }

    // Backplate thing is created because text has weird layering issues with their background
    private static void renderBackplate(PoseStack poseStack, MultiBufferSource bufferSource, Component text, float y, float z, float scale) {
        Font font = Minecraft.getInstance().font;
        float width = font.width(text) + 2;
        int rgb = 50;
        int alpha = 120;
        VertexConsumer vConsumer = bufferSource.getBuffer(RenderType.textBackground());
        renderBillboardingFace(poseStack, vConsumer, -width / 2.0f, y, z, width, font.lineHeight, alpha, rgb, rgb, rgb, scale);
    }

    private static void renderPlayerIcon(PoseStack poseStack, MultiBufferSource bufferSource, UUID uuid, int hex, float y, float scale, IWayPlayer viewer) {
        float sizeOffset = viewer.way$canSeeHeadOutline() ? 0.1f : 0;

        float x = -0.5f;
        float bgWidthHeight = 1 + (sizeOffset * 2);

        // This is to avoid an iris bug
        if (Way.hasIris()) {
            renderHeadOutline(poseStack, bufferSource, hex, x - sizeOffset, y - sizeOffset, bgWidthHeight, scale, viewer);
            renderHead(poseStack, bufferSource, uuid, x, y, scale);
            renderThingInBackToFixLayering(poseStack, bufferSource, x - sizeOffset, y - sizeOffset, bgWidthHeight, scale);
        } else {
            renderThingInBackToFixLayering(poseStack, bufferSource, x - sizeOffset, y - sizeOffset, bgWidthHeight, scale);
            renderHeadOutline(poseStack, bufferSource, hex, x - sizeOffset, y - sizeOffset, bgWidthHeight, scale, viewer);
            renderHead(poseStack, bufferSource, uuid, x, y, scale);
        }
    }

    private static void renderThingInBackToFixLayering(PoseStack poseStack, MultiBufferSource bufferSource, float x, float y, float wh, float scale) {
        VertexConsumer vCon = bufferSource.getBuffer(RenderType.entityCutoutNoCull(FIX_RENDERING_THING));
        renderBillboardingFace(poseStack, vCon, x, y, 0.002f, wh, wh, 0x03FFFFFF, scale);
    }

    private static void renderHeadOutline(PoseStack poseStack, MultiBufferSource bufferSource, int hex, float x, float y, float wh, float scale, IWayPlayer viewer) {
        if (viewer.way$canSeeHeadOutline()) {
            VertexConsumer vConBG = bufferSource.getBuffer(RenderType.textBackgroundSeeThrough());
            renderBillboardingFace(poseStack, vConBG, x, y, 0.001f, wh, wh, hex, scale);
        }
    }

    private static void renderHead(PoseStack poseStack, MultiBufferSource bufferSource, UUID uuid, float x, float y, float scale) {
        ClientPacketListener connection = Minecraft.getInstance().getConnection();

        if (connection != null) {
            PlayerInfo info = connection.getPlayerInfo(uuid);
            if (info != null) {
                VertexConsumer vConSkin = bufferSource.getBuffer(RenderType.textSeeThrough(info.getSkin().texture()));
                renderBillboardingFace(poseStack, vConSkin, x, y, 0, 1, 1, 255, 255, 255, 255, scale);
            }
        }
    }

    private static int getDistance(PoseStack poseStack, float renderTick, Vec3 tagPos) {
        Minecraft client = Minecraft.getInstance();
        Camera camera = client.getEntityRenderDispatcher().camera;

        Vec3 cameraPos = camera.getPosition();
        poseStack.translate(-cameraPos.x(), -cameraPos.y(), -cameraPos.z());

        Vec3 playerPos = client.player.getPosition(renderTick).add(0, 1.53, 0);

        Vec3 direction = playerPos.subtract(tagPos).normalize();
        double currentDistance = playerPos.distanceTo(tagPos);

        int maxOffsetDistance = 10;
        if (currentDistance >= maxOffsetDistance) {
            tagPos = tagPos.add(direction.scale(currentDistance - maxOffsetDistance));
        }

        poseStack.translate(tagPos.x, tagPos.y, tagPos.z);

        return (int) Math.floor(currentDistance);
    }

    /// /////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    private static void renderBillboardingFace(PoseStack poseStack, VertexConsumer vCon, float x, float y, float z, float width, float height, int hex, float scale) {
        FaceRenderingUtil.renderBillboardingFace(poseStack, vCon, x, y, z, UV[0], UV[1], UV[0], UV[1], width, height, hex, scale);
    }

    private static void renderBillboardingFace(PoseStack poseStack, VertexConsumer vCon, float x, float y, float z, float width, float height, int a, int r, int g, int b, float scale) {
        FaceRenderingUtil.renderBillboardingFace(poseStack, vCon, x, y, z, UV[0], UV[1], UV[0], UV[1], width, height, new int[]{a, r, g, b}, scale);
    }
}
