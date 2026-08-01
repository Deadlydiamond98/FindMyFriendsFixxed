package net.deadlydiamond98.way.client;

import net.deadlydiamond98.way.Way;
import net.deadlydiamond98.way.common.events.WayTickingEvent;
import net.deadlydiamond98.way.common.networking.UpdateNameplateRenderPayload;
import net.deadlydiamond98.way.common.networking.UpdatePlayerPayload;
import net.deadlydiamond98.way.util.PlayerLocation;
import net.deadlydiamond98.way.util.mixin.IWayPlayer;
import net.minecraft.client.Minecraft;

public class WayClientPacketHandler {

    public static void onUpdatePlayer(UpdatePlayerPayload payload) {
        WayTickingEvent.PLAYER_POS.add(new PlayerLocation(
                payload.name(), payload.nametagY(),
                payload.x(), payload.y(), payload.z(),
                payload.eyeHeight(),
                payload.uuid(),
                payload.showPlayer(), payload.color(),
                payload.hurtTime(), payload.health(), payload.maxHealth(),
                payload.optedIn()
        ));
    }

    public static void onClearPlayers() {
        WayTickingEvent.PLAYER_POS.clear();
    }

    public static void onUpdateNameplateRender(UpdateNameplateRenderPayload payload) {
        if (Minecraft.getInstance().player instanceof IWayPlayer player) {
            player.way$setToggle(payload.toggle());
            player.way$setSeeName(payload.names());
            player.way$setSeeDist(payload.distance());
            player.way$setSeeColor(payload.colors());
            player.way$setSeeOutline(payload.outlines());
            player.way$setSeeHead(payload.head());
            player.way$setSeeHeadOutline(payload.headOutline());

            Way.colorDistance = payload.colorDistance();
            Way.namePainFlash = payload.namePainFlash();
            Way.namePainGetRedder = payload.namePainGetRedder();

            Way.minRender = payload.minRender();
            Way.maxRender = payload.maxRender();

            player.way$setBypassOpt(payload.bypassOpt());
        }
    }
}
