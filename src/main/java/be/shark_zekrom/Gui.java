package be.shark_zekrom;

import fr.mrmicky.fastboard.FastBoard;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.concurrent.TimeUnit;

public class Gui implements Listener {


    public static void allGames(Player player) {

        Inventory inventory;

        if (player.getOpenInventory().getTopInventory().getType() == InventoryType.CRAFTING) {
            inventory = Bukkit.createInventory(null, 54, Main.inventory_game_name);
            player.openInventory(inventory);

        } else {

            player.getOpenInventory().getTopInventory().clear();
            inventory = player.getOpenInventory().getTopInventory();
        }



        int[] slot = {0};
        for (GameManager gameManager : GameManager.games) {
            if (gameManager.getGameStatus() != GameManager.GameStatus.ENDING) {

                ItemStack itemStack = null;
                ArrayList<String> lore = new ArrayList<>();
                switch (gameManager.getGameStatus()) {
                    case WAITING -> {
                        itemStack = new ItemStack(Material.GREEN_WOOL);
                        for (String string : Main.inventory_waiting) {
                            String newString = string.replaceAll("%max_players%", String.valueOf(gameManager.getMaxPlayers() * 2)).replaceAll("%status%", "Waiting").replaceAll("%players%", String.valueOf(gameManager.getPlayers().size()));
                            lore.add(newString);
                        }
                    }
                    case STARTING -> {
                        itemStack = new ItemStack(Material.YELLOW_WOOL);
                        for (String string : Main.inventory_starting) {
                            String newString = string.replaceAll("%players%", String.valueOf(gameManager.getPlayers().size())).replaceAll("%max_players%", String.valueOf(gameManager.getMaxPlayers() * 2)).replaceAll("%status%", "Starting").replaceAll("%countdown%", String.valueOf(gameManager.getCountdown()));
                            lore.add(newString);
                        }
                    }
                    case INGAME -> {
                        itemStack = new ItemStack(Material.RED_WOOL);


                        for (String string : Main.inventory_ingame) {
                            String newString = string.replaceAll("%players%", String.valueOf(gameManager.getPlayers().size())).replaceAll("%max_players%", String.valueOf(gameManager.getMaxPlayers() * 2)).replaceAll("%status%", "Ingame").replaceAll("%red_points%", String.valueOf(gameManager.getRedPoints())).replaceAll("%blue_points%", String.valueOf(gameManager.getBluePoints())).replaceAll("%time%", Utils.getIntervalTime(gameManager.getTime()));
                            lore.add(newString);
                        }
                    }
                }
                ItemMeta itemMeta = itemStack.getItemMeta();
                itemMeta.setLore(lore);
                itemMeta.setDisplayName(Main.inventory_game_id.replaceAll("%id%", String.valueOf(gameManager.getGameId())));

                itemStack.setItemMeta(itemMeta);

                inventory.setItem(slot[0]++, itemStack);
            }
        }
    }

    public static void teams(Player player) {

        Inventory inventory;

        if (player.getOpenInventory().getTopInventory().getType() == InventoryType.CRAFTING) {
            inventory = Bukkit.createInventory(null, 54, Main.inventory_team_name);
            player.openInventory(inventory);

        } else {

            player.getOpenInventory().getTopInventory().clear();
            inventory = player.getOpenInventory().getTopInventory();
        }

        GameManager gameManager = GameManager.getGameByPlayer(player);

        ItemStack red = new ItemStack(Material.RED_BANNER);
        ItemMeta redMeta = red.getItemMeta();
        redMeta.setDisplayName(Main.inventory_team_red);
        ArrayList<String> redLore = new ArrayList<>();
        for (Player redPlayers : gameManager.getRedPlayers()) {
            redLore.add("- §c" + redPlayers.getName());
        }
        for (int i = 0; i < (gameManager.getMaxPlayers()) - gameManager.getRedPlayers().size() ; i++) {
            redLore.add("-");
        }
        redMeta.setLore(redLore);

        red.setItemMeta(redMeta);
        inventory.setItem(21, red);

        ItemStack blue = new ItemStack(Material.BLUE_BANNER);
        ItemMeta blueMeta = blue.getItemMeta();
        blueMeta.setDisplayName(Main.inventory_team_blue);
        ArrayList<String> blueLore = new ArrayList<>();
        for (Player bluePlayers : gameManager.getBluePlayers()) {
            blueLore.add("- §c" + bluePlayers.getName());
        }
        for (int i = 0; i < (gameManager.getMaxPlayers()) - gameManager.getBluePlayers().size() ; i++) {
            blueLore.add("-");
        }
        blueMeta.setLore(blueLore);

        blue.setItemMeta(blueMeta);
        inventory.setItem(23, blue);

        ItemStack random = new ItemStack(Material.WHITE_BANNER);
        ItemMeta randomMeta = random.getItemMeta();
        randomMeta.setDisplayName(Main.inventory_team_random);
        random.setItemMeta(randomMeta);
        inventory.setItem(22, random);
    }


