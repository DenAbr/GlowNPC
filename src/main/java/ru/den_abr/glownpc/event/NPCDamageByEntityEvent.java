package ru.den_abr.glownpc.event;

import org.bukkit.entity.Entity;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

import ru.den_abr.glownpc.GlowNPC;

public class NPCDamageByEntityEvent extends NPCDamageEvent {
    private Entity damager;

    public NPCDamageByEntityEvent(GlowNPC npc, DamageCause cause, double damage, Entity damager) {
        super(npc, cause, damage);
        this.damager = damager;
    }

    public Entity getDamager() {
        return damager;
    }

}
