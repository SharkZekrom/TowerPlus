package be.shark_zekrom;

import fr.mrmicky.fastboard.FastBoard;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Statistic;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public class GameManager {

    public static final Map<UUID, FastBoard> boards = new HashMap<>();
    public static ArrayList<GameManager> games = new ArrayList<>();

    int gameId;
    public int getGameId() {
        return gameId;
    }

    int gameBluePoints;
    public int getGameBluePoints() {
        return gameBluePoints;
    }
    public void setGameBluePoints(int gameBluePoints) {
        this.gameBluePoints = gameBluePoints;
    }

    int gameRedPoints;
    public int getGameRedPoints() {
        return gameRedPoints;
    }
    public void setGameRedPoints(int gameRedPoints) {
        this.gameRedPoints = gameRedPoints;
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
        return countdown;
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

        getRedPlayers().remove(player);
        getBluePlayers().remove(player);

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
        bluePlayers.add(player);
    }
    public void removeRedPlayer(Player player) {
        bluePlayers.remove(player);
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
        GameManager gameManager = null;
        for (GameManager gameManager1 : games) {
            if (gameManager1.getPlayers().contains(player)) {
                gameManager = gameManager1;
                break;
            }
        }
        return gameManager;
    }


    public GameManager() {
        gameId = games.size() + 1;
        gameBluePoints = 0;
        gameRedPoints = 0;
        gameStatus = GameStatus.WAITING;
        countdown = 30;
        players = new ArrayList<>();
        redPlayers = new ArrayList<>();
        bluePlayers = new ArrayList<>();
        WorldManager.cloneWorld();
        GameManager.games.add(this);

    }


    public static GameManager getGameById(int id) {
        for (GameManager game : GameManager.games) {
            if (game.getGameId() == id) {
                return game;
            }
        }
        return null;
    }




    public enum GameStatus {
        WAITING,
        STARTING,
        INGAME,
        ENDING

    }


    public static void updateBoard(FastBoard board) {
        GameManager game = getGameById(getGameIdByPlayer(board.getPlayer()));

        switch (game.getGameStatus()) {
            case WAITING:
                board.updateLines(
                        "Waiting for players",
                        "ID > " + game.getGameId(),
                        game.getPlayers().size() + "/10"
                );
                break;
            case STARTING:
                board.updateLines(
                        "Starting in " + game.getCountdown(),
                        "ID > " + game.getGameId(),
                        game.getPlayers().size() + "/10"
                );
                break;
            case INGAME:
                board.updateLines(
                        "Red players: " + game.getRedPlayers().size(),
                        "Blue players: " + game.getBluePlayers().size()
                );
                break;
            case ENDING:
                board.updateLines(
                        "",
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

    }
    public static void randomTeam(GameManager gameManager, Player player) {
        Random random = new Random();

        String team;
        if (gameManager.getRedPlayers().size() == gameManager.getBluePlayers().size()) {
            if (random.nextBoolean()) {
                gameManager.addRedPlayer(player);
                team = "&c&lred";
            } else {
                gameManager.addBluePlayer(player);
                team = "&b&lblue";
            }
        } else {
            if (gameManager.getRedPlayers().size() < gameManager.getBluePlayers().size()) {
                gameManager.addRedPlayer(player);
                team = "&c&lred";
            } else {
                gameManager.addBluePlayer(player);
                team = "&b&lblue";
            }
        }
        player.sendMessage("§cYou have joined the " + team + " &ateam.");
    }
}
