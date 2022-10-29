package be.shark_zekrom;

import com.onarandombox.MultiverseCore.MultiverseCore;
import fr.mrmicky.fastboard.FastBoard;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Arrays;

public class Main extends JavaPlugin {


    private static Main instance;

    public static Main getInstance() {
        return instance;
    }

    public static MultiverseCore core;

    public static Integer id = 1, maxPlayers, minPlayers, points, countdown, gamesAtTheSameTime;
    public static String worldToClone, worldPrefix;
    public static Location lobby;

    @Override
    public void onEnable() {
        instance = this;
        PluginManager pm = getServer().getPluginManager();

        core = (MultiverseCore) Bukkit.getServer().getPluginManager().getPlugin("Multiverse-Core");

       // if (core == null) {
         //   Bukkit.getLogger().severe("Multiverse-Core not found! Disabling plugin...");
        //}
        this.getCommand("tower+").setExecutor(new Commands());

        pm.registerEvents(new Gui(), this);
        pm.registerEvents(new Events(), this);

        getServer().getScheduler().runTaskTimer(this, () -> {
            for (FastBoard board : GameManager.boards.values()) {
                GameManager.updateBoard(board);
            }
        }, 0, 20);


        FileConfiguration config = getConfig();
        config.addDefault("worldToClone", "tower");
        config.addDefault("worldPrefix", "TowerPlus-");
        config.addDefault("pointsToWin", 10);
        config.addDefault("maxPlayer", 10);
        config.addDefault("minPlayers", 2);
        config.addDefault("countdown", 30);
        config.addDefault("gamesAtTheSameTime", 10);

        config.addDefault("location.spawn.red.x", 0);
        config.addDefault("location.spawn.red.y", 0);
        config.addDefault("location.spawn.red.z", 0);
        config.addDefault("location.spawn.red.yaw", 0);
        config.addDefault("location.spawn.red.pitch", 0);

        config.addDefault("location.spawn.blue.x", 0);
        config.addDefault("location.spawn.blue.y", 0);
        config.addDefault("location.spawn.blue.z", 0);
        config.addDefault("location.spawn.blue.yaw", 0);
        config.addDefault("location.spawn.blue.pitch", 0);

        config.addDefault("location.waiting-spawn.x", 0);
        config.addDefault("location.waiting-spawn.y", 0);
        config.addDefault("location.waiting-spawn.z", 0);
        config.addDefault("location.waiting-spawn.yaw", 0);
        config.addDefault("location.waiting-spawn.pitch", 0);

        config.addDefault("location.lobby.world", "world");
        config.addDefault("location.lobby.x", 0);
        config.addDefault("location.lobby.y", 0);
        config.addDefault("location.lobby.z", 0);
        config.addDefault("location.lobby.yaw", 0);
        config.addDefault("location.lobby.pitch", 0);



        config.options().copyDefaults(true);
        saveConfig();


        maxPlayers = config.getInt("maxPlayer");
        minPlayers = config.getInt("minPlayers");
        countdown = config.getInt("countdown");
        points = config.getInt("pointsToWin");
        gamesAtTheSameTime = config.getInt("gamesAtTheSameTime");
        worldPrefix = config.getString("worldPrefix");

        worldToClone = config.getString("worldToClone");

        lobby = new Location(Bukkit.getWorld(config.getString("location.lobby.world")), config.getDouble("location.lobby.x"),config.getDouble("location.lobby.y"),config.getDouble("location.lobby.z"), (float) config.getDouble("location.lobby.yaw"), (float) config.getDouble("location.lobby.pitch"));

        WorldManager.deleteAllWorld();

        for (int i = 0; i < gamesAtTheSameTime; i++) {
            new GameManager();
        }


    }

    @Override
    public void onDisable() {

    }
}