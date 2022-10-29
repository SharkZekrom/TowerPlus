package be.shark_zekrom;

import fr.mrmicky.fastboard.FastBoard;
import org.bukkit.*;
import org.bukkit.command.defaults.BukkitCommand;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;

public class GameManager {

    public static final Map<UUID, FastBoard> boards = new HashMap<>();
    public static ArrayList<GameManager> games = new ArrayList<>();

    public static Location waitingSpawn, redSpawn, blueSpawn;

    int gameId;

    public int getGameId() {
        return gameId;
    }

    int bluePoints;

    public int getBluePoints() {
        return bluePoints;
    }

    public void addBluePoints(Player player) {
        bluePoints++;
        Bukkit.broadcastMessage("Blue team has scored a point! (" + bluePoints + ")");
        player.teleport(new Location(player.getWorld(), 0, -42, 0));
        if (bluePoints == Main.points) {
            this.setGameStatus(GameStatus.ENDING);
            endGame(this, "blue");
        }
    }

    public void setBluePoints(int gameBluePoints) {
        this.bluePoints = gameBluePoints;
    }

    int redPoints;

    public int getRedPoints() {
        return redPoints;
    }

    public void addRedPoints(Player player) {
        redPoints++;
        Bukkit.broadcastMessage("Red team has scored a point! (" + redPoints + ")");
        player.teleport(new Location(player.getWorld(), 0, -42, 0));
        if (redPoints == Main.points) {
            this.setGameStatus(GameStatus.ENDING);
            endGame(this, "red");
        }
    }

    public void setRedPoints(int gameRedPoints) {
        this.redPoints = gameRedPoints;
    }

    GameStatus gameStatus;

    public GameStatus getGameStatus() {
        return gameStatus;
    }

    public void setGameStatus(GameStatus gameStatus) {
        this.gameStatus = gameStatus;
    }

    int countdown;
    public int getCountdown() {
        return this.countdown;
    }
    public void setCountdown(int countdown) {
        this.countdown = countdown;
    }

    ArrayList<Player> players;

    public ArrayList<Player> getPlayers() {
        return players;
    }

    public void addPlayer(Player player) {
        this.players.add(player);
    }

    public void removePlayer(Player player) {
        this.players.remove(player);

        this.getRedPlayers().remove(player);
        this.getBluePlayers().remove(player);

        FastBoard board = boards.remove(player.getUniqueId());
        if (board != null) {
            board.delete();
        }
    }

    public static boolean hasPlayer(Player player) {
        boolean hasPlayer = false;
        for (GameManager gameManager : games) {
            if (gameManager.getPlayers().contains(player)) {
                hasPlayer = true;
                break;
            }
        }
        return hasPlayer;
    }


    ArrayList<Player> redPlayers;

    public ArrayList<Player> getRedPlayers() {
        return redPlayers;
    }

    public void addRedPlayer(Player player) {
        redPlayers.add(player);
    }

    public void removeRedPlayer(Player player) {
        redPlayers.remove(player);
    }

    ArrayList<Player> bluePlayers;

    public ArrayList<Player> getBluePlayers() {
        return bluePlayers;
    }

    public void addBluePlayer(Player player) {
        bluePlayers.add(player);
    }

    public void removebluePlayer(Player player) {
        bluePlayers.remove(player);
    }

    public static int getGameIdByPlayer(Player player) {
        int id = 0;
        for (GameManager gameManager : games) {
            if (gameManager.getPlayers().contains(player)) {
                id = gameManager.getGameId();
                break;
            }
        }
        return id;

    }

    public static GameManager getGameByPlayer(Player player) {
        GameManager game = null;
        for (GameManager gameManager : games) {
            if (gameManager.getPlayers().contains(player)) {
                game = gameManager;
                break;
            }
        }
        return game;
    }

    public Location getWaitingSpawn() {
        return waitingSpawn;
    }

    public Location getRedSpawn() {
        return redSpawn;
    }

    public Location getBlueSpawn() {
        return blueSpawn;
    }


