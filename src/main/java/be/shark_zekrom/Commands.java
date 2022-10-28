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
            new GameManager();
        }

        if (args[0].equals("delete")) {
            WorldManager.deleteWorld(Integer.parseInt(args[1]));
            GameManager.getGameById(Integer.parseInt(args[1])).deleteGame();
        }
        if (args[0].equals("status")) {
            GameManager.getGameById(Integer.parseInt(args[1])).setGameStatus(GameManager.GameStatus.valueOf(args[2]));
        }
        if (args[0].equals("inventory")) {
            Gui.allGames(player);
        }
        return true;
    }
}
