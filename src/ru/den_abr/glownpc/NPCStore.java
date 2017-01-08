package ru.den_abr.glownpc;

import org.bukkit.Location;

import net.glowstone.entity.meta.profile.PlayerProfile;
import net.glowstone.io.entity.EntityStore;
import net.glowstone.util.nbt.CompoundTag;

public class NPCStore extends EntityStore<GlowNPC> {

    public NPCStore() {
        super(GlowNPC.class, "NPC");
    }

    @Override
    public GlowNPC createEntity(Location location, CompoundTag tag) {
        return new GlowNPC(location, PlayerProfile.fromNBT(tag.getCompound("profile")));
    }

    @Override
    public void load(GlowNPC entity, CompoundTag compound) {
        super.load(entity, compound);
    }

    @Override
    public void save(GlowNPC entity, CompoundTag tag) {
        super.save(entity, tag);
        tag.putCompound("profile", entity.getProfile().toNBT());
    }

}
