package ru.den_abr.glownpc;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.bukkit.EntityEffect;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.NPC;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.MainHand;
import org.bukkit.inventory.Merchant;
import org.bukkit.potion.PotionEffectType;

import com.flowpowered.network.Message;

import net.glowstone.entity.AttributeManager.Key;
import net.glowstone.entity.GlowHumanEntity;
import net.glowstone.entity.GlowPlayer;
import net.glowstone.entity.meta.MetadataIndex;
import net.glowstone.entity.meta.profile.PlayerProfile;
import net.glowstone.net.message.play.entity.AnimateEntityMessage;
import net.glowstone.net.message.play.game.UserListItemMessage;
import net.glowstone.net.message.play.player.InteractEntityMessage;
import ru.den_abr.glownpc.event.NPCDamageByEntityEvent;
import ru.den_abr.glownpc.event.NPCDamageEvent;
import ru.den_abr.glownpc.event.NPCInteractEvent;

public class GlowNPC extends GlowHumanEntity implements NPC {
    private LivingEntity target;
    private MainHand hand;
    private Set<Player> notified = Collections.newSetFromMap(new ConcurrentHashMap<>());
    private Map<NPCAnimation, Message> visualAnimations = new ConcurrentHashMap<>();

    public GlowNPC(Location location, PlayerProfile profile) {
        super(location, profile);
        this.hand = MainHand.LEFT;
    }

    @Override
    public LivingEntity getTarget() {
        return target;
    }

    @Override
    public EntityType getType() {
        return EntityType.PLAYER;
    }

    @Override
    public void setTarget(LivingEntity target) {
        this.target = target;
    }

    @Override
    public MainHand getMainHand() {
        return hand;
    }

    public void playAnimation(NPCAnimation anim) {
        if (anim.usePacket()) {
            visualAnimations.put(anim, new AnimateEntityMessage(getEntityId(), anim.getAnimationId()));
        } else {
            metadata.setBit(MetadataIndex.STATUS, anim.getAnimationId(), true);
        }
    }

    public void stopAnimation(NPCAnimation anim) {
        if (anim.usePacket()) {
            visualAnimations.remove(anim);
        } else {
            metadata.setBit(MetadataIndex.STATUS, anim.getAnimationId(), false);
        }
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
    public boolean entityInteract(GlowPlayer player, InteractEntityMessage message) {
        return new NPCInteractEvent(this, player, player.isSneaking()).callEvent()
                ? super.entityInteract(player, message) : false;
    }

    @Override
    public boolean shouldSave() {
        return false;
    }

    @Override
    public void damage(double amount, Entity source, DamageCause cause) {
        // fire resistance
        if (cause != null && hasPotionEffect(PotionEffectType.FIRE_RESISTANCE)) {
            switch (cause) {
            case PROJECTILE:
                if (!(source instanceof Fireball)) {
                    break;
                }
            case FIRE:
            case FIRE_TICK:
            case LAVA:
                return;
            }
        }

        // armor damage protection
        // formula source:
        // http://minecraft.gamepedia.com/Armor#Damage_Protection
        double defensePoints = getAttributeManager().getPropertyValue(Key.KEY_ARMOR);
        double toughness = getAttributeManager().getPropertyValue(Key.KEY_ARMOR_TOUGHNESS);
        amount = amount * (1
                - Math.min(20.0, Math.max(defensePoints / 5.0, defensePoints - amount / (2.0 + toughness / 4.0))) / 25);

        // fire event
        NPCDamageEvent event;
        if (source == null) {
            event = new NPCDamageEvent(this, cause, amount);
        } else {
            event = new NPCDamageByEntityEvent(this, cause, amount, source);
        }
        if (!event.callEvent()) {
            return;
        }

        // apply damage
        amount = event.getFinalDamage();
        setLastDamage(amount);
        setHealth(health - amount);
        playEffect(EntityEffect.HURT);

        // play sounds, handle death
        if (health > 0) {
            Sound hurtSound = getHurtSound();
            if (hurtSound != null && !isSilent()) {
                world.playSound(location, hurtSound, getSoundVolume(), getSoundPitch());
            }
        }
        setLastDamager(source);
    }

    public Message getRemoveFromListMessage() {
        return UserListItemMessage.removeOne(getProfile().getUniqueId());
    }

    @Override
    public List<Message> createUpdateMessage() {
        List<Message> messages = new LinkedList<>(super.createUpdateMessage());
        //messages.add(getRemoveFromListMessage());
        messages.addAll(visualAnimations.values());
        return messages;
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
