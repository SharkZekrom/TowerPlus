package be.shark_zekrom;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.ItemMergeEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class Gui {


    public static void allGames(Player player) {
        Inventory inventory = Bukkit.createInventory(null, 54, "All Games");
        player.openInventory(inventory);
        for (int i = 0; i < GameManager.games.size(); i++) {

            ItemStack itemStack = null;
            switch (GameManager.getGameById(i + 1).getGameStatus()) {
                case WAITING:
                    itemStack = new ItemStack(Material.GREEN_WOOL);
                    break;
                case STARTING:
                   itemStack = new ItemStack(Material.YELLOW_WOOL);
                    break;
                case INGAME:
                    itemStack = new ItemStack(Material.RED_WOOL);
                    break;
                case ENDING:
                    itemStack = new ItemStack(Material.BLACK_WOOL);
                    break;
            }

            ItemMeta itemMeta = itemStack.getItemMeta();
            itemMeta.setDisplayName("Game " + (i + 1));
            itemStack.setItemMeta(itemMeta);

            inventory.setItem(i, itemStack);

        }
    }
}
