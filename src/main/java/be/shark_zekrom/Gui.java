package be.shark_zekrom;

import com.onarandombox.MultiverseCore.MVWorld;
import com.onarandombox.MultiverseCore.api.MVWorldManager;
import com.onarandombox.MultiverseCore.api.MultiverseWorld;
import fr.mrmicky.fastboard.FastBoard;
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
import org.bukkit.scheduler.BukkitRunnable;

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
                    lore.add("Players: " + game.getPlayers().size());
                    break;
                case STARTING:
                    itemStack = new ItemStack(Material.YELLOW_WOOL);
                    lore.add("Status: Starting");
                    lore.add("Players: " + game.getPlayers());
                    lore.add("Countdown: " + game.getCountdown());
                    break;
                case INGAME:
                    itemStack = new ItemStack(Material.RED_WOOL);
                    lore.add("Status: INGAME");
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

                    GameManager game = GameManager.getGameById(Integer.parseInt(id));

                    for (Player players : game.getPlayers()) {
                        players.sendMessage("§a" + player.getName() + " §7has joined the game " + game.getPlayers().size() + "/10");
                    }

                    game.addPlayer(player);
                    FastBoard board = new FastBoard(player);
                    board.updateTitle("Tower+");

                    GameManager.boards.put(player.getUniqueId(), board);

                    player.teleport(new Location(Bukkit.getWorld("TowerPlus_" + id), 0, -60,0));
                    GameManager.waitingInventory(player);

                    if (game.getPlayers().size() >= 1) {
                        game.setCountdown(10);
                        game.setGameStatus(GameManager.GameStatus.STARTING);

                        new BukkitRunnable() {
                            @Override
                            public void run() {
                                if (game.getPlayers().size() >= 1) {
                                    if (game.getCountdown() == 0) {
                                        game.setGameStatus(GameManager.GameStatus.INGAME);
                                        game.setCountdown(0);

                                        for (Player players : game.getPlayers()) {
                                            if (!game.getBluePlayers().contains(players) && !game.getRedPlayers().contains(players)) {
                                                GameManager.randomTeam(game, players);
                                            }
                                        }
                                        cancel();
                                    }
                                    game.setCountdown(game.getCountdown() - 1);
                                } else {
                                    game.setGameStatus(GameManager.GameStatus.WAITING);
                                    for (Player players : game.getPlayers()) {
                                        players.sendMessage("§acancelled");
                                    }
                                    cancel();
                                }
                            }
                        }.runTaskTimer(Main.getInstance(), 0, 20);
                    }



                }
            }
        }
    }
}
