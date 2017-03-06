package ru.den_abr.glownpc.event;

import org.bukkit.entity.Player;

import ru.den_abr.glownpc.GlowNPC;

public class NPCInteractEvent extends FCancellable {
    private GlowNPC npc;
    private Player player;
    private boolean shiftClick;

    public NPCInteractEvent(GlowNPC npc, Player player, boolean shift) {
        this.npc = npc;
        this.player = player;
        this.shiftClick = shift;
    }

    public Player getPlayer() {
        return player;
    }

    public GlowNPC getNPC() {
        return npc;
    }

    public boolean isShiftClick() {
        return shiftClick;
    }

}
