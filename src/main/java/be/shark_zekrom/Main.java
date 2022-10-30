package be.shark_zekrom;

import be.shark_zekrom.database.Database;
import be.shark_zekrom.database.SQLite;
import com.onarandombox.MultiverseCore.MultiverseCore;
import fr.mrmicky.fastboard.FastBoard;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.Arrays;

public class Main extends JavaPlugin {


    private static Main instance;

    public static Main getInstance() {
        return instance;
    }

    public static MultiverseCore core;

    public static Integer id = 1, maxPlayersPerTeam, minPlayersToStart, points, countdown, gamesAtTheSameTime;
    public static String worldToClone, worldPrefix, inventory_game_name, inventory_game_id, inventory_team_name, inventory_team_red, inventory_team_blue, inventory_team_random;
    public static Location lobby;
    public static ArrayList<String> inventory_waiting, inventory_starting, inventory_ingame, scoreboard_waiting, scoreboard_starting, scoreboard_ingame, scoreboard_ending;

    private Database db;

    public Database getRDatabase() {
        return this.db;
    }

    @Override
    public void onEnable() {
        instance = this;
        PluginManager pm = getServer().getPluginManager();
        this.db = new SQLite(this);
        this.db.load();

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
        config.addDefault("maxPlayerPerTeam", 5);
        config.addDefault("minPlayersToStart", 2);
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

        config.addDefault("location.pool.red.1.x", 0);
        config.addDefault("location.pool.red.1.y", 0);
        config.addDefault("location.pool.red.1.z", 0);
        config.addDefault("location.pool.red.2.x", 0);
        config.addDefault("location.pool.red.2.y", 0);
        config.addDefault("location.pool.red.2.z", 0);

        config.addDefault("location.pool.blue.1.x", 0);
        config.addDefault("location.pool.blue.1.y", 0);
        config.addDefault("location.pool.blue.1.z", 0);
        config.addDefault("location.pool.blue.2.x", 0);
        config.addDefault("location.pool.blue.2.y", 0);
        config.addDefault("location.pool.blue.2.z", 0);

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

        config.addDefault("location.spectator.x", 0);
        config.addDefault("location.spectator.y", 0);
        config.addDefault("location.spectator.z", 0);
        config.addDefault("location.spectator.yaw", 0);
        config.addDefault("location.spectator.pitch", 0);

        config.addDefault("inventory.waiting", Arrays.asList("","§6Status §7» %status%", "§6Players §7» %players%", "§6Max players §7» %max_players%","","§eClick to join"));
        config.addDefault("inventory.starting", Arrays.asList("","§6Status §7» %status%", "§6Players §7» %players%", "§6Max players §7» %max_players%", "", "§eStarting in %countdown%","","§eClick to join"));
        config.addDefault("inventory.ingame", Arrays.asList("","§6Status §7» %status%", "§6Players §7» %players%", "", "§6Red team §7» %red_points%", "§6Blue team » %blue_points%","§6Time §7» §e%time%","","§6Click to join in spectator"));
        config.addDefault("inventory.game.name", "TowerPlus");
        config.addDefault("inventory.game_id", "§eGame %id%");
        config.addDefault("inventory.team.name", "Teams");
        config.addDefault("inventory.team.red", "Red");
        config.addDefault("inventory.team.blue", "Blue");
        config.addDefault("inventory.team.random", "Random");
        config.addDefault("message.team_change_blue","§aYou joined the blue team!");
        config.addDefault("message.team_change_red","§aYou joined the red team!");
        config.addDefault("message.team_change_random","§aYou joined a random team!");
        config.addDefault("message.team_change_full","§cThis team is full!");

        config.addDefault("scoreboard.title", "TowerPlus");
        config.addDefault("scoreboard.waiting", Arrays.asList("Waiting for players","ID > TowerPlus-%id%","players: %players%/%max_players%"));
        config.addDefault("scoreboard.starting", Arrays.asList("Starting in %countdown%","ID > TowerPlus-%id%", "players: %players%/%max_players%"));
        config.addDefault("scoreboard.ingame", Arrays.asList("Red points: %red_points%", "Blue points: %blue_points%","players: %players%", "Time: %time%"));
        config.addDefault("scoreboard.endgame", Arrays.asList("","Finish",""));

        config.addDefault("message.leave_message", "§a%player% §7leave the game %players%/%max_players%");
        config.addDefault("message.join_message", "§a%player% §7join the game %players%/%max_players%");
        config.addDefault("message.start_message", "§aThe game start in %countdown%");
        config.addDefault("message.full_game", "§cThe game is full");
        config.addDefault("message.more_players", "§cNot enough players to start the game");
        config.addDefault("message.kill_message", "§a%player% §7kill §a%killed%");
        config.addDefault("message.death_void_message", "§a%player% §7fall in the void");
        config.addDefault("message.death_fall_message", "§a%player% §7fall");

        config.options().copyDefaults(true);
        saveConfig();


        maxPlayersPerTeam = config.getInt("maxPlayerPerTeam");
        minPlayersToStart = config.getInt("minPlayersToStart");
        countdown = config.getInt("countdown");
        points = config.getInt("pointsToWin");
        gamesAtTheSameTime = config.getInt("gamesAtTheSameTime");
        worldPrefix = config.getString("worldPrefix");
        inventory_game_name = config.getString("inventory.game.name");
        inventory_game_id = config.getString("inventory.game_id");
        inventory_team_name = config.getString("inventory.team.name");
        inventory_team_red = config.getString("inventory.team.red");
        inventory_team_blue = config.getString("inventory.team.blue");
        inventory_team_random = config.getString("inventory.team.random");

        worldToClone = config.getString("worldToClone");

        lobby = new Location(Bukkit.getWorld(config.getString("location.lobby.world")), config.getDouble("location.lobby.x"),config.getDouble("location.lobby.y"),config.getDouble("location.lobby.z"), (float) config.getDouble("location.lobby.yaw"), (float) config.getDouble("location.lobby.pitch"));

        inventory_waiting = (ArrayList<String>) config.getStringList("inventory.waiting");
        inventory_starting = (ArrayList<String>) config.getStringList("inventory.starting");
        inventory_ingame = (ArrayList<String>) config.getStringList("inventory.ingame");

        scoreboard_waiting = (ArrayList<String>) config.getStringList("scoreboard.waiting");
        scoreboard_starting = (ArrayList<String>) config.getStringList("scoreboard.starting");
        scoreboard_ingame = (ArrayList<String>) config.getStringList("scoreboard.ingame");
        scoreboard_ending = (ArrayList<String>) config.getStringList("scoreboard.endgame");


        WorldManager.deleteAllWorld();

        for (int i = 0; i < gamesAtTheSameTime; i++) {
            new GameManager(maxPlayersPerTeam, minPlayersToStart, points, countdown);
        }


    }

    @Override
    public void onDisable() {
        for (GameManager gameManager : GameManager.games) {
            for (Player player : gameManager.players) {
                player.teleport(Main.lobby);

                FastBoard board = GameManager.boards.remove(player.getUniqueId());
                if (board != null) {
                    board.delete();
                }
            }
        }
    }
}