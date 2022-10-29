package be.shark_zekrom;

import com.onarandombox.MultiverseCore.api.MVWorldManager;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.WorldType;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Commands implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        Player player = (Player) sender;

        if (args[0].equals("add")) {
            new GameManager(Main.maxPlayers, Main.minPlayers, Main.points, Main.countdown);
        }

        if (args[0].equals("delete")) {
            WorldManager.deleteWorld(Integer.parseInt(args[1]));
            GameManager.getGameById(Integer.parseInt(args[1])).deleteGame();
        }
        if (args[0].equals("status")) {
            GameManager.getGameById(Integer.parseInt(args[1])).setGameStatus(GameManager.GameStatus.valueOf(args[2]));
        }
        if (args[0].equals("inventory")) {
            if (GameManager.hasPlayer(player)) {
                player.sendMessage("§cYou are already in a game");
            } else {
                Gui.allGames(player);
            }
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
