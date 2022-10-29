package be.shark_zekrom;

import fr.mrmicky.fastboard.FastBoard;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;

public class Gui implements Listener {


    public static void allGames(Player player) {
        Inventory inventory = Bukkit.createInventory(null, 54, Main.inventory_name);
        player.openInventory(inventory);

        int[] slot = {0};
        GameManager.games.forEach(game -> {

            ItemStack itemStack = null;
            ArrayList<String> lore = new ArrayList<>();
            switch (game.getGameStatus()) {
                case WAITING:
                    itemStack = new ItemStack(Material.GREEN_WOOL);
                    for (String string : Main.inventory_waiting) {
                        String newString = string.replaceAll("%max_players%", String.valueOf(game.getMaxPlayers())).replaceAll("%status%", "Waiting").replaceAll("%players%", String.valueOf(game.getPlayers().size()));
                        lore.add(newString);
                    }

                    break;
                case STARTING:
                    itemStack = new ItemStack(Material.YELLOW_WOOL);

                    for (String string : Main.inventory_starting) {
                        String newString = string.replaceAll("%players%", String.valueOf(game.getPlayers().size())).replaceAll("%max_players%", String.valueOf(game.getMaxPlayers())).replaceAll("%status%", "Starting").replaceAll("%countdown%",String.valueOf(game.getCountdown()));
                        lore.add(newString);
                    }
                    break;
                case INGAME:
                    itemStack = new ItemStack(Material.RED_WOOL);

                    for (String string : Main.inventory_ingame) {
                        String newString = string.replaceAll("%players%", String.valueOf(game.getPlayers().size())).replaceAll("%max_players%", String.valueOf(game.getMaxPlayers())).replaceAll("%status%", "Ingame").replaceAll("%red_points%", String.valueOf(game.getRedPoints())).replaceAll("%blue_points%", String.valueOf(game.getBluePoints()));
                        lore.add(newString);
                    }
                    break;
                case ENDING:
                    itemStack = new ItemStack(Material.BLACK_WOOL);
                    break;
            }
            ItemMeta itemMeta = itemStack.getItemMeta();
            itemMeta.setLore(lore);
            itemMeta.setDisplayName(Main.inventory_game_name.replaceAll("%id%", String.valueOf(game.getGameId())));

            itemStack.setItemMeta(itemMeta);

            inventory.setItem(slot[0]++, itemStack);
        });

    }

    @EventHandler
    private void onInventoryClick(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        int slot = event.getSlot();
        if (event.getView().getTitle().equalsIgnoreCase(Main.inventory_name)) {
            event.setCancelled(true);

            if (event.getInventory().getItem(slot) != null) {
                if (event.getInventory().getItem(slot).getType() == Material.GREEN_WOOL || event.getInventory().getItem(slot).getType() == Material.YELLOW_WOOL) {
                    String id = event.getInventory().getItem(slot).getItemMeta().getDisplayName().replaceAll("[ a-zA-Z]", "");

                    GameManager game = GameManager.getGameById(Integer.parseInt(id));

                    if (game.getPlayers().size() < Main.maxPlayers) {
                        for (Player players : game.getPlayers()) {
                            players.sendMessage("§a" + player.getName() + " §7has joined the game " + game.getPlayers().size() + "/10");
                        }

                        game.addPlayer(player);
                        FastBoard board = new FastBoard(player);
                        board.updateTitle("Tower+");

                        GameManager.boards.put(player.getUniqueId(), board);
                        player.teleport(game.getWaitingSpawn());
                        GameManager.waitingInventory(player);


                    } else {
                        player.sendMessage("§cThis game is full");
                    }
                }
            }
        }
    }
}