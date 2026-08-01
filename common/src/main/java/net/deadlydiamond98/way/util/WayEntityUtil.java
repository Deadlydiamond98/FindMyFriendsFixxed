package net.deadlydiamond98.way.util;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityAttachment;
import net.minecraft.world.phys.Vec3;

public class WayEntityUtil {

    public static float nameTagOffsetY(Entity entity) {
        Vec3 attachment = entity.getAttachments().getNullable(EntityAttachment.NAME_TAG, 0, entity.getYRot());
        return attachment == null ? entity.getBbHeight() + 0.5f : (float) (attachment.y + 0.5);
    }
}
