package ru.den_abr.glownpc.event;

import org.bukkit.event.entity.EntityDamageEvent;

import ru.den_abr.glownpc.GlowNPC;

public class NPCDamageEvent extends EntityDamageEvent {

    public NPCDamageEvent(GlowNPC damagee, DamageCause cause, double damage) {
        super(damagee, cause, damage);
    }
    
    @Override
    public GlowNPC getEntity() {
        return (GlowNPC) super.getEntity();
    }

}
