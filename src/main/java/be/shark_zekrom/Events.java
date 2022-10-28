package be.shark_zekrom;

import org.apache.commons.lang.math.IntRange;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class Events implements Listener {


    @EventHandler
    private void onRightClick(PlayerInteractEvent event) {

        if (event.getItem().getType() == Material.BARRIER && GameManager.hasPlayer(event.getPlayer())) {
            Player player = event.getPlayer();
            GameManager gameManager = GameManager.getGameByPlayer(player);
            gameManager.removePlayer(player);

            player.teleport(new Location(Bukkit.getWorld("World"), 0, -60,0));
            GameManager.returnInventory(player);

            for (Player players : gameManager.getPlayers()) {
                players.sendMessage("§a" + player.getName() + " §7leave the game" + gameManager.getPlayers().size() + "/10");

            }
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
    private void onMove(PlayerMoveEvent event) {
        Boolean inRegion = inRegion(event.getPlayer().getLocation(), new Location(Bukkit.getWorld("World"), 0, -60,0), new Location(Bukkit.getWorld("World"), 0, -50,0));
        if (inRegion) {
            Bukkit.broadcastMessage("true");
        }

        GameManager gameManager = GameManager.getGameByPlayer(event.getPlayer());
        if (gameManager != null) {
            Bukkit.broadcastMessage("test1");
            if (gameManager.getGameStatus() == GameManager.GameStatus.INGAME) {
                Bukkit.broadcastMessage("test2");
                if (gameManager.getRedPlayers().contains(event.getPlayer())) {
                    Bukkit.broadcastMessage("test3");
                    if (inRegion(event.getPlayer().getLocation(), new Location(Bukkit.getWorld("World"), 0.7, -44,-8.7), new Location(Bukkit.getWorld("World"), 0.3, -43,-8.3))) {
                        Bukkit.broadcastMessage("red");

                    }
                } else if (gameManager.getBluePlayers().contains(event.getPlayer())) {
                    Bukkit.broadcastMessage("test4");
                    if (inRegion(event.getPlayer().getLocation(), new Location(Bukkit.getWorld("World"), 0.3, -44,9.7), new Location(Bukkit.getWorld("World"), 0.7, -43,9.3))) {
                        Bukkit.broadcastMessage("blue");

                    }
                }


            }
        }
    }


    public boolean inRegion(Location playerLoc, Location loc1, Location loc2){
        return new IntRange(loc1.getX(), loc2.getX()).containsDouble(playerLoc.getX())
                && new IntRange(loc1.getY(), loc2.getY()).containsDouble(playerLoc.getY())
                &&  new IntRange(loc1.getZ(), loc2.getZ()).containsDouble(playerLoc.getZ());
    }

}
