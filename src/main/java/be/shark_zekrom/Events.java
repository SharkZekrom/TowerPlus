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
                    if (inRegion(event.getPlayer().getLocation(), new Location(event.getPlayer().getWorld(), 1, -43, 11), new Location(event.getPlayer().getWorld(), -1, -44, 9))) {
                        gameManager.addRedPoints(event.getPlayer());


                    }
                } else if (gameManager.getBluePlayers().contains(event.getPlayer())) {
                    if (inRegion(event.getPlayer().getLocation(), new Location(event.getPlayer().getWorld(), -1, -43, -9), new Location(event.getPlayer().getWorld(), 1, -44, -11))) {

                        gameManager.addBluePoints(event.getPlayer());
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
