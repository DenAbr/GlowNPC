package ru.den_abr.glownpc;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import net.glowstone.GlowWorld;
import net.glowstone.entity.EntityRegistry;
import net.glowstone.entity.meta.profile.PlayerProfile;
import net.glowstone.net.message.play.game.UserListItemMessage;

public class GlowNPCPlugin extends JavaPlugin {

    @Override
    public void onEnable() {
        EntityRegistry.registerCustomEntity(new NPCEntityDescriptor(this));
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0) {
            sender.sendMessage("Specify NPC name");
            return true;
        }
        final Player player = (Player) sender;
        String name = args[0];
        PlayerProfile profile = PlayerProfile.getProfile(name);
        GlowNPC npc = new GlowNPC(player.getLocation(), profile);
        // wait when client loads skin and cape and send packet to remove name
        // from player list
        getServer().getScheduler().runTaskLater(this, () -> {
            GlowWorld world = (GlowWorld) player.getWorld();
            UserListItemMessage removeMessage = UserListItemMessage.removeOne(npc.getUniqueId());
            world.getRawPlayers().forEach(rawPlayer -> rawPlayer.getSession().send(removeMessage));
        }, 20L);
        player.sendMessage("NPC " + profile.getName() + " spawned. ID=" + npc.getEntityId());
        return true;
    }
}
