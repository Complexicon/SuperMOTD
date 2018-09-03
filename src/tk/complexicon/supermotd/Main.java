package tk.complexicon.supermotd;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.ServerListPingEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.logging.Logger;

public class Main extends JavaPlugin implements Listener {

    Logger l = getLogger();
    FileConfiguration cfg = getConfig();

    String heading;
    List<String> motd;

    @Override
    public void onEnable() {
        l.info("Registering Events...");
        Bukkit.getPluginManager().registerEvents(this, this);

        l.info("Loading Config...");
        cfg.addDefault("heading", "&cSuperMOTD Default Heading");
        cfg.addDefault("motdlist", Arrays.asList("&aMOTD Random Line1", "&aMOTD Random Line2", "&aMOTD Random Line3"));
        cfg.options().copyDefaults(true);
        saveConfig();
        heading = cfg.getString("heading").replaceAll("&", "§");
        motd = cfg.getStringList("motdlist");

        Bukkit.getConsoleSender().sendMessage("Setting MOTD Heading to: " + heading);

        l.info("Loaded!");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if(cmd.getName().equalsIgnoreCase("reloadmotd")){
            if(sender.hasPermission("supermotd.reloadmotd")){
                sender.sendMessage(ChatColor.GREEN + "Reloading MOTD Config...");
                reloadConfig();
                cfg = getConfig();
                heading = cfg.getString("heading").replaceAll("&", "§");
                motd = cfg.getStringList("motdlist");
                sender.sendMessage("New MOTD Heading: " + heading);
                return true;
            }
        }
        return false;
    }

    @EventHandler
    public void onServerPing(ServerListPingEvent e){
        Random r = new Random();
        String randString = motd.get(r.nextInt(motd.size())).replaceAll("&", "§");
        e.setMotd(heading + "\n" + randString);
    }

}