    @EventHandler
    private void onInventoryClick(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        int slot = event.getSlot();
        if (event.getView().getTitle().equalsIgnoreCase(Main.inventory_game_name)) {
            event.setCancelled(true);

            if (event.getInventory().getItem(slot) != null) {
                if (event.getInventory().getItem(slot).getType() == Material.GREEN_WOOL || event.getInventory().getItem(slot).getType() == Material.YELLOW_WOOL) {
                    String id = event.getInventory().getItem(slot).getItemMeta().getDisplayName().replaceAll("[ §a-zA-Z]", "");

                    GameManager game = GameManager.getGameById(Integer.parseInt(id));

                    if (game.getPlayers().size() < Main.maxPlayersPerTeam * 2) {
                        for (Player players : game.getPlayers()) {
                            players.sendMessage(Main.getInstance().getConfig().getString("message.join_message").replaceAll("%player%", player.getName()).replaceAll("%players%", String.valueOf(game.getPlayers().size() + 1)).replaceAll("%max_players%", String.valueOf(game.getMaxPlayers() * 2)));
                        }

                        game.addPlayer(player);
                        FastBoard board = new FastBoard(player);
                        board.updateTitle(Main.getInstance().getConfig().getString("scoreboard.title"));

                        GameManager.boards.put(player.getUniqueId(), board);
                        player.teleport(game.getWaitingSpawn());
                        GameManager.waitingInventory(player);


                    } else {
                        player.sendMessage(Main.getInstance().getConfig().getString("message.full_game"));
                    }
                }
            }
        } else if (event.getView().getTitle().equalsIgnoreCase(Main.inventory_team_name)) {
            event.setCancelled(true);

            if (slot == 21 || slot == 23) {
                GameManager game = GameManager.getGameByPlayer(player);

                if (slot == 21) {
                    if (game.getBluePlayers().contains(player)) {
                        if (game.getRedPlayers().size() < Main.maxPlayersPerTeam) {
                            game.getBluePlayers().remove(player);
                            game.getRedPlayers().add(player);
                            player.sendMessage(Main.getInstance().getConfig().getString("message.team_change_red"));

                        } else {
                            player.sendMessage(Main.getInstance().getConfig().getString("message.team_full"));
                        }
                    } else {
                        if (!game.getRedPlayers().contains(player)) {
                            if (game.getRedPlayers().size() < Main.maxPlayersPerTeam) {
                                game.getRedPlayers().add(player);
                                player.sendMessage(Main.getInstance().getConfig().getString("message.team_change_red"));
                            } else {
                                player.sendMessage(Main.getInstance().getConfig().getString("message.team_change_full"));
                            }
                        }
                    }

                }
                if (slot == 23) {
                    if (game.getRedPlayers().contains(player)) {
                        if (game.getBluePlayers().size() < Main.maxPlayersPerTeam) {
                            game.getRedPlayers().remove(player);
                            game.getBluePlayers().add(player);
                            player.sendMessage(Main.getInstance().getConfig().getString("message.team_change_blue"));

                        } else {
                            player.sendMessage(Main.getInstance().getConfig().getString("message.team_change_full"));
                        }
                    } else {
                        if (!game.getBluePlayers().contains(player)) {
                            if (game.getBluePlayers().size() < Main.maxPlayersPerTeam) {
                                game.getBluePlayers().add(player);
                                player.sendMessage(Main.getInstance().getConfig().getString("message.team_change_blue").replaceAll("%team%", "blue"));
                            } else {
                                player.sendMessage(Main.getInstance().getConfig().getString("message.team_change_full"));
                            }
                        }
                    }
                }

                GameManager.waitingInventory(player);

            }
            if (slot == 22) {
                GameManager game = GameManager.getGameByPlayer(player);

                if (game.getRedPlayers().contains(player)) {
                    game.removeRedPlayer(player);
                } else if (game.getBluePlayers().contains(player)) {
                    game.removeBluePlayer(player);
                }
                player.sendMessage(Main.getInstance().getConfig().getString("message.team_change_random"));
                GameManager.waitingInventory(player);
            }
        }
    }
}