package ru.den_abr.glownpc;

import java.util.LinkedList;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.NPC;
import org.bukkit.entity.Villager;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.MainHand;
import org.bukkit.inventory.Merchant;

import com.flowpowered.network.Message;

import net.glowstone.entity.GlowHumanEntity;
import net.glowstone.entity.meta.profile.PlayerProfile;
import net.glowstone.net.message.play.game.UserListItemMessage;

public class GlowNPC extends GlowHumanEntity implements NPC {

    public GlowNPC(Location location, PlayerProfile profile) {
        super(location, profile);
    }

    @Override
    public LivingEntity getTarget() {
        return null;
    }

    @Override
    public void setTarget(LivingEntity arg0) {

    }

    @Override
    public MainHand getMainHand() {
        return MainHand.RIGHT;
    }

    @Override
    public boolean isHandRaised() {
        return false;
    }

    @Override
    public InventoryView openMerchant(Villager arg0, boolean arg1) {
        return null;
    }

    @Override
    public InventoryView openMerchant(Merchant arg0, boolean arg1) {
        return null;
    }

    @Override
    public List<Message> createSpawnMessage() {
        final List<Message> list = new LinkedList<>();
        
        // required packet to show entity on client side
        list.add(UserListItemMessage.addOne(getProfile()));
        list.addAll(super.createSpawnMessage());
        return list;
    }
}
