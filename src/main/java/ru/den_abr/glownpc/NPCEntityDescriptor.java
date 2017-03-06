package ru.den_abr.glownpc;

import org.bukkit.plugin.Plugin;

import net.glowstone.entity.CustomEntityDescriptor;

public class NPCEntityDescriptor extends CustomEntityDescriptor<GlowNPC> {

    public NPCEntityDescriptor(Plugin plugin) {
        super(GlowNPC.class, plugin, "NPC", null);
    }

}
