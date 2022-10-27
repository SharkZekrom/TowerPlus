package be.shark_zekrom;

import com.onarandombox.MultiverseCore.MVWorld;
import com.onarandombox.MultiverseCore.api.MVWorldManager;
import com.onarandombox.MultiverseCore.api.MultiverseWorld;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ItemMergeEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.sql.SQLException;
import java.util.ArrayList;

public class Gui implements Listener {


    public static void allGames(Player player) {
        Inventory inventory = Bukkit.createInventory(null, 54, "All Games");
        player.openInventory(inventory);
        for (int i = 0; i < GameManager.games.size(); i++) {

            GameManager game = GameManager.getGameById(i + 1);

            ItemStack itemStack = null;
            ArrayList<String> lore = new ArrayList<>();
            switch (game.getGameStatus()) {
                case WAITING:
                    itemStack = new ItemStack(Material.GREEN_WOOL);
                    lore.add("Status: Waiting");
                    lore.add("Players: " + game.getPlayers());
                    break;
                case STARTING:
                    itemStack = new ItemStack(Material.YELLOW_WOOL);
                    lore.add("Status: Starting");
                    lore.add("Players: " + game.getPlayers());
                    lore.add("Countdown: " + game.getCountdown());
                    break;
                case INGAME:
                    itemStack = new ItemStack(Material.RED_WOOL);
                    lore.add("Status: Starting");
                    lore.add("Players: " + game.getPlayers());
                    lore.add("Red : " + game.getGameRedPoints() + "(" + game.getRedPlayers().size() + ")");
                    lore.add("Blue : " + game.getGameBluePoints() + "(" + game.getRedPlayers().size() + ")");

                    break;
                case ENDING:
                    itemStack = new ItemStack(Material.BLACK_WOOL);
                    break;
            }
            ItemMeta itemMeta = itemStack.getItemMeta();
            itemMeta.setLore(lore);
            itemMeta.setDisplayName("Game " + (i + 1));
            itemStack.setItemMeta(itemMeta);

            inventory.setItem(i, itemStack);

        }
    }

    @EventHandler
    private void onInventoryClick(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        int slot = event.getSlot();
        if (event.getView().getTitle().equalsIgnoreCase("All Games")) {
            event.setCancelled(true);

            if (event.getInventory().getItem(slot) != null) {
                if (event.getInventory().getItem(slot).getType() == Material.GREEN_WOOL) {
                    String id = event.getInventory().getItem(slot).getItemMeta().getDisplayName().replaceAll("[ a-zA-Z]", "");
                    player.teleport(new Location(Bukkit.getWorld("TowerPlus_" + id), 0, -60,0));





                }
            }
        }
    }
}
