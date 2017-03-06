package ru.den_abr.glownpc.event;

import ru.den_abr.glownpc.GlowNPC;

public class NPCSpawnedEvent extends FEvent {
    private GlowNPC npc;

    public NPCSpawnedEvent(GlowNPC npc) {
        this.npc = npc;
    }

    public GlowNPC getNPC() {
        return npc;
    }
}
