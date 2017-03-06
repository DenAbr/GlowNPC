package ru.den_abr.glownpc;

import net.glowstone.entity.meta.MetadataIndex.StatusFlags;
import net.glowstone.net.message.play.entity.AnimateEntityMessage;

public enum NPCAnimation {
    SWING_MAIN_ARM(AnimateEntityMessage.SWING_MAIN_HAND, true), SWING_OFF_HAND(AnimateEntityMessage.SWING_OFF_HAND,
            true), DAMAGE(AnimateEntityMessage.TAKE_DAMAGE, true), CRIT(AnimateEntityMessage.CRIT, true), MAGIC_CRIT(
                    AnimateEntityMessage.MAGIC_CRIT,
                    true), SNEAK(StatusFlags.SNEAKING, false), GLIDING(StatusFlags.GLIDING,
                            false), GLOWING(StatusFlags.GLOWING, false), ARM_UP(StatusFlags.ARM_UP, false);

    private int animId;
    private boolean usePacket;

    private NPCAnimation(int id, boolean usePacket) {
        this.animId = id;
        this.usePacket = usePacket;
    }

    public int getAnimationId() {
        return animId;
    }

    public boolean usePacket() {
        return usePacket;
    }
}
