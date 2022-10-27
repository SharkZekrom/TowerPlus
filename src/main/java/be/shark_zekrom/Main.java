package be.shark_zekrom;

import com.onarandombox.MultiverseCore.MultiverseCore;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {


    private static Main instance;

    public static Main getInstance() {
        return instance;
    }

    public static MultiverseCore core;

    @Override
    public void onEnable() {
        instance = this;
        PluginManager pm = getServer().getPluginManager();

        core = (MultiverseCore) Bukkit.getServer().getPluginManager().getPlugin("Multiverse-Core");

        if (core == null) {
            Bukkit.getLogger().severe("Multiverse-Core not found! Disabling plugin...");
        }
        WorldManager.deleteAllWorld();

        this.getCommand("tower+").setExecutor(new Commands());



        FileConfiguration config = getConfig();
        config.addDefault("lobby", "world");
        config.addDefault("worldToClone", "tower");

        config.addDefault("spawn.red", new int[]{0, 0, 0, 0, 0});
        config.addDefault("spawn.blue", new int[]{0, 0, 0, 0, 0});
        config.addDefault("waitingSpawn", new int[]{0, 0, 0, 0, 0});

        config.addDefault("pointsToWin", 10);
        config.addDefault("maxPlayerPerTeam", 10);
        config.addDefault("minPlayersToStart", 2);


        config.options().copyDefaults(true);
        saveConfig();


        new GameManager(1, 0, 0, GameManager.GameStatus.WAITING);

    }

    @Override
    public void onDisable() {

    }
}