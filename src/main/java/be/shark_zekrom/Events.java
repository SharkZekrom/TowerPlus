package be.shark_zekrom;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
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


}
