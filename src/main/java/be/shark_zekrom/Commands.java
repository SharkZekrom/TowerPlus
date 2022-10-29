package be.shark_zekrom;

import com.onarandombox.MultiverseCore.api.MVWorldManager;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.WorldType;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.ArrayList;
import java.util.HashMap;

public class Commands implements CommandExecutor {

    public static HashMap<Player, BukkitTask> runnableInventory = new HashMap<>();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        Player player = (Player) sender;

        if (args[0].equals("add")) {
            new GameManager(Main.maxPlayers, Main.minPlayers, Main.points, Main.countdown);
        }

        if (args[0].equals("delete")) {
            GameManager.forceEndGame(Integer.parseInt(args[1]));

        }
        if (args[0].equals("status")) {
            GameManager.getGameById(Integer.parseInt(args[1])).setGameStatus(GameManager.GameStatus.valueOf(args[2]));
        }
        if (args[0].equals("inventory")) {
            //if (GameManager.hasPlayer(player)) {
            //   player.sendMessage("§cYou are already in a game");
            //   } else {
            runnableInventory.put(player,new BukkitRunnable() {
                @Override
                public void run() {

                    Gui.allGames(player);


                }
            }.runTaskTimer(Main.getInstance(), 0, 20));

            // }
        }
        if (args[0].equals("event")) {
            new GameManager(100, 2, Main.points, Main.countdown);
        }
        if (args[0].equals("modifymaxplayers")) {
            GameManager.getGameByPlayer(player).setMinPlayers(Integer.parseInt(args[1]));
        }
        return true;
    }
}
