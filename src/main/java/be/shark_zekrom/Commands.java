package be.shark_zekrom;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.HashMap;

public class Commands implements CommandExecutor {

    public static HashMap<Player, BukkitTask> runnableInventory = new HashMap<>();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        Player player = (Player) sender;

        if (player.hasPermission("Tower+.admin")) {
            if (args[0].equals("admin")) {
                if (args[1].equals("worldToClone")) {
                    Main.getInstance().getConfig().set("worldToClone", args[2]);
                    Main.getInstance().saveConfig();
                    player.sendMessage("§aWorldToClone set to " + args[2]);
                }
                if (args[1].equals("worldPrefix")) {
                    Main.getInstance().getConfig().set("worldPrefix", args[2]);
                    Main.getInstance().saveConfig();
                    player.sendMessage("§aWorldPrefix set to " + args[2]);
                }
                if (args[1].equals("maxPlayerPerTeam")) {
                    Main.getInstance().getConfig().set("maxPlayerPerTeam", Integer.valueOf(args[2]));
                    Main.getInstance().saveConfig();
                    player.sendMessage("§aMaxPlayerPerTeam set to " + args[2]);
                }
                if (args[1].equals("minPlayersToStart")) {
                    Main.getInstance().getConfig().set("minPlayersToStart", Integer.valueOf(args[2]));
                    Main.getInstance().saveConfig();
                    player.sendMessage("§aMinPlayersToStart set to " + args[2]);
                }
                if (args[1].equals("countdown")) {
                    Main.getInstance().getConfig().set("countdown", Integer.valueOf(args[2]));
                    Main.getInstance().saveConfig();
                    player.sendMessage("§aCountdown set to " + args[2]);
                }
                if (args[1].equals("gamesAtTheSameTime")) {
                    Main.getInstance().getConfig().set("gamesAtTheSameTime", Integer.valueOf(args[2]));
                    Main.getInstance().saveConfig();
                    player.sendMessage("§aGamesAtTheSameTime set to " + args[2]);
                }
                if (args[1].equals("setLeaderboard")) {
                    Main.getInstance().getConfig().set("leaderboard.location.world", player.getLocation().getWorld().getName());
                    Main.getInstance().getConfig().set("leaderboard.location.x", player.getLocation().getX());
                    Main.getInstance().getConfig().set("leaderboard.location.y", player.getLocation().getY());
                    Main.getInstance().getConfig().set("leaderboard.location.z", player.getLocation().getZ());
                    Main.getInstance().saveConfig();
                    player.sendMessage("§aLeaderboard location changed.");
                }

                if (args[1].equals("setLobby")) {
                    Main.getInstance().getConfig().set("location.lobby.world", player.getLocation().getWorld().getName());
                    Main.getInstance().getConfig().set("location.lobby.x", player.getLocation().getX());
                    Main.getInstance().getConfig().set("location.lobby.y", player.getLocation().getY());
                    Main.getInstance().getConfig().set("location.lobby.z", player.getLocation().getZ());
                    Main.getInstance().getConfig().set("location.lobby.yaw", player.getLocation().getYaw());
                    Main.getInstance().getConfig().set("location.lobby.pitch", player.getLocation().getPitch());
                    Main.getInstance().saveConfig();
                    player.sendMessage("§aLobby location changed.");
                }
                if (args[1].equals("setWaitingSpawn")) {
                    Main.getInstance().getConfig().set("location.waiting-spawn.x", player.getLocation().getX());
                    Main.getInstance().getConfig().set("location.waiting-spawn.y", player.getLocation().getY());
                    Main.getInstance().getConfig().set("location.waiting-spawn.z", player.getLocation().getZ());
                    Main.getInstance().getConfig().set("location.waiting-spawn.yaw", player.getLocation().getYaw());
                    Main.getInstance().getConfig().set("location.waiting-spawn.pitch", player.getLocation().getPitch());
                    Main.getInstance().saveConfig();
                    player.sendMessage("§aWaiting spawn location changed.");
                }
                if (args[1].equals("setSpectatorSpawn")) {
                    Main.getInstance().getConfig().set("location.spectator.x", player.getLocation().getX());
                    Main.getInstance().getConfig().set("location.spectator.y", player.getLocation().getY());
                    Main.getInstance().getConfig().set("location.spectator.z", player.getLocation().getZ());
                    Main.getInstance().getConfig().set("location.spectator.yaw", player.getLocation().getYaw());
                    Main.getInstance().getConfig().set("location.spectator.pitch", player.getLocation().getPitch());
                    Main.getInstance().saveConfig();
                    player.sendMessage("§aSpectator spawn location changed.");
                }

                if (args[1].equals("setRedSpawn")) {
                    Main.getInstance().getConfig().set("location.spawn.red.x", player.getLocation().getX());
                    Main.getInstance().getConfig().set("location.spawn.red.y", player.getLocation().getY());
                    Main.getInstance().getConfig().set("location.spawn.red.z", player.getLocation().getZ());
                    Main.getInstance().getConfig().set("location.spawn.red.yaw", player.getLocation().getYaw());
                    Main.getInstance().getConfig().set("location.spawn.red.pitch", player.getLocation().getPitch());
                    Main.getInstance().saveConfig();
                    player.sendMessage("§aRed spawn location changed.");
                }
                if (args[1].equals("setBlueSpawn")) {
                    Main.getInstance().getConfig().set("location.spawn.blue.x", player.getLocation().getX());
                    Main.getInstance().getConfig().set("location.spawn.blue.y", player.getLocation().getY());
                    Main.getInstance().getConfig().set("location.spawn.blue.z", player.getLocation().getZ());
                    Main.getInstance().getConfig().set("location.spawn.blue.yaw", player.getLocation().getYaw());
                    Main.getInstance().getConfig().set("location.spawn.blue.pitch", player.getLocation().getPitch());
                    Main.getInstance().saveConfig();
                    player.sendMessage("§aBlue spawn location changed.");
                }
                if (args[1].equals("setRedPool")) {
                    if (args[2].equals("1")) {
                        Main.getInstance().getConfig().set("location.pool.red.1.x", player.getLocation().getX());
                        Main.getInstance().getConfig().set("location.pool.red.1.y", player.getLocation().getY());
                        Main.getInstance().getConfig().set("location.pool.red.1.z", player.getLocation().getZ());
                        Main.getInstance().saveConfig();
                        player.sendMessage("§aRed pool 1 location changed.");
                    }
                    if (args[2].equals("2")) {
                        Main.getInstance().getConfig().set("location.pool.red.2.x", player.getLocation().getX());
                        Main.getInstance().getConfig().set("location.pool.red.2.y", player.getLocation().getY());
                        Main.getInstance().getConfig().set("location.pool.red.2.z", player.getLocation().getZ());
                        Main.getInstance().saveConfig();
                        player.sendMessage("§aRed pool 2 location changed.");
                    }
                }
                if (args[1].equals("setBluePool")) {
                    if (args[2].equals("1")) {
                        Main.getInstance().getConfig().set("location.pool.blue.1.x", player.getLocation().getX());
                        Main.getInstance().getConfig().set("location.pool.blue.1.y", player.getLocation().getY());
                        Main.getInstance().getConfig().set("location.pool.blue.1.z", player.getLocation().getZ());
                        Main.getInstance().saveConfig();
                        player.sendMessage("§aBlue pool 1 location changed.");
                    }
                    if (args[2].equals("2")) {
                        Main.getInstance().getConfig().set("location.pool.blue.2.x", player.getLocation().getX());
                        Main.getInstance().getConfig().set("location.pool.blue.2.y", player.getLocation().getY());
                        Main.getInstance().getConfig().set("location.pool.blue.2.z", player.getLocation().getZ());
                        Main.getInstance().saveConfig();
                        player.sendMessage("§aBlue pool 2 location changed.");
                    }
                }
            }

            if (args[0].equals("add")) {
                new GameManager(Main.maxPlayersPerTeam, Main.minPlayersToStart, Main.points, Main.countdown);
            }
            if (args[0].equals("status")) {
                GameManager.getGameById(Integer.parseInt(args[1])).setGameStatus(GameManager.GameStatus.valueOf(args[2]));
            }
            if (args[0].equals("event")) {
                new GameManager(100, 2, Main.points, Main.countdown);
            }
            if (args[0].equals("modifymaxplayers")) {
                GameManager.getGameByPlayer(player).setMinPlayers(Integer.parseInt(args[1]));
            }

            if (args[0].equals("forcestart")) {
                GameManager.forceStart(GameManager.getGameByPlayer(player));
            }
            if (args[0].equals("forceend")) {
                GameManager.forceEnd(GameManager.getGameByPlayer(player));

            }

        } else {
            player.sendMessage(Main.prefix + Main.noPermission);
        }
        if (args[0].equals("inventory")) {
            if (GameManager.hasPlayer(player)) {
                player.sendMessage(Main.prefix + Main.getInstance().getConfig().getString("messages.already_in_game"));
            } else {
                runnableInventory.put(player, new BukkitRunnable() {
                    @Override
                    public void run() {

                        Gui.allGames(player);


                    }
                }.runTaskTimer(Main.getInstance(), 0, 20));
            }
        }
        return true;
    }
}