    public GameManager() {
        gameId = Main.id;
        bluePoints = 0;
        redPoints = 0;
        countdown = Main.countdown;
        gameStatus = GameStatus.WAITING;
        players = new ArrayList<>();
        redPlayers = new ArrayList<>();
        bluePlayers = new ArrayList<>();

        WorldManager.cloneWorld();

        waitingSpawn = new Location(Bukkit.getWorld(Main.worldPrefix + Main.id), Main.getInstance().getConfig().getDouble("location.waiting-spawn.x"),Main.getInstance().getConfig().getDouble("location.waiting-spawn.y"),Main.getInstance().getConfig().getDouble("location.waiting-spawn.z"), (float) Main.getInstance().getConfig().getDouble("location.waiting-spawn.yaw"), (float) Main.getInstance().getConfig().getDouble("location.waiting-spawn.pitch"));
        blueSpawn = new Location(Bukkit.getWorld(Main.worldPrefix + Main.id), Main.getInstance().getConfig().getDouble("location.spawn.blue.x"),Main.getInstance().getConfig().getDouble("location.spawn.blue.y"),Main.getInstance().getConfig().getDouble("location.spawn.blue.z"), (float) Main.getInstance().getConfig().getDouble("location.spawn.blue.yaw"), (float) Main.getInstance().getConfig().getDouble("location.spawn.blue.pitch"));
        redSpawn = new Location(Bukkit.getWorld(Main.worldPrefix + Main.id), Main.getInstance().getConfig().getDouble("location.spawn.red.x"),Main.getInstance().getConfig().getDouble("location.spawn.red.y"),Main.getInstance().getConfig().getDouble("location.spawn.red.z"), (float) Main.getInstance().getConfig().getDouble("location.spawn.red.yaw"), (float) Main.getInstance().getConfig().getDouble("location.spawn.red.pitch"));

        games.add(this);

        Main.id++;




    }


    public static GameManager getGameById(int id) {
        for (GameManager game : GameManager.games) {
            if (game.getGameId() == id) {
                return game;
            }
        }
        return null;
    }

    public void deleteGame() {
        games.remove(this);
    }


    public enum GameStatus {WAITING, STARTING, INGAME, ENDING}


    public static void updateBoard(FastBoard board) {
        GameManager game = getGameById(getGameIdByPlayer(board.getPlayer()));

        switch (game.getGameStatus()) {
            case WAITING:
                board.updateLines(
                        "Waiting for players",
                        "ID > TowerPlus-" + game.getGameId(),
                        "players: " + game.getPlayers().size() + "/" + Main.maxPlayers
                );
                break;
            case STARTING:
                board.updateLines(
                        "Starting in " + game.getCountdown(),
                        "ID > TowerPlus-" + game.getGameId(),
                        "players: " + game.getPlayers().size() + "/" + Main.maxPlayers
                );
                break;
            case INGAME:
                board.updateLines(
                        "Red points: " + game.getRedPoints(),
                        "Blue points: " + game.getBluePoints(),
                        "players: " + game.getPlayers().size() + "/" + Main.maxPlayers


                );
                break;
            case ENDING:
                board.updateLines(
                        "",
                        "Finish",
                        ""
                );
                break;
        }
    }


    public static void waitingInventory(Player player) {
        player.getInventory().clear();
        player.getInventory().setItem(3, new ItemStack(Material.BLUE_BANNER));
        player.getInventory().setItem(4, new ItemStack(Material.STONE));
        player.getInventory().setItem(5, new ItemStack(Material.RED_BANNER));

        player.getInventory().setItem(8, new ItemStack(Material.BARRIER));

    }

    public static void returnInventory(Player player) {
        player.getInventory().clear();

    }

    public static void randomTeam(GameManager gameManager, Player player) {
        Random random = new Random();

        String team;
        if (gameManager.getRedPlayers().size() == gameManager.getBluePlayers().size()) {
            if (random.nextBoolean()) {
                gameManager.addRedPlayer(player);
                team = "red";
            } else {
                gameManager.addBluePlayer(player);
                team = "blue";
            }
        } else {
            if (gameManager.getRedPlayers().size() < gameManager.getBluePlayers().size()) {
                gameManager.addRedPlayer(player);
                team = "red";
            } else {
                gameManager.addBluePlayer(player);
                team = "blue";
            }
        }
        player.sendMessage("§cYou have joined the " + team + " team.");
    }


    public static void endGame(GameManager gameManager, String team) {

        for (Player players : gameManager.getPlayers()) {
            players.sendMessage("§cThe " + team + " team has won the game!");

            FastBoard board = boards.remove(players.getUniqueId());
            if (board != null) {
                board.delete();
            }
            new BukkitRunnable() {
                @Override
                public void run() {
                    players.teleport(Main.lobby);
                }
            }.runTaskLater(Main.getPlugin(Main.class), 20 * 5);
        }
        games.remove(gameManager);

        if (GameManager.games.size() < Main.gamesAtTheSameTime) {
            new GameManager();
        }
    }
}
