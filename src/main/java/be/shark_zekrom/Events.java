package be.shark_zekrom;

import be.shark_zekrom.database.Database;
import be.shark_zekrom.database.Errors;
import org.apache.commons.lang.math.IntRange;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByBlockEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.*;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

public class Events implements Listener {

    public static HashMap<Player, BukkitTask> runnableInventoryTeams = new HashMap<>();


    @EventHandler
    private void onRightClick(PlayerInteractEvent event) {

        if (event.getItem() != null) {
            if (GameManager.hasPlayer(event.getPlayer())) {
                if (event.getItem().getType() == Material.BARRIER) {
                    Player player = event.getPlayer();
                    GameManager gameManager = GameManager.getGameByPlayer(player);
                    gameManager.removePlayer(player);

                    player.teleport(new Location(Bukkit.getWorld("World"), 0, -60, 0));
                    GameManager.returnInventory(player);

                    for (Player players : gameManager.getPlayers()) {
                        players.sendMessage(Main.getInstance().getConfig().getString("message.leave_message").replaceAll("%player%", player.getName()).replaceAll("%players%", String.valueOf(gameManager.getPlayers().size())).replaceAll("%max_players%", String.valueOf(gameManager.getMaxPlayers())));
                    }
                } else if (event.getItem().getType() == Material.WHITE_BANNER || event.getItem().getType() == Material.RED_BANNER || event.getItem().getType() == Material.BLUE_BANNER) {

                    runnableInventoryTeams.put(event.getPlayer(),new BukkitRunnable() {
                        @Override
                        public void run() {

                            Gui.teams(event.getPlayer());


                        }
                    }.runTaskTimer(Main.getInstance(), 0, 20));


                }
            }
        }
    }

    @EventHandler
    private void onJoin(PlayerJoinEvent event) {
        event.getPlayer().teleport(Main.lobby);
        if (Main.db.hasAccount(String.valueOf(event.getPlayer().getUniqueId()), "game_played")) {
            Main.db.getAccountName(event.getPlayer(), "game_played");
        } else {
            Main.db.initializeAccount(event.getPlayer(), "game_played");
        }
        if (Main.db.hasAccount(String.valueOf(event.getPlayer().getUniqueId()), "game_won")) {
            Main.db.getAccountName(event.getPlayer(), "game_won");
        } else {
            Main.db.initializeAccount(event.getPlayer(), "game_won");
        }
        if (Main.db.hasAccount(String.valueOf(event.getPlayer().getUniqueId()), "points_scored")) {
            Main.db.getAccountName(event.getPlayer(), "points_scored");
        } else {
            Main.db.initializeAccount(event.getPlayer(), "points_scored");
        }
        if (Main.db.hasAccount(String.valueOf(event.getPlayer().getUniqueId()), "kills")) {
            Main.db.getAccountName(event.getPlayer(), "kills");
        } else {
            Main.db.initializeAccount(event.getPlayer(), "kills");
        }
    }

    @EventHandler
    private void onQuit(PlayerQuitEvent event) {
        if (GameManager.hasPlayer(event.getPlayer())) {
            Player player = event.getPlayer();

            GameManager gameManager = GameManager.getGameByPlayer(player);
            gameManager.removePlayer(player);
            GameManager.returnInventory(player);


        }
    }

    @EventHandler
    private void onInventoryClose(InventoryCloseEvent event) {

        if (Commands.runnableInventory.containsKey(event.getPlayer())) {
            Commands.runnableInventory.get(event.getPlayer()).cancel();
            Commands.runnableInventory.remove(event.getPlayer());

        }

        if (runnableInventoryTeams.containsKey(event.getPlayer())) {
            runnableInventoryTeams.get(event.getPlayer()).cancel();
            runnableInventoryTeams.remove(event.getPlayer());
        }
    }

    @EventHandler
    private void onMove(PlayerMoveEvent event) {

        GameManager gameManager = GameManager.getGameByPlayer(event.getPlayer());
        if (gameManager != null) {
            if (gameManager.getGameStatus() == GameManager.GameStatus.INGAME) {
                if (gameManager.getRedPlayers().contains(event.getPlayer())) {

                    Cuboid cuboid = new Cuboid(gameManager.getPool_blue_1(), gameManager.getPool_blue_2());
                    if (cuboid.contains(event.getPlayer().getLocation())) {
                        gameManager.addRedPoints(event.getPlayer());
                    }

                } else if (gameManager.getBluePlayers().contains(event.getPlayer())) {
                    Cuboid cuboid = new Cuboid(gameManager.getPool_red_1(), gameManager.getPool_red_2());
                    if (cuboid.contains(event.getPlayer().getLocation())) {
                        gameManager.addBluePoints(event.getPlayer());
                    }
                }


            }
        }
    }

    @EventHandler
    private void onHit(EntityDamageByEntityEvent event) {
        if (event.getEntity() instanceof Player player) {
            if (GameManager.hasPlayer(player)) {
                Player damager = (Player) event.getDamager();

                GameManager gameManager = GameManager.getGameByPlayer(player);

                if (gameManager.getGameStatus() == GameManager.GameStatus.INGAME) {
                    if (gameManager.getRedPlayers().contains(player) && gameManager.getRedPlayers().contains(damager)) {
                        event.setCancelled(true);
                    } else if (gameManager.getBluePlayers().contains(player) && gameManager.getBluePlayers().contains(damager)) {
                        event.setCancelled(true);
                    }
                } else {
                    event.setCancelled(true);

                }
            }
        }
    }

    @EventHandler
    private void onRespawn(PlayerRespawnEvent event) {
        Player player = event.getPlayer();
        if (GameManager.hasPlayer(player)) {
            GameManager gameManager = GameManager.getGameByPlayer(player);
            Location location = null;

            if (gameManager.getRedPlayers().contains(player)) {
                location = gameManager.getRedSpawn();
            } else if (gameManager.getBluePlayers().contains(player)) {
                location = gameManager.getBlueSpawn();

            }
            location.setWorld(Bukkit.getWorld(Main.worldPrefix + gameManager.gameId));
            event.setRespawnLocation(location);

        }
    }

    @EventHandler
    private void onDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();

        if (GameManager.hasPlayer(player)) {
            GameManager gameManager = GameManager.getGameByPlayer(player);

            EntityDamageEvent.DamageCause cause = event.getEntity().getLastDamageCause().getCause();
            switch (cause) {
                case FALL -> {
                    for (Player players : gameManager.getPlayers()) {
                        players.sendMessage(Main.getInstance().getConfig().getString("message.death_fall_message").replaceAll("%player%", player.getName()));
                    }
                }
                case VOID -> {
                    for (Player players : gameManager.getPlayers()) {
                        players.sendMessage(Main.getInstance().getConfig().getString("message.death_void_message").replaceAll("%player%", player.getName()));
                    }
                }
                case ENTITY_ATTACK -> {
                    for (Player players : gameManager.getPlayers()) {
                        players.sendMessage(Main.getInstance().getConfig().getString("message.kill_message").replaceAll("%player%", player.getName()).replaceAll("%killed%", event.getEntity().getKiller().getName()));
                    }
                }
            }
        }

    }
}