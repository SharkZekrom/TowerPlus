package be.shark_zekrom;

import org.apache.commons.lang.math.IntRange;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class Events implements Listener {


    @EventHandler
    private void onRightClick(PlayerInteractEvent event) {

        if (event.getItem() != null) {
            if (event.getItem().getType() == Material.BARRIER && GameManager.hasPlayer(event.getPlayer())) {
                Player player = event.getPlayer();
                GameManager gameManager = GameManager.getGameByPlayer(player);
                gameManager.removePlayer(player);

                player.teleport(new Location(Bukkit.getWorld("World"), 0, -60, 0));
                GameManager.returnInventory(player);

                for (Player players : gameManager.getPlayers()) {
                    players.sendMessage("§a" + player.getName() + " §7leave the game" + gameManager.getPlayers().size() + "/10");

                }
            }
        }

    }

    @EventHandler
    private void onJoin(PlayerJoinEvent event) {
        event.getPlayer().teleport(Main.lobby);
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
    }

    @EventHandler
    private void onMove(PlayerMoveEvent event) {

        GameManager gameManager = GameManager.getGameByPlayer(event.getPlayer());
        if (gameManager != null) {
            if (gameManager.getGameStatus() == GameManager.GameStatus.INGAME) {
                if (gameManager.getRedPlayers().contains(event.getPlayer())) {

                    Cuboid cuboid = new Cuboid(new Location(event.getPlayer().getWorld(), 1.7, -44, 9.3), new Location(event.getPlayer().getWorld(), -0.7, -44, 11.7));
                    if (cuboid.contains(event.getPlayer().getLocation())) {
                        gameManager.addRedPoints(event.getPlayer());
                    }

                } else if (gameManager.getBluePlayers().contains(event.getPlayer())) {
                    Cuboid cuboid = new Cuboid(new Location(event.getPlayer().getWorld(), 1.7, -44, -8.3), new Location(event.getPlayer().getWorld(), -0.7, -44, -10.7));
                    if (cuboid.contains(event.getPlayer().getLocation())) {
                        gameManager.addBluePoints(event.getPlayer());
                    }
                }


            }
        }
    }
}
