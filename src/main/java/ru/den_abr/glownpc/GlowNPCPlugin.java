package ru.den_abr.glownpc;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.NPC;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;

import net.glowstone.entity.EntityRegistry;
import net.glowstone.entity.GlowPlayer;
import net.glowstone.entity.meta.profile.PlayerProfile;
import net.glowstone.net.GlowSession;

public class GlowNPCPlugin extends JavaPlugin implements Listener {

    @Override
    public void onLoad() {
        getLogger().info("Registering custom NPC descriptor");
        EntityRegistry.registerCustomEntity(new NPCEntityDescriptor(this));
    }

    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(this, this);
    }

    @Override
    public void onDisable() {
        getServer().getWorlds().forEach(world -> world.getEntitiesByClass(NPC.class).forEach(e -> e.remove()));
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        getServer().getScheduler().runTaskLater(this, () -> {
            Player p = e.getPlayer();
            if (!p.isOnline())
                return;
            GlowPlayer player = (GlowPlayer) p;
            GlowSession ses = player.getSession();
            player.getWorld().getEntitiesByClass(GlowNPC.class)
                    .forEach(npc -> ses.send(npc.getRemoveFromListMessage()));
        }, 15);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        String name = args[0];
        GlowNPC npc = new GlowNPC(((Entity) sender).getLocation(), PlayerProfile.getProfile(name));
        npc.playAnimation(NPCAnimation.DAMAGE);
        npc.playAnimation(NPCAnimation.GLOWING);
        npc.playAnimation(NPCAnimation.SNEAK);
        return true;
    }
}
