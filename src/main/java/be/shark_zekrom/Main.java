package be.shark_zekrom;

import com.onarandombox.MultiverseCore.MultiverseCore;
import fr.mrmicky.fastboard.FastBoard;
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

    public static int id, maxPlayers, minPlayers, points, countdown;


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

        pm.registerEvents(new Gui(), this);
        pm.registerEvents(new Events(), this);

        getServer().getScheduler().runTaskTimer(this, () -> {
            for (FastBoard board : GameManager.boards.values()) {
                GameManager.updateBoard(board);
            }
        }, 0, 20);


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

        maxPlayers = 9;
        minPlayers = 1;
        countdown = 10;
        points = 1;

        for (int i = 0; i < 2; i++) {
            new GameManager();
        }


    }

    @Override
    public void onDisable() {

    }
}